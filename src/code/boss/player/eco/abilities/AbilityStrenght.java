package code.boss.player.eco.abilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbilityStrenght extends Ability {

	static int id = 2;
	
	public AbilityStrenght(Player p) {
		super(p, id);
		start();
	}
	
	public AbilityStrenght(Player p, int level) {
		super(p, id, level);
		start();
	}
	
	private void start(){
		max = 7;
		name = ChatColor.YELLOW+"Boost";
		
		int delay = (40+(level*30))/20;
		int strenght = 1+(level/2);
		
		
		stats.put("Delay:", delay+" Seconds");
		stats.put("Power:", strenght+"");
	}
	
	@Override
	public int getCost(){
		return 50+(120*level);
	}
	
	@Override
	public boolean use(){
		int delay = 40+(level*30);
		int strenght = 0+(level/2);
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, delay, strenght));
		
		return true;
	}
	

}
