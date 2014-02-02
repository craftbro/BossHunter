package code.boss.bosses;





import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import code.boss.main.main;

public class BossGolem extends Boss implements Listener{

	final String u_n = ChatColor.RED+"["+ChatColor.DARK_PURPLE+"???"+ChatColor.RED+"]: ";
	final String p_n = ChatColor.DARK_RED+"["+ChatColor.DARK_GRAY+"Golem"+ChatColor.DARK_RED+"]: ";
	final String green = ChatColor.DARK_GREEN+""+ChatColor.BOLD;
	final String yellow = ChatColor.DARK_PURPLE+""+ChatColor.BOLD;
	final String ugn = u_n+green;
	final String pyn = p_n+yellow;
	

	boolean fall = false;
	

	int amount = 1;
	int duration = 300; 
	
	int stage = 0;
	
	Random r = new Random();
	
	public BossGolem(main m){
		super(m);
	}
	
	public void start(){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		plugin.util.broadcastDelaySound(ChatColor.GREEN+"Goal: "+ChatColor.DARK_AQUA+"Kill the Golem!", null, 1, 0);
		plugin.util.broadcastDelaySound(ChatColor.GREEN+"Tip: "+ChatColor.DARK_AQUA+"Run! Just Run!", null, 1, 0);
		
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Any sign of the golem yet?", Sound.VILLAGER_HAGGLE, 1, 20);
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Gosh those things are good at Hide 'n Seek!", Sound.VILLAGER_HAGGLE, 1, 60);
		
		plugin.util.broadcastDelaySound(ugn+"Thanks!", Sound.BURP, -2, 100);
		plugin.util.broadcastDelaySound(ugn+"Oh oops, Golem spoiled Golem's suprise!", Sound.BURP, -2, 160);
		plugin.util.broadcastDelaySound(ugn+"Hello humans! I'm a "+ChatColor.DARK_GRAY+""+ChatColor.BOLD+"Golem", Sound.BURP, -2, 220);
		
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Found him! Let's get em!", Sound.VILLAGER_HAGGLE, 1, 260);
		
		plugin.util.broadcastDelaySound(pyn+"What! Humans want to kill Golem! Humans not nice!", Sound.ENDERDRAGON_GROWL, 2, 320);
		
		plugin.util.broadcastDelaySound(pyn+"Run humans, run! Golem will get you!", Sound.ENDERDRAGON_GROWL, 2, 360);
		
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Shit! It's pissed! Just play along! We need to calm him down!", Sound.VILLAGER_HAGGLE, 1, 420);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				fall = true;
			}
			
		}, 360);
	}
	

	private void bossDeath(){
		spawned = false;
		
		boss.remove();
		
		plugin.arena.clear();
		
		
		if(stage == 1){
		plugin.util.broadcastDelaySound(pyn+"Ough! Meh! Take this stupid humans!", Sound.ENDERDRAGON_GROWL, 2, 20);
		fall = true;
		amount+=3;
		duration = 500;
		stage++;
		}else if(stage == 2){
			plugin.util.broadcastDelaySound(pyn+"NO! I wont die! NOOOOOoooooooo.....", Sound.ENDERDRAGON_GROWL, 2, 20);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Well guys, we just did that nice and smooth!", Sound.VILLAGER_HAGGLE, 1, 80);
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"(aldo i still fell kinda guilty...)", Sound.VILLAGER_HAGGLE, 1, 120);
			fall = false;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					plugin.stop();
				}
				
			}, 360);
			}
	}
	

	
@SuppressWarnings("deprecation")
private void spawnGolem(){
	

	if(stage == 1){
		boss = Bukkit.getWorld("BOSS").spawnCreature(plugin.arena.getSpawn(), EntityType.IRON_GOLEM);
		
		boss.setCustomName(ChatColor.DARK_GRAY+"Golem");
		boss.setCustomNameVisible(true);
		
		boss.setRemoveWhenFarAway(false);
		
		spawned = true;
		
	}else if(stage == 2){
	boss = Bukkit.getWorld("BOSS").spawnCreature(plugin.arena.getSpawn(), EntityType.IRON_GOLEM);
		
		boss.setCustomName(ChatColor.DARK_GRAY+"Golem");
		boss.setCustomNameVisible(true);
		
		boss.setRemoveWhenFarAway(false);
		
		boss.setMaxHealth(boss.getHealth()*Bukkit.getOnlinePlayers().length);
		boss.setHealth(boss.getMaxHealth());
		
		boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		
		spawned = true;
	}
	
}
	
	
	
	@EventHandler
	public void bossDamage(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof IronGolem){
			if(spawned){
				
			}
		}
	}
	
	@EventHandler
	public void minionDeath(EntityDeathEvent event){
		if(minions.contains(event.getEntity())){

		}else if(boss != null && !boss.isDead() && event.getEntity().getUniqueId() == boss.getUniqueId()){
			bossDeath();
		}
	}
	
	
	private Block getRandomBlock(){
		int x = r.nextInt(34);
		int z = r.nextInt(30);
		
		
		return Bukkit.getWorld("BOSS").getBlockAt(68+x, 74, 191+z);
	}
	
	private void talk(){
		if(stage == 1){		
			plugin.util.broadcastDelaySound(pyn+"Stupid humans! Just ru..su..hate th..", Sound.ENDERDRAGON_GROWL, 2, 20);
			plugin.util.broadcastDelaySound(pyn+"I will come get you myself", Sound.ENDERDRAGON_GROWL, 2, 70);
			
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Crap! Come on! Get him!", Sound.VILLAGER_HAGGLE, 1, 130);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					fall = false;
					spawnGolem();
				}
				
			}, 140);
		}else if(stage == 2){		
			plugin.util.broadcastDelaySound(pyn+"NONONONONONO! I hate you! Go away!", Sound.ENDERDRAGON_GROWL, 2, 20);
			plugin.util.broadcastDelaySound(pyn+"I will kill you al this time", Sound.ENDERDRAGON_GROWL, 2, 70);
			
			plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Guys, we need to kill it NOW, else we wont survive this trip!", Sound.VILLAGER_HAGGLE, 1, 130);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					amount += 5;
					spawnGolem();
				}
				
			}, 140);
		}
	}
	
	private void fall(Block b){
		plugin.util.fall(b);
	}
	
	private void attack1(){
		for(Player p : Bukkit.getOnlinePlayers()){
			p.setVelocity(p.getVelocity().add(new Vector(0, 1, 0)));
			
			p.playSound(p.getEyeLocation(), Sound.ANVIL_BREAK, 100, 4);
		}
	}
	
	@Override
	public void tick(){
		
		if(spawned){
			if(r.nextInt(200) == 1){
				attack1();
			}
		}
		
		if(fall){
			if(duration > 0){
			duration--;
		
		
		
				for(int i=0; i<=amount; i++){
				Block b = getRandomBlock();
				if(spawned){
					if(b.getLocation().distance(boss.getLocation()) <= 5){
						return;
					}
				}
				fall(b);
				}
			
		
		}else{
			fall = false;
			stage++;
			amount++;
			talk();
		}
	
	}
	}
}
	

