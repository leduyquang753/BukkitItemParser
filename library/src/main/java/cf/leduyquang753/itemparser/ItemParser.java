package cf.leduyquang753.itemparser;

import java.util.*;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

/**
 * An utility class for parsing items from strings.
 */
public class ItemParser {
	/**
	 * The flags sequence.
	 */
	public static final ItemFlag[] flagsArray = new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE };
	
	/**
	 * The parser's hooks for specific materials.
	 */
	public static HashMap<Material, ParserHook> hooks = new HashMap<>();
	static {
		hooks.put(Material.CROSSBOW, new CrossbowParser());
		hooks.put(Material.FIREWORK_ROCKET, new FireworkParser());
		hooks.put(Material.FIREWORK_STAR, new FireworkStarParser());
		hooks.put(Material.KNOWLEDGE_BOOK, new KnowledgeBookParser());
		hooks.put(Material.FILLED_MAP, new MapParser());
		hooks.put(Material.PLAYER_HEAD, new SkullParser());
		hooks.put(Material.TROPICAL_FISH_BUCKET, new TropicalFishBucketParser());
		PotionParser potionParser = new PotionParser();
		hooks.put(Material.POTION, potionParser);
		hooks.put(Material.SPLASH_POTION, potionParser);
		hooks.put(Material.LINGERING_POTION, potionParser);
		BookParser bookParser = new BookParser();
		hooks.put(Material.WRITABLE_BOOK, bookParser);
		hooks.put(Material.WRITTEN_BOOK, bookParser);
		BannerParser bannerParser = new BannerParser();
		hooks.put(Material.BLACK_BANNER, bannerParser);
		hooks.put(Material.BLUE_BANNER, bannerParser);
		hooks.put(Material.BROWN_BANNER, bannerParser);
		hooks.put(Material.CYAN_BANNER, bannerParser);
		hooks.put(Material.GRAY_BANNER, bannerParser);
		hooks.put(Material.GREEN_BANNER, bannerParser);
		hooks.put(Material.LIGHT_BLUE_BANNER, bannerParser);
		hooks.put(Material.LIGHT_GRAY_BANNER, bannerParser);
		hooks.put(Material.LIME_BANNER, bannerParser);
		hooks.put(Material.MAGENTA_BANNER, bannerParser);
		hooks.put(Material.ORANGE_BANNER, bannerParser);
		hooks.put(Material.PINK_BANNER, bannerParser);
		hooks.put(Material.PURPLE_BANNER, bannerParser);
		hooks.put(Material.RED_BANNER, bannerParser);
		hooks.put(Material.WHITE_BANNER, bannerParser);
		hooks.put(Material.YELLOW_BANNER, bannerParser);
		LeatherArmorParser leatherArmorParser = new LeatherArmorParser();
		hooks.put(Material.LEATHER_HELMET, leatherArmorParser);
		hooks.put(Material.LEATHER_CHESTPLATE, leatherArmorParser);
		hooks.put(Material.LEATHER_LEGGINGS, leatherArmorParser);
		hooks.put(Material.LEATHER_BOOTS, leatherArmorParser);
		hooks.put(Material.LEATHER_HORSE_ARMOR, leatherArmorParser);
	}
	
	/**
	 * Parse item from given string. Arguments are split with |, so to include the character in an argument, \| has to be used.<br>
	 * For information on what arguments mean, see {@link #parse(String[]) parse(String...)}.
	 * @param s The string to parse.
	 * @return The parsed item.
	 * @throws InvalidItemException When the string is invalid.
	 */
	public static ItemStack parse(String s) throws InvalidItemException {
		String[] data = s.split("(?<![\\\\])\\|");
		for (int i = 0; i < data.length; i++) {
			data[i] = data[i].trim().replace("\\|", "|");
		}
		return parse(data);
	}
	
	/**
	 * Parse the item with given arguments.<br><br>
	 * Arguments order:<ul>
	 * <li>Material (required).
	 * <li>Stack size, or nothing to take the default 1.
	 * <li>Display name.
	 * <li>Lore. Use "\n" to break line, use "\\n" to escape "\n".
	 * <li>Item damage, "unbreakable" if it is unbreakable, or nothing to take the default undamaged.
	 * <li>Item flags: a 6-character-minimum sequence of "1" or any other character. "1" indicates that flag is set. Flags sequence:<ul>
	 *     <li>Hide attributes.
	 *     <li>Hide what it can destroy.
	 *     <li>Hide enchantments.
	 *     <li>Hide what it can be placed on.
	 *     <li>Hide potion effects.
	 *     <li>Hide "unbreakable" attribute.</ul>
	 * <li>Enchantments, separated by ";". Each enchantment's arguments are themselves separated by " ". Arguments:<ul>
	 *     <li>Enchantment name.
	 *     <li>Enchantment level, or nothing to take the default 1.</ul>
	 * <li>Additional arguments for specific materials. See that material's parser for more details.
	 * </ul>
	 * For display name, lore, and additional arguments marked with "!string", there are special cases:<ul>
	 * <li>Nothing: The item will take the default name, have no lore, or whatever default.
	 * <li>"\": The item will have a blank name/lore/....
	 * <li>"\\": The item's name/lore/... will be "\".
	 * </ul>
	 * @param data The arguments for the item stack.
	 * @return The parsed item.
	 * @throws InvalidItemException When the arguments are invalid.
	 */
	public static ItemStack parse(String... data) throws InvalidItemException {
		Material material = Material.matchMaterial(data[0]);
		if (material == null) throw new InvalidItemException("Cannot find the material specified: " + data[0]);
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		ParserHook hook = hooks.get(material);
		if (data.length > 7 && hook != null) {
			String[] arguments = new String[data.length-7];
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = data[7+i];
			}
			hook.parse(stack, meta, arguments);
		}
		int lengthForSwitch = data.length > 7 ? 7 : data.length;
		switch (lengthForSwitch) {
			case 7:
				if (!data[6].isEmpty()) {
					if (material == Material.ENCHANTED_BOOK) {
						String[] enchantments = data[6].split(";");
						for (String enchantment : enchantments) {
							String[] split = enchantment.trim().split(" ");
							int level = 1;
							if (split.length > 1) {
								try {
									level = Integer.parseInt(split[1]);
								} catch (Exception e) {
									throw new InvalidItemException("Invalid level value.");
								}
							}
							Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(split[0]));
							if (ench == null)
								throw new InvalidItemException("\"" + split[0] + "\" is not a valid enchantment key.");
							((EnchantmentStorageMeta)meta).addStoredEnchant(ench, level, true);
						}
					} else {
						String[] enchantments = data[6].split(";");
						for (String enchantment : enchantments) {
							String[] split = enchantment.trim().split(" ");
							int level = 1;
							if (split.length > 1) {
								try {
									level = Integer.parseInt(split[1]);
								} catch (Exception e) {
									throw new InvalidItemException("Invalid level value.");
								}
							}
							Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(split[0]));
							if (ench == null)
								throw new InvalidItemException("\"" + split[0] + "\" is not a valid enchantment key.");
							meta.addEnchant(ench, level, true);
						}
					}
				}
			case 6:
				if (data[5].length() > 5) {
					for (int i = 0; i < 6; i++) {
						if (data[5].charAt(i) == '1') {
							meta.addItemFlags(flagsArray[i]);
						}
					}
				}
			case 5:
				if (!data[4].isEmpty()) {
					if (data[4].equalsIgnoreCase("unbreakable")) {
						meta.setUnbreakable(true);
					} else if (meta instanceof Damageable) {
						try {
							int damage = Integer.parseInt(data[4]);
							((Damageable)meta).setDamage(damage);
						} catch (Exception e) {
							throw new InvalidItemException("Invalid damage value: " + data[4]);
						}
					}
				}
			case 4:
				String lore = parseString(data[3]).replaceAll("(?<![\\\\])\\\\n", "\n");
				if (!lore.isEmpty()) {
					meta.setLore(Arrays.asList(lore));
				}
			case 3:
				String name = parseString(data[2]);
				if (!name.isEmpty()) {
					meta.setDisplayName("§r" + name);
				}
			case 2:
				if (!data[1].isEmpty()) {
					try {
						stack.setAmount(Integer.parseInt(data[1]));
					} catch (Exception e) {
						throw new InvalidItemException("Invalid stack amount: " + data[1]);
					}
				}
		}
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static String parseString(String in) {
		if (in.equals("\\")) return "";
		if (in.equals("\\\\")) return "\\";
		return in;
	}

	public static String reverseParseString(String in) {
		if (in.equals("")) return "\\";
		if (in.equals("\\")) return "\\\\";
		return in;
	}

	/**
	 * Parse an item stack back to its string equivalent, including additional metadata specific to some materials.
	 * @param stack The item stack to be parsed.
	 * @return The parsed string.
	 */
	public static String reverseParse(ItemStack stack) {
		ArrayList<String> args = new ArrayList<>();
		int lastNonEmpty = 1;
		ItemMeta meta = stack.getItemMeta();
		args.add(stack.getType().getKey().getKey());
		if (stack.getAmount() == 1) {
			args.add("");
		} else {
			args.add(stack.getAmount()+"");
			lastNonEmpty = 2;
		}
		if (meta.hasDisplayName()) {
			args.add(reverseParseString(meta.getDisplayName()));
			lastNonEmpty = 3;
		} else {
			args.add("");
		}
		if (meta.hasLore()) {
			boolean isNotFirst = false;
			String lore = "";
			for (String line : meta.getLore()) {
				lore += (isNotFirst ? "\\n" : "") + line.replace("\\n", "\\\\n").replace("\n", "\\n");
				isNotFirst = true;
			}
			args.add(reverseParseString(lore));
			lastNonEmpty = 4;
		} else {
			args.add("");
		}
		if (meta.isUnbreakable()) {
			args.add("unbreakable");
			lastNonEmpty = 5;
		} else if (meta instanceof Damageable && ((Damageable)meta).hasDamage()) {
			args.add(((Damageable)meta).getDamage()+"");
			lastNonEmpty = 6;
		} else {
			args.add("");
		}
		String flags = "";
		Set<ItemFlag> itemFlags = meta.getItemFlags();
		for (ItemFlag flag : flagsArray) {
			flags += itemFlags.contains(flag) ? "1" : "0";
		}
		if (flags.equals("000000")) {
			args.add("");
		} else {
			args.add(flags);
			lastNonEmpty = 7;
		}
		if (meta.hasEnchants()) {
			Map<Enchantment, Integer> enchantments = meta.getEnchants();
			boolean isNotFirst = false;
			String res = "";
			for (Enchantment enchantment : enchantments.keySet()) {
				int level = enchantments.get(enchantment);
				if (level == 0) {
					continue;
				}
				res += (isNotFirst ? "; " : "") + enchantment.getKey().getKey();
				if (level != 1) {
					res += " " + level;
				}
				isNotFirst = true;
			}
			args.add(res);
			lastNonEmpty = 8;
		}
		ParserHook hook = hooks.get(stack.getType());
		if (hook != null) {
			int argNum = hook.reverseParse(stack, meta, args);
			if (argNum > 0) {
				lastNonEmpty = 8+argNum;
			}
		}
		String res = "";
		boolean isNotFirst = false;
		int argsSize = args.size();
		for (int i = 0; i < lastNonEmpty && i < argsSize; i++) {
			res += (isNotFirst ? " | ": "") + args.get(i);
		}
		return res;
	}
}
