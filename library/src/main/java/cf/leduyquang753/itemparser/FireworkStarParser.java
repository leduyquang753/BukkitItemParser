package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.FireworkEffect.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Arguments (all required):<ul>
 * <li>Effect type.
 * <li>Main colors separated by " ".
 * <li>Fade colors separated by " ".
 * <li>Flags:<ul>
 *     <li>Flicker.
 *     <li>Trail.</ul>
 * </ul>
 * Colors are written in 8-bit RGB hex format, example: 6F357A.
 */
public class FireworkStarParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		if (arguments.length < 4) throw new InvalidItemException("Not enough data for firework effect.");
		Builder builder = FireworkEffect.builder();
		try {
			builder.with(Type.valueOf(arguments[0].toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new InvalidItemException("Invalid firework effect type: " + arguments[0].trim());
		}
		builder.withColor(parseColors(arguments[1]));
		builder.withFade(parseColors(arguments[2]));
		String options = arguments[3];
		if (options.length() > 1) {
			if (options.charAt(0) == '1') {
				builder.withFlicker();
			}
			if (options.charAt(1) == '1') {
				builder.withTrail();
			}
		}
		((FireworkEffectMeta)meta).setEffect(builder.build());
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
		FireworkEffectMeta star = (FireworkEffectMeta) meta;
		if (star.hasEffect()) {
			FireworkEffect effect = star.getEffect();
			arguments.add(effect.getType().name());
			arguments.add(FireworkParser.reverseParseColors(effect.getColors()));
			arguments.add(FireworkParser.reverseParseColors(effect.getFadeColors()));
			arguments.add(effect.hasFlicker() || effect.hasTrail() ? effect.hasFlicker() ? "1" : "0" : effect.hasTrail() ? "1" : "0");
			return 4;
		}
		return 0;
	}
}
