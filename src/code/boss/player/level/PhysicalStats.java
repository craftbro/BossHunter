package code.boss.player.level;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import code.configtesting.config.Config;

public class PhysicalStats {

	Player p;
	int level;
	
	float speed = 2;
	int health = 20;
	int defense = 0;
	
	public PhysicalStats(Player p){
		this.p = p;
		this.level = (Integer) Config.getData(p, "levelphysic");
		
		start();
	}
	
	public PhysicalStats(Player p, int level){
		this.p = p;
		this.level = level;
		
		start();
	}

	
	private int getDefAmount(){
		int i=0;
		int l=level;
		
		while(l >= 6){
			l-=6;
			i++;
		}
		
		return i;
	}
	
	public void equip(){
		p.setWalkSpeed(speed/10);
		p.setMaxHealth(health);
		p.setHealth(health);
		
		if(defense > 0){
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, defense-1));
		}
		
	}
	
	private void start(){
		int l = level;
		int r = getDefAmount();
		
		int d = 0;
		
		while(l > 0){
			l--;
			
			if(d == 0){
				speed+=0.25;
				d++;
			}else if(d == 1){
				health+=2;
				d = 0;
			}
			
		}
		
		defense+=r;
		
		
	}
	
	
}
