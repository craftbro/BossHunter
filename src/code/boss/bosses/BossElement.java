package code.boss.bosses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Warning;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ras.RasEffect;
import code.boss.main.main;

/**
 * The class of the boss "Element Keeper"
 * @author rasmusrune
 */
@SuppressWarnings("deprecation")
@Deprecated
@Warning(value=true,reason="This Boss Dosnt Work")
public class BossElement extends Boss implements Listener{
	public static int timer = 0;
	static int attacks = 0;
	public String bossName = "Element Keeper";
	public String u_v = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "???" + ChatColor.BLACK + "] " + ChatColor.GRAY;
	public String l_v = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + bossName + ChatColor.DARK_GREEN + "] " + ChatColor.GRAY;
	public List<UUID> geysing = new ArrayList<UUID>();
	
	Random r = new Random();
	
	public BossElement(main m){
		super(m);
	}
	
	
	public void start(){
		timer = 0;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		/*new BukkitRunnable(){
			public void run(){
				boss = (LivingEntity) Bukkit.getWorld("BOSS").spawnEntity(plugin.arena.getSpawn(), EntityType.SKELETON);
				boss.setMaxHealth(545 * Bukkit.getOnlinePlayers().length);
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
		}.runTaskLater(plugin, timer += 80);*/
	}
	
	
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event){
		event.setCancelled(true);
	}
	
	
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event){
		if (event.getMessage().startsWith("debug.growTree")){
			String[] array = event.getMessage().split(" ");
			if (array.length >= 3){
				try {
					Integer.parseInt(array[1]);
				} catch (NumberFormatException numException){
					event.getPlayer().sendMessage(ChatColor.DARK_RED + "FAILED! Wrong Grow Time");
					event.setCancelled(true);
					return;
				}
				try {
					Integer.parseInt(array[2]);
				} catch (NumberFormatException numException){
					event.getPlayer().sendMessage(ChatColor.DARK_RED + "FAILED! Wrong Grow Time Offset");
					event.setCancelled(true);
					return;
				}
				if (event.getMessage().split(" ").length >= 4){
					growTree(event.getPlayer().getLocation(), Integer.parseInt(array[1]), Integer.parseInt(array[2]), Boolean.parseBoolean(event.getMessage().split(" ")[3]));
				}
				growTree(event.getPlayer().getLocation(), Integer.parseInt(array[1]), Integer.parseInt(array[2]), false);
			} else {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "FAILED! You miss some args! You need Grow Time and Grow Time Offset");
				event.setCancelled(true);
				return;
			}
			event.setCancelled(true);
		} else if (event.getMessage().startsWith("debug.geyser")){
			if (event.getMessage().split(" ").length > 1){
				geyser(event.getPlayer(), Boolean.parseBoolean(event.getMessage().split(" ")[1]));
			}
			geyser(event.getPlayer(), false);
			event.setCancelled(true);
		} else if (event.getMessage().startsWith("debug.windBlow")){
			if (event.getMessage().split(" ").length > 1){
				try {
					Integer.parseInt(event.getMessage().split(" ")[1]);
				} catch (NumberFormatException numException){
					event.getPlayer().sendMessage(ChatColor.DARK_RED + "FAILED! Wrong Distance!");
					event.setCancelled(true);
					return;
				}
				List<Block> list = event.getPlayer().getLastTwoTargetBlocks(null, Integer.parseInt(event.getMessage().split(" ")[1]));
				Item item = event.getPlayer().getWorld().dropItem(list.get(list.size() - 1).getLocation(), new ItemStack(Material.SNOW_BALL));
				item.setPickupDelay(999999);
				windBlow(item, event.getPlayer());
				item.remove();
			} else {
				List<Block> list = event.getPlayer().getLastTwoTargetBlocks(null, 15);
				Item item = event.getPlayer().getWorld().dropItem(list.get(list.size() - 1).getLocation(), new ItemStack(Material.SNOW_BALL));
				item.setPickupDelay(999999);
				windBlow(item, event.getPlayer());
				item.remove();
			}
			event.setCancelled(true);
		}
	}
	
	
	public void growTree(final Location loc, final int timeToGrow, final int growTimeOffset, boolean forceFrost){
		if (r.nextInt(25) == 0 || forceFrost){
			if (placeBlockIfEmpty(loc, Material.LEAVES, (byte) 1)){
				Location belowLoc = loc.clone();
				belowLoc.setY(belowLoc.getY() - 1);
				belowLoc.getWorld().playEffect(belowLoc, Effect.STEP_SOUND, belowLoc.getBlock().getType());
				new BukkitRunnable(){
					public void run(){
						final Location newLoc = loc.clone();
						newLoc.setY(loc.getY() + 1);
						placeBlockIfEmpty(newLoc, Material.LEAVES, (byte) 1);
						loc.getBlock().setType(Material.LOG);
						loc.getBlock().setData((byte) 1);
						new BukkitRunnable(){
							public void run(){
								final short[] bCode = { 
									0,0,0,0,2,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1,0,1,2,1,0,1,0,0,0,0,0,1,0,0,0,0
								};
								final Location minLoc = loc.clone();
								minLoc.setX(minLoc.getBlockX() - 1);
								minLoc.setY(minLoc.getBlockY() - 0);
								minLoc.setZ(minLoc.getBlockZ() - 1);
								final Location maxLoc = loc.clone();
								maxLoc.setX(maxLoc.getBlockX() + 2);
								maxLoc.setY(maxLoc.getBlockY() + 4);
								maxLoc.setZ(maxLoc.getBlockZ() + 2);
								int num = -1;
								for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
									for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
										for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
											num++;
											Location location = new Location(loc.getWorld(), x, y, z);
											if (location.getBlock().isEmpty() || location.getBlock().getType() == Material.LEAVES){
												if (bCode[num] == 1){
													location.getBlock().setType(Material.LEAVES);
													location.getBlock().setData((byte) 1);
												} else if (bCode[num] == 2){
													location.getBlock().setType(Material.LOG);
													location.getBlock().setData((byte) 1);
												}
											}
										}
									}
								}
								new BukkitRunnable(){
									public void run(){
										final short[] bCode = { 
											0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,2,1,0,0,1,1,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,1,1,2,1,1,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0
										};
										final Location minLoc = loc.clone();
										minLoc.setX(minLoc.getBlockX() - 2);
										minLoc.setY(minLoc.getBlockY() - 0);
										minLoc.setZ(minLoc.getBlockZ() - 2);
										final Location maxLoc = loc.clone();
										maxLoc.setX(maxLoc.getBlockX() + 3);
										maxLoc.setY(maxLoc.getBlockY() + 5);
										maxLoc.setZ(maxLoc.getBlockZ() + 3);
										int num = -1;
										for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
											for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
												for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
													num++;
													Location location = new Location(loc.getWorld(), x, y, z);
													if (location.getBlock().isEmpty() || location.getBlock().getType() == Material.LEAVES){
														if (bCode[num] == 1){
															location.getBlock().setType(Material.LEAVES);
															location.getBlock().setData((byte) 1);
														} else if (bCode[num] == 2){
															location.getBlock().setType(Material.LOG);
															location.getBlock().setData((byte) 1);
														}
													}
												}
											}
										}
										new BukkitRunnable(){
											public void run(){
												final short[] bCode = { 
													0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1,1,1,0,0,1,2,1,0,0,1,1,1,0,1,0,0,0,1,0,0,1,0,0,0,1,1,1,0,1,1,2,1,1,0,1,1,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,1,1,2,1,1,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,1,0,0,0,2,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0
												};
												final Location minLoc = loc.clone();
												minLoc.setX(minLoc.getBlockX() - 2);
												minLoc.setY(minLoc.getBlockY() - 0);
												minLoc.setZ(minLoc.getBlockZ() - 2);
												final Location maxLoc = loc.clone();
												maxLoc.setX(maxLoc.getBlockX() + 3);
												maxLoc.setY(maxLoc.getBlockY() + 7);
												maxLoc.setZ(maxLoc.getBlockZ() + 3);
												int num = -1;
												for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
													for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
														for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
															num++;
															Location location = new Location(loc.getWorld(), x, y, z);
															if (location.getBlock().isEmpty() || location.getBlock().getType() == Material.LEAVES){
																if (bCode[num] == 1){
																	location.getBlock().setType(Material.LEAVES);
																	location.getBlock().setData((byte) 1);
																} else if (bCode[num] == 2){
																	location.getBlock().setType(Material.LOG);
																	location.getBlock().setData((byte) 1);
																}
															}
														}
													}
												}
												new BukkitRunnable(){
													public void run(){
														final short[] bCode = { 
															0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1,1,1,0,0,1,2,1,0,0,1,1,1,0,1,0,0,0,1,0,0,1,0,0,0,1,1,1,0,1,1,2,1,1,0,1,1,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,1,1,2,1,1,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,1,0,0,0,2,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0
														};
														final Location minLoc = loc.clone();
														minLoc.setX(minLoc.getBlockX() - 2);
														minLoc.setY(minLoc.getBlockY() - 0);
														minLoc.setZ(minLoc.getBlockZ() - 2);
														final Location maxLoc = loc.clone();
														maxLoc.setX(maxLoc.getBlockX() + 3);
														maxLoc.setY(maxLoc.getBlockY() + 7);
														maxLoc.setZ(maxLoc.getBlockZ() + 3);
														int num = -1;
														List<String> damaged = new ArrayList<String>();
														for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
															for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
																for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
																	num++;
																	Location location = new Location(loc.getWorld(), x, y, z);
																	if (location.getBlock().getType() == Material.LEAVES || location.getBlock().getType() == Material.LOG){
																		if (bCode[num] == 2){
																			location.getWorld().playEffect(location, Effect.STEP_SOUND, Material.SNOW_BLOCK);
																			Item item = location.getWorld().dropItem(location, new ItemStack(Material.LOG));
																			item.setPickupDelay(99999999);
																			Entity[] array = item.getNearbyEntities(2.7, 2.7, 2.7).toArray(new Entity[0]).clone();
																			for (Entity entity : array){
																				if (entity instanceof Player){
																					if (!damaged.contains(((Player) entity).getName())){
																						((Player) entity).damage(5);
																						damaged.add(((Player) entity).getName());
																					}
																				}
																			}
																			item.remove();
																			location.getBlock().setType(Material.AIR);
																		} else if (bCode[num] == 1){
																			location.getWorld().playEffect(location, Effect.STEP_SOUND, Material.ICE);
																			Item item = location.getWorld().dropItem(location, new ItemStack(Material.LOG));
																			item.setPickupDelay(99999999);
																			Entity[] array = item.getNearbyEntities(2.7, 2.7, 2.7).toArray(new Entity[0]).clone();
																			for (Entity entity : array){
																				if (entity instanceof Player){
																					if (!damaged.contains(((Player) entity).getName())){
																						((Player) entity).damage(5);
																						damaged.add(((Player) entity).getName());
																					}
																				}
																			}
																			item.remove();
																			location.getBlock().setType(Material.AIR);
																		}
																	}
																}
															}
														}
													}
												}.runTaskLater(plugin, (long) (timeToGrow * 1.515 + r.nextInt((int) ((growTimeOffset + 1) * 1.515))));
											}
										}.runTaskLater(plugin, (long) (timeToGrow * 1.3 + r.nextInt((int) ((growTimeOffset + 1) * 1.3))));
									}
								}.runTaskLater(plugin, (long) (timeToGrow * 1.15 + r.nextInt((int) ((growTimeOffset + 1) * 1.15))));
							}
						}.runTaskLater(plugin, (long) (timeToGrow * 1.025 + r.nextInt((int) ((growTimeOffset + 1) * 1.025))));
					}
				}.runTaskLater(plugin, timeToGrow + r.nextInt(growTimeOffset + 1));
			}
		} else {
			if (placeBlockIfEmpty(loc, Material.LEAVES, (byte) 0)){
				Location belowLoc = loc.clone();
				belowLoc.setY(belowLoc.getY() - 1);
				belowLoc.getWorld().playEffect(belowLoc, Effect.STEP_SOUND, belowLoc.getBlock().getType());
				new BukkitRunnable(){
					public void run(){
						final Location newLoc = loc.clone();
						newLoc.setY(loc.getY() + 1);
						placeBlockIfEmpty(newLoc, Material.LEAVES, (byte) 0);
						loc.getBlock().setType(Material.LOG);
						loc.getBlock().setData((byte) 0);
						new BukkitRunnable(){
							public void run(){
								final short[] bCode = { 
									0,0,0,0,2,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1,0,1,2,1,0,1,0,0,0,0,0,1,0,0,0,0
								};
								final Location minLoc = loc.clone();
								minLoc.setX(minLoc.getBlockX() - 1);
								minLoc.setY(minLoc.getBlockY() - 0);
								minLoc.setZ(minLoc.getBlockZ() - 1);
								final Location maxLoc = loc.clone();
								maxLoc.setX(maxLoc.getBlockX() + 2);
								maxLoc.setY(maxLoc.getBlockY() + 4);
								maxLoc.setZ(maxLoc.getBlockZ() + 2);
								int num = -1;
								for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
									for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
										for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
											num++;
											Location location = new Location(loc.getWorld(), x, y, z);
											if (location.getBlock().isEmpty() || location.getBlock().getType() == Material.LEAVES){
												if (bCode[num] == 1){
													location.getBlock().setType(Material.LEAVES);
												} else if (bCode[num] == 2){
													location.getBlock().setType(Material.LOG);
													location.getBlock().setData((byte) 0);
												}
											}
										}
									}
								}
								new BukkitRunnable(){
									public void run(){
										final short[] bCode = { 
											0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,1,0,1,1,2,1,1,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,2,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0
										};
										final Location minLoc = loc.clone();
										minLoc.setX(minLoc.getBlockX() - 2);
										minLoc.setY(minLoc.getBlockY() - 0);
										minLoc.setZ(minLoc.getBlockZ() - 2);
										final Location maxLoc = loc.clone();
										maxLoc.setX(maxLoc.getBlockX() + 3);
										maxLoc.setY(maxLoc.getBlockY() + 5);
										maxLoc.setZ(maxLoc.getBlockZ() + 3);
										int num = -1;
										for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
											for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
												for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
													num++;
													Location location = new Location(loc.getWorld(), x, y, z);
													if (location.getBlock().isEmpty() || location.getBlock().getType() == Material.LEAVES){
														if (bCode[num] == 1){
															location.getBlock().setType(Material.LEAVES);
														} else if (bCode[num] == 2){
															location.getBlock().setType(Material.LOG);
															location.getBlock().setData((byte) 0);
														}
													}
												}
											}
										}
										new BukkitRunnable(){
											public void run(){
												final short[] bCode = { 
													0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,0,0,0,1,1,1,0,1,1,2,1,1,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0
												};
												final Location minLoc = loc.clone();
												minLoc.setX(minLoc.getBlockX() - 2);
												minLoc.setY(minLoc.getBlockY() - 0);
												minLoc.setZ(minLoc.getBlockZ() - 2);
												final Location maxLoc = loc.clone();
												maxLoc.setX(maxLoc.getBlockX() + 3);
												maxLoc.setY(maxLoc.getBlockY() + 5);
												maxLoc.setZ(maxLoc.getBlockZ() + 3);
												int num = -1;
												for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
													for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
														for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
															num++;
															Location location = new Location(loc.getWorld(), x, y, z);
															if (location.getBlock().isEmpty() || location.getBlock().getType() == Material.LEAVES){
																if (bCode[num] == 1){
																	location.getBlock().setType(Material.LEAVES);
																} else if (bCode[num] == 2){
																	location.getBlock().setType(Material.LOG);
																	location.getBlock().setData((byte) 0);
																}
															}
														}
													}
												}
												new BukkitRunnable(){
													public void run(){
														final short[] bCode = { 
															0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,0,0,0,1,1,1,0,1,1,1,1,1,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0
														};
														final Location minLoc = loc.clone();
														minLoc.setX(minLoc.getBlockX() - 2);
														minLoc.setY(minLoc.getBlockY() - 0);
														minLoc.setZ(minLoc.getBlockZ() - 2);
														final Location maxLoc = loc.clone();
														maxLoc.setX(maxLoc.getBlockX() + 3);
														maxLoc.setY(maxLoc.getBlockY() + 5);
														maxLoc.setZ(maxLoc.getBlockZ() + 3);
														int num = -1;
														List<String> damaged = new ArrayList<String>();
														for (int y = (int) minLoc.getY(); y < maxLoc.getY(); y++){
															for (int x = (int) minLoc.getX(); x < maxLoc.getX(); x++){
																for (int z = (int) minLoc.getZ(); z < maxLoc.getZ(); z++){
																	num++;
																	Location location = new Location(loc.getWorld(), x, y, z);
																	if (location.getBlock().getType() == Material.LEAVES || location.getBlock().getType() == Material.LOG){
																		if (bCode[num] == 1){
																			location.getWorld().playEffect(location, Effect.STEP_SOUND, location.getBlock().getType());
																			Item item = location.getWorld().dropItem(location, new ItemStack(Material.LOG));
																			item.setPickupDelay(99999999);
																			Entity[] array = item.getNearbyEntities(2, 2, 2).toArray(new Entity[0]).clone();
																			for (Entity entity : array){
																				if (entity instanceof Player){
																					if (!damaged.contains(((Player) entity).getName())){
																						((Player) entity).damage(5);
																						damaged.add(((Player) entity).getName());
																					}
																				}
																			}
																			item.remove();
																			location.getBlock().setType(Material.AIR);
																		}
																	}
																}
															}
														}
													}
												}.runTaskLater(plugin, (long) (timeToGrow * 1.515 + r.nextInt((int) ((growTimeOffset + 1) * 1.515))));
											}
										}.runTaskLater(plugin, (long) (timeToGrow * 1.3 + r.nextInt((int) ((growTimeOffset + 1) * 1.3))));
									}
								}.runTaskLater(plugin, (long) (timeToGrow * 1.15 + r.nextInt((int) ((growTimeOffset + 1) * 1.15))));
							}
						}.runTaskLater(plugin, (long) (timeToGrow * 1.025 + r.nextInt((int) ((growTimeOffset + 1) * 1.025))));
					}
				}.runTaskLater(plugin, timeToGrow + r.nextInt(growTimeOffset + 1));
			}
		}
	}
	
	
	public boolean geyser(final Entity entity, boolean forceLava){
		if (!geysing.contains(entity.getUniqueId())){
			geysing.add(entity.getUniqueId());
			if (r.nextInt(25) == 0 && entity instanceof Damageable || forceLava){
				final Vector vec = new Vector(0, 0.3, 0);
				new BukkitRunnable(){
					int times = 0;
					public void run(){
						entity.setVelocity(vec);
						RasEffect.FLAME.display(entity.getLocation(), (float) 0.5, 0, (float) 0.5, 1, 10);
						RasEffect.displayBlockCrack(entity.getLocation(), Material.LAVA.getId(), (byte) 0, (float) 0.5, 0, (float) 0.5, 1, 2 + r.nextInt(2));
						((Damageable) entity).damage(2);
						if (times > 4){
							this.cancel();
							new BukkitRunnable(){
								public void run(){
									if (geysing.contains(entity.getUniqueId())){
										geysing.remove(entity.getUniqueId());
									}
								}
							}.runTaskLater(plugin, Math.round(7 * 2.5 * 2));
						}
						times++;
					}
				}.runTaskTimer(plugin, 0, 7);
				return true;
			} else {
				final Vector vec = new Vector(0, 0.475, 0);
				new BukkitRunnable(){
					int times = 0;
					public void run(){
						entity.setVelocity(vec);
						RasEffect.SPLASH.display(entity.getLocation(), (float) 0.5, 0, (float) 0.5, 1, 10);
						RasEffect.displayBlockCrack(entity.getLocation(), Material.WATER.getId(), (byte) 0, (float) 0.5, 0, (float) 0.5, 1, 2 + r.nextInt(2));
						if (times > 4){
							this.cancel();
							new BukkitRunnable(){
								public void run(){
									if (geysing.contains(entity.getUniqueId())){
										geysing.remove(entity.getUniqueId());
									}
								}
							}.runTaskLater(plugin, Math.round(7 * 3.75 * 2));
						}
						times++;
					}
				}.runTaskTimer(plugin, 0, 7);
				return true;
			}
		} else return false;
	}
	
	
	public void windBlow(Entity from, final LivingEntity to){
		final Location sLoc = from.getLocation();
		Location tLoc = to.getEyeLocation();
		final Vector vec = tLoc.toVector().subtract(sLoc.toVector()).normalize().multiply(0.25);
		new BukkitRunnable(){
			int counter = 0;
			Location currentLoc = sLoc.add(vec);
			int hitsLeft = 6;
			public void run() {
				counter++;
				Location tLoc = to.getEyeLocation();
				Vector vec = tLoc.toVector().subtract(currentLoc.toVector()).normalize().multiply(0.2);
				currentLoc = sLoc.add(vec);
				RasEffect.CLOUD.display(currentLoc, 0.6F, 0.6F, 0.6F, 0.325F, 10);
				Item item = currentLoc.getWorld().dropItem(currentLoc, new ItemStack(Material.SNOW_BALL));
				item.setPickupDelay(99999);
				Iterator<Entity> itr = item.getNearbyEntities(1, 1, 1).iterator();
				item.remove();
				while (itr.hasNext()){
					Entity entity = itr.next();
					if (entity.getUniqueId() == to.getUniqueId()){
						currentLoc.getWorld().playSound(currentLoc, Sound.BAT_TAKEOFF, 4, 1);
						entity.setVelocity(vec.clone().normalize().multiply(2).setY(.6));
						hitsLeft--;
						if (hitsLeft < 0){
							this.cancel();
						}
						break;
					}
				}
				if (counter >= 350){
					RasEffect.CLOUD.display(currentLoc, 2, 2, 2, 1.25F, 25);
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	
	public void flameMinion(Location loc){
		PigZombie flame = loc.getWorld().spawn(loc, PigZombie.class);
	}
	
	
	public boolean placeBlockIfEmpty(Location loc, Material mat, byte data){
		if (loc.getBlock().isEmpty()){
			loc.getBlock().setType(mat);
			loc.getBlock().setData(data);
			return true;
		} else {
			return false;
		}
	}
}
