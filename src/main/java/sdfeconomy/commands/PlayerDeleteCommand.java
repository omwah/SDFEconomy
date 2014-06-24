package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import org.bukkit.command.CommandSender;
import sdfeconomy.SDFEconomyAPI;

import java.util.ResourceBundle;

public class PlayerDeleteCommand extends PlayerAndLocationSpecificCommand
{

	public PlayerDeleteCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("player delete", api, translation);

		setArgumentRange(2, 2);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.admin");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 1);

		if (ploc == null)
		{
			// Unable to succesfully get player name and or location, helper routine will send appropriate message
			return false;
		}
		// If we pass the above check we will have a valid player and location string
		// since both were passed as arguments via CommandHandler

		boolean success = api.deletePlayerAccount(ploc.playerName, ploc.locationName);
		if (success)
		{
			sender.sendMessage(getClassTranslation("delete_success", ploc.playerName, ploc.locationName));
		}
		else
		{
			sender.sendMessage(getClassTranslation("delete_failure", ploc.playerName, ploc.locationName));
			return false;
		}

		return true;
	}
}
