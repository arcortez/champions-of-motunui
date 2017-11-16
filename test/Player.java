import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;

public class Player extends JPanel implements Runnable{
	int xpos;
	int ypos;
	int playerid;
	BufferedImage image;

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
}