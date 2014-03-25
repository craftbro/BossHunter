package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Warning;

import code.boss.bosses.BossElement;
import code.boss.main.main;

@Deprecated
@Warning(reason="This Arena Uses a Boss that Dosnt Work")
public class ArenaElement extends Arena{
	public ArenaElement(main instance) {
		super(instance, new BossElement(instance), new Location(Bukkit.getWorld("BOSS"), 1032, 57, -1130));
	}

}