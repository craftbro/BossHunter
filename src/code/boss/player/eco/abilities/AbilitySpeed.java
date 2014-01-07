package code.boss.player.eco.abilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbilitySpeed extends Ability {

	static int id = 3;
	
	public AbilitySpeed(Player p) {
		super(p, id);
		start();
	}
	
	public AbilitySpeed(Player p, int level) {
		super(p, id, level);
		start();
	}
	
	private void start(){
		max = 10;
		name = ChatColor.WHITE+"Swift";
		
		int delay = (60+(level*40))/20;
		int strenght = 1+level;
		
		
		stats.put("Delay:", delay+" Seconds");
		stats.put("Speed:", strenght+"");
	}
	
	@Override
	public int getCost(){
		return 50+(80*level);
	}
	
	@Override
	public boolean use(){
		int delay = (60+(level*40));
		int strenght = level;
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, delay, strenght));
		
		return true;
	}
	

}
