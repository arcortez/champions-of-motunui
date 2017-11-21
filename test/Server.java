import java.net.*;
import java.io.*;
import java.util.*;


public class Server extends Thread{
	static ServerSocket serverSocket;
	static Socket[] clients;
	static int port;
	static int maxPlayers = 0;
    static DatagramSocket serverDataSocket = null;
    static final int WAITING_FOR_PLAYERS = 1;
    static final int GAME_START = 2;
    static final int ONGOING = 3;	
    static DataInputStream in;
	static DataOutputStream out;
	private static boolean[] ready;

	Thread t = new Thread(this);
   
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
		ready = new boolean[num];
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
						try{
							clients[playerCount] = serverSocket.accept();
							in = new DataInputStream(clients[playerCount].getInputStream());
							System.out.println("Just connected to player [" + in.readUTF() + "] on "+clients[playerCount].getRemoteSocketAddress());
							playerCount++;
						}catch(IOException e){
							e.printStackTrace();
							System.out.println("Input/Output Error!");
							break;
						}
						
						if(playerCount >= maxPlayers){
							stage = GAME_START;
							System.out.println("GAME HAS STARTED.");
						}
					}
					
					break;
				case GAME_START:
					broadcast("GAME START");
					stage = ONGOING;
          			
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

	public void send(InetAddress address,String msg){
		try{
			byte[] buf = msg.getBytes();
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        	socketRcv.send(packet);
        }catch(Exception l){}
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
		while(true){
			try{
				for(int i=0;i<Server.maxPlayers;i++){
					if(Server.clients[i] != null){
						Server.in = new DataInputStream(Server.clients[i].getInputStream());
						String msg = Server.in.readUTF();
	        			System.out.println(msg); 	
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