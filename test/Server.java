import java.net.*;
import java.io.*;
import java.util.*;


public class Server extends Thread{
	static ServerSocket serverSocket;
	static Socket[] clients;
	static int[][] scores;
	static int[][] lives;
	static Map<Integer,String> players;
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

		players = new HashMap();
		this.maxPlayers = num;
		clients = new Socket[num];
		scores = new int[num][2];
		lives = new int[num][2];
		for(int i=0;i<lives.length;i++){
			lives[i][1] = 3;
		}

		System.out.print("Initializing Scores: [");
		for(int i=0;i<num;i++){
			scores[i][0] = i;
			scores[i][1] = 0;
			System.out.print("#");
		}
		System.out.println("] 100%");

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
			// System.out.println("playerData: " + playerData.trim());
			switch(stage){
				case WAITING_FOR_PLAYERS:
					if (playerData.startsWith("JOIN")) {
						String tokens[] = playerData.split(" ");
						Player player = new Player(tokens[1], packet.getAddress(), packet.getPort(), 450, 20*(playerCount+1), playerCount);
						gameState.update(tokens[1].trim(), player);

						// broadcast("JOINED " + tokens[1] + " " + playerCount );
						String msg = "ID " + playerCount + " " + maxPlayers;
						players.put(playerCount,tokens[1]);
						send(player, msg);
						playerCount++;
						
						if(playerCount >= maxPlayers){
							stage = GAME_START;
							System.out.println("ALL PLAYERS HAVE CONNECTED.");
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
						stage = ONGOING;
						System.out.println("\nSTAGE: " + stage);
						String b = "GAMESTART";
						for(int i=0;i<maxPlayers;i++){
							b = b.trim() + " " + i + " " + players.get(i).trim();
						}
						System.out.println(b);
						broadcast(b);
					}
          			
					break;
				case ONGOING:   
					if (playerData.startsWith("MOVE")){
						String[] playerInfo = playerData.split(" ");
						String name = playerInfo[1];
						int x = Integer.parseInt(playerInfo[2].trim());
						int y = Integer.parseInt(playerInfo[3].trim());

						String b = "MOVE " + name + " " + x + " " + y;
						broadcast(b);
					}else if(playerData.startsWith("SCORE")){
						String[] playerInfo = playerData.split(" ");
						int pID = Integer.parseInt(playerInfo[1].trim());
						int newscore = Integer.parseInt(playerInfo[2].trim());						
						scores[pID][1] = newscore;

						System.out.println("SCOREDDAW: " + pID + " " + newscore);
						
						Arrays.sort(scores, new Comparator<int[]>(){
							@Override
							public int compare(int[] p1, int[] p2){
								Integer score1 = p1[1];
								Integer score2 = p2[1];
								return score2.compareTo(score1);
							}
						});

						String b = "LEADERBOARD";
						for(int i=0;i<scores.length;i++){
							String name = players.get(scores[i][0]);
							b = b + " " + name.trim() + " " + scores[i][1];
						}
						broadcast(b);
					}else if(playerData.startsWith("GAME CLEAR")){
						for(int i=0;i<lives.length;i++){
							scores[i][1] = scores[i][1] + (lives[i][1] *20);
							System.out.println(scores[i][0] + " " + scores[i][1]);
						}
						Arrays.sort(scores, new Comparator<int[]>(){
							@Override
							public int compare(int[] p1, int[] p2){
								Integer score1 = p1[1];
								Integer score2 = p2[1];
								return score2.compareTo(score1);
							}
						});
						broadcast("GAMECLEAR " + scores[0][0]);
					}else if(playerData.startsWith("HIT")){
						String[] player = playerData.split(" ");
						int pID = Integer.parseInt(player[1].trim());

						for(int i=0;i<lives.length;i++){
							if(i==pID){
								lives[i][1]--;
								if(lives[i][1] == 0){
									broadcast("OUT " + pID);
								} else {
									broadcast("HIT " + pID + " " + lives[i][1]);
								}
								break; 
							}
						}
					}else{
						broadcast(playerData);
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
