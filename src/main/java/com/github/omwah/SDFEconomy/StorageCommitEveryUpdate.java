/*
 *
 */
package com.github.omwah.SDFEconomy;

import java.util.Observer;
import java.util.Observable;

/*
 * Simple Observer that calls the Storage commit method on every update 
 */

public class StorageCommitEveryUpdate implements Observer {
    public void update(Observable o, Object arg) {
        if (o instanceof EconomyStorage) {
            ((EconomyStorage) o).commit();
        }
    }
}
