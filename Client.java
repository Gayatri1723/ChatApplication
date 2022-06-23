package pkg_gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {

	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	//Declare components
	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea=new JTextArea();
	private JTextField messageInput= new JTextField();
	private Font font=new Font("Roboto", Font.PLAIN, 20);
	
	
	//constructor
	public Client() {	
		
		try {
			
			System.out.println("Sending request to server..");
			socket = new Socket("127.0.0.1",6666);
			System.out.println("Connection Done!");
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));		//object
			
			out = new PrintWriter(socket.getOutputStream());								//object
			
			createGUI();
			handleEvents();
			startReading();
			startWriting();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	

	private void createGUI() {
		//gui code..
		this.setTitle("Client Messenger");
		this.setSize(500,650);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		
		heading.setIcon(new ImageIcon("msgg.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //padding
		
		messageArea.setEditable(false);
		messageArea.setCaretPosition(messageArea.getDocument().getLength()); //to automate scrolling
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		//frame layout
		this.setLayout(new BorderLayout());
		
		//adding components to frame
		this.add(heading,BorderLayout.NORTH);
		JScrollPane Jsp = new JScrollPane(messageArea);		//scrollbar
		this.add(Jsp,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	
	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("key released "+e.getKeyCode());
				
				if(e.getKeyCode()==10) {
					//System.out.println("You have pressed enter button");
					String contentToSend = messageInput.getText();
					messageArea.append("Me: "+contentToSend+"\n");
					
					out.println(contentToSend);
					out.flush();
					messageInput.setText(" ");     //to set input to blank, after sending msg
					messageInput.requestFocus();
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
			
			
	}
	

	//method start reading (receive)
	public void startReading() {
		//runnable interface with lambda for anonymous class
		Runnable r1=()->{
			System.out.println("Reader Started..");
			try {
				while(true)							//thread-reading continuously
				{
				
					String msg=br.readLine();
					if(msg.equals("exit"))
					{
						System.out.println("Server terminated the chat");
						JOptionPane.showMessageDialog(this,"Server Terminated the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
					
					//System.out.println("Server: "+msg);
					messageArea.append("Server : "+msg+"\n");
				
				} 
				
			
			}catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Connection closed!");
			}
			
		};

		//starting the thread by making thread object and calling start method
		new Thread(r1).start();
		
	}
	
	//method start writing (send)
	public void startWriting() {
		
		//thread- taking msg from user continuously
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
			}
			catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Connection closed!");
			}
		};
		
		//starting the thread by making thread object and calling start method
		 new Thread(r2).start();
		
	}


	public static void main(String[] args) {
		System.out.println("This is Client");
		new Client();
		
	}

}
