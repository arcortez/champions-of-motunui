import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import java.awt.image.BufferedImage;

import java.awt.Graphics;

public class OverlaidField extends JPanel{
	private BufferedImage image;

	public OverlaidField(){
		this.setLayout(new OverlayLayout(this));
		this.image = Assets.gameScreen;
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, 0,0,null);

	}
}