package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.Boss;
import code.boss.bosses.BossTest;
import code.boss.main.main;

public class ArenaPig extends Arena{

	public ArenaPig(main instance) {
		super(instance, new BossTest(instance), new Location(Bukkit.getWorld("BOSSworld"), 43, 58, -160));
	}

}
