package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossUnknown;
import code.boss.main.main;

public class ArenaUnnamed extends Arena{
	
	public ArenaUnnamed(main instance) {
		super(instance, new BossUnknown(instance), new Location(Bukkit.getWorld("BOSS"), 1032, 57, -1130));
	}

}
