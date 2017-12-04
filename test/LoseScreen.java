import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import java.awt.image.BufferedImage;

import java.awt.Graphics;

public class LoseScreen extends JPanel{
	private BufferedImage image;

	public LoseScreen(){
		this.setLayout(new OverlayLayout(this));
		this.image = Assets.loseScreen;
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, 0,0,null);

	}
}