package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;

/**
 * Arguments:<ul>
 * <li>Base potion type (required).
 * <li>Potion color.
 * <li>Base potion flags:<ul>
 *     <li>Extended.
 *     <li>Upgraded.</ul>
 * <li>Additional potion effects, with arguments separated by ";". Arguments:<ul>
 *     <li>Potion effect type (required).
 *     <li>Duration in ticks (required).
 *     <li>Level. Default: 1.
 *     <li>Non-empty: the effect is ambient (produce more translucent particles)
 *     <li>Non-empty: the effect doesn't produce particles.
 *     <li>Non-empty: the effect doesn't display an icon.</ul>
 * </ul>
 */
public class PotionParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		if (arguments.length < 1) throw new InvalidItemException("Insufficient potion arguments.");
		boolean extended = false, upgraded = false;
		PotionType baseType;
		PotionMeta potMeta = (PotionMeta)meta;
		try {
			baseType = PotionType.valueOf(arguments[0].toUpperCase());
		} catch (Exception e) {
			throw new InvalidItemException("Invalid base potion type: " + arguments[1]);
		}
		if (arguments.length > 1) {
			try {
				potMeta.setColor(Color.fromRGB(Integer.parseInt(arguments[1], 16)));
			} catch (Exception e) {
				throw new InvalidItemException("Invalid potion color: " + arguments[1]);
			}
		}
		if (arguments.length > 2 && arguments[2].length() > 1) {
			extended = arguments[2].charAt(0) == '1';
			upgraded = arguments[2].charAt(1) == '1';
		}
		potMeta.setBasePotionData(new PotionData(baseType, extended, upgraded));
		for (int i = 3; i < arguments.length; i++) {
			String[] effectArgs = arguments[i].split(";");
			if (effectArgs.length < 2) throw new InvalidItemException("Insufficient arguments for potion effect.");
			PotionEffectType type = PotionEffectType.getByName(effectArgs[0].trim());
			if (type == null) throw new InvalidItemException("Invalid potion effect name: " + effectArgs[0].trim());
			int duration = 0, amplifier = 0;
			try {
				duration = Integer.parseInt(effectArgs[1].trim());
			} catch (Exception e) {
				throw new InvalidItemException("Invalid potion effect duration value: " + effectArgs[1].trim());
			}
			if (effectArgs.length > 2 && !effectArgs[2].trim().isEmpty()) {
				try {
					amplifier = Integer.parseInt(effectArgs[2].trim())-1;
				} catch (Exception e) {
					throw new InvalidItemException("Invalid potion effect amplifier value: " + effectArgs[2].trim());
				}
			}
			potMeta.addCustomEffect(new PotionEffect(type, duration, amplifier, effectArgs.length > 3 && !effectArgs[3].trim().isEmpty(), !(effectArgs.length > 4 && !effectArgs[4].trim().isEmpty()), !(effectArgs.length > 5 && !effectArgs[5].trim().isEmpty())), true);
		}
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		int numOfArgs = 1;
		PotionMeta potion = (PotionMeta) meta;
		PotionData baseData = potion.getBasePotionData();
		arguments.add(baseData.getType().name());
		if (potion.hasColor()) {
			arguments.add(Integer.toHexString(potion.getColor().asRGB()));
			numOfArgs = 2;
		} else {
			arguments.add("");
		}
		if (baseData.isExtended() || baseData.isUpgraded()) {
			arguments.add((baseData.isExtended() ? "1" : "0") + (baseData.isUpgraded() ? "1" : "0"));
			numOfArgs = 3;
		} else {
			arguments.add("");
		}
		if (potion.hasCustomEffects()) {
			for (PotionEffect effect : potion.getCustomEffects()) {
				int numOfSubArgs = 2;
				ArrayList<String> subArgs = new ArrayList<>();
				subArgs.add(effect.getType().getName());
				subArgs.add(effect.getDuration() + "");
				if (effect.getAmplifier() != 0) {
					subArgs.add(effect.getAmplifier()+1+"");
					numOfSubArgs = 3;
				} else {
					subArgs.add("");
				}
				if (effect.isAmbient()) {
					subArgs.add("ambient");
					numOfSubArgs = 4;
				} else {
					subArgs.add("");
				}
				if (effect.hasParticles()) {
					subArgs.add("");
				} else {
					subArgs.add("No particles");
					numOfSubArgs = 5;
				}
				if (effect.hasIcon()) {
					subArgs.add("");
				} else {
					subArgs.add("No icon");
					numOfSubArgs = 6;
				}
				String arg = "";
				boolean isNotFirst = false;
				for (int i = 0; i < numOfSubArgs; i++) {
					arg += (isNotFirst ? "; " : "") + subArgs.get(i);
				}
				arguments.add(arg);
			}
			return 3+potion.getCustomEffects().size();
		}
		return numOfArgs;
	}
}
