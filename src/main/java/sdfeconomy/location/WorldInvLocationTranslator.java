package sdfeconomy.location;

import me.drayshak.WorldInventories.api.WorldInventoriesAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 * Returns the location of a player based on WorldInventories world groupings
 */
public class WorldInvLocationTranslator extends SetDestinationLocationTranslator
{
	private WorldInventoriesAPI worldInv = null;

	public WorldInvLocationTranslator(Plugin plugin)
	{
		super(plugin.getServer());

		// Try and get WorldInventories plugin, in case loaded before
		// SDFEconomy
		loadWorldInventories();

		// Launch a listener to check for the loading and uloading of WorldInventories
		if (this.worldInv == null)
		{
			server.getPluginManager().registerEvents(new WILoadListener(this), plugin);
		}
	}

	/*
	 * Add listener to wait for WorldInventories to be loaded
	 */
	public class WILoadListener implements Listener
	{
		WorldInvLocationTranslator translator = null;

		public WILoadListener(WorldInvLocationTranslator translator)
		{
			this.translator = translator;
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event)
		{
			if (event.getPlugin().getDescription().getName().equals("WorldInventories"))
			{
				translator.loadWorldInventories();
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginDisable(PluginDisableEvent event)
		{
			if (event.getPlugin().getDescription().getName().equals("WorldInventories"))
			{
				translator.unloadWorldInventories();
			}
		}
	}

	private void loadWorldInventories()
	{
		Plugin plugin = this.server.getPluginManager().getPlugin("WorldInventories");

		if (plugin != null)
		{
			this.worldInv = (WorldInventoriesAPI) plugin;
		}
	}

	private void unloadWorldInventories()
	{
		this.worldInv = null;
	}

	/*
	 * Get location name based on which groups a World belongs to
	 */
	private String getLocationName(World world)
	{
		String locationName = null;

		// Try and retrieve a name based on MultiverseInvetories groupings of worlds
		if (this.worldInv != null)
		{
			locationName = WorldInventoriesAPI.findGroup(world.getName()).getName();
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
