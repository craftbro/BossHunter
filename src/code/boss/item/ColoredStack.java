package code.boss.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ColoredStack extends ItemStack{

	public ColoredStack(Color c, Material m){
		this(c, m, 1, (byte) 0);
	}
	
	public ColoredStack(Color c, Material m, int i){
		this(c, m, i, (byte) 0);
	}
	
	public ColoredStack(Color c, Material m, int i, byte b){
		super(m, i, b);
		
		name(c);
	}
	
	private void name(Color c){
		LeatherArmorMeta meta = (LeatherArmorMeta)this.getItemMeta();
		meta.setColor(c);
		this.setItemMeta(meta);
	}
}
