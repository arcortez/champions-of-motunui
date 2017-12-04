import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EnemyArrow extends JPanel implements Runnable{
	private BufferedImage image;
	private int xpos;
	private int ypos;

	public EnemyArrow(){
		this.xpos = 900;
		this.ypos = 900;
		this.setOpaque(false);
		try {
			this.image = ImageIO.read(new File("../assets/arrow_down.png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		while(this.ypos <= 680){
			this.repaint();
			this.ypos += 10;
			int len = Client.maxPlayers;
			for(int i=0;i<len;i++){
				if(Client.players[i].xpos >= this.ypos - 15 && Client.players[i].xpos <= this.ypos + 15 && Client.players[i].ypos >= this.xpos - 15 && Client.players[i].ypos <= this.xpos + 15){
					this.xpos = 1000;
					this.ypos = 1000;
					Client.players[i].hit();
					System.out.println(Client.players[i].name + " WAS HIT!");	
					break;					
				}
			}
			try{
					Thread.sleep(100);
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

	
	public boolean setPos(int x, int y){
		if(this.ypos > 680){
			this.xpos = x;
			this.ypos = y;
			Thread t = new Thread(this);
			t.start();
			return true;
		}
		return false;
	}
}