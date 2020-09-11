package factory;


import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.io.*;

public class OutputWriter implements Runnable {
	private ConcurrentLinkedQueue<String> outQueue;


	public OutputWriter(ConcurrentLinkedQueue<String> outQueue) {
			this.outQueue = outQueue;
			//this.file = file;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(5000);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			try {
			    BufferedWriter writer = new BufferedWriter(new FileWriter("factory/output.dat", true));  
				
				//System.out.println(outQueue.size());
			    for(int i = 0; i < outQueue.size(); i++) {

				    writer.newLine();   //Add new line
				    String out = outQueue.poll();
				    writer.write(out);
				}
				writer.close();
			} catch(FileNotFoundException e) {
				System.out.println("File error");
			} catch(IOException e) {
				System.out.println("File error 2");

			}
		}

	}

}