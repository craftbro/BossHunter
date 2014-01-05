package code.boss.player.eco.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import code.boss.effect.ParticleEffect;

public class AbilityHeal extends Ability {

	public AbilityHeal(Player p) {
		super(p, 1);
		start();
	}
	
	public AbilityHeal(Player p, int level) {
		super(p, 1, level);
		start();
	}
	
	private void start(){
		max = 5;
		name = ChatColor.LIGHT_PURPLE+"Heal";
		
		int heal = (15+(4*level))/2;
		stats.put("Heal:", heal+" Hearts");
	}
	
	@Override
	public int getCost(){
		return 30+(100*level);
	}
	
	@Override
	public boolean use(){
		int heal = 15+(4*level);
		
		if(p.getHealth() < p.getMaxHealth()){
		if(p.getHealth()+heal <= p.getMaxHealth()){
			p.setHealth(p.getHealth()+heal);
		}else{
			p.setHealth(p.getMaxHealth());
		}
		
		p.playSound(p.getEyeLocation(), Sound.FIZZ, 100, 3);
		ParticleEffect.HEART.animateAtLocation(p.getLocation(), 1, 1);
		
		return true;
	}else{
		return false;
	}
	}

}
