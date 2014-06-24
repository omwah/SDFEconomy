package sdfeconomy.location;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

/**
 * Returns the location of the Player based on their current World
 */
public class PerWorldLocationTranslator extends SetDestinationLocationTranslator
{

	public PerWorldLocationTranslator(Server server)
	{
		super(server);
	}

	public String getLocationName(Location location)
	{
		return location.getWorld().getName();
	}

	public boolean validLocationName(String locationName)
	{
		for (World curr_world : server.getWorlds())
		{
			if (curr_world.getName().equalsIgnoreCase(locationName))
			{
				return true;
			}
		}
		return false;
	}
}
