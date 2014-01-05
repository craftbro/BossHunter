package code.boss.player.eco.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import code.configtesting.config.Config;

public class Ability {
	
	HashMap<String, String> stats = new HashMap<String, String>();

	Player p;
	int level;
	int id = -1;
	int max = 999;
	String name = "Unkown";
	
	public Ability(Player p, int id){
		this.p = p;
		this.id = id;
		this.level = (Integer) Config.getData(p, "levelabil"+id);
	}
	
	public Ability(Player p, int id, int l){
		this.p = p;
		this.id = id;
		
		if(l > -1){
		this.level = l;
		}else{
			this.level = (Integer) Config.getData(p, "levelabil"+id);
		}
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getCost(){
		return 100+(50*level);
	}
	
	public List<String> getStats(){
		List<String> stats = new ArrayList<String>();
		
		for(String s : this.stats.keySet()){
			stats.add(ChatColor.DARK_GREEN+s+" "+ChatColor.GREEN+this.stats.get(s));
		}
		
		return stats;
	}
	
	public boolean use(){
		return false;
	}

}
