package code.boss.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import code.boss.main.main;

public class Game {

	main plugin;
	
	public int cd = 60;
	public boolean running = false;
	public boolean force = false;
	
	Scoreboard board;
	Objective ob;
	
	public Game(main instance){
		plugin = instance;
		
		start();
		
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		ob = board.registerNewObjective("cd", "dummy");
		ob.setDisplayName(ChatColor.DARK_AQUA+"Waiting...");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		ob.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA+"Players")).setScore(count());
	}
	
	private void start(){
		new BukkitRunnable(){

			@Override
			public void run() {
				tick();
			}
			
		}.runTaskTimer(plugin, 5, 20);
	}
	
	private int count(){
		int i = 0;
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getGameMode() != GameMode.CREATIVE){
				i++;
			}
		}
		
		return i;
	}
	
	
	private void tick(){
		for(Player p : Bukkit.getOnlinePlayers()){
			p.setScoreboard(board);
		}
		
		if(!running){
			ob.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA+"Players")).setScore(count());
			if(count() >= 2 || force){
			cd--;
			
			ob.setDisplayName(ChatColor.DARK_AQUA+"Starts In "+ChatColor.RED+cd);
			
			if(cd > 0){
			}else{
				ob.unregister();
				running = true;
				plugin.arena.start();
			}
			}else{
				ob.setDisplayName(ChatColor.DARK_AQUA+"Waiting...");
			}
		}
	}
	
}
