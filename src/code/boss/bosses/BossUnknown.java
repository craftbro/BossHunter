package code.boss.bosses;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.main.main;

/**
 * The class of the boss "Unknown" (his real name is "Squiddy")
 * @author rasmusrune
 */
public class BossUnknown extends Boss implements Listener{
	static int attacks = 0;
	public static int timer = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String s_v = ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "Squiddy" + ChatColor.DARK_BLUE + "] " + ChatColor.AQUA;
	public boolean shield = true;
	public int timesDeath = 0;
	
	Random r = new Random();
	
	public BossUnknown(main m){
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
				attacks = 1;
				spawned = true;
			}
		}.runTaskLater(plugin, timer += 80);
	}
	
	
	public void tick(){
		if (spawned){
			if(!boss.getLocation().getChunk().isLoaded()){
				boss.getLocation().getChunk().load();
			}
			if (shield){
				if (attacks >= 1){
					if (r.nextInt(240) == 0){
						if (getMinionsSize() < 5){
							skellies(randomPlayer());
						}
					}
				}
				if (attacks >= 2){
					if (r.nextInt(279) == 0){
						if (!flameForm(randomPlayer(), randomPlayer())){
							if (!flameForm(randomPlayer(), randomPlayer())){
								flameForm(randomPlayer(), randomPlayer());
							}
						}
					}
				}
				if (attacks >= 3){
					if (r.nextInt(280) == 0){
						smite(randomPlayer());
					}
				}
				if (attacks >= 4){
					if (r.nextInt(340) == 0){
						lazer(randomPlayer().getLocation());
					}
					if (r.nextInt(300) == 0){
						shock(randomPlayer());
					}
				}
				if (attacks >= 5){
					if (r.nextInt(300 + r.nextInt(101)) == 0){
						Player target = randomPlayer();
						annoyer(target, target.getWorld().spawnEntity(target.getLocation(), EntityType.PIG));
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
	
	
	public static boolean optimized1 = false;
	public static boolean optimized2 = false;
	public static boolean optimized3 = false;
	public static boolean optimized4 = false;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if (spawned){
			if (event.getEntity().getUniqueId() == boss.getUniqueId()){
				if (event.getCause() != null && event.getCause() == DamageCause.SUFFOCATION){
					event.setCancelled(true);
				}
			}
			if (spawned && boss.getHealth() / 5.25 <= 80 && !optimized1){
				optimized1 = true;
				attacks = 2;
			}
			if (spawned && boss.getHealth() / 5.25 <= 60 && !optimized2){
				optimized2 = true;
				attacks = 3;
			}
			if (spawned && boss.getHealth() / 5.25 <= 40 && !optimized3){
				optimized3 = true;
				attacks = 4;
			}
			if (spawned && boss.getHealth() / 5.25 <= 30 && !optimized4){
				optimized4 = true;
				attacks = 5;
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if (spawned){
			if (boss.getUniqueId() == event.getEntity().getUniqueId()){
				spawned = false;
				if (shield){
					shield = false;
					timer = 0;
					new BukkitRunnable(){
						public void run(){
							boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(randomPlayer().getLocation(), EntityType.SQUID);
							boss.setMaxHealth(25);
							boss.setHealth(25);
							boss.setCustomName(ChatColor.BLUE + "Squiddy");
							boss.setCustomNameVisible(true);
							boss.setCanPickupItems(false);
							boss.setRemoveWhenFarAway(false);
							attacks = 1;
							spawned = true;
							timesDeath++;
						}
					}.runTaskLater(plugin, timer += 90);
				} else {
					if (timesDeath == 1){
						new BukkitRunnable(){
							public void run(){
								boss.setMaxHealth(20);
								boss.setHealth(20);
								attacks = 1;
								spawned = true;
								timesDeath++;
							}
						}.runTaskLater(plugin, 2);
					} else if (timesDeath == 2){
						new BukkitRunnable(){
							public void run(){
								boss.setMaxHealth(10);
								boss.setHealth(10);
								attacks = 1;
								spawned = true;
								timesDeath++;
							}
						}.runTaskLater(plugin, 4);
					} else {
						timesDeath++;
						boss.remove();
						new BukkitRunnable(){
							public void run(){
								plugin.stop();
							}
						}.runTaskLater(plugin, 90);
					}
				}
			}
		}
		if (minions.contains(event.getEntity())){
			minions.remove(event.getEntity());
		}
	}
	
	
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event){
		event.setCancelled(true);
	}
	
	
	
	public void skellies(final Entity target){
		Location loc = target.getLocation();
		loc.setY(loc.getY() + 3.35);
		final Skeleton leader = (Skeleton) target.getWorld().spawnEntity(loc, EntityType.SKELETON);
		for (int x = 0; x < r.nextInt(6) + 5; x++){
			final Skeleton skellyton = (Skeleton) target.getWorld().spawnEntity(loc, EntityType.SKELETON);
			skellyton.getEquipment().setItemInHand(new ItemStack(Material.BOW));
			minions.add(skellyton);
			new BukkitRunnable(){
				public void run(){
					if (skellyton.isValid() && !skellyton.isDead()){
						minions.remove(skellyton);
						skellyton.setHealth(0);
					}
				}
			}.runTaskLater(plugin, r.nextInt(201) + 200);
		}
		leader.setSkeletonType(SkeletonType.WITHER);
		target.setPassenger(leader);
		leader.setMaxHealth(4);
		leader.setHealth(4);
		leader.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SWORD));
		minions.add(leader);
		new BukkitRunnable(){
			public void run(){
				if (leader.isValid() && !leader.isDead()){
					minions.remove(leader);
					leader.setHealth(0);
				}
			}
		}.runTaskLater(plugin, r.nextInt(101) + 100);
	}
	
	
	public boolean flameForm(final LivingEntity from, final LivingEntity to){
		if (from.getUniqueId() == to.getUniqueId()){
			return false;
		}
		final Location sLocOriginal = from.getEyeLocation();
		final Location sLoc = from.getEyeLocation();
		Location tLoc = to.getEyeLocation();
		final Vector vec = tLoc.toVector().subtract(sLoc.toVector()).normalize().multiply(0.2);
		new BukkitRunnable(){
			int counter = 0;
			Location currentLoc = sLoc.add(vec);
			
			public void run() {
				counter++;
				Location tLoc = to.getLocation();
				Vector vec = tLoc.toVector().subtract(currentLoc.toVector()).normalize().multiply(0.2);
				currentLoc = sLoc.add(vec);
				ParticleEffect.FLAME.animateAtLocation(currentLoc, 1, 1);
				Item item = currentLoc.getWorld().dropItem(currentLoc, new ItemStack(Material.FIRE));
				item.setPickupDelay(99999);
				Iterator<Entity> itr = item.getNearbyEntities(1, 1, 1).iterator();
				item.remove();
				while (itr.hasNext()){
					Entity entity = itr.next();
					if (entity.getUniqueId() == to.getUniqueId()){
						currentLoc.getWorld().playSound(currentLoc, Sound.BURP, 4, 0.7F);
						from.teleport(to.getLocation());
						to.setVelocity(tLoc.toVector().subtract(sLocOriginal.toVector()).normalize().multiply(1.14));
						this.cancel();
						break;
					}
				}
				if (counter >= 500){
					currentLoc.getWorld().playSound(currentLoc, Sound.BURP, 4, 0.7F);
					from.teleport(currentLoc);
					to.setVelocity(tLoc.toVector().subtract(currentLoc.toVector()).normalize().multiply(1.14));
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		return true;
	}
	
	
	public void smite(final Entity target){
		final Location sLoc = target.getLocation();
		Location tLoc = target.getLocation();
		sLoc.setY(sLoc.getY() + 100);
		final Vector vec = tLoc.toVector().subtract(sLoc.toVector()).normalize();
		vec.setY(-0.5);
		new BukkitRunnable(){
			int counter = 0;
			Location currentLoc = sLoc.add(vec);
			
			public void run() {
				Location tLoc = target.getLocation();
				Vector vec = tLoc.toVector().subtract(currentLoc.toVector()).normalize();
				vec.setY(-0.5);
				currentLoc = sLoc.add(vec);
				ParticleEffect.ENCHANTMENT_TABLE.animateAtLocation(currentLoc, 1, 1);
				if (counter >= 40){
					counter = 0;
					ParticleEffect.CLOUD.animateAtLocation(currentLoc, 10, 1);
				}
				counter++;
				Item item = currentLoc.getWorld().dropItem(currentLoc, new ItemStack(Material.SUGAR));
				item.setPickupDelay(99999);
				Iterator<Entity> itr = item.getNearbyEntities(1, 1, 1).iterator();
				item.remove();
				while (itr.hasNext()){
					Entity entity = itr.next();
					if (entity.getUniqueId() == target.getUniqueId()){
						currentLoc.getWorld().playSound(currentLoc, Sound.AMBIENCE_THUNDER, 4, 1);
						target.setFireTicks(target.getFireTicks() + 65);
						damageWithEvent(target, 4, DamageCause.LIGHTNING, false);
						this.cancel();
						break;
					}
				}
				if (!currentLoc.getBlock().isEmpty() && !currentLoc.getBlock().isLiquid()){
					currentLoc.getWorld().playSound(currentLoc, Sound.AMBIENCE_THUNDER, 4, 1);
					target.setFireTicks(target.getFireTicks() + 65);
					damageWithEvent(target, 4, DamageCause.LIGHTNING, false);
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	
	public void lazer(final Location loc){
		final Location loc2 = loc.clone();
		loc2.setY(loc2.getY() - 1);
		new BukkitRunnable(){
			public void run(){
				loc.getWorld().playEffect(loc2, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
				new BukkitRunnable(){
					public void run(){
						loc.getWorld().playEffect(loc2, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
						new BukkitRunnable(){
							public void run(){
								loc.getWorld().playEffect(loc2, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
								new BukkitRunnable(){
									public void run(){
										final Location tempLoc = loc.clone();
										for (int x = 0; x < 35; x++){
											new BukkitRunnable(){
												public void run(){
													tempLoc.setX(tempLoc.getX() + 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setZ(tempLoc.getZ() + 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setX(tempLoc.getX() - 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setX(tempLoc.getX() - 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setX(tempLoc.getX() - 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setZ(tempLoc.getZ() - 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setZ(tempLoc.getX() - 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setZ(tempLoc.getX() - 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setX(tempLoc.getX() + 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													tempLoc.setX(tempLoc.getX() + 1);
													tempLoc.getWorld().playEffect(tempLoc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
													loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.SNOW_BLOCK);
													Item item = loc.getWorld().dropItem(loc, new ItemStack(Material.SNOW_BLOCK));
													Iterator<Entity> itr = item.getNearbyEntities(2, 2, 2).iterator();
													while (itr.hasNext()){
														Entity entity = itr.next();
														damageWithEvent(entity, r.nextInt(6) + 10, DamageCause.FIRE, false);
														entity.setFireTicks(entity.getFireTicks() + 12);
													}
													tempLoc.setY(tempLoc.getY() - 1);
													item.remove();
												}
											}.runTaskLater(plugin, x);
										}
									}
								}.runTaskLater(plugin, 71);
							}
						}.runTaskLater(plugin, 43);
					}
				}.runTaskLater(plugin, 43);
			}
		}.runTaskLater(plugin, 43);
	}
	
	
	public void shock(final Damageable target){
		new BukkitRunnable(){
			int counter = r.nextInt(71) + 130;
			
			public void run(){
				counter++;
				if (target instanceof LivingEntity){
					int maxNoDamageTicks = ((LivingEntity) target).getMaximumNoDamageTicks();
					int noDamageTicks = ((LivingEntity) target).getNoDamageTicks();
					((LivingEntity) target).setMaximumNoDamageTicks(0);
					((LivingEntity) target).setNoDamageTicks(0);
					target.damage(1);
					plugin.util.heal((LivingEntity) target, 1);
					((LivingEntity) target).setMaximumNoDamageTicks(maxNoDamageTicks);
					((LivingEntity) target).setNoDamageTicks(noDamageTicks);
				}
				if (counter <= 0){
					this.cancel();
				}
			}
		}.runTaskLater(plugin, 1);
	}
	
	
	public void annoyer(final LivingEntity target, final Entity annoyer){
		new BukkitRunnable(){
			int counter = 0;
			
			public void run() {
				counter++;
				if (annoyer instanceof Damageable){
					((Damageable) annoyer).setMaxHealth(Float.MAX_VALUE);
					((Damageable) annoyer).setHealth(((Damageable) annoyer).getMaxHealth());
				}
				ParticleEffect.MOB_SPELL_AMBIENT.animateAtLocation(target.getEyeLocation(), 15, 1);
				annoyer.teleport(target.getEyeLocation());
				if (counter >= r.nextInt(201) + 300){
					annoyer.remove();
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	
	public void damageWithEvent(Entity entity, float damage, DamageCause cause, boolean ignoreEventChanges){
		EntityDamageEvent event = new EntityDamageEvent(entity, cause, damage);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getEntity() instanceof Damageable){
			if (!ignoreEventChanges){
				if (!event.isCancelled()){
					((Damageable) event.getEntity()).damage(event.getDamage());
					event.getEntity().setLastDamageCause(event);
				}
			} else {
				((Damageable) entity).damage(damage);
				event.getEntity().setLastDamageCause(event);
			}
		}
	}
	
	
	public void damageWithEvent(Entity entity, float damage, DamageCause cause, Entity damager, boolean ignoreEventChanges){
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, entity, cause, damage);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getEntity() instanceof Damageable){
			if (!ignoreEventChanges){
				if (!event.isCancelled()){
					((Damageable) event.getEntity()).damage(event.getDamage(), event.getDamager());
					event.getEntity().setLastDamageCause(event);
				}
			} else {
				((Damageable) entity).damage(damage, damager);
				event.getEntity().setLastDamageCause(event);
			}
		}
	}
	
	
	public Player randomPlayer(){
		return Bukkit.getOnlinePlayers()[r.nextInt(Bukkit.getOnlinePlayers().length)];
	}
}
