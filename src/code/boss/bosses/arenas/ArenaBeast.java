package code.boss.bosses.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Warning;

import code.boss.bosses.BossBeast;
import code.boss.main.main;

@Deprecated
@Warning(value=true,reason="Boss is F*ck'd up")
public class ArenaBeast extends Arena{
	
	public ArenaBeast(main instance) {
		super(instance, new BossBeast(instance), new Location(Bukkit.getWorld("BOSS"), 123, 78.5, 123));
	}

}
