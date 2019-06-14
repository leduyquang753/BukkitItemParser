package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Argument: The item in the crossbow, parsed like {@link ItemParser#parse(String[]) any other item}, or nothing for nothing.
 */
public class CrossbowParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		if (arguments.length > 0) {
			((CrossbowMeta)meta).addChargedProjectile(ItemParser.parse(arguments));
		}
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		CrossbowMeta crossbow = (CrossbowMeta) meta;
		if (crossbow.hasChargedProjectiles()) {
			arguments.add(ItemParser.reverseParse(crossbow.getChargedProjectiles().get(0)));
			return 1;
		}
		return 0;
	}
}
