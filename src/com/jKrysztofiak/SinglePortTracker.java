package com.jKrysztofiak;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SinglePortTracker extends Thread {
	
	DatagramSocket socket;
	int portOg;
	
	InetAddress clientIP;
	int clientPort;
	
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	
	boolean done = false;
	
	private BlockingQueue<Integer> queue;
	
	public SinglePortTracker(int port, BlockingQueue<Integer> queue, InetAddress clientIP, int clientPort) throws SocketException {
		System.out.println("PORT "+port+" OPENED!");
		this.portOg = port;
		socket = new DatagramSocket(port);
		socket.setSoTimeout(10000);
		this.queue = queue;
		this.clientIP = clientIP;
		this.clientPort = clientPort;
	}
	
	public boolean isDone(){
		return done;
	}
	
	@Override
	public void run() {
		try{
			while(!socket.isClosed()){
				System.out.println("PORT "+portOg+" IS WAITING!");
				//Nawiązanie połącznia
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				socket.receive(receivePacket);
				String req  = new String(receivePacket.getData());
				System.out.println("FROM CLIENT: " + req);
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				
				if(queue.peek() == portOg && clientIP.equals(IPAddress) && clientPort == port){
					System.out.println("QUEUE PEEK " + queue.peek());
					queue.take();
					//Odpowiedź
					String resp = "KNOCK! KNOCK! ON PORT: "+portOg+" GETTING CLOSER!";
					sendData = resp.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					socket.send(sendPacket);
					done=true;
					socket.close();
				}else{
					if(!clientIP.equals(IPAddress)  || clientPort != port){
						System.out.printf("WRONG CLIENT! [%s:%d != %s:%s]",clientIP, clientPort, IPAddress, port);
					}else{
						System.out.printf("ERROR: PORT CONNECTED: %d ,PORT EXPECTED: %d\n",portOg,queue.peek());
						socket.close();
					}
					done = false;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
