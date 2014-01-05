package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossBeast;
import code.boss.main.main;

public class ArenaBlazo extends Arena{
	
	public ArenaBlazo(main instance) {
		super(instance, new BossBeast(instance), new Location(Bukkit.getWorld("BOSS"), -393, 62.5, -8));
	}

}
