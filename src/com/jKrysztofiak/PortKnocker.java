package com.jKrysztofiak;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PortKnocker extends Thread {
	
	List<Integer> portOrder;
	List<SinglePortTracker> portsOpen;
	InetAddress clientIP;
	int clientPort;
	
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
					System.out.println("ERROR");
					this.interrupt();
				}
			}
			
			System.out.println("DOORS OPEN...");
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
