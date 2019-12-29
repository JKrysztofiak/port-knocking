package com.jKrysztofiak;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PortKnocker extends Thread {
	
	List<Integer> portOrder;
	List<PortTracker> portsOpen;
	
	private BlockingQueue<Integer> queue;
	
	
	public PortKnocker(List<Integer> portsNumbers){
		this.portOrder = portsNumbers;
		queue = new ArrayBlockingQueue<>(portsNumbers.size());
	}
	
	
	@Override
	public void run() {
		portsOpen = new ArrayList<>();
		try{
			for(int i=0; i<portOrder.size();i++){
				portsOpen.add(new PortTracker(portOrder.get(i),queue));
				portsOpen.get(i).start();
				queue.put(portOrder.get(i));
			}
			
			for(PortTracker t: portsOpen){
				t.join();
				if(t.isDone()){
					System.out.println("CLOSER ON "+t.getPort());
				}
				else {
					System.out.println("ERROR");
					this.interrupt();
				}
			}
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
