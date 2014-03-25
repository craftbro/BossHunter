package code.boss.bosses;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import code.boss.effect.ParticleEffect;
import code.boss.item.SkullStack;
import code.boss.main.main;

/**
 * The class of the boss "Hacker"
 * @author rasmusrune
 */
public class BossHacker extends Boss implements Listener{
	static int attacks = 0;
	public static int timer = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String h_v = ChatColor.DARK_BLUE + "[" + ChatColor.DARK_RED + "Hacker" + ChatColor.DARK_BLUE + "] " + ChatColor.GOLD;
	public int mode = 0;
	public boolean instakill = false;
	public int timesInARow = 0;
	public Player p;
	
	Random r = new Random();
	
	public BossHacker(main m){
		super(m);
	}
	
	
	public void start(){
		timer = 0;
		p = randomPlayer();
		plugin.util.broadcastDelaySound(u_v + "Hi " + p.getName() + " wanna have a PVP match?", Sound.VILLAGER_HAGGLE, 1, timer += 90);
		plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+p.getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "k but who are you?", Sound.VILLAGER_IDLE, 1, timer += 80);
		plugin.util.broadcastDelaySound(h_v + "The one who is going to take all your stuff!", Sound.ENDERDRAGON_GROWL, 1, timer += 75);
		if (Bukkit.getOnlinePlayers().length > 1){
			plugin.util.broadcastDelaySound(h_v + "And your friends can help too! You cant win anyways!", Sound.ENDERDRAGON_GROWL, 1, timer += 80);
		}
		plugin.util.broadcastDelaySound(ChatColor.GOLD+"["+ChatColor.DARK_GREEN+p.getName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW + "We will see! Come down!", Sound.VILLAGER_NO, 1, timer += 77);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(plugin.arena.getSpawn(), EntityType.SKELETON);
				boss.setMaxHealth(425 * Bukkit.getOnlinePlayers().length);
				boss.setHealth(boss.getMaxHealth());
				boss.setCustomName(ChatColor.DARK_RED + "Hacker");
				boss.setCustomNameVisible(false);
				boss.setCanPickupItems(false);
				boss.setRemoveWhenFarAway(false);
				boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
				EntityEquipment equipment = boss.getEquipment();
				equipment.setHelmet(new SkullStack("MHF_Sheep"));
				equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				equipment.setLeggings(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				equipment.setBoots(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
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
			if (r.nextInt(60) == 0){
				Iterator<Entity> killAura = boss.getNearbyEntities(r.nextInt(2) + 4, r.nextInt(2) + 4, r.nextInt(2) + 4).iterator();
				while (killAura.hasNext()){
					Entity next = killAura.next();
					if (next instanceof Player){
						Player player = (Player) next;
						player.damage(r.nextInt(1 + r.nextInt(1 + r.nextInt(2))) + 1, boss);
					}
				}
			}
			if (r.nextInt(90) == 0){
				if (boss.getNearbyEntities(8, 8, 8).isEmpty()){
					boss.getEquipment().setItemInHand(new ItemStack(Material.BOW));
				} else {
					boss.getEquipment().setItemInHand(new ItemStack(Material.IRON_AXE));
				}
			}
			if (r.nextInt(80) == 0){
				for (Player player : Bukkit.getOnlinePlayers()){
					if (boss.hasLineOfSight(player)){
						if (boss.getEquipment().getItemInHand().getType() == Material.IRON_AXE){
							damageWithEvent(player, (float) 3.5, DamageCause.ENTITY_ATTACK, boss, false);
						} else {
							damageWithEvent(player, (float) 1, DamageCause.ENTITY_ATTACK, boss, false);
						}
					}
				}
			}
			if (attacks >= 1){
				if (r.nextInt(240) == 0){
					megaBow(boss);
				}
			}
			if (attacks >= 2){
				if (r.nextInt(190) == 0){
					for (int x = 0; x < (3 * Bukkit.getOnlinePlayers().length > 5 ? 5 : 3 * Bukkit.getOnlinePlayers().length); x++){
						if (r.nextBoolean()){
							minions(boss.getLocation());
						} else {
							minions(boss.getEyeLocation());
						}
					}
				}
			}
			if (attacks >= 3){
				if (r.nextInt(370) == 0){
					speedup();
				}
			}
			if (attacks >= 4){
				if (r.nextInt(220) == 0){
					doInstaKill();
				}
			}
		}
	}
	

	public static boolean optimized1 = false;
	public static boolean optimized2 = false;
	public static boolean optimized3 = false;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if (spawned){
			if (event.getEntity().getUniqueId() == boss.getUniqueId()){
				if (mode == 1){
					event.setDamage(event.getDamage() / 2);
				}
				if (spawned && boss.getHealth() / (boss.getMaxHealth() / 100) <= 80 && !optimized1){
					optimized1 = true;
					attacks = 2;
				}
				if (spawned && boss.getHealth() / (boss.getMaxHealth() / 100) <= 60 && !optimized2){
					optimized2 = true;
					attacks = 3;
				}
				if (spawned && boss.getHealth() / (boss.getMaxHealth() / 100) <= 40 && !optimized3){
					optimized3 = true;
					attacks = 4;
					((Skeleton) boss).setSkeletonType(SkeletonType.WITHER);
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if (spawned){
			if (event.getDamager().getUniqueId() == boss.getUniqueId() && event.getEntity() instanceof Player){
				if (instakill){
					event.setDamage(event.getDamage() * 2);
					ParticleEffect.LARGE_EXPLODE.animateAtLocation(event.getEntity().getLocation(), 5, 2);
				}
				if (mode == 0){
					event.setDamage(event.getDamage() * 2);
					timesInARow++;
					if (timesInARow >= 4){
						mode = 1;
						timesInARow = 0;
					}
				}
			} else if (event.getDamager() instanceof Player && event.getEntity().getUniqueId() == boss.getUniqueId()){
				if (mode == 1){
					timesInARow++;
					if (timesInARow >= 4){
						mode = 0;
						timesInARow = 0;
					}
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		LivingEntity entity = event.getEntity();
		if (minions.contains(entity)){
			minions.remove(entity);
		}
		if (spawned){
			if (entity.getUniqueId() == boss.getUniqueId()){
				spawned = false;
				timer = 0;
				plugin.util.broadcastDelaySound(h_v + "You may have beaten me but i can just respawn and smash you all!", Sound.ENDERDRAGON_GROWL, 1, timer += 80);
				plugin.util.broadcastDelaySound(ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "Moderator" + ChatColor.DARK_BLUE + "] " + ChatColor.AQUA + "I dont think so!", Sound.CAT_MEOW, 0, timer += 70);
				plugin.util.broadcastDelaySound(h_v + "Nooooooooooooo! Damn you " + p.getName(), Sound.ENDERDRAGON_DEATH, 1, timer += 90);
				plugin.util.broadcastDelaySound(ChatColor.DARK_RED + "Hacker" + ChatColor.RESET + " Has been temporary banned for Hacking by " + ChatColor.BLUE + "Moderator" + ChatColor.RESET + " For \"Hacks!\"", Sound.FIREWORK_BLAST, 0, timer += 140);
				new BukkitRunnable(){
					public void run(){
						plugin.stop();
					}
				}.runTaskLater(plugin, timer += 85);
			}
		}
	}
	
	
	public void megaBow(final LivingEntity attacker){
		new BukkitRunnable(){
			int times = 0;
			public void run(){
				times++;
				if (attacker.getEquipment().getItemInHand().getType() != Material.BOW){
					attacker.getEquipment().setItemInHand(new ItemStack(Material.BOW));
				}
				final Arrow arrow = attacker.launchProjectile(Arrow.class);
				new BukkitRunnable(){
					public void run(){
						arrow.remove();
					}
				}.runTaskLater(plugin, 200);
				if (times > 10){
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 5);
	}
	
	
	public void minions(Location loc){
		if (getMinionsSize() < (5 + r.nextInt(4) + (r.nextBoolean() ? r.nextInt(3) : 0)) / 2){
			final Skeleton player = loc.getWorld().spawn(loc, Skeleton.class);
			if (optimized3){
				if (r.nextInt(3) == 0){
					player.setSkeletonType(SkeletonType.WITHER);
				}
			}
			String basedOf = plugin.util.randomName();
			player.setCustomName(basedOf + " 2.0");
			player.setCustomNameVisible(true);
			player.getEquipment().setHelmet(new SkullStack(basedOf));
			player.getEquipment().setBoots(getRandomArmorPiece());
			player.getEquipment().setLeggings(getRandomArmorPiece());
			player.getEquipment().setChestplate(getRandomArmorPiece());
			minions.add(player);
			new BukkitRunnable(){
				public void run(){
					ParticleEffect.ANGRY_VILLAGER.animateAtLocation(player.getEyeLocation(), 10, 1);
				}
			}.runTaskLater(plugin, r.nextInt(401) + 100);
		}
	}
	
	
	public void speedup(){
		boss.removePotionEffect(PotionEffectType.SPEED);
		boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
		new BukkitRunnable(){
			public void run(){
				boss.removePotionEffect(PotionEffectType.SPEED);
				boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
			}
		}.runTaskLater(plugin, 60);
	}
	
	
	public void doInstaKill(){
		instakill = true;
		ParticleEffect.LAVA.animateAtLocation(boss.getLocation(), 10, 1);
		new BukkitRunnable(){
			public void run(){
				ParticleEffect.LARGE_SMOKE.animateAtLocation(boss.getLocation(), 10, (float) 1.25);
				instakill = false;
			}
		}.runTaskLater(plugin, 20);
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
	
	public ItemStack getRandomArmorPiece(){
		int rArmor = r.nextInt(9);
		switch (rArmor) {
		case 0:
			return new ItemStack(Material.LEATHER_BOOTS);
		case 1:
			return new ItemStack(Material.LEATHER_CHESTPLATE);
		case 2:
			return new ItemStack(Material.LEATHER_HELMET);
		case 3:
			return new ItemStack(Material.LEATHER_LEGGINGS);
		case 4:
			return new ItemStack(Material.IRON_BOOTS);
		case 5:
			return new ItemStack(Material.IRON_HELMET);
		case 6:
			return new ItemStack(Material.IRON_CHESTPLATE);
		case 7:
			return new ItemStack(Material.IRON_LEGGINGS);
		case 8:
			return new ItemStack(Material.AIR);
		default:
			return new ItemStack(Material.AIR);
		}
	}
}
