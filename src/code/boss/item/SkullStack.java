package code.boss.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullStack extends ItemStack{
	
	
	public SkullStack(String s){
		super(Material.SKULL_ITEM, 1, (byte)3);
		
		skull(s);
	}
	
	private void skull(String s){
		SkullMeta meta = (SkullMeta) this.getItemMeta();
		meta.setOwner(s);
		this.setItemMeta(meta);
	}
}
