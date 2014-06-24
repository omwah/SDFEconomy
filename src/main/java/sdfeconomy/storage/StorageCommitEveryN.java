/*
 *
 */

package sdfeconomy.storage;

import java.util.Observable;
import java.util.Observer;

/*
 * Simple Observer that calls the Storage commit method every N updates
 * Commits on the Nth update.
 * This implementation probably should not be used in production
 */

public class StorageCommitEveryN implements Observer
{
	private long update_count = 0;
	private long updates_till_commit = 0;

	public StorageCommitEveryN(long updates_till_commit)
	{
		this.updates_till_commit = updates_till_commit;
	}

	public void update(Observable o, Object arg)
	{
		if (o instanceof EconomyStorage && update_count >= (updates_till_commit - 1))
		{
			update_count = 0;
			((EconomyStorage) o).commit();
		}
		else
		{
			update_count++;
		}
	}
}
