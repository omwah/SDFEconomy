package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sdfeconomy.SDFEconomyAPI;
import sdfeconomy.storage.BankAccount;

import java.util.ResourceBundle;

public class BankRemoveMemberCommand extends TranslatedCommand
{

	private SDFEconomyAPI api;

	public BankRemoveMemberCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank removemember", translation);

		this.api = api;

		setArgumentRange(2, 2);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_bank");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		String account_name = args[0];
		String member_name = args[1];
		BankAccount account = api.getBankAccount(account_name);

		if (account != null)
		{
			if (handler.hasAdminPermission(sender) || sender instanceof Player && account.isOwner(((Player) sender).getName()))
			{

				if (!account.isMember(member_name))
				{
					sender.sendMessage(getClassTranslation("not_a_member", member_name, account_name));
					return false;
				}

				account.removeMember(member_name);
				if (!account.isMember(member_name))
				{
					sender.sendMessage(getClassTranslation("remove_success", member_name, account_name));
				}
				else
				{
					sender.sendMessage(getClassTranslation("remove_failure", member_name, account_name));
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
