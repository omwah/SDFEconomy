package sdfeconomy.commands;

import com.github.omwah.omcommands.NestedCommandExecutor;
import com.github.omwah.omcommands.PluginCommand;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sdfeconomy.SDFEconomy;
import sdfeconomy.SDFEconomyAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/*
 * CommandExectuor that dispatches commands to CommandHandler classes
 */
public class SDFEconomyCommandExecutor extends NestedCommandExecutor
{

	/*
	 * This command executor needs to know about its plugin from which it came from
	 */
	public SDFEconomyCommandExecutor(SDFEconomy plugin, Command cmd, Locale currentLocale)
	{
		super(plugin, cmd, "sdfeconomy.admin", ResourceBundle.getBundle("CommandTranslation", currentLocale));
	}

    /*
	 * Declares commands to be used by the plugin
     */

	@Override
	protected List<PluginCommand> getSubCommands(JavaPlugin plugin)
	{
		SDFEconomy sdf_economy = (SDFEconomy) plugin;
		SDFEconomyAPI api = sdf_economy.getAPI();
		Server server = sdf_economy.getServer();
		FileConfiguration config = sdf_economy.getConfig();

		// Set up which subcommands of the main command are available
		ArrayList<PluginCommand> sub_cmd_list = new ArrayList<PluginCommand>();
		sub_cmd_list.add(new BalanceCommand(api, getTranslation()));
		sub_cmd_list.add(new PayCommand(api, server, getTranslation()));

		// Enable bank commands only if enabled
		if (api.hasBankSupport())
		{
			sub_cmd_list.add(new BankListCommand(api, getTranslation()));
			sub_cmd_list.add(new BankInfoCommand(api, getTranslation()));

			sub_cmd_list.add(new BankDepositCommand(api, getTranslation()));
			sub_cmd_list.add(new BankWithdrawCommand(api, getTranslation()));

			sub_cmd_list.add(new BankCreateCommand(api, getTranslation()));
			sub_cmd_list.add(new BankRemoveCommand(api, getTranslation()));
			sub_cmd_list.add(new BankRenameCommand(api, getTranslation()));

			sub_cmd_list.add(new BankAddMemberCommand(api, getTranslation()));
			sub_cmd_list.add(new BankRemoveMemberCommand(api, getTranslation()));

			sub_cmd_list.add(new BankSetOwnerCommand(api, getTranslation()));
		}

		if (config.contains("commands.topN"))
		{
			server.getLogger().info("[SDFEconomy] Config option commands.topN deprecated, use commands.top.number");
		}
		int top_number = config.getInt("commands.top.number", 5);
		boolean top_include_banks = config.getBoolean("commands.top.include_banks", true);
		sub_cmd_list.add(new TopAccountsCommand(api, top_number, top_include_banks, getTranslation()));

		sub_cmd_list.add(new ReloadCommand(api, getTranslation()));
		sub_cmd_list.add(new SetCommand(api, getTranslation()));
		sub_cmd_list.add(new DepositAdminCommand(api, getTranslation()));
		sub_cmd_list.add(new WithdrawAdminCommand(api, getTranslation()));
		sub_cmd_list.add(new ScaleCommand(api, getTranslation()));
		sub_cmd_list.add(new ConvertCommand(api, server, getTranslation()));
		sub_cmd_list.add(new PlayerCreateCommand(api, getTranslation()));
		sub_cmd_list.add(new PlayerDeleteCommand(api, getTranslation()));

		sub_cmd_list.add(new ListLocationsCommand(api, server, getTranslation()));

		return sub_cmd_list;
	}
}
