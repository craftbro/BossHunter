package code.boss.bosses;





import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.item.ColoredStack;
import code.boss.item.SkullStack;
import code.boss.main.main;

public class BossTest extends Boss implements Listener{

	final String u_n = ChatColor.RED+"["+ChatColor.DARK_PURPLE+"???"+ChatColor.RED+"]: ";
	final String p_n = ChatColor.RED+"["+ChatColor.YELLOW+"Pig Master"+ChatColor.RED+"]: ";
	final String green = ChatColor.DARK_GREEN+""+ChatColor.BOLD;
	final String yellow = ChatColor.DARK_AQUA+""+ChatColor.BOLD;
	final String ugn = u_n+green;
	final String pyn = p_n+yellow;
	
	boolean talk1 = false;
	boolean talk2 = false;
	boolean talk3 = false;
	
	boolean recharged = false;
	
	boolean talk = true;
	
	int stage = 0;
	
	Random r = new Random();
	
	public BossTest(main m){
		super(m);
	}
	
	public void start(){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		plugin.util.broadcastDelaySound(ChatColor.GREEN+"Goal: "+ChatColor.DARK_AQUA+"Get your farm back!", null, 1, 0);
		plugin.util.broadcastDelaySound(ChatColor.GREEN+"Tip: "+ChatColor.DARK_AQUA+"This is an easy boss. Just melee", null, 1, 0);
		
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Hey, look! Someone broke into our farm!", Sound.VILLAGER_HAGGLE, 1, 20);
		
		plugin.util.broadcastDelaySound(ugn+"Hello there!", Sound.BURP, -2, 100);
		plugin.util.broadcastDelaySound(ugn+"I took your house and your wife "+plugin.util.randomName()+"!", Sound.BURP, -2, 180);
		plugin.util.broadcastDelaySound(ugn+"This land is owned by me, "+ChatColor.YELLOW+""+ChatColor.BOLD+"The Pig Master", Sound.BURP, -2, 240);
		
		plugin.util.broadcastDelaySound(pyn+"You will be punished for enetring my land!", Sound.PIG_IDLE, 1, 300);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				spawn();
			}
			
		}, 340);
	}
	
	private void muteTalk(int i){
		talk = false;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				talk = true;
			}
			
		}, i)	;
		}
	
	private void bossDeath(){
		spawned = false;
		
		boss.remove();
		
		plugin.arena.clear();
		
		
		
		plugin.util.broadcastDelaySound(pyn+"Ok! Ok! You can have my land!", Sound.PIG_IDLE, 1, 60);
		plugin.util.broadcastDelaySound(pyn+"So uuuhm, I'll just leave now", Sound.PIG_IDLE, 1, 120);
		plugin.util.broadcastDelaySound(pyn+"B..bye", Sound.PIG_IDLE, 1, 170);
		
		plugin.util.broadcastDelaySound(plugin.util.randomNameFormat()+"Yeah! We got our farm back!", Sound.VILLAGER_HAGGLE, 1, 220);
		
		plugin.util.broadcastDelaySound(ChatColor.GOLD+"You got your farm back!", Sound.LEVEL_UP, 1, 300);
	}
	
	private void upgrade(){
		spawned = false;
		boss.remove();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				stage++;
				
				boss = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 37, 58, -183), EntityType.PIG_ZOMBIE);
				
				boss.getEquipment().setHelmet(new SkullStack("MHF_PigZombie"));
				boss.getEquipment().setChestplate(new ColoredStack(Color.fromRGB(231, 150, 154), Material.LEATHER_CHESTPLATE));
				boss.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				boss.getEquipment().setBoots(new ColoredStack(Color.fromRGB(171, 127, 54), Material.LEATHER_BOOTS));
				
				ItemStack hand = new ItemStack(Material.GOLD_SWORD);
				
				ItemMeta meta = hand.getItemMeta();
				meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
				meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
				hand.setItemMeta(meta);
				
				boss.getEquipment().setItemInHand(hand);
				
				boss.setCustomName(ChatColor.YELLOW+""+ChatColor.BOLD+"Pig Master");
				boss.setCustomNameVisible(true);
				
				boss.setMaxHealth(100+(Bukkit.getOnlinePlayers().length*80));
				boss.setHealth(boss.getMaxHealth());
				
				boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
				
				boss.setRemoveWhenFarAway(false);
				
				spawned = true;
			}
			
		}, 70);
	
	}
	
	@Override
	public void spawn(){
		boss = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 37, 58, -183), EntityType.PIG_ZOMBIE);
		
		boss.getEquipment().setHelmet(new SkullStack("MHF_Pig"));
		boss.getEquipment().setChestplate(new ColoredStack(Color.fromRGB(231, 150, 154), Material.LEATHER_CHESTPLATE));
		boss.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		boss.getEquipment().setBoots(new ColoredStack(Color.fromRGB(171, 127, 54), Material.LEATHER_BOOTS));
		
		ItemStack hand = new ItemStack(Material.GOLD_SWORD);
		
		ItemMeta meta = hand.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
		meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
		hand.setItemMeta(meta);
		
		boss.getEquipment().setItemInHand(hand);
		
		boss.setCustomName(ChatColor.YELLOW+""+ChatColor.BOLD+"Pig Master");
		boss.setCustomNameVisible(true);
		
		boss.setMaxHealth(100);
		boss.setHealth(100);
		
		boss.setRemoveWhenFarAway(false);
		
		this.spawned = true;
		
	}
	
	public int getMinionsSize(){
		int i = 0;
	
		
		for(LivingEntity e : minions){
			if(!e.isDead()){
				i++;
			}
		}
		
		return i;
	}
	
	private void recharge(){
		recharged = true;
		
		new BukkitRunnable(){

			@Override
			public void run() {
				if(boss.getHealth() < boss.getMaxHealth()){
					if(boss.getHealth() + boss.getMaxHealth()/15 <= boss.getMaxHealth()){
						boss.setHealth(boss.getHealth() + boss.getMaxHealth()/15);
					}else{
						boss.setHealth(boss.getMaxHealth());
					}
				}else{
					cancel();
				}
			}
			
		}.runTaskTimer(plugin, 1, 5);
	}
	
	@EventHandler
	public void bossDamage(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof PigZombie){
			if(spawned){
				if(talk){
				if(!talk1){
					plugin.util.broadcastDelaySound(pyn+"Ough! That hurts!", Sound.PIG_IDLE, 1, 1);
					talk1 = true;
					muteTalk(60);
				}else if(!talk2){
					plugin.util.broadcastDelaySound(pyn+"Hey! Stop! This isn't funny anymore!", Sound.PIG_IDLE, 1, 1);
					talk2 = true;
					muteTalk(60);
				}else if(!talk3){
					plugin.util.broadcastDelaySound(pyn+"Ok then. If this is what you want", Sound.PIG_IDLE, -1, 1);
					talk3 = true;
					upgrade();
				}
				}
			}
		}
	}
	
	@EventHandler
	public void minionDeath(EntityDeathEvent event){
		if(minions.contains(event.getEntity())){

		}else if(event.getEntity().getUniqueId() == boss.getUniqueId() && stage == 1){
			bossDeath();
		}
	}
	
	
	
	private void attack1(){
		ParticleEffect.INSTANT_SPELL.animateAtLocation(boss.getLocation(), 10, 1);
		for(int i=0; i<=3; i++){
			Skeleton s = (Skeleton)boss.getWorld().spawnEntity(boss.getLocation(), EntityType.SKELETON);
			
			s.getEquipment().setHelmet(new SkullStack("MHF_Pig"));
			s.getEquipment().setChestplate(new ColoredStack(Color.fromRGB(231, 150, 154), Material.LEATHER_CHESTPLATE));
			s.getEquipment().setLeggings(new ColoredStack(Color.fromRGB(231, 150, 154), Material.LEATHER_LEGGINGS));
			s.getEquipment().setBoots(new ColoredStack(Color.fromRGB(231, 150, 154), Material.LEATHER_BOOTS));
			
			ItemStack hand = new ItemStack(Material.WOOD_SWORD);
		
			
			ItemMeta meta = hand.getItemMeta();
			meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
			hand.setItemMeta(meta);
			
			s.getEquipment().setItemInHand(hand);
			
			s.setRemoveWhenFarAway(false);
			
			plugin.util.setTargetToNearest(s);
			
			minions.add(s);
			
		}
	}
	
	public void attack2(){
		final Item drop = boss.getWorld().dropItem(boss.getEyeLocation(), new ItemStack(Material.getMaterial(364)));
		
		drop.setPickupDelay(1000);
		drop.setVelocity(boss.getLocation().getDirection().add(new Vector(0, 0.3, 0)));
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				drop.getWorld().createExplosion(drop.getLocation(), 4);
				drop.remove();
			}
			
		}, 40);
		
	}
	
	public void attack3(){
		ParticleEffect.CLOUD.animateAtLocation(boss.getEyeLocation(), 100, 1);
		boss.getWorld().playSound(boss.getEyeLocation(), Sound.PIG_DEATH, 7, 3);
		for(Entity en : boss.getNearbyEntities(10, 10, 10)){
			if(en instanceof LivingEntity){
				LivingEntity e = (LivingEntity)en;
				
				Vector direction = e.getLocation().toVector().subtract(boss.getLocation().toVector()).normalize();
				
				direction.multiply(3);
				direction.add(new Vector(0, 0.8, 0));
				
				e.setVelocity(direction);
			}
		}
	}
	
	private void attack4(){
		ParticleEffect.LAVA.animateAtLocation(boss.getEyeLocation(), 10, 1);
		boss.getWorld().playSound(boss.getEyeLocation(), Sound.DRINK, 7, 0);
		for(Entity en : boss.getNearbyEntities(10, 10, 10)){
			if(en instanceof LivingEntity){
				LivingEntity e = (LivingEntity)en;
				
				e.damage(10, boss);
				
				plugin.util.lineRunnable(e.getEyeLocation(), boss.getEyeLocation(), ParticleEffect.HEART, new Runnable(){

					@Override
					public void run() {
						plugin.util.heal(boss, 20);
					}
					
				});
				
			}
		}
	}
	
	private void attack(){
		
		if(this.getMinionsSize() < 9 && r.nextInt(250) == 0){
	         attack1();
		}
		
		if(r.nextInt(150) == 0){
	         attack2();
		}
		
		if(r.nextInt(200) == 0){
	         attack3();
		}
		
		if(r.nextInt(200) == 0){
	         attack4();
		}
	}
	
	@Override
	public void tick(){
		
		
		
		if(spawned && stage == 1){
			
			if(!boss.getLocation().getChunk().isLoaded()){
			boss.getLocation().getChunk().load();
			}
			
			plugin.util.setTargetToNearest(boss);
			
			if(boss.getHealth() <= boss.getMaxHealth()/5 && !recharged){
				recharge();
				plugin.util.broadcastDelaySound(pyn+"Hehe. You thought i was low on health didn't you?", Sound.PIG_IDLE, 1, 1);
			}
			
			attack();

		
		}
		
	}
	
	
}
