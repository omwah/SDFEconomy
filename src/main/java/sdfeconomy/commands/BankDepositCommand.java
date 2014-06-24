package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sdfeconomy.SDFEconomyAPI;
import sdfeconomy.storage.BankAccount;

import java.util.ResourceBundle;

public class BankDepositCommand extends PlayerAndLocationSpecificCommand
{

	public BankDepositCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank deposit", api, translation);

		setArgumentRange(2, 4);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_bank");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 2, 3);

		String bank_name = args[0];

		// Try and parse amount, fail gracefully
		double amount;
		try
		{
			amount = Double.parseDouble(args[1]);
		}
		catch (NumberFormatException e)
		{
			sender.sendMessage(getTranslation("AccountCommon-invalid_amount", args[1]));
			return false;
		}

		BankAccount bank_account = api.getBankAccount(bank_name);

		// If arguments are supplied then check for another players balance
		if (ploc != null)
		{
			// Check that we have a valid player account
			if (!api.hasAccount(ploc.playerName, ploc.locationName))
			{
				sender.sendMessage(getTranslation("AccountCommon-cannot_find_account", ploc.playerName, ploc.locationName));
				return false;
			}

			// Check that the bank account exists
			if (bank_account == null)
			{
				sender.sendMessage(getTranslation("BankCommon-bank_not_found", bank_name));
				return false;
			}

			// Check that location of player and bank location match
			if (!ploc.locationName.equalsIgnoreCase(bank_account.getLocation()))
			{
				sender.sendMessage(getClassTranslation("location_mismatch", bank_account.getName(), bank_account.getLocation(), ploc.locationName));
				return false;
			}

			// Now make sure player has permissions and do withdraw
			if (handler.hasAdminPermission(sender) ||
					sender instanceof Player && bank_account.isOwner(((Player) sender).getName()) ||
					bank_account.isMember(((Player) sender).getName()))
			{

				EconomyResponse player_w_res = api.withdrawPlayer(ploc.playerName, amount, ploc.locationName);
				if (player_w_res.type != ResponseType.SUCCESS)
				{
					sender.sendMessage(getClassTranslation("player_widthdraw_error", ploc.playerName, player_w_res.errorMessage));
					return false;
				}

				EconomyResponse bank_d_res = api.bankDeposit(bank_name, amount);
				if (bank_d_res.type != ResponseType.SUCCESS)
				{
					sender.sendMessage(getClassTranslation("bank_deposit_error", bank_account.getName(), bank_d_res.errorMessage));
					return false;
				}

				sender.sendMessage(getClassTranslation("bank_deposit_success", api.format(amount), ploc.playerName, ploc.locationName, bank_name));
			}
			else
			{
				sender.sendMessage(getTranslation("BankCommon-not_owner", bank_name));
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
