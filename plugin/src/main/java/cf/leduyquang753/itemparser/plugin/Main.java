package cf.leduyquang753.itemparser.plugin;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Permission
	parse = new Permission("itemparser.parse"),
	reverseParse = new Permission("itemparser.reverseparse");
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().addPermission(parse);
		getServer().getPluginManager().addPermission(reverseParse);
		getCommand("parseitem").setExecutor(new CommandParse());
		getCommand("reverseparseitem").setExecutor(new CommandReverseParse());
	}
}
