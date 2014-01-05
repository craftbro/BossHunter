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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
import code.boss.item.SkullStack;
import code.boss.main.main;

/**
 * The class of the boss "Skellyton"
 * Please find a better name.....
 * @author rasmusrune
 */
public class BossSkellyton extends Boss{
	public List<Entity> charging = new ArrayList<Entity>();
	public static int timer = 0;
	static int attacks = 0;
	public String bossName = "Skellyton";
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String s_v = ChatColor.DARK_RED + "[" + ChatColor.RED + bossName + ChatColor.DARK_RED + "] " + ChatColor.GOLD;
	
	Random r = new Random();
	
	public BossSkellyton(main m){
		super(m);
	}

	
	public void start(){
		
	}
	
	
	public void tick(){
		if (spawned){
			if (r.nextInt(7) == 0){
				Vector vec = plugin.util.getNearest(boss).getLocation().toVector().subtract(boss.getLocation().toVector()).normalize().multiply(2);
				vec.setY(0);
				boss.setVelocity(vec);
				plugin.util.setTargetToNearest(boss);
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
	}
}
