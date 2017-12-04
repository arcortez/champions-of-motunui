import java.awt.*;
import java.util.Random;

public class ArrowLauncher extends Thread{
	private boolean gameover;

	public ArrowLauncher(){
		this.gameover = false;
	}

	public void run(){
		while(this.gameover == false){
			try{
				Thread.sleep(4000);
				Random rand = new Random();
				int pos = rand.nextInt(745) + 45;
				Server.broadcast("ENEMYFIRE "+pos);
			}catch(InterruptedException e){e.printStackTrace();}
		}
	}

	public void gameOver(){
		this.gameover = true;
	}
}