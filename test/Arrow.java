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
			this.image = ImageIO.read(new File("../assets/arrow_up.png"));	
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		while(this.ypos >= -20){
			this.repaint();
			this.ypos -= 10;
			for(int i=0;i<4;i++){
				for(int j=0;j<14;j++){
					if(Client.kaks[i][j].xpos >= this.ypos - 15 && Client.kaks[i][j].xpos <= this.ypos + 15 && Client.kaks[i][j].ypos >= this.xpos - 15 && Client.kaks[i][j].ypos <= this.xpos + 15){
						System.out.println(Client.kaks[i][j].toString());
						Client.kaks[i][j].hit(this.id);
						this.xpos = -900;
						this.ypos = -900;
						
						if(this.id == Client.playerID){
							String newScore = String.valueOf(Integer.parseInt(Client.score.getText()) + 5);
							System.out.println("newScore: " + newScore);
							Client.send("SCORE " + this.id + " " + newScore);	
							newScore = Assets.addZeroes(newScore, 8);
							Client.score.setText(newScore);
						}
					}
				}
			}
			boolean clear = true;
			for(int i=0;i<4;i++){
				for(int j=0;j<14;j++){
					if(Client.kaks[i][j].wasHit() == false){
						clear = false;
					}
				}
			}
			if(clear){
				Client.send("GAME CLEAR");
				break;
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

	
	public void setPos(int x, int y){
		if(this.ypos <= -20){
			this.xpos = x;
			this.ypos = y;
			Thread t = new Thread(this);
			t.start();
		}

	}
}