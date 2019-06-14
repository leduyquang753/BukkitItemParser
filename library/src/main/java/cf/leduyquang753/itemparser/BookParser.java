package cf.leduyquang753.itemparser;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.BookMeta.Generation;

/**
 * Arguments (all not required):<ul>
 * <li>Book title. !string
 * <li>Book author. !string
 * <li>Book generation:<ul>
 *     <li>Original
 *     <li>Copy of original
 *     <li>Copy of copy
 *     <li>Tattered</ul>
 *     If not anything of the above, it will take the default.
 * <li>Book pages.
 * </ul>
 */
public class BookParser implements ParserHook {
	@Override
	public void parse(ItemStack stack, ItemMeta meta, String... arguments) throws InvalidItemException {
		BookMeta book = (BookMeta)meta;
		int lengthForSwitch = arguments.length > 3 ? 4 : arguments.length;
		switch (lengthForSwitch) {
			case 4:
				for (int i = 3; i < arguments.length; i++) {
					book.addPage(arguments[i].replaceAll("(?<![\\\\])\\\\n", "\n"));
				}
			case 3:
				switch (arguments[2].toLowerCase()) {
					case "original":
						book.setGeneration(Generation.ORIGINAL);
						break;
					case "copy of original":
						book.setGeneration(Generation.COPY_OF_ORIGINAL);
						break;
					case "copy of copy":
						book.setGeneration(Generation.COPY_OF_COPY);
						break;
					case "tattered":
						book.setGeneration(Generation.TATTERED);
				}
			case 2:
				String author = ItemParser.parseString(arguments[1]);
				if (!author.isEmpty()) {
					book.setAuthor(author);
				}
			case 1:
				String title = ItemParser.parseString(arguments[0]);
				if (!title.isEmpty()) {
					book.setTitle(title);
				}
		}
	}

	@Override
	public int reverseParse(ItemStack stack, ItemMeta meta, ArrayList<String> arguments) {
		int numOfArgs = 0;
		BookMeta book = (BookMeta) meta;
		if (book.hasTitle()) {
			arguments.add(ItemParser.reverseParseString(book.getTitle()));
			numOfArgs = 1;
		} else {
			arguments.add("");
		}
		if (book.hasAuthor()) {
			arguments.add(ItemParser.reverseParseString(book.getAuthor()));
			numOfArgs = 2;
		} else {
			arguments.add("");
		}
		switch (book.getGeneration()) {
			case ORIGINAL:
				arguments.add("");
				break;
			case COPY_OF_ORIGINAL:
				arguments.add("Copy of original");
				numOfArgs = 3;
				break;
			case COPY_OF_COPY:
				arguments.add("Copy of copy");
				numOfArgs = 3;
				break;
			case TATTERED:
				arguments.add("Tattered");
				numOfArgs = 3;
				break;
		}
		if (book.hasPages() && book.getPageCount() > 0) {
			for (String page : book.getPages()) {
				arguments.add(ItemParser.reverseParseString(page));
			}
			return book.getPageCount() + 3;
		} else return numOfArgs;
	}
}
