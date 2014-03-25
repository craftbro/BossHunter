package code.boss.bosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import code.boss.effect.ParticleEffect;
import code.boss.main.main;

@SuppressWarnings("deprecation")
public class BossMagican extends Boss implements Listener{
	public int attacks = 0;
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String m_v = ChatColor.LIGHT_PURPLE + "[" + ChatColor.GREEN + "Magican" + ChatColor.LIGHT_PURPLE + "] " + ChatColor.GRAY;
	
	public boolean isAttacking = false;
	
	public BossMagican(main m) {
		super(m);
	}
	
	public void start() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void tick() {
		
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event){
		if (event.getMessage().startsWith("debug.cluckoff")){
			cluckoff(event.getPlayer());
		}
	}
	
	
	
	public void cluckoff(final Entity entity){
		isAttacking = true;
		final Location loc = entity.getLocation();
		new BukkitRunnable(){
			int times = 0;
			public void run(){
				for (Player p : Bukkit.getOnlinePlayers()){
					p.playSound(p.getLocation(), Sound.CHICKEN_IDLE, 10, times / 10);
				}
				entity.teleport(loc);
				ParticleEffect.PORTAL.animateAtLocation(loc, 10, 5);
				if (times > 20){
					for (Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.HORSE_SKELETON_DEATH, 10, 1);
					}
					for (Entity entitie : entity.getWorld().getEntities()){
						ParticleEffect.LAVA.animateAtLocation(loc, 10, 1);
						if (!(spawned && entitie.getUniqueId() == boss.getUniqueId())){
							if (entitie instanceof Damageable){
								((Damageable) entitie).setHealth(0);
							} else {
								entitie.remove();
							}
						}
					}
					isAttacking = false;
					this.cancel();
				}
				times++;
			}
		}.runTaskTimer(plugin, 0, 4);
	}
}
