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
	}

	public void run(){
		while(true){
			try{
				Thread.sleep(500);

			}catch(InterruptedException e){e.printStackTrace();System.exit(1);}
		}
	}

	public void paintComponent(Graphics g){
		g.drawImage(this.im, this.xpos, this.ypos, null);
	}
}