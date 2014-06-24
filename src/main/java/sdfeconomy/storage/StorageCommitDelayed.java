/*
 *
 */

package sdfeconomy.storage;

import org.bukkit.plugin.Plugin;

import java.util.Observable;
import java.util.Observer;

/*
 * Uses the Bukkit scheduler to call commit at a later time, specified in server ticks
 * Any other updates that happen while an outstanding commit is waiting will not
 * trigger additional commits until the delayed commit has finished.
 */

public class StorageCommitDelayed implements Observer
{
	private final Plugin plugin;
	private final long delay;

	private boolean commit_scheduled = false;

	private class CommitRunnable implements Runnable
	{
		private final EconomyStorage storage;

		public CommitRunnable(EconomyStorage storage)
		{
			commit_scheduled = true;
			this.storage = storage;
		}

		@Override
		public void run()
		{
			commit_scheduled = false;
			storage.commit();
		}
	}

	/*
	 * Need the Plugin class passed here since it is required by
	 * the Bukkit scheduler API
	 */
	public StorageCommitDelayed(Plugin plugin, long delayInTicks)
	{
		this.plugin = plugin;
		this.delay = delayInTicks;
	}

	public void update(Observable o, Object arg)
	{
		if (o instanceof EconomyStorage && !commit_scheduled)
		{
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new CommitRunnable((EconomyStorage) o), delay);
		}
	}
}
