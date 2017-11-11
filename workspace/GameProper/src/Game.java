import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame{
	public Game(){
		super("Game");
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.drawOval(100,100,20,20);
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		
	}
	
	@Override
	public void update(GameContainer gc, int x) throws SlickException {
		
	}
}
