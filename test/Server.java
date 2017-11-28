import java.net.*;
import java.io.*;
import java.util.*;


public class Server extends Thread{
	static ServerSocket serverSocket;
	static Socket[] clients;
	static int maxPlayers = 0;
    static DatagramSocket serverDataSocket = null;
    static final int WAITING_FOR_PLAYERS = 1;
    static final int GAME_START = 2;
    static final int ONGOING = 3;	
    static DataInputStream in;
	static DataOutputStream out;

	Thread t = new Thread(this);

	static int ready = 0;
   
	GameState gameState;
	private static ChatServer cserver;

	public Server(int port, int num) throws IOException{
		try {
			serverDataSocket = new DatagramSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		gameState = new GameState();

		serverSocket = new ServerSocket(port);

		this.maxPlayers = num;
		clients = new Socket[num];
	
		System.out.println("Server is running at port "+port+"...");

		t.start();

	}
	public void broadcast(String msg){
		for(Iterator ite=gameState.getPlayers().keySet().iterator();ite.hasNext();){
			String name = (String) ite.next();
			Player player = (Player)gameState.getPlayers().get(name);
			send(player, msg);
		}
	}

	public void send(Player p, String msg) {
		DatagramPacket packet;

		byte[] buf = msg.getBytes();
		packet = new DatagramPacket(buf, buf.length, p.getAddress(), p.getPort());

		try {
			serverDataSocket.send(packet);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void run(){
		boolean connected = true;
		int playerCount = 0;
		String playerData;
		int stage = WAITING_FOR_PLAYERS;

		while(true){
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try{
				serverDataSocket.receive(packet);
			}catch(Exception e){
				e.printStackTrace();
			}

			playerData = new String(packet.getData());

			System.out.println("playerData: " + playerData);
			System.out.println("stage: " + stage);
			switch(stage){
				case WAITING_FOR_PLAYERS:
					if (playerData.startsWith("JOIN")) {
						String tokens[] = playerData.split(" ");
						Player player = new Player(tokens[1], packet.getAddress(), packet.getPort(), 450, 20*(playerCount+1), playerCount);
						gameState.update(tokens[1].trim(), player);

						// broadcast("JOINED " + tokens[1] + " " + playerCount );
						String msg = "ID " + playerCount + " " + maxPlayers;
						send(player, msg);
						playerCount++;
						
						if(playerCount >= maxPlayers){
							stage = GAME_START;
							System.out.println("GAME HAS STARTED.");
						}
					}
					
					break;
				case GAME_START:
					if(playerData.startsWith("READY")){
						String[] tokens = playerData.split(" ");

						int pID =  Integer.parseInt(tokens[1].trim());
						ready++;
					}
					if(ready >= maxPlayers){
						broadcast("GAME START");
						stage = ONGOING;
					}
          			
					break;
				case ONGOING:   
					System.out.println("ongoingDATA: " + playerData);
					if (playerData.startsWith("MOVE")){
						String[] playerInfo = playerData.split(" ");
						String name = playerInfo[1];
						int x = Integer.parseInt(playerInfo[2].trim());
						int y = Integer.parseInt(playerInfo[3].trim());

						String b = "MOVE " + name + " " + x + " " + y;
						broadcast(b);
					}
					break;
			}
		}
	}

	public static void main(String[] args){
		try{
            int port = Integer.parseInt(args[0]);
            int num = Integer.parseInt(args[1]);
            
			new Server(port, num);
			cserver = new ChatServer(port);

        }catch(IOException e){
            //e.printStackTrace();
            System.out.println("Usage: java Server <port no.> <no. of players>\n"+
                    "Make sure to use valid ports (greater than 1023)");
        }catch(ArrayIndexOutOfBoundsException e){
            //e.printStackTrace();
            System.out.println("Usage: java Server <port no.> <no. of players>\n"+
                    "Insufficient arguments given.");
        }

	}
}

class ChatServer {
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

