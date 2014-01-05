package code.boss.player.level;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import code.configtesting.config.Config;

public class ArmorStats {

	Player p;
	
	int armor = 0;
	int projectile = 0;
	int explosion = 0;
	int level;
	
	ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
	ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
	ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
	ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
	
	public ArmorStats(Player p){
		this.p = p;
		this.level = (Integer) Config.getData(p, "levelarmor");
		
		start();
	}
	
	public ArmorStats(Player p, int level){
		this.p = p;
		this.level = level;
		
		start();
	}
	
	private int getArmorPlus(){
		int i=0;
		int l=level;
		
		while(l >= 3){
			l-=3;
			i++;
		}
		
		return i;
	}
	
	
	
	private void upgrade(ItemStack i){
		String n = i.getType().name();
		
		n = n.replace("IRON", "DIAMOND");
		n = n.replace("CHAINMAIL", "IRON");
		n = n.replace("GOLD", "CHAINMAIL");
		n = n.replace("LEATHER", "GOLD");
		
		i.setType(Material.getMaterial(n));
	}
	
	private int getRest(){
		int l=level;
		
		while(l >= 3){
			l-=3;
		}
		
		return l;
	}
	
	private void start(){
	int ap = getArmorPlus();
	int re = getRest();
	
	armor += ap;
	
	if(re == 1){
		projectile+=(ap/2)+1;
	}else if(re == 2){
		projectile+=(ap/2)+1;
		explosion+=(ap/2)+1;
	}
	
	calculate();
	
	}
	
	public void equip(){
		p.getEquipment().setHelmet(helmet);
		p.getEquipment().setChestplate(chestplate);
		p.getEquipment().setLeggings(leggings);
		p.getEquipment().setBoots(boots);
	}
	
	
	private void calculate(){
		int a = armor*2;
		
		int d = 0;
		
		while(a > 0){
			a--;
			
			if(d == 0){
				d++;
				upgrade(boots);
			}else if(d == 1){
				d++;
				upgrade(leggings);
			}else if(d == 2){
				d++;
				upgrade(chestplate);
			}else if(d == 3){
				d=0;
				upgrade(helmet);
			}
		}
		
		if(projectile > 0){
			ItemMeta meta = chestplate.getItemMeta();
			meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, projectile, true);
			chestplate.setItemMeta(meta);
			}
		
		if(explosion > 0){
			ItemMeta meta = leggings.getItemMeta();
			meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, explosion, true);
			leggings.setItemMeta(meta);
			}
		
	}
	
}
