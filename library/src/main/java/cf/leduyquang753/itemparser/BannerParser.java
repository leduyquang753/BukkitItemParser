package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Arguments: Patterns, with arguments separated by ";" (all required):<ul>
 * <li>{@link PatternType Pattern type}.
 * <li>{@link DyeColor Pattern color}.</ul>
 */
public class BannerParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		BannerMeta banner = (BannerMeta)meta;
		for (String argument : arguments) {
			String[] split = argument.split(";");
			if (split.length < 2) throw new InvalidItemException("Insufficient pattern arguments.");
			PatternType pattern;
			DyeColor color;
			try {
				pattern = PatternType.valueOf(split[0].trim().toUpperCase());
			} catch (Exception e) {
				throw new InvalidItemException("Invalid pattern type: " + split[0].trim());
			}
			try {
				color = DyeColor.valueOf(split[1].trim().toUpperCase());
			} catch (Exception e) {
				throw new InvalidItemException("Invalid pattern color: " + split[1].trim());
			}
			banner.addPattern(new Pattern(color, pattern));
		}
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		BannerMeta banner = (BannerMeta) meta;
		for (Pattern pattern : banner.getPatterns()) {
			arguments.add(pattern.getPattern().name() + "; " + pattern.getColor().name());
		}
		return banner.getPatterns().size();
	}
}
