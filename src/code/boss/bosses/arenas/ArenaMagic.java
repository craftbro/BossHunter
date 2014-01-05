package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import code.boss.bosses.BossMagic;
import code.boss.main.main;

public class ArenaMagic extends Arena{

	public ArenaMagic(main instance) {
		super(instance, new BossMagic(instance), new Location(Bukkit.getWorld("BOSSworld"), 78, 78.5, 211));
	}

}
