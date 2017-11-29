import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer{
	private static int uniqueId;
	private ArrayList<ClientThread> al;
	private int port;
	private boolean connected;

	public ChatServer(int port) {
		this.port = port;
		al = new ArrayList<ClientThread>();

		connected = true;
		try{
			ServerSocket serverSocket = new ServerSocket(port+10);
			while(connected){
				System.out.println("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();
			
				ClientThread t = new ClientThread(socket);
				al.add(t);
				t.start();
			}
			try{
				serverSocket.close();
				for(int i=0;i<al.size();i++){
					ClientThread tc = al.get(i);
					try{
						tc.in.close();
						tc.out.close();
						tc.socket.close();
					}
					catch(IOException err){
						err.printStackTrace();
						System.exit(1);
					}
				}
			}catch(Exception e) {e.printStackTrace();System.exit(1);}
		}catch(Exception e) {e.printStackTrace();System.exit(1);}
	}

	private synchronized void broadcast(String message) {
		System.out.print(message);

		for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
			if(!ct.writemsg(message)) {
				al.remove(i);
				System.out.println("Disconnected Client " + ct.name + " removed from list.");
			}
		}
	}


	synchronized void remove(int id) {
		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}
	
	//one instance of this will run for each client
	class ClientThread extends Thread {
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		int id;
		String name;
		ChatMessage cm;

		ClientThread(Socket socket) {
			id = ++uniqueId;
			this.socket = socket;
			try
			{
				out = new ObjectOutputStream(socket.getOutputStream());
				in  = new ObjectInputStream(socket.getInputStream());

				name = (String) in.readObject();
				System.out.println(name + " just connected.");
			}
			catch (IOException e){e.printStackTrace();System.exit(1);}
			catch (ClassNotFoundException e){e.printStackTrace();System.exit(1);}
		}

		public void run() {
			boolean connected = true;
			while(connected) {
				try {
					cm = (ChatMessage) in.readObject();
				}
				catch (IOException e){e.printStackTrace();System.exit(1);}
				catch(ClassNotFoundException err){err.printStackTrace();System.exit(1);}

				String message = cm.getMessage();
				broadcast(name + ": " + message + "\n");
			}
			close();
		}
		
		private void close() {
			try {
				if(out != null) out.close();
			}
			catch(Exception e) {}
			try {
				if(in != null) in.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		
		private boolean writemsg(String msg) {
			if(!socket.isConnected()) {
				close();
				return false;
			}
			try {
				out.writeObject(msg);
			}catch(IOException e) {
				e.printStackTrace();
			}
			return true;
		}
	}
}

