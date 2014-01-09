package code.boss.bosses;





import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.effect.ras.RasEffect;
import code.boss.item.NamedStack;
import code.boss.item.SkullStack;
import code.boss.main.main;

/**
 * The class of the boss "Magical Queen"
 * @author rasmusrune
 */
public class BossMagic extends Boss implements Listener{
	public static final List<Entity> captured = new ArrayList<Entity>();
	static int attacks = 0;
	public String r_b = ChatColor.BOLD + "" + ChatColor.RED + "[" + ChatColor.GOLD + "Radio Bob" + ChatColor.RED + "] " + ChatColor.RESET + ChatColor.YELLOW;
	public static int timer = 0;

	
	/**
	 * Hey dude
	 * 
	 * Just listing all the things you will need here:
	 * 
	 * The LivingEntity 'boss' is the boss. please use it as you boss entity.
	 * Don't forget to give the boss a custom name, the health bar uses it
	 * 
	 * Set 'spawned' to true when the boss is in the world to make the plugin show its healthbar and stuff
	 * It will glicth when its true but the boss isnt accualy in the world (had some problems with that)
	 * 
	 * You can import events in this class, which is recommned.
	 * 
	 * Play Particle Effects by doing ParticleEffect.EFFECT.animateAtLocation(Location, amount, speed (use 1))
	 * 
	 * do messages with sound and delay with plugin.util.broadcastDelaySound(message, sound, pitch, delay);
	 * 
	 */

	Random r = new Random();
	
	public BossMagic(main m){
		super(m);
	}
	
	
	//called when the game starts
	public void start(){
		timer = 0;
		// Just imported some events for you :) (still need to add this to the API)
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		// => Take a look at this. Just fixed it so i didnt get an error so you still need to modify it to whatever you want <=
		plugin.util.broadcastDelaySound(ChatColor.RED + "New Mission: Destroy the Magical Queen", Sound.LEVEL_UP, 1, timer += 20);
		plugin.util.broadcastDelaySound(ChatColor.RED + "Warning: Fighting it can lead to " + ChatColor.STRIKETHROUGH + "Confusion, Sickness, Blindness, Permanent Damage on Body and or Death" + ChatColor.RESET + ChatColor.RED + " Bad stuff", Sound.LEVEL_UP, 1, timer += 60);
		plugin.util.broadcastDelaySound(ChatColor.GREEN + "Mission Force Accepted!", Sound.LEVEL_UP, 1, timer += 50);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Wait what??", Sound.VILLAGER_HIT, 1, timer += 70);
		plugin.util.broadcastDelaySound(r_b + "Welcome to the mission! I'm going to help you by giving information", Sound.DONKEY_IDLE, 1, timer += 60);
		plugin.util.broadcastDelaySound(r_b + "There's some magical aura nearby", Sound.DONKEY_IDLE, 1, timer += 60);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(new Location(Bukkit.getWorld("world"), 101, 98, 198), EntityType.WITCH);
				boss.setMaxHealth(250);
				boss.setHealth(250);
				boss.setCustomName(ChatColor.LIGHT_PURPLE + "Magical Queen");
				boss.setCustomNameVisible(true);
				attacks = 1;
				spawned = true;
			}
		}.runTaskLater(plugin, timer += 50);
		plugin.util.broadcastDelaySound(r_b + "I found out something! You can't attack it normally", Sound.DONKEY_IDLE, 1, timer += 190);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "How do we do it then?", Sound.VILLAGER_YES, 1, timer += 70);
		plugin.util.broadcastDelaySound(r_b + "You need to kill the red and green orbs to damage it", Sound.DONKEY_IDLE, 1, timer += 60);
		timer = 0;
	}
	
	
	//called every tick
	public void tick(){
		if (r.nextInt(200) == 0){
			for (LivingEntity minion : minions){
				if (minion instanceof Slime){
					RasEffect.HAPPY_VILLAGER.display(minion.getLocation(), 0.5F, 0.5F, 0.5F, 10, 1);
				} else if (minion instanceof MagmaCube){
					RasEffect.FLAME.display(minion.getLocation(), 0.5F, 0.5F, 0.5F, 10, 1);
				}
			}
		}
		if (spawned){
			if (attacks >= 1){
				if (r.nextInt(180) == 0){
					for (int x = r.nextInt(6) + 5; x > 0; x--){
						minions(boss.getLocation());
					}
				}
				if (r.nextInt(100) == 0){
					potion(boss);
				}
			}
			if (attacks >= 2){
				if (r.nextInt(260) == 0){
					undead(boss.getLocation());
				}
			}
			if (attacks >= 3){
				if (r.nextInt(290) == 0){
					batbomb(boss.getEyeLocation());
				}
			}
			if (attacks >= 4){
				if (r.nextInt(350) == 0){
					if (((Witch) boss).getTarget() != null){
						if (((Witch) boss).getTarget() instanceof Player){
							whirlwind(((Witch) boss).getTarget());
						} else {
							whirlwind(randomPlayer());
						}
					} else {
						whirlwind(randomPlayer());
					}
				}
			}
			if (attacks >= 5){
				if (r.nextInt(270) == 0){
					capture(randomPlayer());
				}
			}
			if (attacks >= 6){
				if (r.nextInt(300) == 0){
					final Player target = plugin.util.getNearest(boss);
					for (int x = r.nextInt(11) + 25; x > 0; x--){
						new BukkitRunnable(){
							public void run(){
								potionify(boss, target);
							}
						}.runTaskLater(plugin, x * 2);
					}
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onSlimeSplit(CreatureSpawnEvent event){
		if (event.getSpawnReason() == SpawnReason.SLIME_SPLIT){
			event.setCancelled(true);
		}
	}
	
	
	
	@EventHandler
	public void onBlockSpead(BlockSpreadEvent event){
		event.setCancelled(true);
	}
	
	
	public static boolean optimized1 = false;
	public static boolean optimized2 = false;
	public static boolean optimized3 = false;
	public static boolean optimized4 = false;
	public static boolean optimized5 = false;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		if (entity.getUniqueId() == boss.getUniqueId()){
			if (boss.getHealth() <= 230 && !optimized1){
				optimized1 = true;
				attacks = 2;
				plugin.util.broadcastDelaySound(r_b + "I would just like to warn you. My radar say you are on multiple locations", Sound.DONKEY_IDLE, 1, 4);
				undead(boss.getLocation());
			}
			if (boss.getHealth() <= 190 && !optimized2){
				optimized2 = true;
				attacks = 3;
			}
			if (boss.getHealth() <= 130 && !optimized3){
				optimized3 = true;
				attacks = 4;
				plugin.util.broadcastDelaySound(r_b + "I'm detecting a strong wind energy! Might be strong enough to blow someone up in the air", Sound.DONKEY_IDLE, 1, 10);
				for (Player online : Bukkit.getOnlinePlayers()){
					whirlwind(online);
				}
				plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "We already know it", Sound.VILLAGER_NO, 1, 60);
			}
			if (boss.getHealth() <= 90 && !optimized4){
				optimized4 = true;
				attacks = 5;
			}
			if (boss.getHealth() <= 90 && !optimized5){
				optimized5 = true;
				attacks = 6;
			}
			if (event.getCause() != DamageCause.WITHER){
				event.setCancelled(true);
				ParticleEffect.CLOUD.animateAtLocation(entity.getLocation(), 50, 1);
			}
		} else if (entity.getType() == EntityType.BAT){
			if (event.getCause() != DamageCause.ENTITY_ATTACK){
				event.setCancelled(true);
			}
		} else if (entity.getType() == EntityType.SLIME || entity.getType() == EntityType.MAGMA_CUBE){
			if (event.getCause() == DamageCause.ENTITY_ATTACK){
				event.setCancelled(true);
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		LivingEntity entity = event.getEntity();
		//gets if the dead entity was a minion
		if (minions.contains(entity)){
			minions.remove(entity);
			if (entity.getEquipment().getHelmet().getType() == Material.SKULL_ITEM){
				OfflinePlayer player = Bukkit.getPlayer(((SkullMeta) entity.getEquipment().getHelmet().getItemMeta()).getOwner());
				if (player instanceof Player){
					((Player) player).damage(8);
					ParticleEffect.LARGE_SMOKE.animateAtLocation(((Entity) player).getLocation(), 5, 1);
				}
			}
			if (spawned){
				if (entity.getType() == EntityType.SLIME || entity.getType() == EntityType.MAGMA_CUBE){
					//i didn't use boss.damage(damage) because it doesn't call EntityDamageEvent
					boss.setNoDamageTicks(0);
					boss.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1, 1));
					if (entity.getKiller() != null){
						if (r.nextInt(600) == 0){
							if (entity.getType() == EntityType.SLIME){
								NamedStack greenOrb = new NamedStack(ChatColor.GREEN + "Green Orb", Material.SLIME_BALL);
								List<String> redLore = new ArrayList<String>();
								redLore.add("Earned when killing the minion \"Green Orb\"");
								plugin.util.addLore(greenOrb, redLore);
								plugin.collect.giveItem(entity.getKiller(), greenOrb);
								boss.getKiller().sendMessage(ChatColor.GREEN + "The Item 'Green Orb' was added to your Collection!");
							} else {
								NamedStack redOrb = new NamedStack(ChatColor.RED + "Red Orb", Material.MAGMA_CREAM);
								List<String> redLore = new ArrayList<String>();
								redLore.add("Earned when killing the minion \"Red Orb\"");
								plugin.util.addLore(redOrb, redLore);
								plugin.collect.giveItem(entity.getKiller(), redOrb);
								boss.getKiller().sendMessage(ChatColor.GREEN + "The Item 'Red Orb' was added to your Collection!");
							}
						}
					}
				}
			}
		//} else if (entity == boss){
		// use this, is safer	
		} else if(entity.getUniqueId() == boss.getUniqueId()){
			spawned = false;
			timer = 0;
			if (boss.getKiller() != null){
				if (r.nextInt(60) == 0){
					NamedStack wand = new NamedStack(ChatColor.LIGHT_PURPLE + "Magical Queen's Wand", Material.BLAZE_ROD);
					List<String> wandLore = new ArrayList<String>();
					wandLore.add("Earned when killing the boss \"Magical Queen\"");
					plugin.util.addLore(wand, wandLore);
					plugin.collect.giveItem(boss.getKiller(), wand);
					boss.getKiller().sendMessage(ChatColor.GREEN + "The Item 'Magical Queen's Wand' was added to your Collection!");
				}
			}
			plugin.util.broadcastDelaySound(ChatColor.GOLD + "Congratulations! You beated the Magical Queen" , Sound.ENDERDRAGON_DEATH, 1, timer);
			plugin.util.broadcastDelaySound(r_b + "You beated it! You did it!", Sound.DONKEY_IDLE, 1, timer += 70);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Yeaa! We won! Wooooooo", Sound.VILLAGER_YES, 1, timer += 60);
			plugin.util.broadcastDelaySound(r_b + "Wait! I detect some magical aura similar to the one at the beginning", Sound.DONKEY_IDLE, 1, timer += 54);
			plugin.util.broadcastDelaySound(r_b + "Ahh sorry it was just some stuff from a broken potion", Sound.DONKEY_IDLE, 1, timer += 120);
			plugin.util.broadcastDelaySound(r_b + "Well Congratulations! You won!", Sound.DONKEY_IDLE, 1, timer += 65);
			//plugin.util.broadcastDelaySound(r_b + "Well the one who created this boss dosnt know how to stop the round soo....", Sound.DONKEY_HIT, 1, timer += 60);
			new BukkitRunnable(){
				public void run(){
					plugin.stop();
				}
			}.runTaskLater(plugin, timer += 75);
		}
	}
	
	
	
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event){
		event.setIntensity(boss, 0);
	}
	
	//very nice use of T :)
	//Remember, if you want to use something like this, the boss can only do 1 attack at the time
	// this is how i did it:
	
//	private void attack(){
//		
//		if(this.getMinionsSize() < 9 && r.nextInt(250) == 0){
//	         attack1();
//		}
//		
//		if(r.nextInt(150) == 0){
//	         attack2();
//		}
//		
//		if(r.nextInt(200) == 0){
//	         attack3();
//		}
//		
//		if(r.nextInt(200) == 0){
//	         attack4();
//		}
//	}
	
	//this way, he can do multiple attacks at the time
	
	//i was planning something like that but i dont even know what T does... just saw it in the PlayEffect for multiple inputs
	// T works like object, except i changes to whatever it is ii think. never used it :)
	
	
	public void minions(Location loc){
		if (spawned){
			if (minions.size() < 25){
				for (int x = r.nextInt(6) + 5; x > 0; x--){
					if (r.nextBoolean()){
						Slime sCube = Bukkit.getWorld("world").spawn(loc, Slime.class);
						sCube.setMaximumNoDamageTicks(r.nextInt(20) + 1);
						sCube.setNoDamageTicks(sCube.getMaximumNoDamageTicks());
						sCube.setCustomName(ChatColor.GREEN + "Green Orb");
						sCube.setCustomNameVisible(true);
						if (r.nextInt(3) >= 1){
							sCube.setSize(1);
						} else {
							sCube.setSize(r.nextInt(3) + 1);
						}
						sCube.setRemoveWhenFarAway(false);
						minions.add(sCube);
					} else {
						MagmaCube mCube = Bukkit.getWorld("world").spawn(loc, MagmaCube.class);
						mCube.setMaximumNoDamageTicks(r.nextInt(20) + 1);
						mCube.setNoDamageTicks(mCube.getMaximumNoDamageTicks());
						mCube.setCustomName(ChatColor.RED + "Red Orb");
						mCube.setCustomNameVisible(true);
						if (r.nextInt(3) >= 1){
							mCube.setSize(1);
						} else {
							mCube.setSize(r.nextInt(3) + 1);
						}
						mCube.setRemoveWhenFarAway(false);
						minions.add(mCube);
					}
				}
			}
		}
	}
	
	
	public void potion(LivingEntity entity){
		if (spawned){
			ThrownPotion potion = entity.launchProjectile(ThrownPotion.class);
			potion.getEffects().clear();
			int rEffect = r.nextInt(4);
			if (rEffect == 0){
				potion.getEffects().add(new PotionEffect(PotionEffectType.BLINDNESS, 190, 0));
			} else if (rEffect == 1){
				potion.getEffects().add(new PotionEffect(PotionEffectType.CONFUSION, 160, 0));
			} else if (rEffect == 2){
				potion.getEffects().add(new PotionEffect(PotionEffectType.JUMP, 150, 99));
			} else if (rEffect == 3){
				potion.getEffects().add(new PotionEffect(PotionEffectType.SLOW, 140, 2));
			}
		}
	}
	
	
	public void undead(Location loc){
		if (spawned){
			if (minions.size() < 35){
				Skeleton undead = loc.getWorld().spawn(loc, Skeleton.class);
				undead.setMaximumNoDamageTicks(r.nextInt(20) + 1);
				undead.setNoDamageTicks(undead.getMaximumNoDamageTicks());
				undead.setCustomName(ChatColor.GREEN + "Green Orb");
				undead.setCustomNameVisible(true);
				undead.setRemoveWhenFarAway(false);
				undead.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999999, 0));
				undead.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999999, 0));
				EntityEquipment equipment = undead.getEquipment();
				equipment.setHelmet(new SkullStack(randomPlayer().getName()));
				equipment.setHelmetDropChance(0);
				minions.add(undead);
			}
		}
	}
	
	
	public void batbomb(Location loc){
		if (spawned){
			final Bat bat = loc.getWorld().spawn((Location) loc, Bat.class);
			bat.setVelocity(randomPlayer().getLocation().toVector().subtract(loc.toVector()));
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
				public void run(){
					bat.getWorld().createExplosion(bat.getLocation().getX(), bat.getLocation().getY(), bat.getLocation().getZ(), 3, false, false);
					bat.remove();
				}
			}, r.nextInt(40) + 41);
		}
	}
	
	
	public void whirlwind(final Entity entity){
		if (!captured.contains(entity)){
			for (int x = 0; x < 15; x++){
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
					public void run(){
						entity.setVelocity(new Vector(0, 1, 0));
						ParticleEffect.CLOUD.animateAtLocation(entity.getLocation(), 65, 1);
						entity.getWorld().playSound(entity.getLocation(), Sound.BAT_TAKEOFF, 20, 0.6F);
					}
				}, x * 4);
			}
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
				public void run(){
					LivingEntity chicken = (LivingEntity) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.CHICKEN);
					chicken.setMaxHealth(r.nextInt(11) + 5);
					chicken.setHealth(10);
					chicken.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999999999, 0));
					entity.setPassenger(chicken);
				}
			}, 15 * 4);
		}
	}
	
	
	public void capture(final LivingEntity entity){
		Location loc = entity.getEyeLocation().getBlock().getLocation();
		final short[] bCode = { 
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,0,0,2,2,0,0,2,2,2,2,2,2,2,2,2,2,0,0,2,2,0,0,2,2,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
		};
		List<Block> blocks = new ArrayList<Block>();
		final Location minLoc = loc.clone();
		minLoc.setX(minLoc.getX() - 2);
		minLoc.setY(minLoc.getY() - 2);
		minLoc.setZ(minLoc.getZ() - 2);
		final Location maxLoc = loc.clone();
		maxLoc.setX(maxLoc.getX() + 2);
		maxLoc.setY(maxLoc.getY() + 2);
		maxLoc.setZ(maxLoc.getZ() + 2);
		captured.add(entity);
		int num = -1;
		for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
			for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
				for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
					num++;
					Location location = new Location(entity.getWorld(), x, y, z);
					if (location.getBlock().isEmpty()){
						if (bCode[num] == 1){
							location.getBlock().setType(Material.SMOOTH_BRICK);
							ParticleEffect.MOB_SPELL.animateAtLocation(location, 4, 1);
							blocks.add(location.getBlock());
						} else if (bCode[num] == 2){
							location.getBlock().setType(Material.IRON_FENCE);
							ParticleEffect.MOB_SPELL.animateAtLocation(location, 4, 1);
							blocks.add(location.getBlock());
						}
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
			public void run(){
				captured.remove(entity);
				for (int x = minLoc.getBlockX(); x < maxLoc.getBlockX(); x++){
					for (int y = minLoc.getBlockY(); y < maxLoc.getBlockY(); y++){
						for (int z = minLoc.getBlockZ(); z < maxLoc.getBlockZ(); z++){
							Location location = new Location(entity.getWorld(), x, y, z);
							if (location.getBlock().getType() == Material.SMOOTH_BRICK || location.getBlock().getType() == Material.IRON_FENCE){
								ParticleEffect.MOB_SPELL.animateAtLocation(location, 4, 1);
								location.getBlock().setType(Material.AIR);
							}
						}
					}
				}
			}
		}, 160);
	}
	
	
	public void potionify(LivingEntity shooter, Entity target){
		if (spawned){
			ThrownPotion potion = shooter.launchProjectile(ThrownPotion.class);
			potion.getEffects().clear();
			int rEffect1 = r.nextInt(4);
			int rEffect2 = r.nextInt(3);
			if (rEffect1 == 0){
				potion.getEffects().add(new PotionEffect(PotionEffectType.BLINDNESS, 190, 0));
				if (rEffect2 == 0){
					potion.getEffects().add(new PotionEffect(PotionEffectType.CONFUSION, 160 / 2, 0));
				} else if (rEffect2 == 1){
					potion.getEffects().add(new PotionEffect(PotionEffectType.JUMP, 150 / 2, 99));
				} else if (rEffect2 == 2){
					potion.getEffects().add(new PotionEffect(PotionEffectType.SLOW, 140 / 2, 2));
				}
			} else if (rEffect1 == 1){
				potion.getEffects().add(new PotionEffect(PotionEffectType.CONFUSION, 160, 0));
				if (rEffect2 == 0){
					potion.getEffects().add(new PotionEffect(PotionEffectType.BLINDNESS, 190 / 2, 0));
				} else if (rEffect2 == 1){
					potion.getEffects().add(new PotionEffect(PotionEffectType.JUMP, 150 / 2, 99));
				} else if (rEffect2 == 2){
					potion.getEffects().add(new PotionEffect(PotionEffectType.SLOW, 140 / 2, 2));
				}
			} else if (rEffect1 == 2){
				potion.getEffects().add(new PotionEffect(PotionEffectType.JUMP, 150, 99));
				if (rEffect2 == 0){
					potion.getEffects().add(new PotionEffect(PotionEffectType.BLINDNESS, 190 / 2, 0));
				} else if (rEffect2 == 1){
					potion.getEffects().add(new PotionEffect(PotionEffectType.CONFUSION, 160 / 2, 0));
				} else if (rEffect2 == 2){
					potion.getEffects().add(new PotionEffect(PotionEffectType.SLOW, 140 / 2, 2));
				}
			} else if (rEffect1 == 3){
				potion.getEffects().add(new PotionEffect(PotionEffectType.SLOW, 140, 2));
				if (rEffect2 == 0){
					potion.getEffects().add(new PotionEffect(PotionEffectType.BLINDNESS, 190 / 2, 0));
				} else if (rEffect2 == 1){
					potion.getEffects().add(new PotionEffect(PotionEffectType.CONFUSION, 160 / 2, 0));
				} else if (rEffect2 == 2){
					potion.getEffects().add(new PotionEffect(PotionEffectType.JUMP, 150 / 2, 99));
				}
			}
			Vector vec = target.getLocation().toVector().subtract(shooter.getLocation().toVector());
			vec.setY(vec.getY() * 2.5);
			potion.setVelocity(vec);
		}
	}
	
	
	public Player randomPlayer(){
		return Bukkit.getOnlinePlayers()[r.nextInt(Bukkit.getOnlinePlayers().length)];
	}
}
