package cf.leduyquang753.itemparser;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Argument: UUID of the skull owner, example: 70e55f96-1bb9-43c1-af56-3a03020ce404.
 */
public class SkullParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		UUID uuid;
		try {
			uuid = UUID.fromString(arguments[0]);
		} catch (Exception e) {
			throw new InvalidItemException("Invalid UUID: " + arguments[0]);
		}
		((SkullMeta)meta).setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		SkullMeta skull = (SkullMeta) meta;
		if (skull.hasOwner()) {
			arguments.add(skull.getOwningPlayer().getUniqueId().toString());
			return 1;
		}
		return 0;
	}
}
