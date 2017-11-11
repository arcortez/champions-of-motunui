import java.io.File;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

public class Main {
	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		
		//insert client and server things here
		
		JFrame frame = new JFrame("Champions of Motunui");
		Container c = frame.getContentPane();
		frame.setSize(900, 700);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		
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
