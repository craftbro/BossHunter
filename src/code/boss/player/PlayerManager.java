package code.boss.player;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import code.boss.item.NamedStack;
import code.boss.main.main;
import code.boss.player.eco.abilities.Ability;


public class PlayerManager implements Listener {

	main plugin;
	
	public PlayerManager(main instance){
		plugin = instance;
	}
	
	public void tick(){
		Bukkit.getWorld("world").setTime(16000);
		
		for(Player p : Bukkit.getOnlinePlayers()){
			p.setFoodLevel(15);
			if(plugin.arena.getBoss().isSpawned() && !plugin.arena.getBoss().getBoss().isDead()){
				BarAPI.setMessage(p, plugin.arena.getBoss().getBoss().getCustomName(),  (float) (plugin.arena.getBoss().getBoss().getHealth() * 100/plugin.arena.getBoss().getBoss().getMaxHealth()));
			}else{
					BarAPI.removeBar(p);
				
			}
			
			p.setPlayerTime(p.getWorld().getTime(), true);
		//	p.setPlayerTime(1000, true);
			
			if(plugin.arena.getBoss().isSpawned() && !plugin.arena.getBoss().getBoss().isDead()){
			if(p.getExp() < 1){
				p.setExp(p.getExp()+0.0025f);
			}
			}
			
			if(p.getGameMode() != GameMode.CREATIVE){
			tryReapair(p.getInventory().getItem(0));
			tryReapair(p.getEquipment().getHelmet());
			tryReapair(p.getEquipment().getChestplate());
			tryReapair(p.getEquipment().getLeggings());
			tryReapair(p.getEquipment().getBoots());
			}
		}
	}
	
	private void tryReapair(ItemStack i){
		if(i != null && i.getType() != Material.AIR){
			i.setDurability((short) 0);
		}
	}
	
	
	@EventHandler
	public void respawn(PlayerRespawnEvent event){
		final Player p = event.getPlayer();
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				p.teleport(plugin.arena.getSpawn());
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 10));
			}
			
		}, 5);
	}
	
	@EventHandler	
	public void join(PlayerJoinEvent event){
		final Player p = event.getPlayer();
		
		for(PotionEffect po : p.getActivePotionEffects()){
			p.removePotionEffect(po.getType());
		}
		
		p.setWalkSpeed(0.2f);
		
		if(p.getGameMode() != GameMode.CREATIVE){
			p.getInventory().clear();
			
			if(!plugin.level.isRegisterd(p)){
				plugin.level.register(p);
			}
	

			if(!plugin.eco.isRegisterd(p)){
				plugin.eco.register(p);
			}

			plugin.shop.check(p);
			
			plugin.level.equip(p);
			
		
			p.getInventory().setItem(8, new NamedStack(ChatColor.AQUA+"[Upgrade Stats]", Material.NETHER_STAR));
			p.getInventory().setItem(7, new NamedStack(ChatColor.AQUA+"[Ability Shop]", Material.INK_SACK, 1, (byte)6));
			p.getInventory().setItem(6, new NamedStack(ChatColor.AQUA+"[Select Abilties]", Material.CLAY_BALL));
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					p.teleport(plugin.arena.getSpawn());
				}
				
			}, 2);
		}
	}
	
	@EventHandler
	public void kill(EntityDeathEvent event){
		LivingEntity e = event.getEntity();
		if(e.getKiller() != null){
			if(e.getUniqueId() != plugin.arena.getBoss().getBoss().getUniqueId()){
				plugin.util.giveExpAndMoney(e.getKiller(), 10, 10, "Killing An Enemy");
			}else{
//				plugin.level.giveExp(e.getKiller(), 200, "Killing The Boss");
//				plugin.eco.giveMoney(e.getKiller(), 200, "Killing The Boss");
				plugin.util.giveExpAndMoney(e.getKiller(), 200, 200, "Killing The Boss");
				Bukkit.broadcastMessage(plugin.arena.getBoss().getBoss().getCustomName()+ChatColor.GREEN+" was slain by "+e.getKiller().getName());
			}
		}
	}
	
	
	@EventHandler
	public void inte(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(!plugin.game.running){
		if(event.getPlayer().getItemInHand().getType() == Material.NETHER_STAR){
			plugin.level.showMenu(event.getPlayer());
		}else if(event.getPlayer().getItemInHand().getType() == Material.INK_SACK){
			plugin.shop.openMenu(event.getPlayer());
		}else if(event.getPlayer().getItemInHand().getType() == Material.CLAY_BALL){
			plugin.shop.openSelect(event.getPlayer());
		}
		}else{
			int held = event.getPlayer().getInventory().getHeldItemSlot();
			
			if(held == 3 || held == 4){
				if(p.getExp() >= 1 && plugin.shop.getAbility(p.getItemInHand(), p).use() ){
					p.setExp(0);					
				}
			}
		}
	}

	@EventHandler
	public void drop(PlayerDropItemEvent event){
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void up(PlayerPickupItemEvent event){
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void breakB(BlockBreakEvent  event){
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void placeB(BlockPlaceEvent  event){
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void pdamagep(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) event.setCancelled(true);
	}
	
	@EventHandler
	public void placeF(BlockFadeEvent  event){
			event.setCancelled(true);		
	}
	
	@EventHandler
	public void placeFT(BlockFromToEvent  event){
			event.setCancelled(true);		
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent event){
		event.blockList().clear();
	}
	
	@EventHandler
	public void click(InventoryClickEvent event){
		if(((Player)event.getWhoClicked()).getGameMode() != GameMode.CREATIVE){
			event.setCancelled(true);
			
			Player p = (Player) event.getWhoClicked();
			ItemStack i = event.getCurrentItem();
			
			if(event.getInventory().getTitle().contentEquals(ChatColor.GOLD+"           Upgrade Stats") && event.getSlotType() == SlotType.CONTAINER){
				int tokens = plugin.level.getTokens(p);
				
				if(i.getType() != Material.AIR ){
					if(tokens > 0){
						plugin.level.giveTokens(p, -1);
						
						if(i.getType() == Material.CHAINMAIL_CHESTPLATE){
							p.sendMessage(plugin.util.middleOfset(ChatColor.AQUA+"Upgraded armor to level "+(plugin.level.getArmorLevel(p)+1)+"!"));
							plugin.level.giveArmorLevel(p, 1);
							p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 10, 2);
						}else if(i.getType() == Material.IRON_SWORD){
							p.sendMessage(plugin.util.middleOfset(ChatColor.AQUA+"Upgraded weapon to level "+(plugin.level.getWeaponLevel(p)+1)+"!"));
							plugin.level.giveWeaponLevel(p, 1);
							p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 10, 2);
						}else if(i.getType() == Material.RAW_CHICKEN){
							p.sendMessage(plugin.util.middleOfset(ChatColor.AQUA+"Upgraded physical to level "+(plugin.level.getPhysicalLevel(p)+1)+"!"));
							plugin.level.givePhisicalLevel(p, 1);
							p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 10, 2);
						}
						
						plugin.level.refreshInventory(p);
						
						
					}else{
						p.sendMessage(ChatColor.RED+"You need a token to upgrade your stats!");
						p.playSound(p.getEyeLocation(), Sound.NOTE_BASS, 100, 1);
					}
				}
				
				
			}else 	if(event.getInventory().getTitle().contentEquals(ChatColor.BLUE+"           Ability Shop") && event.getSlotType() == SlotType.CONTAINER){
				
				
				if(i.getType() != Material.AIR ){
					if(!plugin.shop.isMax(i)){
					int c = plugin.shop.getCost(i);
					if(plugin.eco.eco.has(p.getName(), c)){
					
						plugin.eco.eco.withdrawPlayer(p.getName(), c);
						
						Ability ab = plugin.shop.getAbility(i, p);
						plugin.shop.giveAbilityLevel(ab, p);
						
						plugin.shop.refreshInventory(p);
						
						
					}else{
						p.sendMessage(ChatColor.RED+"You have insufisiund funds!");
						p.playSound(p.getEyeLocation(), Sound.NOTE_BASS, 100, 1);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This ability is already at its max level!");
					p.playSound(p.getEyeLocation(), Sound.NOTE_BASS, 100, 1);
				}
				}
				
				
			}else 	if(event.getInventory().getTitle().contentEquals(ChatColor.BLUE+"          Select Ability") && event.getSlotType() == SlotType.CONTAINER){
				
				
				if(i.getType() != Material.AIR ){
				plugin.shop.addAbility(p, i);
				}
				
				
			}
		}
	}
	
	@EventHandler
	public void noUproot(PlayerInteractEvent event)
	{
	    if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
	        event.setCancelled(true);
	}
	
	@EventHandler
	public void noUproot(EntityInteractEvent event)
	{
	    if(event.getBlock().getType() == Material.SOIL)
	        event.setCancelled(true);
	}
	
	
	
}
