import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;

public class PlayerGUI extends JPanel implements Runnable{
	int xpos;
	int ypos;
	int namepos;
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
		if(name.length(	) > 5){
			this.namepos = name.length()*2;
		}else{
			this.namepos = -10;
		}

		Thread t = new Thread(this);
		t.start();
	}

	public void setPos(int x, int y){
		this.xpos = x;
		this.ypos = y;
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
		g.drawImage(this.image, this.ypos, this.xpos, null);
		g.drawString(this.name, this.ypos-this.namepos, this.xpos+65);
	}

}