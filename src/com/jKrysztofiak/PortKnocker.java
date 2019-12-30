package com.jKrysztofiak;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PortKnocker extends Thread {
	
	List<Integer> portOrder;
	List<SinglePortTracker> portsOpen;
	InetAddress clientIP;
	int clientPort;
	
	boolean workingProperly = true;
	
	private BlockingQueue<Integer> queue;
	
	
	public PortKnocker(List<Integer> portsNumbers, InetAddress clientIP, int clientPort){
		this.portOrder = portsNumbers;
		queue = new ArrayBlockingQueue<>(portsNumbers.size());
		this.clientIP = clientIP;
		this.clientPort = clientPort;
	}
	
	
	@Override
	public void run() {
		portsOpen = new ArrayList<>();
		try{
			for(int i=0; i<portOrder.size();i++){
				portsOpen.add(new SinglePortTracker(portOrder.get(i),queue, clientIP, clientPort));
				portsOpen.get(i).start();
				queue.put(portOrder.get(i));
			}
			
			for(SinglePortTracker t: portsOpen){
				t.join();
				if(!t.isDone()){
					System.out.printf("ERROR: THREAD FOR PORT: %d WASN'T FINISHED!\n",t.portOg);
					workingProperly = false;
					//break;
				}
			}
			
			if(workingProperly){
				System.out.println("DOORS OPEN...");
				
				ServerSocket serverSocket = new ServerSocket(5000);
				Socket socket = serverSocket.accept();
				
				InetAddress inetAddress = InetAddress.getByName("localhost");
				
				File file = new File("C:\\skj2019dzienne\\film.mpg");
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				
				OutputStream os = socket.getOutputStream();
				
				byte[] contents;
				long fileLength = file.length();
				long current = 0;
				
				long start = System.nanoTime();
				while (current!=fileLength){
					int size = 10000;
					if(fileLength - current >= size){
						current+=size;
					}
					else{
						size = (int) (fileLength-current);
						current = fileLength;
					}
					contents = new byte[size];
					bis.read(contents, 0, size);
					os.write(contents);
					System.out.print("Sending file ... "+(current*100)/fileLength+"% complete\r");
				}
				
				os.flush();
				socket.close();
				serverSocket.close();
				System.out.println("FILE SENT SUCCESFULLY!");
				
				
			}
			
			
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
