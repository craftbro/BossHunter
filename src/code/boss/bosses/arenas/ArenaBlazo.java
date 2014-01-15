package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossBlazo;
import code.boss.main.main;

public class ArenaBlazo extends Arena{
	
	public ArenaBlazo(main instance) {
		super(instance, new BossBlazo(instance), new Location(Bukkit.getWorld("BOSS"), -393, 62.5, -8));
	}

}
