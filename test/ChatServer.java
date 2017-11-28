import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class ChatServer {
	private static int uniqueId;
	private ArrayList<ClientThread> al;
	private SimpleDateFormat sdf;
	private int port;
	private boolean keepGoing;

	public ChatServer(int port) {
		this.port = port;
		al = new ArrayList<ClientThread>();
	}
	
	public void start() {
		keepGoing = true;
		try{
			ServerSocket serverSocket = new ServerSocket(port);
			while(keepGoing){
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();
			
				ClientThread t = new ClientThread(socket);
				al.add(t);
				t.start();
			}
			try {
				serverSocket.close();
				for(int i=0;i<al.size();i++){
					ClientThread tc = al.get(i);
					try{
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					}
					catch(IOException err){
						err.printStackTrace();
						System.exit(1);
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
		}
	}		
    

	private void display(String msg) {
		System.out.println(msg+"\n");
	}

	private synchronized void broadcast(String message) {
		System.out.print(message);
		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
		for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(message)) {
				al.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
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
	
	/** One instance of this thread will run for each client */
	class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the Username of the Client
		String username;
		// the only type of message a will receive
		ChatMessage cm;
		// the date I connect
		String date;

		// Constructore
		ClientThread(Socket socket) {
			// a unique id
			id = ++uniqueId;
			this.socket = socket;
			/* Creating both Data Stream */
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username
				username = (String) sInput.readObject();
				display(username + " just connected.");
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			// have to catch ClassNotFoundException
			// but I read a String, I am sure it will work
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}

		// what will run forever
		public void run() {
			// to loop until LOGOUT
			boolean keepGoing = true;
			while(keepGoing) {
				// read a String (which is an object)
				try {
					cm = (ChatMessage) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				// the messaage part of the ChatMessage
				String message = cm.getMessage();
				broadcast(username + ": " + message);
			}
			// remove myself from the arrayList containing the list of the
			// connected Clients
			remove(id);
			close();
		}
		
		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		/*
		 * Write a String to the Client output stream
		 */
		private boolean writeMsg(String msg) {
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}

