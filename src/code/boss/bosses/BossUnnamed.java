package code.boss.bosses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftMagmaCube;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import code.boss.main.main;

/**
 * The class of the boss "Unnamed"
 * @author rasmusrune
 */
public class BossUnnamed  extends Boss implements Listener{
	static int attacks = 0;
	public static int timer = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String b_v = ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "Unnamed" + ChatColor.DARK_BLUE + "] " + ChatColor.AQUA;
	
	Random r = new Random();

	public byte[] attackSet = new byte[]{
		(byte) r.nextInt(5), (byte) r.nextInt(5), (byte) r.nextInt(5), (byte) r.nextInt(5), (byte) r.nextInt(5)
	};
	
	public BossUnnamed(main m){
		super(m);
	}
	
	
	public void start(){
		timer = 0;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(new Location(Bukkit.getWorld("BOSS"), 1032, -19, -1130), EntityType.MAGMA_CUBE);
				((MagmaCube) boss).setSize(2);
				boss.setMaxHealth(525);
				boss.setHealth(525);
				boss.setCustomName(ChatColor.BLUE + "Unnamed");
				boss.setCustomNameVisible(true);
				boss.setCanPickupItems(false);
				boss.setRemoveWhenFarAway(false);
				attacks = 1;
				spawned = true;
			}
		}.runTaskLater(plugin, timer += 80);
	}
	
	
	public void tick(){
		if (spawned){
			if (attacks >= 1){
				if (r.nextInt(250) == 0){
					firstAttack(attackSet[0]);
				}
			}
			if (attacks >= 2){
				
			}
		}
	}
}
