package code.boss.player.eco.abilities;

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

public class Shop {

	main plugin;
	
	int abs = 2;
	
	List<ItemStack> abils = new ArrayList<ItemStack>();

	ItemStack heal = new NamedStack(ChatColor.LIGHT_PURPLE+"Heal", Material.MAGMA_CREAM);
	ItemStack power = new NamedStack(ChatColor.YELLOW+"Boost", Material.BLAZE_ROD);
	
	
	
	public Shop(main instance){
		plugin = instance;
		
		abils.add(power);
		abils.add(heal);
	}
	
	public void openMenu(Player p){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE+"           Ability Shop");
		
		for(ItemStack ii : abils){
		ItemStack i = ii.clone();
		
		System.out.print("ADDING: "+ii.getItemMeta().getDisplayName()+" CLONED TO: "+i.getItemMeta().getDisplayName());
			
		addLore(i, p);	
		
		inv.addItem(i);
		}
	
		p.openInventory(inv);
		
	}
	
	public void openSelect(Player p){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE+"          Select Ability");
		
		for(ItemStack ii : abils){
			ItemStack i = ii.clone();
			
			
				
			addLoreSelect(i, p);	
			
			inv.addItem(i);
			}
		
		p.getInventory().setItem(3, new ItemStack(Material.AIR));
		p.getInventory().setItem(4, new ItemStack(Material.AIR));
	
		p.openInventory(inv);
		
	}
	
	private void addLoreSelect(ItemStack i, Player p){
		Ability ch = getAbility(i, p);
		
		int l = getAbilityLevel(p, ch);
		

		
		List<String> lore = new ArrayList<String>();
		
		
		lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+l);
		lore.add(" ");
		for(String s : ch.getStats()) lore.add(s);

		
		plugin.util.addLore(i, lore);
		
	}
	
	private void addLore(ItemStack i, Player p){
		Ability ch = getAbility(i, p);
		
	
		
		int l = getAbilityLevel(p, ch);
		
		Ability nh = getAbility(i, p, l+1);
		
		
		
		List<String> lore = new ArrayList<String>();
		
		
		lore.add(ChatColor.DARK_PURPLE+"Level "+ChatColor.LIGHT_PURPLE+l);
		lore.add(" ");
		lore.add(ChatColor.AQUA+"Current Level:");
		for(String s : ch.getStats()) lore.add(s);
		lore.add(" ");
		if(l < ch.max){
		lore.add(ChatColor.AQUA+"Next Level:");
		for(String s : nh.getStats()) lore.add(s);
		lore.add(" ");
		lore.add(ChatColor.GOLD+"Upgrade Cost: "+ChatColor.YELLOW+ch.getCost());
		}else{
			lore.add(ChatColor.DARK_RED+"MAX LEVEL");
		}  
		
		plugin.util.addLore(i, lore);
		
	}
	
	public int getCost(ItemStack i){
		
		for(String ss : i.getItemMeta().getLore()){
			String s = ChatColor.stripColor(ss).toLowerCase();
			if(s.contains("cost")){
				String c = s.replace("upgrade cost: ", "");
				return Integer.parseInt(c);
			}
		}
		
		return -1;
	}
	
	public void addAbility(Player p, ItemStack i){
		Inventory inv = p.getInventory();
		
		if(this.getAbilityLevel(p, getAbility(i, p)) > 0){
		if(inv.getItem(3) == null){
			inv.setItem(3, i);
		}else{
			inv.setItem(4, i);
			
			p.closeInventory();
		}
		}else{
			p.sendMessage(ChatColor.RED+"Upgrade this ability to level 1 first");
			p.playSound(p.getEyeLocation(), Sound.NOTE_BASS, 100, -3);
		}
		
	}
	
	public void refreshInventory(Player p){
		if(p.getOpenInventory() != null && p.getOpenInventory().getTitle().contentEquals(ChatColor.BLUE+"           Ability Shop")){
			Inventory inv = p.getOpenInventory().getTopInventory();
			
			ItemStack heal = inv.getItem(0);
			
			addLore(heal, p);	
		}
	}
	
	public boolean isMax(ItemStack i){
		return i.getItemMeta().getLore().contains(ChatColor.DARK_RED+"MAX LEVEL");
	}
	
	public void giveAbilityLevel(Ability ab, Player p){
		int cr = getAbilityLevel(p, ab);
		
		p.sendMessage(plugin.util.middleOfset(ChatColor.BLUE+"Upgraded "+ab.getName()+ChatColor.BLUE+" To Level "+(cr+1)+"!"));
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 3);
		
		Config.setData(p, "levelabil"+ab.getId(), cr+1);
	}
	
	public int getAbilityLevel(Player p, Ability ab){

		return (Integer) Config.getData(p, "levelabil"+ab.getId());
	}
	
	public Ability getAbility(ItemStack i, Player p){
		return this.getAbility(i, p, -1);
	}
	
	public Ability getAbility(ItemStack i, Player p, int l){
		String s = ChatColor.stripColor(i.getItemMeta().getDisplayName()).toLowerCase();
		
		if(s.contentEquals("heal")){
			return new AbilityHeal(p, l);
		}else if(s.contentEquals("boost")){
			return new AbilityStrenght(p, l);
		}else{
			return null;
		}
		
	}
	
	public void check(Player p){
		for(int i=1; i<=abs; i++){
			if(!Config.getPlayerFile(p).contains("levelabil"+i)){
				Config.setData(p, "levelabil"+i, 0);
			}
		}
	}
	
	
	
	
}
