package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ParserHook {
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException;
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments);
}
