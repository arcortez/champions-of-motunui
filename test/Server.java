import java.net.*;
import java.io.*;

public class Server extends Thread{
	static ServerSocket serverSocket;
	static DatagramSocket server;
	static DatagramSocket socketRcv;
	static Socket[] clients;
	static int port;
	static int maxPlayers = 0;
    static DatagramSocket serverDataSocket = null;
    static final int WAITING_FOR_PLAYERS = 1;
    static final int GAME_START = 2;
    static final int ONGOING = 3;	
    static DataInputStream in;
    static DataOutputStream out;
    
	public Server(int port, int num) throws IOException{
		serverSocket = new ServerSocket(port);
		server = new DatagramSocket(port);
		socketRcv = new DatagramSocket(port+2);
		this.maxPlayers = num;
		clients = new Socket[num];
		System.out.println("Server is running at port "+port+"...");
	}

	public void run(){
		boolean connected = true;
		int playerCount = 0;
		String playerData;

		try{
			while(playerCount < maxPlayers){
				System.out.println("WAITING_FOR_PLAYERS");
				clients[playerCount] = serverSocket.accept();
				in = new DataInputStream(clients[playerCount].getInputStream());
				System.out.println("Just connected to player [" + in.readUTF() + "] on "+clients[playerCount].getRemoteSocketAddress());
				playerCount++;
			}

			System.out.println("ALL PLAYERS HAVE CONNECTED.");


            ChatServer cserver = new ChatServer();
            while(true){
            	byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				try{
	     			server.receive(packet);
				}catch(Exception ioe){}
				String msg = new String(buf);
				msg = msg.trim();
				System.out.println("Message: "+msg);
					
				for(int i=0;i<maxPlayers;i++){
					send(clients[i].getInetAddress(), msg);
				}

            }

		}catch(SocketException e){
			System.exit(1);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
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