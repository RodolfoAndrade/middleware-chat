/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 *
 * @author risa
 */
public class ClientRequestHandlerReliable {
    private String host;
    private int port;
    
    private Socket clientSocket = null;
    private ObjectOutputStream outToServer = null;
    private ObjectInputStream inFromServer = null;
    
    private Queue<byte[]> queueIN;
    private Queue<byte[]> queueOUT;
    
    public void pushOut(byte[] message) {
		queueOUT.add(message);
		send();
	}

	public ClientRequestHandlerReliable(String host, int port) {
        this.host = host;
        this.port = port;
        this.queueIN = new ArrayDeque<byte[]>();
        this.queueOUT = new ArrayDeque<byte[]>();
        
        boolean receiving = true;
        
        while (receiving) {
			try {
				clientSocket = new Socket("localhost", 1313);
	            (new Thread(new ThreadReceive())).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
	private void send() {
		while (!queueOUT.isEmpty()) {
			byte[] message = queueOUT.remove();
			
	        try {
				clientSocket = new Socket(host, port);
		        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		        
				outToServer.writeObject(message);
    	        outToServer.flush();

    			clientSocket.close();
    	        outToServer.close();
    	        inFromServer.close();
			} catch (IOException e1) {
//				return;
				pushOut(message);
			}  	
		}
	}
    
    class ThreadReceive implements Runnable {
        public void run() {
            while (true) {
	            try {
					clientSocket = new Socket(host, port);
		        	outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        	inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		        	
                	byte[] message = (byte[]) inFromServer.readObject();
                	queueIN.add(message);

					clientSocket.close();
	                outToServer.close();
	                inFromServer.close();
	            } catch (IOException e1) {
	                return;
	            } catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
            }
        }
    }
}
