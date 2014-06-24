package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import org.bukkit.command.CommandSender;
import sdfeconomy.SDFEconomyAPI;

import java.util.ResourceBundle;

public class BalanceCommand extends PlayerAndLocationSpecificCommand
{

	public BalanceCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("balance", api, translation);

		setArgumentRange(0, 2);
		setIdentifiers(this.getName(), "bal");
		setPermission("sdfeconomy.use_account");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 1);

		// If arguments are supplied then check for another players balance
		if (ploc != null)
		{
			if (api.hasAccount(ploc.playerName, ploc.locationName))
			{
				String balance = api.format(this.api.getBalance(ploc.playerName, ploc.locationName));
				sender.sendMessage(getClassTranslation("balance_message", ploc.playerName, ploc.locationName, balance));
			}
			else
			{
				sender.sendMessage(getTranslation("AccountCommon-cannot_find_account", ploc.playerName, ploc.locationName));
			}
		}
		else
		{
			// Unable to succesfully get player name and or location, helper routine will send appropriate message
			return false;
		}

		return true;
	}
}
