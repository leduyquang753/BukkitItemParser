package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

/**
 * Arguments: Recipe keys with two parts separated by ":": namespace and key.
 */
public class KnowledgeBookParser implements ParserHook {
	@Override
	@SuppressWarnings("deprecation")
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		KnowledgeBookMeta book = (KnowledgeBookMeta)meta;
		for (String argument : arguments) {
			String[] split = argument.split(":");
			if (split.length != 2) throw new InvalidItemException("Invalid recipe key: " + argument);
			book.addRecipe(new NamespacedKey(split[0], split[1]));
		}
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		KnowledgeBookMeta book = (KnowledgeBookMeta) meta;
		if (book.hasRecipes()) {
			for (NamespacedKey recipe : book.getRecipes()) {
				arguments.add(recipe.toString());
			}
			return book.getRecipes().size();
		}
		return 0;
	}
}
