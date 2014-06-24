/*
 */

package sdfeconomy;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import sdfeconomy.location.LocationTranslator;

/**
 * Used for testing purposes, return predictable strings
 */
public class TestLocationTranslator implements LocationTranslator
{

	public String getLocationName(String playerName)
	{
		if (playerName == "NullPlayer")
		{
			return null;
		}
		else
		{
			int name_len = playerName.length();
			return "world" + playerName.substring(name_len - 1, name_len);
		}
	}

	public String getLocationName(Player player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getLocationName(Location location)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean validLocationName(String locationName)
	{
		int name_len = locationName.length();
		String prefix = locationName.substring(0, name_len - 1);

		int world_num;
		try
		{
			world_num = Integer.parseInt(locationName.substring(name_len - 1, name_len));
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		if (prefix.equalsIgnoreCase("world") && world_num < 4)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
