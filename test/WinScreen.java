import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import java.awt.image.BufferedImage;

import java.awt.Graphics;

public class WinScreen extends JPanel{
	private BufferedImage image;

	public WinScreen(){
		this.setLayout(new OverlayLayout(this));
		this.image = Assets.winScreen;
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, 0,0,null);

	}
}