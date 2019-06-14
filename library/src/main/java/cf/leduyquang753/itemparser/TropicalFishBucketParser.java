package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Arguments (all required):<ul>
 * <li>{@link DyeColor Body color}.
 * <li>{@link Pattern Pattern name}.
 * <li>{@link DyeColor Pattern color}.
 * </ul>
 */
public class TropicalFishBucketParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		if (arguments.length < 3) throw new InvalidItemException("Insufficient arguments for tropical fish bucket.");
		TropicalFishBucketMeta fish = (TropicalFishBucketMeta)meta;
		try {
			fish.setBodyColor(DyeColor.valueOf(arguments[0].toUpperCase()));
		} catch (Exception e) {
			throw new InvalidItemException("Invalid body color: " + arguments[0]);
		}
		try {
			fish.setPattern(Pattern.valueOf(arguments[1].toUpperCase()));
		} catch (Exception e) {
			throw new InvalidItemException("Invalid pattern name: " + arguments[1]);
		}
		try {
			fish.setPatternColor(DyeColor.valueOf(arguments[2].toUpperCase()));
		} catch (Exception e) {
			throw new InvalidItemException("Invalid pattern color: " + arguments[2]);
		}
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		TropicalFishBucketMeta tfbm = (TropicalFishBucketMeta) meta;
		arguments.add(tfbm.getBodyColor().name());
		arguments.add(tfbm.getPattern().name());
		arguments.add(tfbm.getPatternColor().name());
		return 3;
	}
}
