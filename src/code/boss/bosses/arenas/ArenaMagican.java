package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossMagican;
import code.boss.main.main;

public class ArenaMagican extends Arena{
	public ArenaMagican(main instance) {
		super(instance, new BossMagican(instance), new Location(Bukkit.getWorld("BOSS"), 1032, 57, -1130));
	}

}
