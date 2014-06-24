package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import org.bukkit.command.CommandSender;
import sdfeconomy.SDFEconomyAPI;

import java.util.List;
import java.util.ResourceBundle;

public class ScaleCommand extends PlayerAndLocationSpecificCommand
{

	public ScaleCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("scale", api, translation);

		setArgumentRange(2, 2);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.admin");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		// Command handler, checks permission, but check again just in case
		if (handler.hasAdminPermission(sender))
		{
			double scaling;
			try
			{
				scaling = new Double(args[0]).doubleValue();
			}
			catch (NumberFormatException e)
			{
				sender.sendMessage(getClassTranslation("incorrect_scaling", args[0]));
				return false;
			}

			String location = args[1];

			List<String> players_list = api.getPlayers(location);
			sender.sendMessage(getClassTranslation("scaling_starting", players_list.size(), location, scaling));
			for (String player : players_list)
			{
				double balance = api.getBalance(player, location);
				api.setBalance(player, location, balance * scaling);
			}
			sender.sendMessage(getClassTranslation("scaling_finished"));
		}
		else
		{
			sender.sendMessage(getTranslation("AccountCommon-not_admin"));
		}

		return true;
	}
}
