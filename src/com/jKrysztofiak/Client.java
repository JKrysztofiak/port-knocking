package com.jKrysztofiak;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		
		Scanner in = new Scanner(resp);
		List<Integer> ports = new ArrayList<>();
		
		while(in.hasNextInt()){
			int x = in.nextInt();
			ports.add(x);
//			System.out.println(x);
		}
		
		//TESTING ARRAY
		List<Integer> test = new ArrayList<>();
		test.add(10000);
		test.add(10001);
		test.add(10002);
		
		//TODO: CHANGE TO PROPER LIST
		for(Integer n: test){	//for(Integer n: ports){
			
			//Połącznie z portem
			req = "PORT "+n+" ?";
			sendData = req.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, n);
			clientSocket.send(sendPacket);
			log("Sent request ["+req+"]");
			
			//Response od portu
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			resp = new String(receivePacket.getData());
			System.out.println("FROM SERVER: " + resp);
			
		}
		
		//TODO: Odebranie nowego portu TCP
		
		//TODO: Połącznie z portem TCP
		Socket socket = new Socket(InetAddress.getByName("localhost"),5000);
		
		//TODO: Odebranie pliku
		
		byte[] contents = new byte[10000];
		
		FileOutputStream fos = new FileOutputStream("C:\\skj2019dzienne\\odebrane\\test.mpg");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		InputStream is = socket.getInputStream();
		
		int bytesRead = 0;
		
		while((bytesRead=is.read(contents))!=-1){
			bos.write(contents,0,bytesRead);
		}
		
		bos.flush();
		socket.close();
		
		System.out.println("FILES SAVED ELOOOOOO");
		
		//TODO: Wysłanie EXIT
	}
}