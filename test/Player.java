import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;

public class Player extends JPanel implements Runnable{
	private int xpos;
	private int ypos;
	int playerid;
	BufferedImage image;

	private InetAddress address;

	private int port;

	private String name;

	public Player(String name, InetAdress address, int port, int initx, int inity, int playerid){
		this.address = address;
		this.port = port;
		this.name = name;

		this.xpos = initx;
		this.ypos = inity;
		this.playerid = playerid;
		this.setOpaque(false);
		try {
			this.image = ImageIO.read(new File("testimage.png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Thread t = new Thread(this);
		t.start();
	}

	public InetAddress getAdress() {
		return address;
	}

	public int getPort(){
		return port;
	}

	public int getName(){
		return name;
	}

	public void run(){
		while(true) {
			try {
				this.repaint();
				Thread.sleep(5);
			} catch(Exception e) { 
			}
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, ypos, xpos, null);
	}

	public void moveLeft(){
		if(ypos >= 5)
			this.ypos -= 20;
	}	
	public void moveRight(){
	 	if(ypos <= 800)
	 		this.ypos += 20;
	}

	public String toString(){
		String val = "";
		val +="PLAYER " + name + " " + x + " " + y;
		return val;
	}
}