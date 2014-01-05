package code.boss.player.eco;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import code.boss.main.main;

public class Eco {
	
	main plugin;
	
	public static Economy eco = null;
	
	public Eco(main instance){
		plugin = instance;
		
		this.setupEconomy();
	}
	
	   private boolean setupEconomy()
	    {
	        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            eco = economyProvider.getProvider();
	        }

	        return (eco != null);
	    }
	   
	   public boolean isRegisterd(Player p){
		   return eco.hasAccount(p.getName());
	   }
	   
	   public void register(Player p){
		   eco.createPlayerAccount(p.getName());
	   }
	   
	   public void giveMoney(Player p, int i){
		   eco.depositPlayer(p.getName(), i);
	   }
	   
	   public void giveMoney(Player p, int i, String why){
		   eco.depositPlayer(p.getName(), i);
		   p.sendMessage(ChatColor.GOLD+"+"+ChatColor.RED+i+ChatColor.GRAY+" Coins"+ChatColor.GOLD+" For "+why);
		   p.playSound(p.getEyeLocation(), Sound.CHICKEN_EGG_POP, 100, 1);
	   }

}
