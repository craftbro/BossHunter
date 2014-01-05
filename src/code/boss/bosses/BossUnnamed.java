package code.boss.bosses;





import java.util.Random;

import net.minecraft.server.v1_7_R1.Block;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;


import org.bukkit.craftbukkit.v1_7_R1.entity.CraftBlaze;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import code.boss.main.main;

/**
 * The class of the boss "Unnamed"
 * @author rasmusrune
 */
public class BossUnnamed extends Boss{

	Random r = new Random();
	
	public BossUnnamed(main m){
		super(m);
	}

	
	public void start(){
		boss = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 199, 199, 199), EntityType.BLAZE);
		boss.setCustomName(ChatColor.RED + "Unnamed");
		boss.setCustomNameVisible(true);
		boss.setMaxHealth(450);
		boss.setHealth(450);
		boss.setRemoveWhenFarAway(false);
	}
	
	
	public void tick(){
		
	}
	
	
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		event.setYield(0);
		event.blockList().clear();
	}
	
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event){
		if (event.getCause() != IgniteCause.ENDER_CRYSTAL){
			event.setCancelled(true);
		}
	}
	
	
	public void fireball(Entity entity){
		
	}
}
