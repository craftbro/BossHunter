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
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.effect.ras.RasEffect;
import code.boss.item.NamedStack;
import code.boss.main.main;

/**
 * The class of the boss "Blazo"
 * Please find a better name.....
 * @author rasmusrune
 */
public class BossBlazo extends Boss{
	public List<Entity> charging = new ArrayList<Entity>();
	public static int timer = 0;
	static int attacks = 0;
	public String bossName = "Blazo";
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String b_v = ChatColor.DARK_RED + "[" + ChatColor.RED + bossName + ChatColor.DARK_RED + "] " + ChatColor.GOLD;
	
	Random r = new Random();
	
	public BossBlazo(main m){
		super(m);
	}

	
	public void start(){
		timer = 0;
		plugin.util.broadcastDelaySound(u_v + "Get away from my house " + plugin.util.randomName() + "! And take all your friends with you!", Sound.AMBIENCE_THUNDER, 1, timer += 60);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Your house?? Its mine!", Sound.VILLAGER_HIT, 1, timer += 70);
		plugin.util.broadcastDelaySound(u_v + "Its MY house and i have MY power to squash you", Sound.AMBIENCE_THUNDER, 1, timer += 80);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Its my house and you stole it! Come down so i can fight you", Sound.VILLAGER_NO, 1, timer += 60);
		plugin.util.broadcastDelaySound(u_v + "I am....", Sound.AMBIENCE_THUNDER, 1, timer += 90);
		plugin.util.broadcastDelaySound(b_v + bossName, Sound.AMBIENCE_THUNDER, 1, timer += 130);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Give me my house back now or prepair to die", Sound.VILLAGER_NO, 1, timer += 70);
		plugin.util.broadcastDelaySound(b_v + "If you really wish to get squashed...", Sound.AMBIENCE_THUNDER, 1, timer += 75);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(new Location(Bukkit.getWorld("world"), -369, 62.5, -8), EntityType.BLAZE);
				boss.setCustomName(ChatColor.RED + bossName);
				boss.setCustomNameVisible(true);
				boss.setMaxHealth(450);
				boss.setHealth(450);
				boss.setRemoveWhenFarAway(false);
				spawned = true;
				attacks = 1;
			}
		}.runTaskLater(plugin, timer += 70);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Hey thats unfair! You fly", Sound.VILLAGER_NO, 1, timer += 140);
		plugin.util.broadcastDelaySound(b_v + "Its your own fault! I gave you a chance to flee", Sound.AMBIENCE_THUNDER, 1, timer += 80);
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
	
	
	public static boolean optimized1 = false;
	public static boolean optimized2 = false;
	public static boolean optimized3 = false;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if (charging.contains(event.getEntity())){
			event.setCancelled(true);
		}
		if (event.getEntity().getUniqueId() == boss.getUniqueId()){
			if (boss.getHealth() / 4.5 <= 70 && !optimized1){
				optimized1 = true;
				attacks = 2;
			}
			if (boss.getHealth() / 4.5 <= 50 && !optimized2){
				optimized2 = true;
				attacks = 3;
				timer = 0;
				plugin.util.broadcastDelaySound(b_v + "I tried to go easy on you. But you like nearly ask me to squash you", Sound.AMBIENCE_THUNDER, 1, timer += 40);
			}
			if (boss.getHealth() / 4.5 <= 30 && !optimized3){
				optimized3 = true;
				attacks = 4;
				timer = 0;
				plugin.util.broadcastDelaySound(b_v + "Why are you even trying to take my house? We could be friends!", Sound.AMBIENCE_THUNDER, 1, timer += 20);
				plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "MY house you mean!", Sound.VILLAGER_NO, 1, timer += 70);
				plugin.util.broadcastDelaySound(b_v + "I will never give my house away! Give up already", Sound.AMBIENCE_THUNDER, 1, timer += 60);
				new BukkitRunnable(){
					public void run(){
						if (!charging.contains(boss)){
							bullscharge(boss, plugin.util.getNearest(boss));
						}
					}
				}.runTaskLater(plugin, timer += 10);
			}
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
			timer = 0;
			plugin.util.broadcastDelaySound(b_v + "I give up. Just take my house!", Sound.AMBIENCE_THUNDER, 1, timer += 70);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "We are not taking your house we are taking our house back!", Sound.VILLAGER_NO, 1, timer += 60);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "If your so sure its your house then give us a proof!", Sound.VILLAGER_YES, 1, timer += 65);
			plugin.util.broadcastDelaySound(b_v + "ok if you want a proof i will give you one. Heres my photobook", Sound.AMBIENCE_THUNDER, 1, timer += 70);
			new BukkitRunnable(){
				public void run(){
					for (Player online : Bukkit.getOnlinePlayers()){
						PlayerInventory inv = online.getInventory();
						inv.addItem(new NamedStack(ChatColor.RED + bossName + "'s Photo Book", Material.BOOK));
					}
				}
			}.runTaskLater(plugin, timer += 20);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Wow its actually true! It is his house!", Sound.VILLAGER_HIT, 1, timer += 100);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Well erhmm... sorry for the inconvenience. I hope you wont squash us because of it", Sound.VILLAGER_IDLE, 1, timer += 60);
			plugin.util.broadcastDelaySound(b_v + "We could have been friends...", Sound.AMBIENCE_THUNDER, 1, timer += 50);
			plugin.util.broadcastDelaySound(ChatColor.YELLOW + "New Friend request from: " + ChatColor.RED + bossName, Sound.LEVEL_UP, 1, timer += 70);
			plugin.util.broadcastDelaySound(ChatColor.GREEN + "Friend request accepted", Sound.LEVEL_UP, 1, timer += 90);
			new BukkitRunnable(){
				public void run(){
					plugin.stop();
				}
			}.runTaskLater(plugin, timer += 60);
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
