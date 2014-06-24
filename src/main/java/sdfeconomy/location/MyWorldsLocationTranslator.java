package sdfeconomy.location;

import com.bergerkiller.bukkit.mw.MyWorlds;
import com.bergerkiller.bukkit.mw.WorldConfig;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 * Returns the location of a player based on MyWorlds world inventory groupings
 */
public class MyWorldsLocationTranslator extends SetDestinationLocationTranslator
{
	public static final String PLUGIN_NAME = "My Worlds";
	private boolean enabled = false;

	public MyWorldsLocationTranslator(Plugin plugin)
	{
		super(plugin.getServer());
		// All methods are statically accessible - no MyWorlds instance has to be stored
		setEnabled(true);
		server.getPluginManager().registerEvents(new MWLoadListener(this), plugin);
	}

	/*
	 * Add listener to wait for MyWorlds to be loaded or detect loading later on
	 */
	public class MWLoadListener implements Listener
	{
		MyWorldsLocationTranslator translator = null;

		public MWLoadListener(MyWorldsLocationTranslator translator)
		{
			this.translator = translator;
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event)
		{
			if (event.getPlugin().getDescription().getName().equals(PLUGIN_NAME))
			{
				translator.setEnabled(true);
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginDisable(PluginDisableEvent event)
		{
			if (event.getPlugin().getDescription().getName().equals(PLUGIN_NAME))
			{
				translator.setEnabled(false);
			}
		}
	}

	public boolean isEnabled()
	{
		// Even if enabled, per-world inventories could be disabled
		// This setting can change when the configuration of My Worlds is reloaded
		// For that reason we have to check this each time
		if (this.enabled)
		{
			// Use a try-catch here, you never know when the class is being accessed!
			try
			{
				return MyWorlds.useWorldInventories;
			}
			catch (Throwable t)
			{
				// Nothing (not enabled)
			}
		}
		return false;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled && server.getPluginManager().isPluginEnabled(PLUGIN_NAME);
	}

	public boolean validLocationName(String locationName)
	{
		if (this.isEnabled())
		{
			// Check all MyWorlds' world configurations to see if it is valid
			for (WorldConfig config : WorldConfig.all())
			{
				if (config.inventory.getSharedWorldName().equalsIgnoreCase(locationName))
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			// Check if the location name denotes a valid world
			return server.getWorld(locationName) != null;
		}
	}

	public String getLocationName(Location location)
	{
		if (location == null)
		{
			// Null check (why can the location be null? Oh well.)
			return null;
		}
		else if (this.isEnabled())
		{
			// Get the world configuration for that world, and the inventory container world name of that
			return WorldConfig.get(location).inventory.getSharedWorldName();
		}
		else
		{
			// Fall back to using the world name
			return location.getWorld().getName();
		}
	}
}
