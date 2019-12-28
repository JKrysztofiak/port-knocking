package com.jKrysztofiak;
import java.io.*;
import java.net.*;

class Client {
	
	public static void log(String msg){
		System.out.println("CLIENT: "+msg);
	}
	
	public static void main(String args[]) throws Exception
	{
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		//TODO: Port request
		String req = "GIVE PORTS";
		sendData = req.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		log("Sent request ["+req+"]");
		
		//TODO: Odebranie portów
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String resp = new String(receivePacket.getData());
		System.out.println("FROM SERVER: " + resp);
		


		
		//TODO: Połącznie z port 1
		
		//TODO: Response od port 1
		
		//TODO: Połącznie z port 2
		
		//TODO: Response od port 2
		
		//TODO: Połącznie z port 3
		
		//TODO: Response od port 3
		
		//TODO: Odebranie nowego portu TCP
		
		//TODO: Połącznie z portem TCP
		
		//TODO: Response od portu TCP
		
		//TODO: Odebranie pliku
		
		//TODO: Wysłanie EXIT
	}
}