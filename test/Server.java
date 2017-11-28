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

		ChatServer cserver = new ChatServer();
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
            // Thread t = new Server(port, num);
			// t.start();			
			new Server(port, num);
			ChatServer cserver = new ChatServer();
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


class ChatServer implements Runnable{
	public ChatServer(){
		Thread t = new Thread(this);
		t.start();
	}

	public void run(){
		int playerCount = 0;
		try{
			while(playerCount < Server.maxPlayers){
				System.out.println("WAITING_FOR_PLAYERS");
				Server.clients[playerCount] = Server.serverSocket.accept();
				Server.in = new DataInputStream(Server.clients[playerCount].getInputStream());
				System.out.println("Just connected to player [" + Server.in.readUTF() + "] on "+Server.clients[playerCount].getRemoteSocketAddress());
				playerCount++;
			}
			System.out.println("ALL PLAYERS HAVE CONNECTED.");
		}catch(SocketException e){
			System.exit(1);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}


		while(true){
			try{
				for(int i=0;i<Server.maxPlayers;i++){
					if(Server.clients[i] != null){
						Server.in = new DataInputStream(Server.clients[i].getInputStream());
						String msg = Server.in.readUTF();
	        			for(int j=0;j<Server.maxPlayers;j++){
							if(Server.clients[j] != null){
								Server.out = new DataOutputStream(Server.clients[j].getOutputStream());
								Server.out.writeUTF(msg);
							}	
						}
					}
				}
			}catch(SocketException e){
				System.exit(1);
			}catch(IOException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}