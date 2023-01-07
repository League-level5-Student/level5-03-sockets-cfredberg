package _02_Chat_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/*
 * Using the Click_Chat example, write an application that allows a server computer to chat with a client computer.
 */

public class ChatApp {
	boolean running = true;
	
	public static void main(String[] args) {
		ChatApp app = new ChatApp();
		app.run();
	}
	
	public void run() {
		String mode = JOptionPane.showInputDialog("Are you the client or server? [C/s]");
		if (mode.equalsIgnoreCase("s")){
			server();
		}else {
			client();
		}
	}
	
	public void server(){
		try {
			System.out.println("Server");
			ServerSocket server = new ServerSocket(8080);
			JOptionPane.showMessageDialog(null, "IP: " + getIPAddress());
			System.out.println("Waiting to connect...");
			Socket socket = server.accept();
			System.out.println("Connected");
			System.out.println("IMPORTANT: Type <Terminate> to end conversation");
			
			Thread writer = new Thread(() -> write(socket, "Server"));
			Thread reader = new Thread(() -> recieve(socket));
			
			writer.start();
			reader.start();
			
			while(running) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			server.close();
			System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void client() {
		System.out.println("Client");
		String ip = JOptionPane.showInputDialog("Server IP");
		int port = Integer.parseInt(JOptionPane.showInputDialog("Port #"));
		
		System.out.println("Connected");
		System.out.println("IMPORTANT: Type <Terminate> to end conversation");
		
		try {
			Socket socket = new Socket(ip, port);
			
			Thread writer = new Thread(() -> write(socket, "Client"));
			Thread reader = new Thread(() -> recieve(socket));
			
			writer.start();
			reader.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void write(Socket socket, String device) {
		while(running) {
			try {
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				String message = JOptionPane.showInputDialog(device + "'s Message");
				output.writeUTF(message);
				
				if (message.equals("<Terminate>")) {
					System.out.println("-------------------- Connection Terminated --------------------");
					running = false;
					continue;
				}
				System.out.println("You  said: " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void recieve(Socket socket) {
		while(running) {
			try {
				DataInputStream input = new DataInputStream(socket.getInputStream());
				
				String message = input.readUTF();
				
				if (message.equals("<Terminate>")) {
					System.out.println("-------------------- Connection Terminated --------------------");
					running = false;
					continue;
				}
				
				System.out.println("They said: " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getIPAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "ERROR!!!!!";
		}
	}
}