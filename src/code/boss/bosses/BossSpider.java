package code.boss.bosses;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import code.boss.main.main;

public class BossSpider extends Boss implements Listener{
	static int diseases = 0;
	public static int timer = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String s_v = ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "Squiddy" + ChatColor.DARK_BLUE + "] " + ChatColor.AQUA;
	public int timesDeath = 0;
	
	Random r = new Random();
	
	public BossSpider(main m){
		super(m);
	}
	
	
	public void start(){
		timer = 0;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(plugin.arena.getSpawn(), EntityType.CHICKEN);
				boss.setMaxHealth(525);
				boss.setHealth(boss.getMaxHealth());
				boss.setCustomName(ChatColor.GRAY + "Shield");
				boss.setCustomNameVisible(false);
				boss.setCanPickupItems(false);
				boss.setRemoveWhenFarAway(false);
				diseases = 1;
				spawned = true;
			}
		}.runTaskLater(plugin, timer += 80);
	}
	
	
	public void tick(){
		if (spawned){
			if(!boss.getLocation().getChunk().isLoaded()){
				boss.getLocation().getChunk().load();
			}
			if (diseases >= 1){
				if (r.nextInt(240) == 0){
					if (getMinionsSize() < 5){
						spiderlings(boss.getLocation(), Disease.RANDOM_MOTION);
					}
				}
			}
		}
	}
	
	
	public void spiderlings(Location loc, Disease disease){
		
	}
	
	
	public Player randomPlayer(){
		return Bukkit.getOnlinePlayers()[r.nextInt(Bukkit.getOnlinePlayers().length)];
	}
}

enum Disease {
	RANDOM_MOTION(0),
	AUTO_FLEE(1),
	POISON(2),
	VULNERABILITY(3),
	NOISES(4);
	
	private final int id;
	
	Disease(int id){
		this.id = id;
	}
	
	int getId(){
		return id;
	}
}
