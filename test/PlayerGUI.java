import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;

public class PlayerGUI extends JPanel implements Runnable{
	int xpos;
	int ypos;
	int playerid;
	BufferedImage image;


	private String name;

	public PlayerGUI(String name, int initx, int inity, int playerid){
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

	public String getName(){
		return name;
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
		g.drawImage(Assets.gameScreen, 0,0, null);
		g.drawImage(this.image, ypos, xpos, null);
	}
}