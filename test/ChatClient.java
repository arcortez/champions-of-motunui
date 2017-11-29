import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private String server;
	private String name;
	private int port;

	ChatClient(String server, int port, String name) {
		port = port+10;
		this.server = server;
		this.port = port;
		this.name = name;

		try {
			socket = new Socket(server, port);
		}catch(Exception e) {e.printStackTrace();System.exit(1);}

		System.out.println("Chat connection established at " + socket.getInetAddress() + ":" + socket.getPort());
		try{
			in  = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		}catch(Exception e) {e.printStackTrace();System.exit(1);}
		
		new ListenFromServer().start();

		try{
			out.writeObject(name);
		}catch(IOException e) {e.printStackTrace();System.exit(1);}
	}

	private void display(String msg) {
		System.out.println(msg);
	}
	
	public void sendMessage(ChatMessage msg) {
		try {
			out.writeObject(msg);
		}catch(IOException e) {e.printStackTrace();System.exit(1);}
	}

	private void disconnect() {
		try { 
			if(in != null) in.close();
			if(out != null) out.close();
			if(socket != null) socket.close();
		}catch(Exception e) {e.printStackTrace();System.exit(1);}
	}


	class ListenFromServer extends Thread {
		public void run() {
			System.out.println("Started Chat Listener Thread.");
			while(true) {
				try {
					String msg = (String) in.readObject();
					System.out.println(msg);
					Client.textarea.setText(Client.textarea.getText() + msg);
				}
				catch(IOException e) {e.printStackTrace();System.exit(1);}
				catch(ClassNotFoundException e) {e.printStackTrace();System.exit(1);}
			}
		}
	}
}
