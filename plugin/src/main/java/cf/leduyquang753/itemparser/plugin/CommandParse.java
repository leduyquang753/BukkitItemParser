package cf.leduyquang753.itemparser.plugin;

import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import cf.leduyquang753.itemparser.*;

public class CommandParse implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arguments) {
		if (!sender.hasPermission(Main.parse)) {
			sender.sendMessage("§eYou don't have permission to do this.");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("§eYou must be a player to do this.");
			return true;
		}
		if (arguments.length == 0) {
			sender.sendMessage("§eYou must type something to parse.");
		}
		try {
			Location loc = ((Player)sender).getLocation();
			Item item = (Item) loc.getWorld().spawnEntity(loc, EntityType.DROPPED_ITEM);
			item.setItemStack(ItemParser.parse(String.join(" ", arguments)));
			item.setPickupDelay(0);
			sender.sendMessage("§aItem \"§r" + item.getName() + "§a\" parsed successfully.");
			return true;
		} catch (InvalidItemException e) {
			sender.sendMessage("§eInvalid item: " + e.getMessage());
			return true;
		}
	}
}
