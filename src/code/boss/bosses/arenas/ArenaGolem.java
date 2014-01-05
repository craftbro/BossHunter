package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.Boss;
import code.boss.bosses.BossGolem;
import code.boss.bosses.BossTest;
import code.boss.main.main;

public class ArenaGolem extends Arena{

	public ArenaGolem(main instance) {
		super(instance, new BossGolem(instance), new Location(Bukkit.getWorld("BOSSworld"), 78, 76, 211));
	}

}
