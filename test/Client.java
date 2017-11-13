import java.net.*;
import java.io.*;
import javax.swing.*;


public class Client implements Runnable{
	private boolean connected;
	static JFrame frame;
	static Thread t;
	static DatagramSocket socket;
	private static Socket serverSocket;

	public Client(String serverIP, int port, String name){
		try{
			socket = new DatagramSocket();		
			serverSocket = new Socket(serverIP, port);

			System.out.println("Just connected to " + serverSocket.getRemoteSocketAddress());
		}catch(SocketException e){
			e.printStackTrace();
			System.exit(1);
		}catch(UnknownHostException e){
			e.printStackTrace();
			System.exit(1);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}

		t = new Thread(this);
		t.start();
	}

	public static void main(String[] args){
		try{
			String serverIP = args[0];
			int port = Integer.parseInt(args[1]);
			String name = args[2];
			//usage: java Client <ip address of server> <port number> <name of player>		
			Client client = new Client(serverIP, port, name);
	        
		}catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.> <your name>");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
        }

	}

	public void run(){
		connected = true;
		while(connected){

		}
		try{
			serverSocket.close();	
		}catch(IOException e){

		}
		
	}
}