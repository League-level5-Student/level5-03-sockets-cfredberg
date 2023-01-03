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
			ServerSocket server = new ServerSocket(8080);
			JOptionPane.showMessageDialog(null, "IP: " + getIPAddress());
			System.out.println("Waiting to connect...");
			Socket socket = server.accept();
			System.out.println("Connected");
			
			write(socket);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void client() {
		String ip = JOptionPane.showInputDialog("Server IP");
		int port = Integer.parseInt(JOptionPane.showInputDialog("Port #"));
		
		try {
			Socket socket = new Socket(ip, port);
			recieve(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void write(Socket socket) {
		try {
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String message = JOptionPane.showInputDialog("Message");
			output.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void recieve(Socket socket) {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			JOptionPane.showMessageDialog(null, input.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
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