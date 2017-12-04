import java.io.*;
import java.net.InetAddress;

public class Player{
	int xpos;
	int ypos;
	int playerid;
	int lives;

	private InetAddress address;
	private int port;
	private String name;

	public Player(String name, InetAddress address, int port, int initx, int inity, int playerid){
		this.address = address;
		this.port = port;
		this.name = name;

		this.xpos = initx;
		this.ypos = inity;
		this.playerid = playerid;
		this.lives = 3;	
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort(){
		return port;
	}

	public String getName(){
		return name;
	}

	public String toString(){
		String val = "";
		val +="PLAYER " + name + " " + this.xpos + " " + this.ypos;
		return val;

	}
}