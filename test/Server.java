import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread{
	private ServerSocket serverSocket;
	private Socket[] clients;
	private int maxPlayers = 0;
    static DatagramSocket serverDataSocket = null;
    static final int WAITING_FOR_PLAYERS = 1;
    static final int GAME_START = 2;
    static final int ONGOING = 3;	
    private static DataInputStream in;
    private static DataOutputStream out;
	private static boolean[] ready;

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
		int stage = WAITING_FOR_PLAYERS;
		String playerData;
		while(true){
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				serverDataSocket.receive(packet);
			}catch(Exception e) {}

			playerData = new String(buf);

			playerData = playerData.trim();

			switch(stage){
				case WAITING_FOR_PLAYERS:
					// System.out.println("WAITING_FOR_PLAYERS");

					if (playerData.startsWith("JOIN")) {
						String tokens[] = playerData.split(" ");
						Player player = new Player(tokens[1], packet.getAddress(), packet.getPort(), 450, 20, playerCount);
						System.out.println(tokens[1] + "has joined.");
						gameState.update(tokens[1].trim(), player);
						broadcast("JOINED " + tokens[1]);
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

					if(playerData.startsWith("MOVES")){
						String[] playerInfo = playerData.split(" ");
						String name = playerInfo[1];
						int x = Integer.parseInt(playerInfo[2].trim());
						int y = Integer.parseInt(playerInfo[3].trim());

						Player player = (Player)gameState.getPlayers().get(name);
						

						gameState.update(name, player);

						broadcast(gameState.toString());
					}
					for(int i=0;i<maxPlayers;i++){
						if(clients[i] != null){
							try{
								in = new DataInputStream(clients[i].getInputStream());
								String msg = in.readUTF();
		            			System.out.println(msg); 	
		            			for(int j=0;j<maxPlayers;j++){
									if(clients[j] != null){
										out = new DataOutputStream(clients[j].getOutputStream());
										out.writeUTF(msg);
									}	
								}
							}catch(IOException e){
								e.printStackTrace();
							}
						}
					}

					break;
			}
			
		}

	}

	public static void main(String[] args){
		try{
            int port = Integer.parseInt(args[0]);
            int num = Integer.parseInt(args[1]);
            Thread t = new Server(port, num);
            t.start();
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