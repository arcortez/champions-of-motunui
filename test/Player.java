import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.awt.image.BufferedImage;

public class Player extends JPanel implements Runnable{
	private int xpos;
	private int ypos;
	int playerid;
	BufferedImage image;

	private InetAddress address;
	private int port;


	public Player(int initx, int inity, int playerid){
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

	public void run(){
		while(true) {
			try {
				this.repaint();
				Thread.sleep(5);
			} catch(Exception e) { 
				e.printStackTrace();
				System.exit(1);
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

	public void setXpos(int x){
		this.xpos = x;
	}

	public void setYpos(int y){
		this.ypos = y;
	}

	public int getXpos(){
		return xpos;
	}

	public int getYpos(){
		return ypos;
	}

	public InetAddress getAddress(){
		return address;
	}

	public int getPort(){
		return port;
	}

	public String getInfo(){
		String info="MOVE " + playerid + " " + xpos + " " + ypos;
		return info;
	}
}