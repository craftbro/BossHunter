package code.boss.bosses;





import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.item.ColoredStack;
import code.boss.item.NamedStack;
import code.boss.main.main;

/**
 * The class of the boss "Archer King"
 * @author rasmusrune
 */
public class BossArcher extends Boss implements Listener{
	public List<Entity> charging = new ArrayList<Entity>();
	public static int timer = 0;
	static int attacks = 0;
	public String bossName = "Archer King";
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String a_v = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + bossName + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN;
	
	Random r = new Random();
	
	public BossArcher(main m){
		super(m);
	}

	
	public void start(){
		timer = 0;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		new BukkitRunnable(){
			public void run(){
				for (Player online : Bukkit.getOnlinePlayers()){
					online.getWorld().playSound(online.getLocation(), Sound.ARROW_HIT, 1, 1);
					online.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 2));
					online.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 9));
					online.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, -6));
				}
			}
		}.runTaskLater(plugin, timer += 80);
		plugin.util.broadcastDelaySound(u_v + "i hate when i dont hit perfectly. Now you arnt blind!", Sound.BLAZE_BREATH, 1, timer += 80);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Wait why would you make me blind??", Sound.VILLAGER_HIT, 1, timer += 70);
		plugin.util.broadcastDelaySound(u_v + "Im hungry so i need meat! stand still so i can shoot you!", Sound.BLAZE_BREATH, 1, timer += 90);
		new BukkitRunnable(){
			public void run(){
				for (Player online : Bukkit.getOnlinePlayers()){
					online.getWorld().playSound(online.getLocation(), Sound.ARROW_HIT, 1, 1);
					online.setHealth(1);
					online.removePotionEffect(PotionEffectType.BLINDNESS);
					online.removePotionEffect(PotionEffectType.SLOW);
					online.removePotionEffect(PotionEffectType.JUMP);
				}
			}
		}.runTaskLater(plugin, timer += 60);
		plugin.util.broadcastDelaySound(u_v + "I hate this! Why did i foget to sharpen my arrows??", Sound.BLAZE_BREATH, 1, timer += 70);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "But if you kill us we will die!", Sound.VILLAGER_HIT, 1, timer += 70);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Who are you even?", Sound.VILLAGER_NO, 1, timer += 80);
		plugin.util.broadcastDelaySound(a_v + "The Archer King! Well now stand still", Sound.BLAZE_BREATH, 1, timer += 60);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Come down and fight us if you want to kill us!", Sound.VILLAGER_NO, 1, timer += 70);
		plugin.util.broadcastDelaySound(a_v + "I will come down and fight you but only to train my archer skills", Sound.BLAZE_BREATH, 1, timer += 75);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(new Location(Bukkit.getWorld("BOSS"), /*99, 65, 99*/-393, 62.5, -8), EntityType.SKELETON);
				EntityEquipment equipment = boss.getEquipment();
				equipment.setBoots(new ColoredStack(Color.GREEN, Material.LEATHER_LEGGINGS));
				equipment.setLeggings(new ColoredStack(Color.GREEN, Material.LEATHER_CHESTPLATE));
				equipment.setChestplate(new ColoredStack(Color.GREEN, Material.LEATHER_CHESTPLATE));
				equipment.setHelmet(new ColoredStack(Color.GREEN, Material.LEATHER_HELMET));
				equipment.setItemInHand(new ItemStack(Material.BOW));
				boss.setCanPickupItems(false);
				boss.setCustomName(ChatColor.GREEN + "Archer King");
				boss.setCustomNameVisible(true);
				boss.setRemoveWhenFarAway(false);
				boss.setMaxHealth(600);
				boss.setHealth(600);
				spawned = true;
				attacks = 1;
			}
		}.runTaskLater(plugin, timer += 70);
	}
	
	
	public void tick(){
		if (spawned){
			if(!boss.getLocation().getChunk().isLoaded()){
				boss.getLocation().getChunk().load();
			}
			if (r.nextInt(28) == 0){
				Vector vec = boss.getLocation().toVector().subtract(plugin.util.getNearest(boss).getLocation().toVector()).normalize().multiply(1.2);
				vec.setY(0);
				boss.setVelocity(vec);
				plugin.util.setTargetToNearest(boss);
			}
			if (attacks >= 1){
				if (r.nextInt(210) == 0){
					arrows(boss);
				}
			}
			if (attacks >= 2){
				if (r.nextInt(240) == 0){
					iceshot(boss);
				}
			}
			if (attacks >= 3){
				if (r.nextInt(260) == 0){
					poisonshot(boss);
				}
			}
			if (attacks >= 4){
				if (r.nextInt(270) == 0){
					boomshot(boss);
				}
			}
			if (attacks >= 5){
				if (r.nextInt(170) == 0){
					longshot(boss);
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
		Entity entity = event.getEntity();
		if (spawned && entity.getUniqueId() == boss.getUniqueId()){
			if (boss.getHealth() / 6 <= 80 && !optimized1){
				optimized1 = true;
				attacks = 2;
			}
			if (boss.getHealth() / 6 <= 70 && !optimized2){
				optimized2 = true;
				attacks = 3;
			}
			if (boss.getHealth() / 6 <= 50 && !optimized3){
				optimized3 = true;
				attacks = 4;
			}
			if (boss.getHealth() / 6 <= 40 && !optimized4){
				optimized4 = true;
				attacks = 5;
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		Entity entity = event.getEntity();
		Entity damager = event.getDamager();
		if (boss != null && entity.getUniqueId() == boss.getUniqueId()){
			Vector vec = entity.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize().multiply(1.4);
			vec.setY(0.25);
			entity.setVelocity(vec);
		}
		if (damager.getPassenger() != null && damager.getPassenger().getType() == EntityType.DROPPED_ITEM && damager.getType() == EntityType.ARROW){
			if (entity instanceof LivingEntity){
				if (((Item) damager.getPassenger()).getItemStack().getType() == Material.ICE){
					entity.getWorld().playSound(entity.getLocation(), Sound.SKELETON_HURT, 2, 0);
					entity.getWorld().playEffect(((LivingEntity) entity).getEyeLocation(), Effect.STEP_SOUND, Material.ICE);
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 190, 2));
				} else if (((Item) damager.getPassenger()).getItemStack().getType() == Material.SPIDER_EYE){
					entity.getWorld().playSound(entity.getLocation(), Sound.ZOMBIE_REMEDY, 4, 2);
					ParticleEffect.SLIME.animateAtLocation(entity.getLocation(), 36, 1);
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 1));
				} else if (((Item) damager.getPassenger()).getItemStack().getType() == Material.TNT){
					damager.getWorld().createExplosion(damager.getLocation().getX(), damager.getLocation().getY(), damager.getLocation().getZ(), 2.5F, false, false);
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		if (spawned && entity.getUniqueId() == boss.getUniqueId()){
			spawned = false;
			timer = 0;
			if (boss.getKiller() != null){
				if (r.nextInt(40) == 0){
					NamedStack lunch = new NamedStack(ChatColor.RED + "Lunch Box", Material.TRAPPED_CHEST);
					List<String> lunchLore = new ArrayList<String>();
					lunchLore.add("Earned when killing the boss \"" + bossName + "\"");
					plugin.util.addLore(lunch, lunchLore);
					plugin.collect.giveItem(boss.getKiller(), lunch);
					boss.getKiller().sendMessage(ChatColor.GREEN + "The Item 'Lunch Box' was added to your Collection!");
				}
			}
			final Location loc = boss.getLocation();
			loc.setY(loc.getY() + 1);
			loc.setPitch(-3);
			for (int x = 0; x < 240; x++){
				loc.setYaw(loc.getYaw() + 3);
				final Arrow arrow = boss.getWorld().spawnArrow(loc, loc.getDirection(), 0.8F, 5);
				arrow.setFireTicks(99999999);
				arrow.setBounce(true);
				new BukkitRunnable(){
					public void run() {
						arrow.getWorld().createExplosion(arrow.getLocation().getX(), arrow.getLocation().getY(), arrow.getLocation().getZ(), 3.5F, false, false);
						arrow.remove();
					}
				}.runTaskLater(plugin, 200);
			}
			new BukkitRunnable(){
				public void run(){
					for (int x = 0; x < 240; x++){
						loc.setYaw(loc.getYaw() + 3);
						final Arrow arrow = boss.getWorld().spawnArrow(loc, loc.getDirection(), 0.8F, 5);
						arrow.setFireTicks(99999999);
						arrow.setBounce(true);
						new BukkitRunnable(){
							public void run() {
								arrow.getWorld().createExplosion(arrow.getLocation().getX(), arrow.getLocation().getY(), arrow.getLocation().getZ(), 3.5F, false, false);
								arrow.remove();
							}
						}.runTaskLater(plugin, 200);
					}
				}
			}.runTaskLater(plugin, timer += 100);
			plugin.util.broadcastDelaySound(a_v + "I really need to train some more with my bow", Sound.BLAZE_BREATH, 1, timer += 70);
			plugin.util.broadcastDelaySound(a_v + "It just lost my lunch because i forgot to train last month", Sound.BLAZE_BREATH, 1, timer += 60);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Why cant you just eat something else than meat??", Sound.VILLAGER_IDLE, 1, timer += 80);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Think about the ones you kill to get it", Sound.VILLAGER_HAGGLE, 1, timer += 70);
			plugin.util.broadcastDelaySound(a_v + "As far as i know you dont really think that much about it before you kill an animal to get stuff", Sound.BLAZE_BREATH, 1, timer += 75);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat() + "Your right but just because we dont it dosnt mean you cant think about it first", Sound.VILLAGER_YES, 1, timer += 80);
			plugin.util.broadcastDelaySound(a_v + "I will keep doing it as you do with animals and stuff and i will come back after you another day", Sound.BLAZE_BREATH, 1, timer += 65);
			plugin.util.broadcastDelaySound(a_v + "And when i come back i will be much stronger and will be able to take you down", Sound.BLAZE_BREATH, 1, timer += 68);
			new BukkitRunnable(){
				public void run(){
					plugin.stop();
				}
			}.runTaskLater(plugin, timer += 90);
		}
	}
	
	
	public void arrows(final LivingEntity entity){
		new BukkitRunnable(){
			int times = r.nextInt(6) + 5;
			public void run(){
				final Arrow arrow = entity.launchProjectile(Arrow.class);
				new BukkitRunnable(){
					public void run() {
						arrow.remove();
					}
				}.runTaskLater(plugin, 200);
				if (times < 0){
					this.cancel();
				}
				times--;
			}
		}.runTaskTimer(plugin, 0, 10);
	}
	
	
	public void iceshot(LivingEntity entity){
		final Arrow arrow = entity.launchProjectile(Arrow.class);
		final Item item = arrow.getWorld().dropItem(arrow.getLocation(), new ItemStack(Material.ICE));
		item.setPickupDelay(999999);
		arrow.setPassenger(item);
		new BukkitRunnable(){
			public void run() {
				arrow.remove();
				item.remove();
			}
		}.runTaskLater(plugin, 200);
	}
	
	
	public void poisonshot(LivingEntity entity){
		final Arrow arrow = entity.launchProjectile(Arrow.class);
		final Item item = arrow.getWorld().dropItem(arrow.getLocation(), new ItemStack(Material.SPIDER_EYE));
		item.setPickupDelay(999999);
		arrow.setPassenger(item);
		new BukkitRunnable(){
			public void run() {
				arrow.remove();
				item.remove();
			}
		}.runTaskLater(plugin, 200);
	}
	
	
	public void boomshot(LivingEntity entity){
		final Arrow arrow = entity.launchProjectile(Arrow.class);
		final Item item = arrow.getWorld().dropItem(arrow.getLocation(), new ItemStack(Material.TNT));
		item.setPickupDelay(999999);
		arrow.setPassenger(item);
		new BukkitRunnable(){
			public void run() {
				arrow.getWorld().createExplosion(arrow.getLocation().getX(), arrow.getLocation().getY(), arrow.getLocation().getZ(), 2.5F, false, false);
				arrow.remove();
				item.remove();
			}
		}.runTaskLater(plugin, 200);
	}
	
	
	public void longshot(LivingEntity entity){
		final Arrow arrow = entity.launchProjectile(Arrow.class);
		arrow.setVelocity(arrow.getVelocity().multiply(3.5));
		new BukkitRunnable(){
			public void run() {
				arrow.remove();
			}
		}.runTaskLater(plugin, 200);
	}
}
