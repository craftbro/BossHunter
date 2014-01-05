package code.boss.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NamedStack extends ItemStack{
	
	
	public NamedStack(String s, Material m){
		this(s, m, 1, (byte) 0);
	}
	
	public NamedStack(String s, Material m, int i){
		this(s, m, i, (byte) 0);
	}
	
	public NamedStack(String s, Material m, int i, byte b){
		super(m, i, b);
		
		name(s);
	}
	
	private void name(String s){
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(s);
		this.setItemMeta(meta);
	}


	

}
