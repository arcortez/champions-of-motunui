import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class KakArrow extends JPanel implements Runnable{
	private int xpos;
	private int ypos;
	private static Random rand;
	private BufferedImage im;

	public KakArrow(){
		this.im = Assets.arrow_down;
		this.xpos = 900;
		this.ypos = 900;
	}

	public void run(){
		while(this.ypos <= 680){
			try{
				this.ypos += 10;
				Thread.sleep(100);
			}catch(InterruptedException e){e.printStackTrace();System.exit(1);}
		}
	}

	public void paintComponent(Graphics g){
		g.drawImage(this.im, this.xpos, this.ypos, null);
	}

	public void setPos(int x, int y){
		if(this.ypos > 680){
			this.xpos = x;
			this.ypos = y;
			Thread t = new Thread(this);
			t.start();	
		}
	}
}