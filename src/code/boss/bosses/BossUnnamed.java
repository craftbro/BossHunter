package code.boss.bosses;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
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
import code.boss.main.main;

/**
 * The class of the boss "Unnamed"
 * @author rasmusrune
 */
public class BossUnnamed  extends Boss implements Listener{
	static int attacks = 0;
	public static int timer = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String b_v = ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "Unnamed" + ChatColor.DARK_BLUE + "] " + ChatColor.AQUA;
	
	Random r = new Random();

	public byte[] attackSet = new byte[]{
		(byte) r.nextInt(5), (byte) r.nextInt(5), (byte) r.nextInt(5), (byte) r.nextInt(5), (byte) r.nextInt(5)
	};
	
	public BossUnnamed(main m){
		super(m);
	}
	
	
	public void start(){
		timer = 0;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(new Location(Bukkit.getWorld("BOSS"), 1032, -19, -1130), EntityType.MAGMA_CUBE);
				((MagmaCube) boss).setSize(2);
				boss.setMaxHealth(525);
				boss.setHealth(525);
				boss.setCustomName(ChatColor.BLUE + "Unnamed");
				boss.setCustomNameVisible(true);
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
				if (r.nextInt(250) == 0){
					firstAttack(attackSet[0], boss);
				}
			}
			if (attacks >= 2){
				if (r.nextInt(320) == 0){
					secondAttack(attackSet[1], randomPlayer(), boss);
				}
			}
			if (attacks >= 3){
				if (r.nextInt(190) == 0){
					thirdAttack(attackSet[2], randomPlayer(), boss);
				}
			}
			if (attacks >= 4){
				if (r.nextInt(275) == 0){
					attackFour(attackSet[3], randomPlayer(), boss);
				}
			}
			if (attacks >= 5){
				if (r.nextInt(340) == 0){
					attackFive(attackSet[4], randomPlayer(), boss);
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
		if (attackSet[0] == 4){
			if (event.getEntity().getType() == EntityType.SLIME){
				event.setCancelled(true);
			}
		}
		if (spawned){
			if (event.getEntity().getUniqueId() == boss.getUniqueId()){
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
	}
	
	
	
	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Silverfish){
			switch (attackSet[0]){
			case 1:
				if (event.getEntity() instanceof LivingEntity){
					((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
					((Silverfish) event.getDamager()).damage(((Silverfish) event.getDamager()).getMaxHealth() / 2);
				}
				break;
			}
		} else if (event.getDamager() instanceof Slime){
			switch (attackSet[0]){
			case 2:
				if (event.getEntity() instanceof Damageable){
					if (event.getDamager().getPassenger() == null){
						new BukkitRunnable(){
							int timer = 0;
							public void run(){
								timer = timer + r.nextInt(7) + 17;
								((Slime) event.getDamager()).setMaxHealth(999);
								((Slime) event.getDamager()).setHealth(999);
								event.getEntity().setPassenger(event.getDamager());
								ParticleEffect.PORTAL.animateAtLocation(event.getDamager().getLocation(), 25, (float) 0.75);
								if (!event.getEntity().isValid() || event.getEntity().isDead()){
									this.cancel();
									if (event.getEntity() instanceof LivingEntity){
										((LivingEntity) event.getEntity()).removePotionEffect(PotionEffectType.CONFUSION);
										((LivingEntity) event.getEntity()).removePotionEffect(PotionEffectType.SLOW);
									}
									event.getDamager().remove();
									minions.remove(event.getDamager());
								}
								if (event.getEntity() instanceof Damageable){
									((Damageable) event.getEntity()).damage(r.nextInt(3) + 1);
									if (event.getEntity() instanceof LivingEntity){
										((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 999999, 0));
										((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 2));
									}
								}
								if (timer > r.nextInt(96) + 85){
									this.cancel();
									if (event.getEntity() instanceof LivingEntity){
										((LivingEntity) event.getEntity()).removePotionEffect(PotionEffectType.CONFUSION);
										((LivingEntity) event.getEntity()).removePotionEffect(PotionEffectType.SLOW);
									}
									event.getDamager().remove();
									minions.remove(event.getDamager());
								}
							}
						}.runTaskTimer(plugin, 0, 20);
					}
				}
				break;
			case 4:
				if (event.getEntity().getFireTicks() > 45){
					event.getEntity().setFireTicks(45);
				}
				break;
			}
		}
	}
	
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if (spawned){
			if (event.getEntity().getUniqueId() == boss.getUniqueId()){
				spawned = false;
				timer = 0;
				for (int x = 0; x < 20; x++){
					MagmaCube baby = boss.getWorld().spawn(boss.getLocation(), MagmaCube.class);
					baby.setSize(1);
					baby.setMaxHealth(40);
					baby.setHealth(40);
					minions.add(baby);
				}
				new BukkitRunnable(){
					public void run() {
						plugin.stop();
					}
				}.runTaskLater(plugin, timer += 90);
			}
		}
		if (minions.contains(event.getEntity())){
			minions.remove(event.getEntity());
		}
	}
	
	public void firstAttack(int id, Entity attacker){
		switch (id){
		case 0:
			for (int x = r.nextInt(2) + 1; x > 0; x--){
				if (getMinionsSize() < 4){
					MagmaCube clone = attacker.getWorld().spawn(attacker.getLocation(), MagmaCube.class);
					clone.setSize(2);
					clone.setMaxHealth(20);
					clone.setHealth(20);
					clone.setCustomName(ChatColor.BLUE + "Unnamed");
					clone.setCustomNameVisible(true);
					clone.setCanPickupItems(false);
					clone.setRemoveWhenFarAway(false);
					minions.add(clone);
				} else {
					break;
				}
			}
			break;
		case 1:
			for (int x = r.nextInt(6) + 5; x > 0; x--){
				if (getMinionsSize() < 12){
					Silverfish shadowbug = attacker.getWorld().spawn(attacker.getLocation(), Silverfish.class);
					shadowbug.setMaxHealth(10);
					shadowbug.setHealth(10);
					minions.add(shadowbug);
				} else {
					break;
				}
			}
			break;
		case 2:
			for (int x = r.nextInt(4) + 1; x > 0; x--){
				if (getMinionsSize() < 6){
					Slime brainSucker = attacker.getWorld().spawn(attacker.getLocation(), Slime.class);
					brainSucker.setSize(2);
					brainSucker.setMaxHealth(7);
					brainSucker.setHealth(7);
					minions.add(brainSucker);
				} else {
					break;
				}
			}
			break;
		case 3:
			for (int x = r.nextInt(4) + 2; x > 0; x--){
				if (getMinionsSize() < 6){
					Zombie knocker = attacker.getWorld().spawn(attacker.getLocation(), Zombie.class);
					knocker.setVillager(false);
					knocker.setBaby(false);
					ItemStack punch = new ItemStack(Material.IRON_HOE);
					punch.addUnsafeEnchantment(Enchantment.KNOCKBACK, r.nextInt(3) + 1);
					EntityEquipment equipment = knocker.getEquipment();
					equipment.setItemInHand(punch);
					equipment.setItemInHandDropChance(0);
					equipment.setHelmet(new ItemStack(Material.SKULL_ITEM));
					equipment.setHelmetDropChance(0);
					minions.add(knocker);
				} else {
					break;
				}
			}
			break;
		case 4:
			for (int x = r.nextInt(3) + 2; x > 0; x--){
				if (getMinionsSize() < 8){
					Slime fireSlime = attacker.getWorld().spawn(attacker.getLocation(), Slime.class);
					fireSlime.setSize(2);
					fireSlime.setFireTicks(Integer.MAX_VALUE);
					minions.add(fireSlime);
				}
			}
			break;
		}
	}
	public void secondAttack(int id, final Entity target, final Entity attacker){
		switch (id){
		case 0:
			final Vector vec = target.getLocation().getDirection().multiply(0.5);
			vec.setY(0);
			new BukkitRunnable(){
				int timer = r.nextInt(101) + 50;
				public void run(){
					timer--;
					target.setVelocity(vec);
					if (timer < 0){
						this.cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 4);
			break;
		case 1:
			new BukkitRunnable(){
				int timer = r.nextInt(101) + 100;
				public void run(){
					timer--;
					if (target instanceof LivingEntity){
						ParticleEffect.SMOKE.animateAtLocation(((LivingEntity) target).getEyeLocation(), 10, 1);
					} else {
						ParticleEffect.SMOKE.animateAtLocation(target.getLocation(), 10, 1);
					}
					if (timer < 0){
						this.cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 1);
			break;
		case 2:
			ParticleEffect.PORTAL.animateAtLocation(attacker.getLocation(), 100, 2);
			new BukkitRunnable(){
				public void run(){
					attacker.teleport(target);
					ParticleEffect.PORTAL.animateAtLocation(attacker.getLocation(), 25, (float) 1.25);
				}
			}.runTaskLater(plugin, 45);
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		}
	}
	public void thirdAttack(int id, Entity target, Entity attacker){
		switch (id){
		case 0:
			
			break;
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		}
	}
	public void attackFour(int id, Entity target, Entity attacker){
		switch (id){
		case 0:
			
			break;
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		}
	}
	public void attackFive(int id, Entity target, Entity attacker){
		switch (id){
		case 0:
			
			break;
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		}
	}
	
	public Player randomPlayer(){
		return Bukkit.getOnlinePlayers()[r.nextInt(Bukkit.getOnlinePlayers().length)];
	}
}
