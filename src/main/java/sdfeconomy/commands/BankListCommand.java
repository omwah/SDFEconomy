package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import org.bukkit.command.CommandSender;
import sdfeconomy.SDFEconomyAPI;
import sdfeconomy.storage.BankAccount;

import java.util.List;
import java.util.ResourceBundle;

public class BankListCommand extends PlayerAndLocationSpecificCommand
{
	public BankListCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank list", api, translation);

		setArgumentRange(0, 2);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_bank");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		List<BankAccount> bank_accounts;
		if (handler.hasAdminPermission(sender) && args.length == 0)
		{
			// Get all banks for a location
			bank_accounts = api.getAllBanks();
		}
		else
		{
			PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 1);

			if (ploc == null)
			{
				// Unable to succesfully get player name and or location, helper routine will send appropriate message
				return false;
			}

			// Only report those owned by player unless sender is op or console
			bank_accounts = api.getPlayerBanks(ploc.playerName, ploc.locationName);
		}

		sender.sendMessage(getClassTranslation("banner"));
		for (BankAccount account : bank_accounts)
		{
			String balance_str = api.format(account.getBalance());
			if (handler.hasAdminPermission(sender))
			{
				sender.sendMessage(getClassTranslation("list_line_admin", account.getName(), account.getLocation(), balance_str, account.getOwner()));
			}
			else
			{
				sender.sendMessage(getClassTranslation("list_line_normal", account.getName(), balance_str));
			}
		}

		return true;
	}
}
