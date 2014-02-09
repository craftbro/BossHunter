package code.boss.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import code.boss.bosses.arenas.Arena;
import code.boss.bosses.arenas.ArenaArcher;
import code.boss.bosses.arenas.ArenaBeast;
import code.boss.bosses.arenas.ArenaBlazo;
import code.boss.bosses.arenas.ArenaGolem;
import code.boss.bosses.arenas.ArenaMagic;
import code.boss.bosses.arenas.ArenaPig;
import code.boss.bosses.arenas.ArenaSpider;
import code.boss.bosses.arenas.ArenaUnknown;
import code.boss.bosses.arenas.ArenaUnnamed;
import code.boss.game.Game;
import code.boss.item.SkullStack;
import code.boss.player.PlayerManager;
import code.boss.player.eco.Eco;
import code.boss.player.eco.abilities.Shop;
import code.boss.player.eco.collect.Collect;
import code.boss.player.level.Level;
import code.boss.util.Util;
import code.configtesting.config.Config;



public class main extends JavaPlugin implements Listener{

	public Logger log = Logger.getLogger("Minecraft");
	
	
	public Arena arena;
	public Util util;
	public PlayerManager pm;
	public Game game;
	public Level level;
	public Eco eco;
	public Shop shop;
	public Collect collect;
	
	final List<Arena> arenas = new ArrayList<Arena>();
	
	@Override
	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		
		Launch();
		
		pm.registerEvents(this, this);
		pm.registerEvents(this.pm, this);
		
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				tick();
			}
			
		}, 5, 1);
		
		arena.setup();
	}
	
	@Override
	public void onDisable(){
		shutdown();
		
		Bukkit.unloadWorld("BOSS", false);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command c, String cmd, String[] args){
		
		if(sender instanceof Player){
			Player p = (Player)sender;
			
		 if(cmd.equalsIgnoreCase("start")){
			game.force = true;
			game.cd = 1;
			}else if(cmd.equalsIgnoreCase("head")){
				if(args.length == 1){
					p.getInventory().addItem(new SkullStack(args[0]));
					p.sendMessage("Gave the skull of '"+args[0]+"'");
				}else{
					p.sendMessage("/head <name>");
				}
				}else if(cmd.equalsIgnoreCase("stats")){
					level.showStats(p);
				}else if(cmd.equalsIgnoreCase("exp")){
					if(args.length == 1){
						int e = Integer.parseInt(args[0]);
						level.giveExp(p, e, null);
					}
				}else if(cmd.equalsIgnoreCase("level")){
					if(args.length == 1){
						int e = Integer.parseInt(args[0]);
						level.giveLevel(p, e);
					}
				}else if(cmd.contentEquals("reload")){
					shutdown();
				}else if(cmd.equalsIgnoreCase("warpworld")){
					p.teleport(new Location(Bukkit.getWorld(args[0]), p.getLocation().getX(), p.getLocation().getY()+1, p.getLocation().getZ()));
				}else if(cmd.equalsIgnoreCase("additem")){
					collect.giveItem(p, p.getItemInHand());
				}else if(cmd.equalsIgnoreCase("collection")){
					collect.loadInventory(p);
				}
		}
		
		return false;
	}
	
	private void Launch(){
		log.info("Bossfight Launching...");
		
		if(!this.getDataFolder().exists()){
			this.getDataFolder().mkdir();
		}
		
		Config.setFolder(new File(this.getDataFolder(), File.separator+"PlayerData"));
		
		
		arenas.add(new ArenaPig(this));
		
		int r = new Random().nextInt(arenas.size());
		
	//	arena = arenas.get(r);

		
		util = new Util(this);
		pm = new PlayerManager(this);
		game = new Game(this);
		level = new Level(this);
		eco = new Eco(this);
		shop = new Shop(this);
		collect = new Collect(this);
		
		util.copyWorld().setGameRuleValue("keepInventory", "true");
		
		World world = Bukkit.createWorld(new WorldCreator("worldB"));
		world.setGameRuleValue("keepInventory", "true");
		world.setGameRuleValue("doMobSpawning", "false");
		world.setGameRuleValue("mobGriefing", "false");
		world.setGameRuleValue("doMobLoot", "false");
		
		arena = new ArenaSpider(this);
		
		

		log.info("Bossfight Launched!");
	}
	
	public void stop(){
		shutdown();
		Bukkit.reload();
	}
	
	private void shutdown(){
		arena.clear();
	//	util.shutdown();
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getGameMode() != GameMode.CREATIVE){
				p.kickPlayer(ChatColor.DARK_AQUA+""+ChatColor.BOLD+"BossHunter Restarting...");
			}
		}
	}
	
	private void tick(){
		pm.tick();
		arena.tick();
	}
	
}
