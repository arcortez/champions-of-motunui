import java.util.*;

public class GameState{

	private Map players=new HashMap();
	
	public GameState(){}

	public void update(String name, Player player){
		players.put(name,player);
	}

	public String toString(){
		String retval="";
		for(Iterator ite=players.keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)players.get(name);
			retval+=player.toString()+":";
		}
		return retval;
    }
    
    public Player getPlayer(name){
        for(Iterator ite=players.keySet().iterator();ite.hasNext();){
			String pname=(String)ite.next();
			if(name.equals(pname)) {
                Player player=(NetPlayer)players.get(name);
            }
        }
        
        return player;
    }
	public Map getPlayers(){
		return players;
	}
}