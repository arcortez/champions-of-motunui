import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Arrow extends JPanel implements Runnable{
	private BufferedImage image;
	private int xpos;
	private int ypos;
	private int id;

	public Arrow(int id, int xPos,int yPos, boolean isUpwards){
		this.id = id;
		this.xpos = xPos;
		this.ypos = yPos;
		this.setOpaque(false);
		try {
			this.image = ImageIO.read(new File("proj.png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		while(this.ypos >= -20){
			this.repaint();
			this.ypos -= 10;
			try{
					Thread.sleep(50);
				}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, this.xpos, this.ypos, null);
	}

	
	public void setpos(int x, int y){
		if(this.ypos <= -20){
			this.xpos = x;
			this.ypos = y;
			Thread t = new Thread(this);
			t.start();
		}

	}
}