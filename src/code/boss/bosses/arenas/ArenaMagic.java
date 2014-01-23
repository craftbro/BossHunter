package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Warning;

import code.boss.bosses.BossMagic;
import code.boss.main.main;

@Deprecated
@Warning(reason="This Arena Uses a Boss that Dosnt Work")
public class ArenaMagic extends Arena{

	public ArenaMagic(main instance) {
		super(instance, new BossMagic(instance), new Location(Bukkit.getWorld("BOSS"), 78, 78.5, 211));
	}

}
