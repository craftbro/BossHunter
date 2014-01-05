package code.boss.bosses;





import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.effect.ras.RasEffect;
import code.boss.main.main;

/**
 * The class of the boss "Blazo"
 * Please find a better name.....
 * @author rasmusrune
 */
public class BossUnnamed extends Boss{
	public List<Entity> charging = new ArrayList<Entity>();
	public static long timer = 0;
	static int attacks = 0;
	public String bossName = "Blazo";
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String b_v = ChatColor.DARK_RED + "[" + ChatColor.RED + bossName + ChatColor.DARK_RED + "] " + ChatColor.GOLD;
	
	Random r = new Random();
	
	public BossUnnamed(main m){
		super(m);
	}

	
	public void start(){
		timer = 0;
		boss = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 199, 199, 199), EntityType.BLAZE);
		boss.setCustomName(ChatColor.RED + bossName);
		boss.setCustomNameVisible(true);
		boss.setMaxHealth(450);
		boss.setHealth(450);
		boss.setRemoveWhenFarAway(false);
		spawned = true;
		attacks = 1;
	}
	
	
	public void tick(){
		if (spawned){
			if (attacks >= 1){
				if (r.nextInt(200) == 0){
					fireball(boss);
				}
				if (r.nextInt(180) == 0 && this.getMinionsSize() < 30){
					for (int x = r.nextInt(5) + 3; x > 0; x--){
						fireSpirit(boss.getLocation());
					}
				}
			}
			if (attacks >= 2){
				if (r.nextInt(270) == 0){
					ignite(boss);
				}
			}
			if (attacks >= 3){
				if (r.nextInt(320) == 0){
					for (int x = r.nextInt(21) + 30; x > 0; x--){
						new BukkitRunnable(){
							public void run(){
								rotator(boss);
							}
						}.runTaskLater(plugin, x);
					}
				}
			}
			if (attacks >= 4){
				if (r.nextInt(350) == 0){
					if (!charging.contains(boss)){
						bullscharge(boss, plugin.util.getNearest(boss));
					}
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if (charging.contains(event.getEntity())){
			event.setCancelled(true);
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if (minions.contains(event.getEntity())){
			minions.remove(event.getEntity());
		}
		if (charging.contains(event.getEntity())){
			charging.remove(event.getEntity());
		}
		if (event.getEntity().getUniqueId() == boss.getUniqueId()){
			spawned = false;
			new BukkitRunnable(){
				public void run(){
					plugin.stop();
				}
			}.runTaskLater(plugin, timer += 100);
		}
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
	
	
	public void rotator(LivingEntity entity){
		Location loc = entity.getLocation();
		loc.setYaw(loc.getYaw() + (float) Math.PI * 2);
		entity.teleport(loc);
		entity.launchProjectile(Fireball.class);
		loc.setPitch(loc.getPitch() + ((float) Math.PI * (float) Math.E));
		entity.teleport(loc);
		entity.launchProjectile(Fireball.class);
		loc.setPitch(loc.getPitch() - ((float) Math.PI * (float) Math.E));
		loc.setPitch(loc.getPitch() - ((float) Math.PI * (float) Math.E));
		entity.teleport(loc);
		entity.launchProjectile(Fireball.class);
		loc.setPitch(loc.getPitch() + ((float) Math.PI * (float) Math.E));
		entity.teleport(loc);
	}
	
	
	public void bullscharge(final Entity entity, final Entity target){
		final Location sLoc = entity.getLocation();
		Location tLoc = target.getLocation();
		final Vector vec = tLoc.toVector().subtract(sLoc.toVector()).normalize().multiply(0.6);
		charging.add(entity);
		new BukkitRunnable(){
			int counter = 0;
			Location currentLoc = sLoc.add(vec);
			
			public void run() {
				counter++;
				Location tLoc = target.getLocation();
				Vector vec = tLoc.toVector().subtract(currentLoc.toVector()).normalize().multiply(0.6);
				currentLoc = sLoc.add(vec);
				Iterator<Entity> itr = entity.getNearbyEntities(1, 1, 1).iterator();
				while (itr.hasNext()){
					Entity entity = itr.next();
					if (entity.getUniqueId() == target.getUniqueId()){
						currentLoc.getWorld().playSound(currentLoc, Sound.EXPLODE, 7, 0);
						if (target instanceof LivingEntity){
							((LivingEntity) target).damage(8);
						}
						RasEffect.LARGE_EXPLODE.display(currentLoc, 2, 2, 2, 1, 70);
						target.setVelocity(tLoc.toVector().subtract(sLoc.toVector()).normalize().multiply(2));
						charging.remove(entity);
						this.cancel();
						break;
					}
				}
				if (counter >= 400){
					charging.remove(entity);
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
}
