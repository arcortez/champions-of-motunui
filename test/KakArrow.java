import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class KakArrow extends JPanel implements Runnable{
	private BufferedImage image;
	private int xpos;
	private int ypos;
	private boolean isRunning;
	private Player player1; // TODO: Change into array once multiplayer is set

	public KakArrow(int xPos,int yPos, boolean isDownwards, Player player1){
		this.xpos = xPos;
		this.ypos = yPos;
		this.isRunning = true;
		this.player1 = player1.clone();
		this.setOpaque(false);
		try {
			this.image = ImageIO.read(new File("proj.png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		while(this.isRunning == true && this.ypos <= 500){
			this.repaint();
			this.ypos += 10;

			/*TODO: CHANGE INTO ARRAY ONCE MULTIPLAYER IS SET*/
			if((Math.abs(player1.xpos - this.ypos) <= 15) && (Math.abs(kaks[i][j].ypos - this.xpos) <= 20)) {
				player1.isHit = true;
				this.isRunning = false;
				this.ypos = 500;
				this.repaint();
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
		if(this.ypos >= 500){
			this.xpos = x;
			this.ypos = y;
			this.isRunning = true;
			Thread t = new Thread(this);
			t.start();
		}

	}
}