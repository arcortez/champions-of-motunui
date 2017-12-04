import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import java.awt.image.BufferedImage;

import java.awt.Graphics;

public class GameOverScreen extends JPanel{
	private BufferedImage image;

	public GameOverScreen(){
		this.image = Assets.gameoverScreen;
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, -40,0,null);
	}
}