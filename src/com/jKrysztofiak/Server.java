package com.jKrysztofiak;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	public static void log(String msg){
		System.out.println("SERVER: "+msg);
	}
	
	public static void main(String[] args) throws Exception{
		DatagramSocket serverSocket = new DatagramSocket(9876);
		List<Integer> portList = new ArrayList<>();
		
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		while(true){
			//Nawiązanie połącznia
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String req  = new String(receivePacket.getData());
			System.out.println("RECEIVED: " + req);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			//Tworzenie portów
			String ports = "";
			for(int i=0; i<Integer.parseInt(args[0]); i++){
				int portNumber = 10000+i;
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
