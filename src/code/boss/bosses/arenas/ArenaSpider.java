package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossSpider;
import code.boss.main.main;

public class ArenaSpider extends Arena{
	
	public ArenaSpider(main instance) {
		super(instance, new BossSpider(instance), new Location(Bukkit.getWorld("BOSS"), -115, 68, 16));
	}

}
