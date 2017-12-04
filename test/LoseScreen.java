import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import java.awt.image.BufferedImage;

import java.awt.Graphics;

public class LoseScreen extends JPanel{
	private BufferedImage image;

	public LoseScreen(){
		this.image = Assets.loseScreen;
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, -40,0,null);
		int len = Client.finalScores.length - 1;
		for(int i=1;i<len;i+=2){
			g.drawString(Client.finalScores[i]+" " + Client.finalScores[i+1], 100, 250+(15*i));
		}
	}
}