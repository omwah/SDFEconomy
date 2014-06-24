package sdfeconomy.location;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 * Abstract base class for translators that use the Bukkit server to determine location of players based on their
 * location
 */
public abstract class PlayerLocationTranslator implements LocationTranslator
{
	protected Server server;

	public PlayerLocationTranslator(Server server)
	{
		this.server = server;
	}

	/*
	 * Retrieves the location of the named player based on their
	 * current location or their bed location when they are offline.
	 * This should be a last resort to finding a location for an operation.
	 */
	public String getLocationName(String playerName)
	{
		// If no specific destination try and determine player's location
		// whether online or off
		OfflinePlayer offlinePlayer = server.getOfflinePlayer(playerName);
		Player onlinePlayer = offlinePlayer.getPlayer();

		if (onlinePlayer != null)
		{
			return getLocationName(onlinePlayer.getLocation());
		}
		else if (offlinePlayer.hasPlayedBefore())
		{
			// Make sure the offline player has played before or the call to
			// getBedSpawnLocation will cause a null pointer inside of bukkit
			Location bedLocation = offlinePlayer.getBedSpawnLocation();
			if (bedLocation != null)
			{
				return getLocationName(bedLocation);
			}
		}
		return null;
	}

	public String getLocationName(Player player)
	{
		return getLocationName(player.getName());
	}

	public abstract String getLocationName(Location location);
}
