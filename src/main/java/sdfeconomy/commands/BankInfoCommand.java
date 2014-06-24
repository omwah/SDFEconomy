package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import com.google.common.base.Joiner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sdfeconomy.SDFEconomyAPI;
import sdfeconomy.storage.BankAccount;

import java.util.ResourceBundle;

public class BankInfoCommand extends TranslatedCommand
{

	private SDFEconomyAPI api;

	public BankInfoCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("bank info", translation);

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
			if (handler.hasAdminPermission(sender) ||
					sender instanceof Player && account.isOwner(((Player) sender).getName()) ||
					account.isMember(((Player) sender).getName()))
			{

				String bank_info_desc = getClassTranslation("bank_info");
				sender.sendMessage("§c-----[ " + "§f " + bank_info_desc + " " + account_name + " §c ]-----");
				sender.sendMessage(getClassTranslation("location_line", account.getLocation()));
				sender.sendMessage(getClassTranslation("balance_line", api.format(account.getBalance())));
				sender.sendMessage(getClassTranslation("owner_line", account.getOwner()));

				Joiner joiner = Joiner.on(", ");
				String member_list = joiner.join(account.getMembers());
				sender.sendMessage(getClassTranslation("members_line", member_list));
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
