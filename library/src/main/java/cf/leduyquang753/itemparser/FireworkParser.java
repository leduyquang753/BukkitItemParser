package cf.leduyquang753.itemparser;

import java.util.*;

import org.bukkit.*;
import org.bukkit.FireworkEffect.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Arguments: firework effects, with arguments separated with ";" (all required):<ul>
 * <li>Effect type.
 * <li>Main colors separated by " ".
 * <li>Fade colors separated by " ".
 * <li>Flags:<ul>
 *     <li>Flicker.
 *     <li>Trail.</ul>
 * </ul>
 * Colors are written in 8-bit RGB hex format, example: 6F357A.
 */
public class FireworkParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		for (String effect : arguments) {
			String[] split = effect.split(";");
			if (split.length < 4) throw new InvalidItemException("Not enough data for firework effect.");
			Builder builder = FireworkEffect.builder();
			try {
				builder.with(Type.valueOf(split[0].trim().toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new InvalidItemException("Invalid firework effect type: " + split[0].trim());
			}
			builder.withColor(parseColors(split[1].trim()));
			builder.withFade(parseColors(split[2].trim()));
			String options = split[3].trim();
			if (options.length() > 1) {
				if (options.charAt(0) == '1') {
					builder.withFlicker();
				}
				if (options.charAt(1) == '1') {
					builder.withTrail();
				}
			}
			((FireworkMeta)meta).addEffect(builder.build());
		}
	}
	
	private ArrayList<Color> parseColors(String s) throws InvalidItemException {
		ArrayList<Color> res = new ArrayList<>();
		try {
			for (String color : s.trim().split(" ")) {
				res.add(Color.fromRGB(Integer.parseInt(color, 16)));
			}
		} catch (Exception e) {
			throw new InvalidItemException("Invalid color: " + s);
		}
		return res;
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		FireworkMeta firework = (FireworkMeta) meta;
		if (firework.hasEffects()) {
			for (FireworkEffect effect : firework.getEffects()) {
				arguments.add(effect.getType().name() + "; "
						+ reverseParseColors(effect.getColors()) + "; "
						+ reverseParseColors(effect.getFadeColors()) + "; "
						+ (effect.hasFlicker() || effect.hasTrail() ? effect.hasFlicker() ? "1" : "0" : effect.hasTrail() ? "1" : "0"));
			}
		}
		return firework.getEffectsSize();
	}

	public static String reverseParseColors(List<Color> colors) {
		String res = "";
		boolean isNotFirst = false;
		for (Color color : colors) {
			res += (isNotFirst ? " " : "") + Integer.toHexString(color.asRGB());
			isNotFirst = true;
		}
		return res;
	}
}
