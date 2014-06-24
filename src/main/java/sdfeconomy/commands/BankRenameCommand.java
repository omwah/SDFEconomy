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

public class BankRenameCommand extends TranslatedCommand
{

	private SDFEconomyAPI api;

	public BankRenameCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank rename", translation);

		this.api = api;

		setArgumentRange(2, 2);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_bank");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		String old_account_name = args[0];
		String new_account_name = args[1];
		BankAccount old_account = api.getBankAccount(old_account_name);

		if (old_account != null)
		{
			if (handler.hasAdminPermission(sender) || sender instanceof Player && old_account.isOwner(((Player) sender).getName()))
			{
				// Create a new bank with same attributes as old but with a new name
				EconomyResponse create_res = api.createBank(new_account_name, old_account.getOwner(), old_account.getLocation());
				if (create_res.type != ResponseType.SUCCESS)
				{
					sender.sendMessage(getClassTranslation("create_failed", new_account_name, create_res.errorMessage));
					return false;
				}

				// Set members the same as the old bank
				BankAccount new_account = api.getBankAccount(new_account_name);
				new_account.setMembers(old_account.getMembers());

				// Set balance of new bank account same as old one
				new_account.setBalance(old_account.getBalance());

				// Delete old bank account
				EconomyResponse delete_res = api.deleteBank(old_account_name);
				if (delete_res.type != ResponseType.SUCCESS)
				{
					sender.sendMessage(getClassTranslation("remove_failed", old_account_name, delete_res.errorMessage));
					return false;
				}

				sender.sendMessage(getClassTranslation("rename_success", old_account_name, new_account_name));
			}
			else
			{
				sender.sendMessage(getTranslation("BankCommon-not_owner", old_account_name));
				return false;
			}
		}
		else
		{
			sender.sendMessage(getTranslation("BankCommon-bank_not_found", old_account_name));
			return false;
		}

		return true;
	}
}
