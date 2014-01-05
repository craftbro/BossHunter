package code.boss.player.level;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import code.boss.item.NamedStack;
import code.boss.main.main;
import code.configtesting.config.Config;

public class Level {
	
	main plugin;
	
	public Level(main instance){
		this.plugin = instance;
	}
	
	public boolean isRegisterd(Player p){
		return Config.getPlayerFile(p).contains("level");
	}
	
	public void register(Player p){	
		Config.setData(p, "level", 0);
		Config.setData(p, "xp", 0);
		Config.setData(p, "levelarmor", 0);
		Config.setData(p, "levelsword", 0);
		Config.setData(p, "levelphysic", 0);
		Config.setData(p, "tokens", 0);
	}
	
	public int getNeededExp(int level){
		return (int) (100*(0.3*Math.pow(level, 1.05)));
	}
	
	public int getLevel(Player p){
		return (Integer) Config.getData(p, "level");
	}
	
	public int getTokens(Player p){
		return (Integer) Config.getData(p, "tokens");
	}
	
	public int getArmorLevel(Player p){
		return (Integer) Config.getData(p, "levelarmor");
	}
	
	public void giveArmorLevel(Player p, int add){
		int cw = getArmorLevel(p);
		int nw = cw+add;
		
		Config.setData(p, "levelarmor", nw);
	}
	
	public int getWeaponLevel(Player p){
		return (Integer) Config.getData(p, "levelsword");
	}
	
	public void giveWeaponLevel(Player p, int add){
		int cw = getWeaponLevel(p);
		int nw = cw+add;
		
		Config.setData(p, "levelsword", nw);
	}
	
	public int getPhysicalLevel(Player p){
		return (Integer) Config.getData(p, "levelphysic");
	}
	
	public void givePhisicalLevel(Player p, int add){
		int cw = getPhysicalLevel(p);
		int nw = cw+add;
		
		Config.setData(p, "levelphysic", nw);
	}
	
	public void giveTokens(Player p, int tokens){
		int ct = getTokens(p);
		int nt = ct+tokens;
		
		Config.setData(p, "tokens", nt);
	}
	
	public void giveLevel(Player p, int level){
		int cl = getLevel(p);
		int nl = cl+level;
		
		giveTokens(p, level);
		
		p.sendMessage(plugin.util.middleOfset(ChatColor.GREEN+"Level Up!"));
		p.sendMessage(plugin.util.middleOfset(ChatColor.GREEN+"You are now level "+ChatColor.AQUA+ChatColor.BOLD+nl+ChatColor.RESET+ChatColor.GREEN+"!"));
		p.sendMessage(plugin.util.middleOfset(ChatColor.GREEN+"You received "+level+" token(s) and now have "+ChatColor.AQUA+getTokens(p)+ChatColor.GREEN+"!"));
		
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 1);
		
		Config.setData(p, "level", nl);
	}
	
	public int getExp(Player p){
		return (Integer) Config.getData(p, "xp");
	}
	
	public void showStats(Player p){
		p.sendMessage(ChatColor.GRAY+"--------------------------------------------------");
		p.sendMessage(plugin.util.middleOfset(ChatColor.DARK_AQUA+"Total Level: "+ChatColor.AQUA+getLevel(p)));
		p.sendMessage(plugin.util.middleOfset(ChatColor.DARK_AQUA+"Armor Level: "+ChatColor.AQUA+getArmorLevel(p)));
		p.sendMessage(plugin.util.middleOfset(ChatColor.DARK_AQUA+"Weapon Level: "+ChatColor.AQUA+getWeaponLevel(p)));
		p.sendMessage(plugin.util.middleOfset(ChatColor.DARK_AQUA+"Physical Level: "+ChatColor.AQUA+getPhysicalLevel(p)));
		p.sendMessage(" ");
		p.sendMessage(plugin.util.middleOfset(ChatColor.DARK_GREEN+"Xp: "+ChatColor.GREEN+getExp(p)));
		p.sendMessage(plugin.util.middleOfset(ChatColor.DARK_GREEN+"Xp To Next Level: "+ChatColor.GREEN+(getNeededExp(getLevel(p)+1)-getExp(p))));
		p.sendMessage(" ");
		p.sendMessage(plugin.util.middleOfset(ChatColor.DARK_PURPLE+"Tokens: "+ChatColor.LIGHT_PURPLE+getTokens(p)));
		p.sendMessage(ChatColor.GRAY+"-------------------------------------------------");
	}
	
	public void showMenu(Player p){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD+"           Upgrade Stats");
		
		ItemStack armor = new NamedStack(ChatColor.BLUE+"Armor", Material.CHAINMAIL_CHESTPLATE);
		ItemStack sword = new NamedStack(ChatColor.RED+"Weapons", Material.IRON_SWORD);
		ItemStack physic = new NamedStack(ChatColor.DARK_PURPLE+"Physical", Material.RAW_CHICKEN);
		
		List<String> lore = new ArrayList<String>();
		ArmorStats ca = new ArmorStats(p);
		ArmorStats na = new ArmorStats(p, getArmorLevel(p)+1);
		WeaponStats cw = new WeaponStats(p);
		WeaponStats nw = new WeaponStats(p, getWeaponLevel(p)+1);
		PhysicalStats cp = new PhysicalStats(p);
		PhysicalStats np = new PhysicalStats(p, getPhysicalLevel(p)+1);
		
		lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+this.getArmorLevel(p));
		lore.add(" ");
		lore.add(ChatColor.AQUA+"Current Level: ");
		lore.add(ChatColor.DARK_GREEN+"Armor: "+ChatColor.GREEN+(ca.armor+1));
		lore.add(ChatColor.DARK_GREEN+"Projectile Defense: "+ChatColor.GREEN+ca.projectile);
		lore.add(ChatColor.DARK_GREEN+"Explosion Defense: "+ChatColor.GREEN+ca.explosion);
		lore.add(" ");
		lore.add(ChatColor.AQUA+"Next Level: ");
		lore.add(ChatColor.DARK_GREEN+"Armor: "+ChatColor.GREEN+(na.armor+1));
		lore.add(ChatColor.DARK_GREEN+"Projectile Defense: "+ChatColor.GREEN+na.projectile);
		lore.add(ChatColor.DARK_GREEN+"Explosion Defense: "+ChatColor.GREEN+na.explosion);
		
		plugin.util.addLore(armor, lore);
		
		lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+this.getWeaponLevel(p));
		lore.add(" ");
		lore.add(ChatColor.AQUA+"Current Level: ");
		lore.add(ChatColor.DARK_GREEN+"Material: "+ChatColor.GREEN+cw.getName());
		lore.add(ChatColor.DARK_GREEN+"Knockback: "+ChatColor.GREEN+cw.knock);
		lore.add(" ");
		lore.add(ChatColor.AQUA+"Next Level: ");
		lore.add(ChatColor.DARK_GREEN+"Material: "+ChatColor.GREEN+nw.getName());
		lore.add(ChatColor.DARK_GREEN+"Knockback: "+ChatColor.GREEN+nw.knock);
		
		plugin.util.addLore(sword, lore);
		
		lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+this.getPhysicalLevel(p));
		lore.add(" ");
		lore.add(ChatColor.AQUA+"Current Level: ");
		lore.add(ChatColor.DARK_GREEN+"Speed: "+ChatColor.GREEN+(cp.speed-1));
		lore.add(ChatColor.DARK_GREEN+"Health: "+ChatColor.GREEN+(cp.health/2)+" Hearts");
		lore.add(ChatColor.DARK_GREEN+"Defense: "+ChatColor.GREEN+cp.defense);
		lore.add(" ");
		lore.add(ChatColor.AQUA+"Next Level: ");
		lore.add(ChatColor.DARK_GREEN+"Speed: "+ChatColor.GREEN+(np.speed-1));
		lore.add(ChatColor.DARK_GREEN+"Health: "+ChatColor.GREEN+(np.health/2)+" Hearts");
		lore.add(ChatColor.DARK_GREEN+"Defense: "+ChatColor.GREEN+np.defense);
		
		plugin.util.addLore(physic, lore);
		
		inv.setItem(3, armor);
		inv.setItem(4, sword);
		inv.setItem(5, physic);
		
		p.openInventory(inv);
		
	}
	
	public void refreshInventory(Player p){
		if(p.getOpenInventory() != null && p.getOpenInventory().getTitle().contentEquals(ChatColor.GOLD+"           Upgrade Stats")){
			Inventory inv = p.getOpenInventory().getTopInventory();
			ItemStack armor = inv.getItem(3);
			ItemStack sword = inv.getItem(4);
			ItemStack physic = inv.getItem(5);
			
			List<String> lore = new ArrayList<String>();
			ArmorStats ca = new ArmorStats(p);
			ArmorStats na = new ArmorStats(p, getArmorLevel(p)+1);
			WeaponStats cw = new WeaponStats(p);
			WeaponStats nw = new WeaponStats(p, getWeaponLevel(p)+1);
			PhysicalStats cp = new PhysicalStats(p);
			PhysicalStats np = new PhysicalStats(p, getPhysicalLevel(p)+1);
			
			lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+this.getArmorLevel(p));
			lore.add(" ");
			lore.add(ChatColor.AQUA+"Current Level: ");
			lore.add(ChatColor.DARK_GREEN+"Armor: "+ChatColor.GREEN+(ca.armor+1));
			lore.add(ChatColor.DARK_GREEN+"Projectile Defense: "+ChatColor.GREEN+ca.projectile);
			lore.add(ChatColor.DARK_GREEN+"Explosion Defense: "+ChatColor.GREEN+ca.explosion);
			lore.add(" ");
			lore.add(ChatColor.AQUA+"Next Level: ");
			lore.add(ChatColor.DARK_GREEN+"Armor: "+ChatColor.GREEN+(na.armor+1));
			lore.add(ChatColor.DARK_GREEN+"Projectile Defense: "+ChatColor.GREEN+na.projectile);
			lore.add(ChatColor.DARK_GREEN+"Explosion Defense: "+ChatColor.GREEN+na.explosion);
			
			plugin.util.addLore(armor, lore);
			
			lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+this.getWeaponLevel(p));
			lore.add(" ");
			lore.add(ChatColor.AQUA+"Current Level: ");
			lore.add(ChatColor.DARK_GREEN+"Material: "+ChatColor.GREEN+cw.getName());
			lore.add(ChatColor.DARK_GREEN+"Knockback: "+ChatColor.GREEN+cw.knock);
			lore.add(" ");
			lore.add(ChatColor.AQUA+"Next Level: ");
			lore.add(ChatColor.DARK_GREEN+"Material: "+ChatColor.GREEN+nw.getName());
			lore.add(ChatColor.DARK_GREEN+"Knockback: "+ChatColor.GREEN+nw.knock);
			
			plugin.util.addLore(sword, lore);
			
			lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+this.getPhysicalLevel(p));
			lore.add(" ");
			lore.add(ChatColor.AQUA+"Current Level: ");
			lore.add(ChatColor.DARK_GREEN+"Speed: "+ChatColor.GREEN+(cp.speed-1));
			lore.add(ChatColor.DARK_GREEN+"Health: "+ChatColor.GREEN+(cp.health/2)+" Hearts");
			lore.add(ChatColor.DARK_GREEN+"Defense: "+ChatColor.GREEN+cp.defense);
			lore.add(" ");
			lore.add(ChatColor.AQUA+"Next Level: ");
			lore.add(ChatColor.DARK_GREEN+"Speed: "+ChatColor.GREEN+(np.speed-1));
			lore.add(ChatColor.DARK_GREEN+"Health: "+ChatColor.GREEN+(np.health/2)+" Hearts");
			lore.add(ChatColor.DARK_GREEN+"Defense: "+ChatColor.GREEN+np.defense);
			
			plugin.util.addLore(physic, lore);
		}
	}
	
	
	
	public void equip(Player p){
		ArmorStats aStats = new ArmorStats(p);
		aStats.equip();
		
		WeaponStats wStats = new WeaponStats(p);
		wStats.equip();
		
		PhysicalStats pStats = new PhysicalStats(p);
		pStats.equip();
	}
	
	public void giveExp(Player p, int xp, String why){
		int cxp = getExp(p);
		int level = getLevel(p);
		int needed = getNeededExp(level+1);
		int nxp = cxp+xp;
		int plusLevel = 0;
		
		if(why != null){
			p.sendMessage(ChatColor.GOLD+"+"+ChatColor.RED+xp+ChatColor.GRAY+" XP"+ChatColor.GOLD+" For "+why);
			 p.playSound(p.getEyeLocation(), Sound.CHICKEN_EGG_POP, 100, 1);
		}
		
		while(nxp >= needed){
			plusLevel++;
			nxp -= needed;
			needed = getNeededExp(level+1+plusLevel);
		}
		
		if(plusLevel > 0){
			giveLevel(p, plusLevel);
		}else {
			if(why != null){
				p.sendMessage(ChatColor.GOLD+"+"+ChatColor.RED+xp+ChatColor.GRAY+" XP"+ChatColor.GOLD+" For "+why);
				 p.playSound(p.getEyeLocation(), Sound.CHICKEN_EGG_POP, 100, 1);
			}
		}
		
		
		
		Config.setData(p, "xp", nxp);	
	}

}
