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
		List<DatagramSocket> socketList;
		
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		while(true){
			//TODO: Nawiązanie połącznia
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String req  = new String(receivePacket.getData());
			System.out.println("RECEIVED: " + req);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			//TODO: TWORZENIE PORTÓW
			String ports = "";
			for(int i=0; i<Integer.parseInt(args[0]); i++){
				int portNumber = 10000+i;
				portList.add(portNumber);
				ports+=String.valueOf(portNumber)+" ";
			}
			
			//TODO: Wysłanie portów
			sendData = ports.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			log("Sent ports ["+ports+"]");
			
		}
		
		
		
		
		//TODO: Połącznie na Port 1
		
		//TODO: Wysłanie ACK 1
		
		//TODO: Połącznie na Port 2
		
		//TODO: Wysłanie ACK 2
		
		//TODO: Połącznie na Port 3
		
		//TODO: Wysłanie ACK 3
		
		//TODO: Otworzenie portu TCP
		
		//TODO: Wysłanie portu TCP
		
		//TODO: Połącznie na porcie TCP
		
		//TODO: Wysłanie ACK
		
		//TODO: Wysyłanie pliku
	}
}
