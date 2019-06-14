package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Argument: Map ID.
 */
public class MapParser implements ParserHook {
	@Override
	@SuppressWarnings("deprecation")
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		try {
			((MapMeta)meta).setMapView(Bukkit.getMap(Integer.parseInt(arguments[0])));
		} catch (Exception e) {
			throw new InvalidItemException("Invalid map ID: " + arguments[0]);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		MapMeta map = (MapMeta) meta;
		if (map.hasMapId()) {
			arguments.add(map.getMapId() + "");
			return 1;
		}
		return 0;
	}
}
