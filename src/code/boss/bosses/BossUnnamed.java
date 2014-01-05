package code.boss.bosses;





import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;


import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftBlaze;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftZombie;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import code.boss.effect.ParticleEffect;
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
	
	
	public void fireball(LivingEntity entity){
		LargeFireball fireball = entity.launchProjectile(LargeFireball.class);
		fireball.setYield(0);
		fireball.setIsIncendiary(false);
	}
	
	
	public void fireSpirit(Location loc){
		Zombie spirit = loc.getWorld().spawn(loc, Zombie.class);
		spirit.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 64));
		spirit.getEquipment().setHelmet(new ItemStack(Material.FIRE));
		spirit.getEquipment().setItemInHand(new ItemStack(Material.FIRE));
		if (r.nextInt(5) == 0){
			spirit.setBaby(true);
		} else {
			spirit.setBaby(false);
		}
		if (r.nextInt(24) == 0){
			spirit.setVillager(true);
		} else {
			spirit.setVillager(false);
		}
		minions.add(spirit);
	}
	
	
	public void ignite(Entity entity){
		Iterator<Entity> itr = entity.getNearbyEntities(7, 7, 7).iterator();
		while (itr.hasNext()){
			Entity tEntity = itr.next();
			tEntity.setFireTicks(r.nextInt(151) + 100);
			Location loc = tEntity.getLocation();
			loc.setPitch(loc.getPitch() - loc.getPitch() - loc.getPitch());
			loc.setYaw(loc.getYaw() - loc.getYaw() - loc.getYaw());
			tEntity.teleport(loc);
			tEntity.setVelocity(loc.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(2));
			Location location = loc.clone();
			location.setY(location.getY() + 1);
			ParticleEffect.DRIP_LAVA.animateAtLocation(location, 20, 1);
		}
		ParticleEffect.LAVA.animateAtLocation(entity.getLocation(), 60, 1);
	}
}
