import java.io.File;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

public class Main {
	static JPanel fields = new JPanel(new CardLayout());//initializing fields
	final static String HOME = "Home";
	final static String GAME = "GAME";
	final static String HIGH = "HighScores";
	final static String ABOUT = "About";
	final static String HELP = "Help";
	

	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		
		//insert client and server things here
		
		JFrame frame = new JFrame("Champions of Motunui");
		frame.setPreferredSize(new Dimension(900, 700));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Container c = frame.getContentPane();//container
		c.setLayout(new BorderLayout());

		JPanel north = new JPanel();

		JButton homebutton = new JButton("Home");	//for buttons
		JButton gamebutton = new JButton("Game");	
		JButton highbutton = new JButton("High Scores");
		north.add(homebutton);
		north.add(gamebutton);
		north.add(highbutton);
		north.setBackground(Color.GRAY);	

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		try{
//			Game game = new Game();
//			AppGameContainer app = new AppGameContainer(game);
//			app.setDisplayMode(900,700,false);
//			app.start();
//			
//		}catch(SlickException e){
//			e.printStackTrace();
//		}
			
	}
}
