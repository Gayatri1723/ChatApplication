package pkg_gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	ServerSocket server;
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	//constructor
	public Server() {	
		
		try {
			server = new ServerSocket(6666);	//port number
			System.out.println("Server is ready to accept connection");
			System.out.println("Waiting....");
			socket = server.accept();
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));		//object
			
			out = new PrintWriter(socket.getOutputStream());								//object
			
			startReading();
			startWriting();
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void startReading() {
		//thread-reading continuosly, runnable interface with lambda for anonymous class
		Runnable r1=()->{
			System.out.println("Reader Started..");
			
			try
			{
				while(true) 
				{
					String msg=br.readLine();
					if(msg.equals("exit"))
					{
						System.out.println("Client terminated the chat");
						socket.close();
						break;
					}
					
					System.out.println("Client: "+msg);
				
				} 
				
			}catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Connection closed!");
			}
			
		};

		//starting the thread by making thread object and calling start method
		new Thread(r1).start();
		
	}
	public void startWriting() {
		
		//thread-taking from user continuosly
		Runnable r2=()->{
			
			System.out.println("Writer Started..");
			
			try {
				while(true && !socket.isClosed())
				{
				
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));	//system.in takes input from keyboard
					String content = br1.readLine();
					
					out.println(content);
					out.flush();
					
					if(content.equals("exit")) {
						socket.close();
						break;
					}
					
				}
				//System.out.println("Connection closed!");
			
			}catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Connection closed!");
			}

		};
		
		//starting the thread by making thread object and calling start method
		 new Thread(r2).start();
		
	}
	
	public static void main(String[] args) {
		System.out.println("This is server");
		new Server();


	}

}

