package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossBeast;
import code.boss.main.main;

public class ArenaBeast extends Arena{
	
	public ArenaBeast(main instance) {
		super(instance, new BossBeast(instance), new Location(Bukkit.getWorld("BOSSworld"), 123, 78.5, 123));
	}

}
