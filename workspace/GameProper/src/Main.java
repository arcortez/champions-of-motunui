import java.io.File;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		try{
			Game game = new Game();
			AppGameContainer app = new AppGameContainer(game);
			app.setDisplayMode(900,700,false);
			app.start();
			
		}catch(SlickException e){
			e.printStackTrace();
		}
			
	}
}