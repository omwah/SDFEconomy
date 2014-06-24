/*
 *
 */

package sdfeconomy.storage;

import java.util.Observable;
import java.util.Observer;

/*
 * Simple Observer that calls the Storage commit method on every update 
 * This implementation probably should not be used in production
 */

public class StorageCommitEveryUpdate implements Observer
{
	public void update(Observable o, Object arg)
	{
		if (o instanceof EconomyStorage)
		{
			((EconomyStorage) o).commit();
		}
	}
}
