package code.boss.bosses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.effect.ras.RasEffect;
import code.boss.main.main;

public class BossSpider extends Boss implements Listener{
	static int diseases = 0;
	public static int timer = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String v_v = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "Venommy" + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA;
	public HashMap<String, Integer> dizzy = new HashMap<String, Integer>();
	public HashMap<String, Integer> fear = new HashMap<String, Integer>();
	public HashMap<String, Integer> poison = new HashMap<String, Integer>();
	public HashMap<String, Integer> vulnerable = new HashMap<String, Integer>();
	public HashMap<String, Integer> noises = new HashMap<String, Integer>();
	public boolean shield = false;
	public boolean firstDisease = true;
	
	Random r = new Random();
	
	public BossSpider(main m){
		super(m);
	}
	
	
	public void start(){
		timer = 0;
		plugin.util.broadcastDelaySound("Once upon a time", Sound.CHEST_OPEN, 0, timer += 80);
		if (Bukkit.getOnlinePlayers().length < 2){
			plugin.util.broadcastDelaySound("One brave player ran out to save his town", Sound.DIG_GRAVEL, 1, timer += 70);
		} else {
			plugin.util.broadcastDelaySound(Bukkit.getOnlinePlayers().length + " brave players ran out to save their town", Sound.DIG_GRAVEL, 0, timer += 70);
		}
		plugin.util.broadcastDelaySound("Because the villagers got killed by the dangerous Spider named Venommy", Sound.ANVIL_LAND, 1, timer += 75);
		plugin.util.broadcastDelaySound("But since Venommy is so amazingly strong and amazing they dont have a chance", Sound.CHICKEN_HURT, 1, timer += 70);
		final Player screamer = randomPlayer();
		plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+screamer.getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "What?? And just so you know this isn't a Story", Sound.VILLAGER_NO, 1, timer += 60);
		new BukkitRunnable(){
			public void run(){
				screamer.teleport(new Location(screamer.getWorld(), screamer.getLocation().getX(), screamer.getLocation().getY() - 1, screamer.getLocation().getZ()));
				screamer.damage(3);
			}
		}.runTaskLater(plugin, timer += 20);
		plugin.util.broadcastDelaySound(v_v + "Ok ok im the amazing " + ChatColor.LIGHT_PURPLE + "Venommy" + ChatColor.AQUA + " But you still dont have a chance to help your town!", Sound.SPIDER_IDLE, 1, timer += 80);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "We will see...", Sound.VILLAGER_HAGGLE, 1, timer += 68);
		plugin.util.broadcastDelaySound(v_v + "Yes we will right now!", Sound.SPIDER_IDLE, 1, timer += 70);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(plugin.arena.getSpawn(), EntityType.SPIDER);
				boss.setMaxHealth(550);
				boss.setHealth(boss.getMaxHealth());
				boss.setCustomName(ChatColor.LIGHT_PURPLE + "Venommy");
				boss.setCustomNameVisible(true);
				boss.setCanPickupItems(false);
				boss.setRemoveWhenFarAway(false);
				diseases = 1;
				spawned = true;
				shield = false;
				startDiseaseWorker();
			}
		}.runTaskLater(plugin, timer -= 20);
	}
	
	
	public void tick(){
		if (spawned){
			boss.getWorld().setTime(14000);
			if(!boss.getLocation().getChunk().isLoaded()){
				boss.getLocation().getChunk().load();
			}
			if (diseases >= 1){
				if (r.nextInt(100) == 0){
					if (getMinionsSize() < 9){
						spiderling(boss.getLocation(), Disease.DIZZY);
					}
				}
			}
			if (diseases >= 2){
				if (r.nextInt(160) == 0){
					if (getMinionsSize() < 17){
						spiderling(boss.getLocation(), Disease.FEAR);
					}
				}
			}
			if (diseases >= 3){
				if (r.nextInt(220) == 0){
					if (getMinionsSize() < 21){
						spiderling(boss.getLocation(), Disease.POISON);
					}
				}
			}
			if (diseases >= 4){
				if (r.nextInt(280) == 0){
					if (getMinionsSize() < 25){
						spiderling(boss.getLocation(), Disease.VULNERABILITY);
					}
				}
			}
			if (diseases >= 5){
				if (r.nextInt(360) == 0){
					if (getMinionsSize() < 30){
						spiderling(boss.getLocation(), Disease.NOISES);
					}
				}
			}
		}
	}
	
	
	public static boolean optimized1 = false;
	public static boolean optimized2 = false;
	public static boolean optimized3 = false;
	public static boolean optimized4 = false;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if (event.getEntity() instanceof Player){
			if (vulnerable.containsKey(((Player) event.getEntity()).getName())){
				event.setDamage(event.getDamage() * 1.5);
			}
		}
		if (spawned){
			if (event.getEntity().getUniqueId() == boss.getUniqueId()){
				if (event.getCause() != null && event.getCause() == DamageCause.SUFFOCATION){
					event.setCancelled(true);
				}
			}
			if (spawned && boss.getHealth() / 5.5 <= 80 && !optimized1){
				optimized1 = true;
				diseases = 2;
			}
			if (spawned && boss.getHealth() / 5.5 <= 70 && !optimized2){
				optimized2 = true;
				diseases = 3;
			}
			if (spawned && boss.getHealth() / 5.5 <= 55 && !optimized3){
				optimized3 = true;
				diseases = 4;
				shield = true;
			}
			if (spawned && boss.getHealth() / 5.5 <= 30 && !optimized4){
				optimized4 = true;
				diseases = 5;
			}
			if (shield){
				event.setDamage(event.getDamage() / 1.6);
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof CaveSpider){
			if (event.getEntity() instanceof Player){
				String name = ((CaveSpider) event.getDamager()).getCustomName();
				if (name == Disease.DIZZY.getName()){
					if (!dizzy.containsKey(((Player) event.getEntity()).getName())){
						dizzy.put(((Player) event.getEntity()).getName(), r.nextInt(21) + 20);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with " + ((CaveSpider) event.getDamager()).getCustomName());
					}
				} else if (name == Disease.FEAR.getName()){
					if (!fear.containsKey(((Player) event.getEntity()).getName())){
						fear.put(((Player) event.getEntity()).getName(), r.nextInt(51) + 50);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with " + ((CaveSpider) event.getDamager()).getCustomName());
					}
				} else if (name == Disease.NOISES.getName()){
					if (!noises.containsKey(((Player) event.getEntity()).getName())){
						noises.put(((Player) event.getEntity()).getName(), r.nextInt(11) + 10);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with " + ((CaveSpider) event.getDamager()).getCustomName());
					}
				} else if (name == Disease.POISON.getName()){
					if (!poison.containsKey(((Player) event.getEntity()).getName())){
						poison.put(((Player) event.getEntity()).getName(), r.nextInt(11) + 5);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with " + ((CaveSpider) event.getDamager()).getCustomName());
					}
				} else if (name == Disease.VULNERABILITY.getName()){
					if (!vulnerable.containsKey(((Player) event.getEntity()).getName())){
						vulnerable.put(((Player) event.getEntity()).getName(), r.nextInt(16) + 15);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with " + ((CaveSpider) event.getDamager()).getCustomName());
					}
				}
				if (r.nextInt(3) != 0){
					ParticleEffect.FIREWORK_SPARK.animateAtLocation(event.getDamager().getLocation(), 10, (float) 0.8);
					event.getDamager().remove();
				}
			}
		} else if (spawned && event.getDamager().getUniqueId() == boss.getUniqueId()){
			if (event.getEntity() instanceof Player){
				int rDisease = r.nextInt(101);
				if (rDisease < 39){
					if (!dizzy.containsKey(((Player) event.getEntity()).getName()) && diseases > 0){
						dizzy.put(((Player) event.getEntity()).getName(), r.nextInt(21) + 20);
						RasEffect.SLIME.display(event.getDamager().getLocation(), (float) 0.5, (float) 0.5, (float) 0.5, 10, 1);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with Dizzy");
					}
				} else if (rDisease < 59){
					if (!fear.containsKey(((Player) event.getEntity()).getName()) && diseases > 1){
						fear.put(((Player) event.getEntity()).getName(), r.nextInt(51) + 50);
						RasEffect.SLIME.display(event.getDamager().getLocation(), (float) 0.5, (float) 0.5, (float) 0.5, 10, 1);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with Fear");
					}
				} else if (rDisease < 69){
					if (!poison.containsKey(((Player) event.getEntity()).getName()) && diseases > 2){
						poison.put(((Player) event.getEntity()).getName(), r.nextInt(11) + 5);
						RasEffect.SLIME.display(event.getDamager().getLocation(), (float) 0.5, (float) 0.5, (float) 0.5, 10, 1);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with Poison");
					}
				} else if (rDisease < 74){
					if (!vulnerable.containsKey(((Player) event.getEntity()).getName()) && diseases > 3){
						vulnerable.put(((Player) event.getEntity()).getName(), r.nextInt(16) + 15);
						RasEffect.SLIME.display(event.getDamager().getLocation(), (float) 0.5, (float) 0.5, (float) 0.5, 10, 1);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with Vulnerability");
					}
				} else if (rDisease <  88){
					if (!noises.containsKey(((Player) event.getEntity()).getName()) && diseases > 4){
						noises.put(((Player) event.getEntity()).getName(), r.nextInt(11) + 10);
						RasEffect.SLIME.display(event.getDamager().getLocation(), (float) 0.5, (float) 0.5, (float) 0.5, 10, 1);
						if (firstDisease){
							plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+((Player) event.getEntity()).getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "Ouch! she infected me!", Sound.VILLAGER_DEATH, 1, 4);
							firstDisease = false;
						}
						((Player) event.getEntity()).sendMessage(ChatColor.RED + "You got infected with Noises");
					}
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if (minions.contains(event.getEntity())){
			minions.remove(event.getEntity());
		}
		if (spawned && event.getEntity().getUniqueId() == boss.getUniqueId()){
			spawned = false;
			timer = 0;
			dizzy.clear();
			fear.clear();
			noises.clear();
			vulnerable.clear();
			poison.clear();
			new BukkitRunnable(){
				public void run(){
					plugin.stop();
				}
			}.runTaskLater(plugin, timer += 90);
		}
	}
	
	
	public void spiderling(Location loc, Disease disease){
		final CaveSpider spider = loc.getWorld().spawn(loc,CaveSpider.class);
		spider.setCustomName(disease.getName());
		spider.setCustomNameVisible(true);
		spider.setMaxHealth(r.nextInt(4) + 2);
		spider.setHealth(spider.getMaxHealth());
		spider.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, r.nextInt(2)));
		minions.add(spider);
		new BukkitRunnable(){
			public void run(){
				if (spider.isValid() && !spider.isDead()){
					minions.remove(spider);
					spider.setHealth(0);
				}
			}
		}.runTaskLater(plugin, r.nextInt(151) + 150);
	}
	
	
	public Player randomPlayer(){
		return Bukkit.getOnlinePlayers()[r.nextInt(Bukkit.getOnlinePlayers().length)];
	}
	
	/**
	 * Gets the nearest Entity
	 * @param from The entity to check from
	 * @param radius The radius to check with
	 * @param onlyLiving If it should only be living entities
	 * @return The nearest Entity
	 * @Warning will return null if none found
	 */
	public Entity getNearestEntityInRadius(Entity from, double radius, boolean onlyLiving){
		Entity nearest = null;
		double distance = Double.MAX_VALUE;
		List<Entity> entities = from.getNearbyEntities(radius, radius, radius);
		for(Entity check : entities){
			if(check.getLocation().distance(from.getLocation()) < distance){
				if (onlyLiving){
					if (check instanceof LivingEntity){
						nearest = check;
						distance = check.getLocation().distance(from.getLocation());
					}
				} else {
					nearest = check;
					distance = check.getLocation().distance(from.getLocation());
				}
			}
		}
		return nearest;
	}
	
	public void startDiseaseWorker(){
		new BukkitRunnable(){
			public void run(){
				for (String playerName : dizzy.keySet()){
					if (Bukkit.getOfflinePlayer(playerName).isOnline()){
						Player player = Bukkit.getPlayer(playerName);
						Vector vec = new Vector((double) (r.nextInt(21) - 10) / 10,(double) (r.nextInt(21) - 10) / 10,(double) (r.nextInt(21) - 10) / 10).multiply(0.125);
						vec.setY((double) r.nextInt(4 + r.nextInt(4) - 1) / 100);
						if (r.nextBoolean()){
							player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, r.nextInt(4) + 1, 2));
						}
						player.setVelocity(vec);
						if (dizzy.get(playerName) < 0){
							dizzy.remove(playerName);
						} else {
							Integer counter = dizzy.get(playerName);
							dizzy.remove(playerName);
							dizzy.put(playerName, counter - 1);
						}
					} else {
						dizzy.remove(playerName);
					}
				}
			}
		}.runTaskTimer(plugin, 4, 4);
		new BukkitRunnable(){
			public void run(){
				for (String playerName : fear.keySet()){
					if (Bukkit.getOfflinePlayer(playerName).isOnline()){
						Player player = Bukkit.getPlayer(playerName);
						if (getNearestEntityInRadius(player, 7.45, true) != null){
							Vector vec = player.getLocation().toVector().subtract(getNearestEntityInRadius(player, 8, true).getLocation().toVector()).normalize().multiply(0.14);
							vec.setY(0);
							player.setVelocity(vec);
						}
						if (fear.get(playerName) < 0){
							fear.remove(playerName);
						} else {
							Integer counter = fear.get(playerName);
							fear.remove(playerName);
							fear.put(playerName, counter - 1);
						}
					} else {
						fear.remove(playerName);
					}
				}
			}
		}.runTaskTimer(plugin, 2, 2);
		new BukkitRunnable(){
			public void run(){
				for (String playerName : poison.keySet()){
					if (Bukkit.getOfflinePlayer(playerName).isOnline()){
						Player player = Bukkit.getPlayer(playerName);
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 16, 2));
						player.damage((r.nextInt(6) + 5) / 10);
						if (poison.get(playerName) < 0){
							poison.remove(playerName);
						} else {
							Integer counter = poison.get(playerName);
							poison.remove(playerName);
							poison.put(playerName, counter - 1);
						}
					} else {
						poison.remove(playerName);
					}
				}
			}
		}.runTaskTimer(plugin, 18, 18);
		new BukkitRunnable(){
			public void run(){
				for (String playerName : vulnerable.keySet()){
					if (Bukkit.getOfflinePlayer(playerName).isOnline()){
						if (vulnerable.get(playerName) < 0){
							vulnerable.remove(playerName);
						} else {
							Integer counter = vulnerable.get(playerName);
							vulnerable.remove(playerName);
							vulnerable.put(playerName, counter - 1);
						}
					} else {
						vulnerable.remove(playerName);
					}
				}
			}
		}.runTaskTimer(plugin, 20, 20);
		new BukkitRunnable(){
			public void run(){
				for (String playerName : noises.keySet()){
					if (Bukkit.getOfflinePlayer(playerName).isOnline()){
						Player player = Bukkit.getPlayer(playerName);
						Iterator<Entity> itr = player.getNearbyEntities(6, 6, 6).iterator();
						while (itr.hasNext()){
							Entity entity = itr.next();
							if (entity instanceof Player){
								((Player) entity).damage(2);
								int rNoise = r.nextInt(5);
								if (rNoise == 0){
									((Player) entity).playSound(entity.getLocation(), Sound.BURP, 100, 1);
								} else if (rNoise == 1){
									((Player) entity).playSound(entity.getLocation(), Sound.VILLAGER_DEATH, 100, 1);
								} else if (rNoise == 2){
									((Player) entity).playSound(entity.getLocation(), Sound.DRINK, 100, 1);
								} else if (rNoise == 3){
									((Player) entity).playSound(entity.getLocation(), Sound.EAT, 100, 1);
								} else if (rNoise == 4){
									((Player) entity).playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1);
								}
							}
						}
						player.damage(r.nextInt(2) + 1);
						int rNoise = r.nextInt(5);
						if (rNoise == 0){
							player.playSound(player.getLocation(), Sound.BURP, 100, 1);
						} else if (rNoise == 1){
							player.playSound(player.getLocation(), Sound.VILLAGER_DEATH, 100, 1);
						} else if (rNoise == 2){
							player.playSound(player.getLocation(), Sound.DRINK, 100, 1);
						} else if (rNoise == 3){
							player.playSound(player.getLocation(), Sound.EAT, 100, 1);
						} else if (rNoise == 4){
							player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1);
						}
						if (noises.get(playerName) < 0){
							noises.remove(playerName);
						} else {
							Integer counter = noises.get(playerName);
							noises.remove(playerName);
							noises.put(playerName, counter - 1);
						}
					} else {
						noises.remove(playerName);
					}
				}
			}
		}.runTaskTimer(plugin, 10, 10);
	}
}

enum Disease {
	DIZZY(0, "Dizzy"),
	FEAR(1, "Fear"),
	POISON(2, "Poison"),
	VULNERABILITY(3, "Vulnerability"),
	NOISES(4, "Noises");
	
	private final int id;
	private final String name;
	
	Disease(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	int getId(){
		return id;
	}
	
	String getName(){
		return name;
	}
	
	static Disease getByName(String name) throws IllegalArgumentException {
		if (name == "Dizzy"){
			return Disease.DIZZY;
		} else if (name == "Fear"){
			return Disease.FEAR;
		} else if (name == "Poison"){
			return Disease.POISON;
		} else if (name == "Vulnerability"){
			return Disease.VULNERABILITY;
		} else if (name == "Noises"){
			return Disease.NOISES;
		} else {
			throw new IllegalArgumentException("Theres no Disease named \"" + name + "\"");
		}
	}
}
