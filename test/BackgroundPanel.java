import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.*;


public class BackgroundPanel extends JPanel{
	public BackgroundPanel(){
	}

	public void paintComponent(Graphics g){
		g.drawImage(Assets.gameScreen, 0,0,null);
	}
}