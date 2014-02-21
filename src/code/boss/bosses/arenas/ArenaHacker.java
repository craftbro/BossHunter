package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossHacker;
import code.boss.main.main;

public class ArenaHacker extends Arena{
	
	public ArenaHacker(main instance) {
		super(instance, new BossHacker(instance), new Location(Bukkit.getWorld("BOSS"), 1032, 57, -1130));
	}

}
