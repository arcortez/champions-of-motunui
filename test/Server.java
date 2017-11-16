import java.net.*;
import java.io.*;

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

	public Server(int port, int num) throws IOException{
		serverSocket = new ServerSocket(port);
		this.maxPlayers = num;
		clients = new Socket[num];
		ready = new boolean[num];
		System.out.println("Server is running at port "+port+"...");
	}

	public void run(){
		boolean connected = true;
		int playerCount = 0;
		int stage = WAITING_FOR_PLAYERS;
		String playerData;
		while(true){
			switch(stage){
				case WAITING_FOR_PLAYERS:
					System.out.println("WAITING_FOR_PLAYERS");
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


					break;
				case GAME_START:
					
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
