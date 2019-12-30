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
		
		//Port request
		String req = "GIVE PORTS";
		sendData = req.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		log("Sent request ["+req+"]");
		
		//Odebranie portów
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
		
		for(Integer n: ports){	//for(Integer n: ports){
			
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
		
		//Połącznie z portem TCP
		log("TRING TO CONNECT TO TCP");
		Socket socket = new Socket(InetAddress.getByName("localhost"),5000);
		
		//Odebranie pliku
		byte[] contents = new byte[10000];
		
		//TODO: File name Change!
		
		FileOutputStream fos = new FileOutputStream("C:\\skj2019dzienne\\odebrane\\test.mpg");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		InputStream is = socket.getInputStream();
		
		int bytesRead = 0;
		
		while((bytesRead=is.read(contents))!=-1){
			bos.write(contents,0,bytesRead);
		}
		
		bos.flush();
		socket.close();
		
		System.out.println("FILES SAVED SUCCESFULLY");
		
		//TODO: Wysłanie EXIT
	}
}