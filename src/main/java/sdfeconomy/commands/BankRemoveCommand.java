package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sdfeconomy.SDFEconomyAPI;
import sdfeconomy.storage.BankAccount;

import java.util.ResourceBundle;

public class BankRemoveCommand extends TranslatedCommand
{

	private SDFEconomyAPI api;

	public BankRemoveCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank remove", translation);

		this.api = api;

		setArgumentRange(1, 1);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_bank");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		String account_name = args[0];
		BankAccount account = api.getBankAccount(account_name);

		if (account != null)
		{
			if (handler.hasAdminPermission(sender) || sender instanceof Player && account.isOwner(((Player) sender).getName()))
			{
				String owner = account.getOwner();
				String location = account.getLocation();
				double bank_balance = account.getBalance();

				EconomyResponse result = api.deleteBank(account_name);
				if (result.type == ResponseType.SUCCESS)
				{
					sender.sendMessage(getClassTranslation("remove_success", account_name));
				}
				else
				{
					sender.sendMessage(getClassTranslation("remove_failure", account_name));
					return false;
				}

				result = api.depositPlayer(owner, bank_balance, location);
				if (result.type == ResponseType.SUCCESS)
				{
					sender.sendMessage(getClassTranslation("deposit_success", api.format(bank_balance), owner));
				}
				else
				{
					sender.sendMessage(getClassTranslation("deposit_failure", api.format(bank_balance), owner));
					return false;
				}
			}
			else
			{
				sender.sendMessage(getTranslation("BankCommon-not_owner", account_name));
				return false;
			}
		}
		else
		{
			sender.sendMessage(getTranslation("BankCommon-bank_not_found", account_name));
			return false;
		}

		return true;
	}
}
