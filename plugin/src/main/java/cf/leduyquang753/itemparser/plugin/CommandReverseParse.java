package cf.leduyquang753.itemparser.plugin;

import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.*;

import cf.leduyquang753.itemparser.ItemParser;

public class CommandReverseParse implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arguments) {
		if (!sender.hasPermission(Main.reverseParse)) {
			sender.sendMessage("§eYou don't have permission to do this.");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("§eYou must be a player to do this.");
			return true;
		}
		ItemStack holding = ((Player)sender).getInventory().getItemInMainHand();
		if (holding.getType() == Material.AIR) {
			sender.sendMessage("§eYou must hold something to reverse parse.");
			return true;
		}
		String parsed = ItemParser.reverseParse(holding);
		sender.sendMessage("\n§aParsed:");
		TextComponent withLink = new TextComponent(parsed);
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("Click to paste in chat.")});
		withLink.setHoverEvent(hoverEvent);
		ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, parsed);
		withLink.setClickEvent(clickEvent);
		sender.spigot().sendMessage(new BaseComponent[] { withLink });
		return true;
	}
}
