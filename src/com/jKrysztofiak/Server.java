package com.jKrysztofiak;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
	
	public static void log(String msg){
		System.out.println("SERVER: "+msg);
	}
	
	private static int getRandomNumberInRange(int min, int max) {
		
		if (min >= max) {
			System.out.println("max must be greater than min");
		}
		
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public static void main(String[] args) throws Exception{
		DatagramSocket serverSocket = new DatagramSocket(9876);
		List<Integer> portList;
		
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		while(true){
			portList = new ArrayList<>();
			//Nawiązanie połącznia
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String req  = new String(receivePacket.getData());
			System.out.printf("CLIENT: %s:%d\n",receivePacket.getAddress(),receivePacket.getPort());
			System.out.println("RECEIVED: " + req);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			//Tworzenie portów
			String ports = "";
			for(int i=0; i<Integer.parseInt(args[0]); i++){
				int portNumber = getRandomNumberInRange(49152,65535);
				portList.add(portNumber);
				ports+=String.valueOf(portNumber)+" ";
			}
			
			//Wysłanie portów
			sendData = ports.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			log("Sent ports ["+ports+"]");
			
			PortKnocker doors = new PortKnocker(portList, IPAddress, port);
			doors.start();
			
			
		}
	}
}
