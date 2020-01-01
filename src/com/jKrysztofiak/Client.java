package com.jKrysztofiak;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Client {
	
	public static void log(String msg){
		System.out.println("CLIENT: "+msg);
	}
	
	public static void main(String args[]) throws Exception
	{
		int openingPort = 1000;
		String adressIP = "127.0.0.1";
		InetAddress IPAddress = InetAddress.getByName(adressIP);
		DatagramSocket clientSocket = new DatagramSocket(openingPort);
		clientSocket.setSoTimeout(20000); // TIMEOUT SET TO 20 SECONDS
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[4096];
		
		//Port request
		String req = "GIVE PORTS";
		sendData = req.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		log("Sent request ["+req+"]");
		
		
		
		//Odebranie portów
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String resp = new String(receivePacket.getData(),0, receivePacket.getLength());
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
			resp = new String(receivePacket.getData(),0, receivePacket.getLength());
			System.out.println("FROM SERVER: " + resp);
		}
		
		//TODO: Odebranie nowego portu TCP
		System.out.println("WAITING FOR TCP PORT NUMBER");
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		resp = new String(receivePacket.getData(),0, receivePacket.getLength());
		System.out.println("FROM SERVER: OPENED TCP PORT - " + resp);
		
		int portTCP = Integer.parseInt(resp);
		
		//Połącznie z portem TCP
		log("TRYING TO CONNECT TO TCP");
		Socket socket = new Socket(InetAddress.getByName("localhost"),portTCP);
		
		//Odebranie pliku
		byte[] contents = new byte[10000];
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		String adress =IPAddress.getHostAddress().replace(".","_")+"_"+openingPort;
		String name = adress+"_"+format.format(date);
		String path = "C:\\skj2019dzienne\\odebrane\\"+name+".mpg";
		
		FileOutputStream fos = new FileOutputStream(path);
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