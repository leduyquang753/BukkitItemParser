package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Argument: The color of the armor in 8-bit RGB hex format, example: 7CD43E.
 */
public class LeatherArmorParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		try {
			((LeatherArmorMeta)meta).setColor(Color.fromRGB(Integer.parseInt(arguments[0], 16)));
		} catch (Exception e) {
			throw new InvalidItemException("Invalid leather armor color: " + arguments[0]);
		}
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		LeatherArmorMeta armor = (LeatherArmorMeta) meta;
		if (armor.getColor() == Bukkit.getItemFactory().getDefaultLeatherColor()) return 0;
		else {
			arguments.add(Integer.toHexString(armor.getColor().asRGB()));
			return 1;
		}
	}
}
