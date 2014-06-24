package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.command.CommandSender;
import sdfeconomy.SDFEconomyAPI;

import java.util.ResourceBundle;

public class BankCreateCommand extends PlayerAndLocationSpecificCommand
{

	public BankCreateCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank create", api, translation);

		setArgumentRange(1, 3);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_bank");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 1, 2);

		if (ploc != null)
		{
			String account_name = args[0];

			EconomyResponse result = api.createBank(account_name, ploc.playerName, ploc.locationName);
			if (result.type == ResponseType.SUCCESS)
			{
				sender.sendMessage(getClassTranslation("create_success", account_name));
			}
			else
			{
				sender.sendMessage(getClassTranslation("create_failed", result.errorMessage));
				return false;
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
