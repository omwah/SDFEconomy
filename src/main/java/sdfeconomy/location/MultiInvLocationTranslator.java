package sdfeconomy.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import uk.co.tggl.pluckerpluck.multiinv.MultiInv;
import uk.co.tggl.pluckerpluck.multiinv.MultiInvAPI;

/**
 * Returns the location of a player based on MultiInv world groupings
 */
public class MultiInvLocationTranslator extends SetDestinationLocationTranslator
{
	private MultiInvAPI multiInv = null;

	public MultiInvLocationTranslator(Plugin plugin)
	{
		super(plugin.getServer());

		// Try and get MultiInv plugin, in case loaded before
		// SDFEconomy
		loadMultiInv();

		// Launch a listener to check for the loading and uloading of MultiInv
		if (this.multiInv == null)
		{
			server.getPluginManager().registerEvents(new MVILoadListener(this), plugin);
		}
	}

	/*
	 * Add listener to wait for MultiInv to be loaded
	 */
	public class MVILoadListener implements Listener
	{
		MultiInvLocationTranslator translator = null;

		public MVILoadListener(MultiInvLocationTranslator translator)
		{
			this.translator = translator;
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event)
		{
			if (event.getPlugin().getDescription().getName().equals("MultiInv"))
			{
				translator.loadMultiInv();
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginDisable(PluginDisableEvent event)
		{
			if (event.getPlugin().getDescription().getName().equals("MultiInv"))
			{
				translator.unloadMultiInv();
			}
		}
	}

	private void loadMultiInv()
	{
		Plugin plugin = this.server.getPluginManager().getPlugin("MultiInv");

		if (plugin != null && plugin instanceof MultiInv)
		{
			this.multiInv = ((MultiInv) plugin).getAPI();
		}
	}

	private void unloadMultiInv()
	{
		this.multiInv = null;
	}

	/*
	 * Get location name based on which groups a World belongs to
	 */
	private String getLocationName(World world)
	{
		String locationName = null;

		// Try and retrieve a name based on MultiverseInvetories groupings of worlds
		if (multiInv != null)
		{
			if (multiInv.getGroups().containsKey(world.getName()))
			{
				locationName = multiInv.getGroups().get(world.getName());
			}
		}

		// If all else fails fall back to direct world name
		if (locationName == null || locationName.length() == 0)
		{
			locationName = world.getName();
		}
		return locationName;
	}

	public String getLocationName(Location location)
	{
		if (location != null)
		{
			return getLocationName(location.getWorld());
		}
		else
		{
			return null;
		}
	}

	public boolean validLocationName(String locationName)
	{
		// Go through each world and determine the location names
		// for that World and check if it matches the supplied world name
		for (World curr_world : server.getWorlds())
		{
			String worldLocName = getLocationName(curr_world);
			if (worldLocName.equalsIgnoreCase(locationName))
			{
				return true;
			}
		}
		return false;
	}
}
