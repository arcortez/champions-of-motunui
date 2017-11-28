import javax.swing.JPanel;
import java.awt.*;

public class ScoreKeeper extends JPanel implements Runnable{
	private String currentScore;

	public ScoreKeeper(){
		this.currentScore = "000000";
	}

	public void run(){
		while(true){
			this.repaint();
		}
	}


	public void setScore(int n){
		String str = "";
		int temp = n;
		while(temp > 9){
			temp /= 10;
			str = str + "0";
		}
		str = str + n;
		this.currentScore = str;
	}

	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(845, 0, 50, 20);
		g.setColor(Color.BLACK);
		g.drawString(this.currentScore, 850, 15);
	}


}	