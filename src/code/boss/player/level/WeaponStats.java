package code.boss.player.level;

import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import code.configtesting.config.Config;

public class WeaponStats {
	
	Player p;
	int level;
	
	ItemStack sword = new ItemStack(Material.GOLD_SWORD);
	int knock = 0;
	
	private void upgrade(ItemStack i){
		String n = i.getType().name();
		
		n = n.replace("IRON", "DIAMOND");
		n = n.replace("STONE", "IRON");
		n = n.replace("GOLD", "STONE");

		
		i.setType(Material.getMaterial(n));
	}
	
	public WeaponStats(Player p){
		this.p = p;
		this.level = (Integer) Config.getData(p, "levelsword");
		
		start();
	}
	
	public WeaponStats(Player p, int level){
		this.p = p;
		this.level = level;
		
		start();
	}
	
	String getName(){
		String n = sword.getType().name();
		
		n = n.replace("_SWORD", "");
		n = n.toLowerCase();
		Character.toUpperCase(n.charAt(0));
		
		return n;
	}
	
	private int getUpAmount(){
		int i=0;
		int l=level;
		
		while(l >= 3){
			l-=3;
			i++;
		}
		
		return i;
	}
	
	private int getRest(){
		int l=level;
		
		while(l >= 3){
			l-=3;
		}
		
		return l;
	}
	
	public void equip(){
		p.getInventory().addItem(sword);
	}
	
	private void start(){
		int a = getUpAmount();
		int r = getRest();
		
		while(a > 0){
			a--;
			upgrade(sword);
		}
		
		knock+=r;
		
		if(r > 0){
			ItemMeta meta = sword.getItemMeta();
			meta.addEnchant(Enchantment.KNOCKBACK, r, true);
			sword.setItemMeta(meta);
		}
		
	}
	
}
