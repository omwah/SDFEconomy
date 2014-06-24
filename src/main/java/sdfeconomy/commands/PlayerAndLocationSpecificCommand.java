package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sdfeconomy.SDFEconomyAPI;

import java.util.ResourceBundle;

/**
 * Implements convenience routines for getting player name and location from the arguments
 */
public abstract class PlayerAndLocationSpecificCommand extends TranslatedCommand
{
	protected final SDFEconomyAPI api;

	public PlayerAndLocationSpecificCommand(String name, SDFEconomyAPI api, ResourceBundle translation)
	{
		super(name, translation);

		this.api = api;
	}

	/*
	 * Nested class to used to pass back
	 * player and location name together
	 */
	protected class PlayerAndLocation
	{
		public String playerName;
		public String locationName;

		public PlayerAndLocation(String playerName, String locationName)
		{
			this.playerName = playerName;
			this.locationName = locationName;
		}
	}

	protected PlayerAndLocation getPlayerAndLocation(CommandHandler handler, CommandSender sender, String[] args, int playerIndex, int locationIndex)
	{

		// Make sure we are at console or sender has sufficient privileges
		// if a owner name is specified
		String player_name = null;
		if (playerIndex >= 0 && args.length > playerIndex && handler.hasAdminPermission(sender))
		{

			// Op or admin creating a bank account for another player
			player_name = args[playerIndex];
		}
		else if (sender instanceof Player)
		{
			// Player is sender, make sure they only try and access their own account when a player name
			// is specified
			//
			Player player_obj = (Player) sender;
			if (playerIndex >= 0 && args.length > playerIndex && !args[playerIndex].equalsIgnoreCase(player_obj.getName()))
			{
				// Player tried to specify owner's name without sufficient privileges
				sender.sendMessage(getTranslation("PlayerSpecificCommand-no_permission"));
				return null;
			}
			else
			{
				// Be extra careful that we don't entirely trust the string they sent
				player_name = ((Player) sender).getName();
			}
		}
		else
		{
			// Sender is not a Player so must be a console access
			sender.sendMessage(getTranslation("PlayerSpecificCommand-specify_player_name"));
			return null;
		}

		String location_name = null;
		if (locationIndex >= 0 && args.length > locationIndex)
		{
			location_name = args[locationIndex];
		}

		// Use the API's last location for player if none supplied as argument
		if (location_name == null && player_name != null)
		{
			// This will not be executed if the player did not supply a location
			location_name = api.getPlayerLocationName(player_name);
		}

		if (location_name == null)
		{
			sender.sendMessage(getTranslation("PlayerAndLocationSpecificCommand-cannot_find_location"));
			return null;
		}

		return new PlayerAndLocation(player_name, location_name);
	}
}
