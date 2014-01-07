package code.boss.player.eco.collect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import code.boss.main.main;
import code.configtesting.config.Config;

public class Collect {

	main plugin;
	
	public Collect(main instance){
		plugin = instance;
	}
	

	/**Adds an itemstack to the players collection
	 * @param p the player who is given the item	
	 * @param i the itemstack to be added
	 */
	
	public void giveItem(Player p, ItemStack i){
		List<ItemStack> items = getItems(p);
		
		items.add(i);
		
		Config.setData(p, "items", items);
	}
	
	/**Removes an itemstack from the players collection
	 * @param p the player who the item is taken from	
	 * @param i the itemstack to be removed
	 */
	
	public void removeItem(Player p, ItemStack i){
		List<ItemStack> items = getItems(p);
		
		items.remove(i);
		
		Config.setData(p, "items", items);
	}
	
	/**Checs if the players collection contaisn the item 
	 * @param p the player to be checked
	 * @param i the itemstack to be checked for
	 * @return if the players collection contains the itemstack
	 */
	
	public boolean hasItem(Player p, ItemStack i){
		return getItems(p).contains(i);
	}
	
	/**Gets all the items in a players collection
	 * @param p the player to be checked
	 * @return the items in the players collection (empty if it doesn't containany items)
	 */
	
	public List<ItemStack> getItems(Player p){
		if(Config.getData(p, "items") != null){
			return (List<ItemStack>) Config.getData(p, "items");
		}else{
			return new ArrayList<ItemStack>();
		}		
	}
	
	/**Shows the player his collection
	 * 
	 * @param p the player which is showen his collection 
	 */
	
	public void loadInventory(Player p){
		Inventory inv = Bukkit.createInventory(null, 45, ChatColor.YELLOW+"Your Collection");
		
		for(ItemStack i : getItems(p)){
			inv.addItem(i);
		}
		
		p.openInventory(inv);
	}
	
}
