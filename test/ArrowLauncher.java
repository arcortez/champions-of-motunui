import java.awt.*;
import java.util.Random;

public class ArrowLauncher extends Thread{
	public ArrowLauncher(){

	}

	public void run(){
		while(true){
			try{
				Thread.sleep(4000);
				Random rand = new Random();
				int pos = rand.nextInt(745) + 45;
				Server.broadcast("ENEMYFIRE "+pos);
			}catch(InterruptedException e){e.printStackTrace();}
		}
	}
}