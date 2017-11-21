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
	private boolean isRunning;
	private Kakamora[][] kaks;

	public Arrow(int xPos,int yPos, boolean isUpwards, Kakamora kaks[][]){
		this.xpos = xPos;
		this.ypos = yPos;
		this.isRunning = true;
		this.kaks = kaks.clone();
		this.setOpaque(false);
		try {
			this.image = ImageIO.read(new File("proj.png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		while(this.isRunning == true && this.ypos >= -20){
			this.repaint();
			this.ypos -= 10;
			for(int i=3; i>=0; i--){
				for(int j=0; j<19; j++){
					// if((Math.abs(kaks[i][j].xpos - this.xpos) == 0) && (Math.abs(kaks[i][j].ypos - this.ypos) < 5))
					// 	System.out.println("YEY!!"+ "x" + i + "y" + j);
					if((Math.abs(kaks[i][j].xpos - this.ypos) <= 12) && (Math.abs(kaks[i][j].ypos - this.xpos) <= 18)) {
						kaks[i][j].isHit = true;
						Client.incrementScore(5);
						System.out.println("YEY! +5" + Client.playerScore);
						this.isRunning = false;
						this.ypos = -40;
						this.repaint();
					}
				}
			}
			try{
					Thread.sleep(50);
			}catch(InterruptedException e){
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
			this.isRunning = true;
			Thread t = new Thread(this);
			t.start();
		}

	}
}