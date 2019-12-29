package com.jKrysztofiak;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class PortKnocker implements Runnable {
	
	List<Integer> portOrder;
	List<PortTracker> portsOpen;
	int portsToKnock;
	
	public PortKnocker(List<Integer> portsNumbers) throws SocketException {
		this.portOrder = portsNumbers;
		portsToKnock = portsNumbers.size();
	}
	
	
	@Override
	public void run() {
		portsOpen = new ArrayList<>();
		try{
			for(int i=0; i<portOrder.size();i++){
				portsOpen.add(new PortTracker(portOrder.get(i)));
				portsOpen.get(i).start();
			}
			
			for(PortTracker t: portsOpen){
				t.join();
				if(t.isDone()){
					System.out.println("CLOSER ON "+t.getPort());
				}
				else {
					System.out.println("ERROR");
				}
				
			}
			
			System.out.println("ALL RIGHT ");
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
