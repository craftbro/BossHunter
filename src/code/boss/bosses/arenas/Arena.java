package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import code.boss.bosses.Boss;
import code.boss.main.main;

public class Arena {

	main plugin;
	Boss boss;
	Location spawn;
	
	public Arena(main instance, Boss boss, Location spawn){
		this.plugin = instance;
		this.boss = boss;
		this.spawn = spawn;
	}
	
	public Arena(main instance, Boss boss){
		this.plugin = instance;
		this.boss = boss;
	}
	
	public Location getSpawn(){
		return spawn;
	}
	
	public void start(){
		boss.start();
		plugin.pm.start();
	}
	
	public Boss getBoss(){
		return boss;
	}
	
	public void tick(){
		boss.tick();
	}
	
	public void setup(){
		
	}
	
	public void clear(){
		for(Entity e : Bukkit.getWorld("BOSS").getEntities()){
			if(!(e instanceof Player)){
				e.remove();
			}
		}
	}
	
}
