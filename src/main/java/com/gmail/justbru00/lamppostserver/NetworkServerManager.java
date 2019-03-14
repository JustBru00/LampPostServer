package com.gmail.justbru00.lamppostserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class NetworkServerManager {

	private static ServerSocket listener;
	private static int clientNumber = 0;
	private static ArrayList<ClientManager> clients = new ArrayList<ClientManager>();
	
	public static void setLampState(String name, boolean state) {
		for (ClientManager client : clients) {
			if (client.LAMP_NAME.equalsIgnoreCase(name)) {
				client.lampState = state;
			}
		}
	}
	
	public static void startServer() {
		
		try {
			Messager.info("Creating ServerSocket...");
			listener = new ServerSocket(2019);
			Messager.info("DONE");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		try {
			while (LampPostServerMain.RUNNING) {
			Messager.info("Waiting for any CLIENT");
			// Create client handler
			ClientManager client = new ClientManager(listener.accept(), clientNumber++);
			client.start();
			clients.add(client);
			}
		} catch (SocketException e2){
			Messager.warn("Socket Closed");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void closeServer() {
		
		for (ClientManager c : clients) {
			c.shutdown();
		}
		
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class ClientManager extends Thread {
        private Socket socket;
        private int clientNumber;
        private boolean stop = false;
        private BufferedReader in;
        private PrintWriter out;
        public String LAMP_NAME = "not set";
        public boolean lampState = false;

        public ClientManager(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            Messager.info("New connection with client# " + clientNumber + " at " + socket);
        }
        
        public void shutdown() {
        	stop = true;
        	try {
				in.close();
				socket.close();
			} catch (IOException e) {
				Messager.warn("Uhh.. Couldn't close input stream.");
				e.printStackTrace();
			}
        	out.close();        	
        }

        /**
         * Services this thread's client repeatedly reading strings
         * and sending back LAMPON or LAMPOFF
         */
        public void run() {
            try {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
               in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
               out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("HELLO");
                /**
                 * LAMP1 or LAMP2
                 */
                String lampName = in.readLine();
                LAMP_NAME = lampName;

                // Get messages from the client, line by line; 
                while (!stop) {
                    String input = in.readLine();                   
                   
                    if (input.equalsIgnoreCase("WHAT_DO_YOU_WANT_FROM_ME?")) {
                    	// Send LAMPON or LAMPOFF
                    	if (lampState) {
                    		out.println("LAMPON");
                    	} else {
                    		out.println("LAMPOFF");
                    	}
                    } else {
                    	out.println("UNKNOWN COMMAND");
                    }
                }
            } catch (IOException e) {
               Messager.warn("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    Messager.critical("Couldn't close a socket, what's going on?");
                }
               Messager.info("Connection with client# " + clientNumber + " closed");
            }
        }
	
}
}