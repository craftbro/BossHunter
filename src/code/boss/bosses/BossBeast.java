package code.boss.bosses;





import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;








import code.boss.effect.ParticleEffect;
import code.boss.effect.ras.RasEffect;
import code.boss.item.ColoredStack;
import code.boss.item.NamedStack;
import code.boss.main.main;

/**
 * The class of the boss "Beast"
 * @author rasmusrune
 */
public class BossBeast extends Boss implements Listener{
	static int attacks = 0;
	public static int timer = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String v_m = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Mayor" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD;
	public static long deadVillagers = 0;
	public static UUID mayorUuid;
	
	Random r = new Random();
	
	public BossBeast(main m){
		super(m);
	}
	
	
	public void start(){
		timer = 0;
		plugin.util.broadcastDelaySound(u_v + "Hello " + plugin.util.randomNameFormat() + "!", Sound.EAT, 1, timer += 70);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Who are you?", Sound.VILLAGER_NO, 1, timer += 60);
		plugin.util.broadcastDelaySound(u_v + "That can wait! Please help us", Sound.EAT, 1, timer += 50);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "We won't help before we know who you are", Sound.VILLAGER_NO, 1, timer += 65);
		plugin.util.broadcastDelaySound(ChatColor.DARK_GREEN + "A loud sound can be heard nearby", Sound.ENDERDRAGON_GROWL, 1, timer += 70);
		plugin.util.broadcastDelaySound(u_v + "Oh no! Its already! Hide before its too late", Sound.EAT, 1, timer += 80);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Wait what??", Sound.VILLAGER_HIT, 1, timer += 60);
		plugin.util.broadcastDelaySound(u_v + "Now its too late! Its here", Sound.ENDERDRAGON_GROWL, 1, timer += 65);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(randomPlayer().getEyeLocation(), EntityType.ZOMBIE);
				boss.setMaxHealth(650);
				boss.setHealth(650);
				boss.setCustomName(ChatColor.DARK_GREEN + "Beast");
				boss.setCustomNameVisible(true);
				((Zombie) boss).setBaby(true);
				((Zombie) boss).setVillager(false);
				EntityEquipment eq = boss.getEquipment();
				eq.clear();
				eq.setHelmet(new ColoredStack(Color.fromRGB(22, 158, 47), Material.LEATHER_LEGGINGS));
				eq.setChestplate(new ColoredStack(Color.fromRGB(34, 211, 214), Material.LEATHER_LEGGINGS));
				eq.setLeggings(new ColoredStack(Color.fromRGB(34, 133, 214), Material.LEATHER_LEGGINGS));
				eq.setBoots(new ColoredStack(Color.fromRGB(148, 148, 148), Material.LEATHER_LEGGINGS));
				eq.setBootsDropChance(0);
				eq.setLeggingsDropChance(0);
				eq.setChestplateDropChance(0);
				eq.setHelmetDropChance(0);
				((CraftLivingEntity) boss).getHandle().setSprinting(true);
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
				if (r.nextInt(205) == 0 && this.getMinionsSize() < 35){
					for (int x = r.nextInt(6) + 5; x > 0; x--){
						infect(boss);
					}
				}
			}
			if (attacks >= 2){
				if (r.nextInt(190) == 0){
					call(boss.getLocation());
				}
			}
			if (attacks >= 3){
				if (r.nextInt(250) == 0){
					rottenAura(boss);
				}
			}
			if (attacks >= 4){
				if (r.nextInt(350) == 0){
					doomShot(boss, plugin.util.getNearest(boss));
				}
			}
		}
	}
	
	
	public static boolean optimized1 = false;
	public static boolean optimized2 = false;
	public static boolean optimized3 = false;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		if (entity.getUniqueId() == boss.getUniqueId()){
			for (ItemStack armor : boss.getEquipment().getArmorContents()){
				armor.setDurability((short) 0);
			}
			if (boss.getHealth() / 6.5 <= 70 && !optimized1){
				optimized1 = true;
				spawned = false;
				plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Was that really what you was that scared of? Now show yourself!", Sound.VILLAGER_YES, 1, 0);
				plugin.util.broadcastDelaySound(u_v + "Its far from over yet!", Sound.EAT, 1, 60);
				RasEffect.LARGE_SMOKE.display(boss.getLocation(), 1, 2, 1, 1.5F, 40);
				final double health = boss.getHealth();
				boss.remove();
				new BukkitRunnable(){
					public void run(){
						boss = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 101, 98, 198), EntityType.ZOMBIE);
						boss.setMaxHealth(650);
						boss.setHealth(health);
						boss.setCustomName(ChatColor.DARK_GREEN + "Beast");
						boss.setCustomNameVisible(true);
						boss.setRemoveWhenFarAway(false);
						boss.setCanPickupItems(false);
						((Zombie) boss).setBaby(false);
						((Zombie) boss).setVillager(false);
						EntityEquipment eq = boss.getEquipment();
						eq.clear();
						eq.setHelmet(new ColoredStack(Color.fromRGB(22, 158, 47), Material.LEATHER_LEGGINGS));
						eq.setChestplate(new ColoredStack(Color.fromRGB(34, 211, 214), Material.LEATHER_LEGGINGS));
						eq.setLeggings(new ColoredStack(Color.fromRGB(34, 133, 214), Material.LEATHER_LEGGINGS));
						eq.setBoots(new ColoredStack(Color.fromRGB(148, 148, 148), Material.LEATHER_LEGGINGS));
						eq.setBootsDropChance(0);
						eq.setLeggingsDropChance(0);
						eq.setChestplateDropChance(0);
						eq.setHelmetDropChance(0);
						((CraftLivingEntity) boss).getHandle().setSprinting(true);
						attacks = 2;
						spawned = true;
					}
				}.runTaskLater(plugin, 140);
			}
			if (boss.getHealth() / 6.5 <= 50 && !optimized2){
				optimized2 = true;
				attacks = 3;
			}
			if (boss.getHealth() / 6.5 <= 30 && !optimized3){
				optimized3 = true;
				attacks = 4;
			}
		} else if (mayorUuid != null){
			if (entity.getUniqueId() == mayorUuid){
				event.setCancelled(true);
				Location loc = ((LivingEntity) entity).getEyeLocation();
				loc.setY(loc.getY() + 1);
				ParticleEffect.ANGRY_VILLAGER.animateAtLocation(loc, 40, 1);
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.VILLAGER){
			deadVillagers++;
			Zombie zombie = entity.getWorld().spawn(entity.getLocation(), Zombie.class);
			zombie.setCanPickupItems(false);
			if (!((Villager) entity).isAdult()){
				zombie.setBaby(true);
			} else {
				zombie.setBaby(false);
			}
			zombie.setVillager(true);
			zombie.setRemoveWhenFarAway(false);
			minions.add(zombie);
		} else if (entity.getUniqueId() == boss.getUniqueId()){
			spawned = false;
			timer = 0;
			if (boss.getKiller() != null){
				if (r.nextInt(60) == 0){
					NamedStack potato = new NamedStack(ChatColor.GOLD + "Mutated Potato", Material.BAKED_POTATO);
					List<String> potatoLore = new ArrayList<String>();
					potatoLore.add("Earned when killing the boss \"Mutated Potato\"");
					plugin.util.addLore(potato, potatoLore);
					if (plugin.collect.hasItem(boss.getKiller(), potato)){
						plugin.collect.giveItem(boss.getKiller(), potato);
						boss.getKiller().sendMessage(ChatColor.GREEN + "The Item 'Mutated Potato' was added to your Collection!");
					}
				}
			}
			plugin.util.broadcastDelaySound(ChatColor.GOLD + "Congratulations! You beated the Beast" , Sound.ENDERDRAGON_DEATH, 1, timer);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Finnaly we got that beast down! Now who are you?", Sound.VILLAGER_YES, 1, timer += 160);
			plugin.util.broadcastDelaySound(u_v + "I am...", Sound.EAT, 1, timer += 80);
			plugin.util.broadcastDelaySound(v_m + "The Mayor of Happy Town!", Sound.FIREWORK_LAUNCH, 1, timer += 120);
			new BukkitRunnable() {
				public void run() {
					Villager villager = randomPlayer().getWorld().spawn(randomPlayer().getLocation(), Villager.class);
					((CraftVillager) villager).getHandle().setProfession(5);
					villager.setAdult();
					villager.setAgeLock(true);
					villager.setRemoveWhenFarAway(false);
					villager.setCustomName(ChatColor.GREEN + "Mayor of Happy Town");
					villager.setCustomNameVisible(true);
					mayorUuid = villager.getUniqueId();
				}
			}.runTaskLater(plugin, timer += 10);
			plugin.util.broadcastDelaySound(v_m + "Thanks for freeing us from the beast!", Sound.EAT, 1, timer += 60);
			plugin.util.broadcastDelaySound(v_m + "But sadly " + deadVillagers + " Died...", Sound.EAT, 1, timer += 65);
			plugin.util.broadcastDelaySound(v_m + "So get out of here before i destroy you too!", Sound.EAT, 1, timer += 70);
			new BukkitRunnable(){
				public void run(){
					for (Player online : Bukkit.getOnlinePlayers()){
						online.setVelocity(new Vector(0, Math.PI, 0));
					}
				}
			}.runTaskLater(plugin, timer += 40);
			new BukkitRunnable(){
				public void run(){
					plugin.stop();
				}
			}.runTaskLater(plugin, timer += 50);
		}
		if (minions.contains(entity)){
			minions.remove(entity);
		}
	}
	
	
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event){
		if (event.getEntity().getType() == EntityType.PLAYER && event.getTarget().getType() == EntityType.PLAYER){
			event.setCancelled(true);
		}
	}
	
	
	public void infect(Entity entity){
		Villager villager = entity.getWorld().spawn(entity.getLocation(), Villager.class);
		villager.setAgeLock(true);
		villager.setAdult();
		villager.setMaximumNoDamageTicks(4);
		villager.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999999999, 64));
		if (r.nextInt(3) == 0){
			Villager villager2 = villager.getWorld().spawn(entity.getLocation(), Villager.class);
			villager.setBaby();
			villager2.setMaximumNoDamageTicks(4);
			villager2.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999999999, 64));
			villager.setPassenger(villager2);
		}
		entity.setPassenger(villager);
	}
	
	
	public void call(Location loc){
		loc.getWorld().playSound(loc, Sound.ENDERDRAGON_GROWL, 12, 0.4f);
		for (int x = r.nextInt(5) + 3; x > 0; x--){
			final Location nearLoc = getNearbyLoc(loc, 6, 6);
			if (nearLoc.getBlock().getType() != Material.AIR){
				nearLoc.getWorld().playEffect(nearLoc, Effect.STEP_SOUND, nearLoc.getBlock().getType());
				new BukkitRunnable(){
					public void run(){
						nearLoc.getWorld().playEffect(nearLoc, Effect.STEP_SOUND, nearLoc.getBlock().getType());
						new BukkitRunnable(){
							public void run(){
								nearLoc.getWorld().playEffect(nearLoc, Effect.STEP_SOUND, nearLoc.getBlock().getType());
								new BukkitRunnable(){
									public void run(){
										Zombie zombie = nearLoc.getWorld().spawn(nearLoc, Zombie.class);
										if (r.nextInt(5) == 0){
											zombie.setBaby(true);
										} else {
											zombie.setBaby(false);
										}
										if (r.nextInt(24) == 0){
											zombie.setVillager(true);
										} else {
											zombie.setVillager(false);
										}
										zombie.setRemoveWhenFarAway(false);
										deadVillagers++;
										minions.add(zombie);
									}
								}.runTaskLater(plugin, 10L);
							}
						}.runTaskLater(plugin, 10L);
					}
				}.runTaskLater(plugin, 10L);
			}
		}
	}
	
	
	public void rottenAura(Entity entity){
		Iterator<Entity> itr = entity.getNearbyEntities(4, 4, 4).iterator();
		while (itr.hasNext()){
			Entity nEntity = itr.next();
			if (nEntity instanceof LivingEntity){
				ParticleEffect.SLIME.animateAtLocation(nEntity.getLocation(), 60, 1);
				((LivingEntity) nEntity).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 120, 0));
				((LivingEntity) nEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 1));
				((LivingEntity) nEntity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 40, 0));
				((LivingEntity) nEntity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 140, 0));
				((LivingEntity) nEntity).damage(6, entity);
			}
		}
		RasEffect.HAPPY_VILLAGER.display(entity.getLocation(), 1, 1, 1, 1, 65);
	}
	
	
	public void doomShot(LivingEntity shooter, final Entity target){
		final Location sLoc = shooter.getEyeLocation();
		Location tLoc = target.getLocation();
		final Vector vec = tLoc.toVector().subtract(sLoc.toVector()).normalize().multiply(0.25);
		new BukkitRunnable(){
			int counter = 0;
			Location currentLoc = sLoc.add(vec);
			
			public void run() {
				counter++;
				Location tLoc = target.getLocation();
				Vector vec = tLoc.toVector().subtract(currentLoc.toVector()).normalize().multiply(0.2);
				currentLoc = sLoc.add(vec);
				ParticleEffect.CRIT.animateAtLocation(currentLoc, 1, 1);
				Item item = currentLoc.getWorld().dropItem(currentLoc, new ItemStack(Material.GLOWSTONE));
				item.setPickupDelay(99999);
				Iterator<Entity> itr = item.getNearbyEntities(1, 1, 1).iterator();
				item.remove();
				while (itr.hasNext()){
					Entity entity = itr.next();
					if (entity.getUniqueId() == target.getUniqueId()){
						currentLoc.getWorld().playSound(currentLoc, Sound.FIZZ, 4, 0.7F);
						if (target instanceof LivingEntity){
							((LivingEntity) target).damage(99E99 * (Math.PI * Math.E) * r.nextInt(10));
						} else {
							target.remove();
						}
						this.cancel();
						break;
					}
				}
				if (counter >= 500){
					currentLoc.getWorld().playSound(currentLoc, Sound.FIZZ, 4, 0.7F);
					target.setVelocity(currentLoc.toVector().subtract(target.getLocation().toVector()));
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	
	public Location getNearbyLoc(Location location, int radiusX, int radiusZ){
		Location loc = location.clone();
		loc.setY(loc.getY() - 1);
		if (r.nextBoolean()){
			loc.setX(loc.getX() + r.nextInt(radiusX) + 1);
		} else {
			loc.setX(loc.getX() - r.nextInt(radiusX) - 1);
		}
		if (r.nextBoolean()){
			loc.setZ(loc.getZ() + r.nextInt(radiusZ) + 1);
		} else {
			loc.setZ(loc.getZ() - r.nextInt(radiusZ) - 1);
		}
		return loc;
	}
	
	
	public Player randomPlayer(){
		return Bukkit.getOnlinePlayers()[r.nextInt(Bukkit.getOnlinePlayers().length)];
	}
}
