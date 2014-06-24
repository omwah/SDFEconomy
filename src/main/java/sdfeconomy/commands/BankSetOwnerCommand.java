package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sdfeconomy.SDFEconomyAPI;
import sdfeconomy.storage.BankAccount;

import java.util.ResourceBundle;

public class BankSetOwnerCommand extends TranslatedCommand
{

	private SDFEconomyAPI api;

	public BankSetOwnerCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank setowner", translation);

		this.api = api;

		setArgumentRange(2, 2);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_bank");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		String account_name = args[0];
		String new_owner = args[1];
		BankAccount account = api.getBankAccount(account_name);

		if (account != null)
		{
			if (handler.hasAdminPermission(sender) || sender instanceof Player && account.isOwner(((Player) sender).getName()))
			{
				String old_owner = account.getOwner();

				account.setOwner(new_owner);

				if (account.isOwner(new_owner))
				{
					sender.sendMessage(getClassTranslation("set_success", new_owner, account_name));
				}
				else
				{
					sender.sendMessage(getClassTranslation("set_failed", new_owner, account_name));
					return false;
				}

				// new owner does not need to a be a member of their own bank
				if (account.isMember(new_owner))
				{
					sender.sendMessage(getClassTranslation("member_removing", new_owner, account_name));
					account.removeMember(new_owner);
				}

				// Add previous owner as member of new bank
				if (!account.isMember(old_owner))
				{
					sender.sendMessage(getClassTranslation("member_adding", old_owner, account_name));
					account.addMember(old_owner);
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
