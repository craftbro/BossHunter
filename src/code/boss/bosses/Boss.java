package code.boss.bosses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;

import code.boss.main.main;

public class Boss{

    public LivingEntity boss = null;
	protected main plugin;
	protected boolean spawned = false;
	protected List<LivingEntity> minions = new ArrayList<LivingEntity>();
	
	public Boss(main instance){
	plugin = instance;
	}
	
	public void spawn(){}
	public void start(){}

	public int getMinionsSize(){
		int i = 0;
	
		
		for(LivingEntity e : minions){
			if(!e.isDead()){
				i++;
			}
		}
		
		return i;
	}
	
	
	public boolean isSpawned(){
		return spawned;
	}
	
	public LivingEntity getBoss(){
		return boss;
	}
	
	public void tick(){
		
	}
	
	
}
