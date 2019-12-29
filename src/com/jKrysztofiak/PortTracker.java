package com.jKrysztofiak;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PortTracker extends Thread {
	
	DatagramSocket socket;
	int portOg;
	
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	
	boolean done = false;
	
	private BlockingQueue<Integer> queue;
	
	public PortTracker(int port, BlockingQueue<Integer> queue) throws SocketException {
		System.out.println("PORT "+port+" OPENED!");
		this.portOg = port;
		socket = new DatagramSocket(port);
		socket.setSoTimeout(10000);
		this.queue = queue;
	}
	
	public boolean isDone(){
		return done;
	}
	public int getPort(){
		return portOg;
	}
	
	@Override
	public void run() {
		try{
			System.out.println("PORT "+portOg+" IS WAITING!");
			//Nawiązanie połącznia
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);
			String req  = new String(receivePacket.getData());
			System.out.println("RECEIVED: " + req);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			done = true;
			if(queue.peek() == portOg){
				System.out.println("QUEEUE PEEK " + queue.peek());
				queue.take();
				//Odpowiedź
				String resp = "KNOCK! KNOCK! ON PORT: "+portOg+" GETTING CLOSER!";
				sendData = resp.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				socket.send(sendPacket);
				socket.close();
			}else{
				System.out.printf("HERE IS AN ERROR PORT CONNECTED: %d ,PORT EXPECTED: %d\n",portOg,queue.peek());
				done = false;
				socket.close();
			}
			

			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
