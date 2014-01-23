package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossArcher;
import code.boss.main.main;

public class ArenaArcher extends Arena{
	
	public ArenaArcher(main instance) {
		super(instance, new BossArcher(instance), new Location(Bukkit.getWorld("BOSS"), -393, 62.5, -8));
	}

}
