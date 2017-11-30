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
			this.image = ImageIO.read(new File("../assets/kakamora"+type+".png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run(){
		while(this.xpos <= 550 && this.isHit == false) {
			if(this.isHit == true) break;
			try {
				for(int i=this.type;i<this.type+12;i++){
					if(i%6 == 0){
						this.ypos -= 5;
						Thread.sleep(500);
					}else if(i%6 == 1){
						this.ypos -= 5;
						Thread.sleep(500);
					}else if(i%6 == 2){
						this.ypos += 5;
						Thread.sleep(500);
					}else if(i%6 == 3){
						this.ypos += 5;
						Thread.sleep(500);
					}else if(i%6 == 4){
						this.ypos -= 5;
						Thread.sleep(500);
					}else if(i%6 == 5){
						this.ypos += 5;
						Thread.sleep(500);
					}
				}
			}catch(Exception e){e.printStackTrace();System.exit(1);}
			this.xpos += 20;
		}

		if(this.isHit == false){
			Client.send("GAME OVER");
		}
	}


	public boolean wasHit(){
		return this.isHit;
	}

	public void hit(int playerArrowID){
		this.isHit = true;
		this.xpos = 1000;
		this.ypos = 1000;
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
		return "ID: " + this.id + " type: " + this.type + " x: " + this.xpos + " y: " + this.ypos;

	}

}