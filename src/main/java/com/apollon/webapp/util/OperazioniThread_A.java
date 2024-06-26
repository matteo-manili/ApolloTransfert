package com.apollon.webapp.util;



// public class ZZZ_A extends Thread{

public class OperazioniThread_A implements Runnable{
	
	
	public OperazioniThread_A(Object parameter) {
	       // store parameter for later user
	   }
	
	
	@Override
	public void run(){
	  
		System.out.println("ciao mondooooooooooooooooooooo");
		
	  try {
		  for(int volte = 0; volte < 10; volte++) {
			  System.out.println("CIao modno");
			  Thread.sleep(3000);
		  }
		  System.out.println("FINEEEEEEE");
	  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  
	  
	  
	  
	}
}
