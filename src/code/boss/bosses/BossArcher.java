package code.boss.bosses;





import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.item.ColoredStack;
import code.boss.main.main;

/**
 * The class of the boss "Archer King"
 * Please find a better name.....
 * @author rasmusrune
 */
public class BossArcher extends Boss{
	public List<Entity> charging = new ArrayList<Entity>();
	public static int timer = 0;
	static int attacks = 0;
	public String bossName = "Archer King";
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String s_v = ChatColor.DARK_RED + "[" + ChatColor.RED + bossName + ChatColor.DARK_RED + "] " + ChatColor.GOLD;
	
	Random r = new Random();
	
	public BossArcher(main m){
		super(m);
	}

	
	public void start(){
		timer = 0;
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(new Location(Bukkit.getWorld("BOSS"), 99, 65, 99), EntityType.SKELETON);
				EntityEquipment equipment = boss.getEquipment();
				equipment.setBoots(new ColoredStack(Color.GREEN, Material.LEATHER_LEGGINGS));
				equipment.setLeggings(new ColoredStack(Color.GREEN, Material.LEATHER_CHESTPLATE));
				equipment.setChestplate(new ColoredStack(Color.GREEN, Material.LEATHER_CHESTPLATE));
				equipment.setHelmet(new ColoredStack(Color.GREEN, Material.LEATHER_HELMET));
				boss.setCanPickupItems(false);
				boss.setCustomName(ChatColor.GREEN + "Archer King");
				boss.setCustomNameVisible(true);
				boss.setRemoveWhenFarAway(false);
			}
		}.runTaskLater(plugin, timer += 70);
	}
	
	
	public void tick(){
		if (spawned){
			if (r.nextInt(11) == 0){
				Vector vec = plugin.util.getNearest(boss).getLocation().toVector().subtract(boss.getLocation().toVector()).normalize().multiply(2);
				vec.setY(0);
				boss.setVelocity(vec);
				plugin.util.setTargetToNearest(boss);
			}
			if (attacks >= 1){
				if (r.nextInt(200) == 0){
					for (int x = r.nextInt(21) + 10; x > 0; x--){
						arrow(boss);
					}
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
		if (entity.getUniqueId() == boss.getUniqueId()){
			if (boss.getHealth() / 6 <= 500 && !optimized1){
				optimized1 = true;
				attacks = 2;
			}
			if (boss.getHealth() / 6 <= 400 && !optimized2){
				optimized2 = true;
				attacks = 3;
			}
			if (boss.getHealth() / 6 <= 300 && !optimized3){
				optimized3 = true;
				attacks = 4;
			}
			if (boss.getHealth() / 6 <= 200 && !optimized4){
				optimized4 = true;
				attacks = 5;
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		Entity entity = event.getEntity();
		Entity damager = event.getDamager();
		if (entity.getUniqueId() == boss.getUniqueId()){
			Vector vec = damager.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(3);
			vec.setY(0.25);
			entity.setVelocity(vec);
		}
		if (damager.getPassenger().getType() == EntityType.DROPPED_ITEM && damager.getType() == EntityType.ARROW){
			if (entity instanceof LivingEntity){
				if (((Item) damager.getPassenger()).getItemStack().getType() == Material.ICE){
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 190, 2));
				} else if (((Item) damager.getPassenger()).getItemStack().getType() == Material.SPIDER_EYE){
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 1));
				} else if (((Item) damager.getPassenger()).getItemStack().getType() == Material.SPIDER_EYE){
					damager.getWorld().createExplosion(damager.getLocation().getX(), damager.getLocation().getY(), damager.getLocation().getZ(), 2.5F, false, false);
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		if (entity.getUniqueId() == boss.getUniqueId()){
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
			}.runTaskLater(plugin, 100);
		}
	}
	
	
	public void arrow(LivingEntity entity){
		final Arrow arrow = entity.launchProjectile(Arrow.class);
		new BukkitRunnable(){
			public void run() {
				arrow.remove();
			}
		}.runTaskLater(plugin, 200);
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
