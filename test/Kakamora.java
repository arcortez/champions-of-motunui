import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Kakamora extends JPanel implements Runnable{
	int xpos;
	int ypos;
	int id;
	int type;
	boolean isHit;
	BufferedImage image;

	public Kakamora(int initx, int inity, int id, int type){
		this.xpos = initx;
		this.ypos = inity;
		this.id = id;
		this.type = type;
		this.isHit = false;
		this.setOpaque(false);
		try {
			this.image = ImageIO.read(new File("kakamora"+type+".png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run(){
		while(this.xpos <= 650 && isHit == false) {
			try {
				if(this.type == 1){
					for(int i=0;i<4;i++){
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
					}
				}else if(this.type == 2){
					for(int i=0;i<4;i++){
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
					}
				}else if(this.type == 3){
					for(int i=0;i<4;i++){
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 10;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 10;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
					}
				}else{
					for(int i=0;i<4;i++){
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos += 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
						this.ypos -= 5;
						this.repaint();
						if(isHit == true) break;
						Thread.sleep(300);
						if(isHit == true) break;
					}
				}
				this.xpos += 15;
			} catch(Exception e) { 
			}
		}
		this.ypos = -40;
		this.repaint();
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