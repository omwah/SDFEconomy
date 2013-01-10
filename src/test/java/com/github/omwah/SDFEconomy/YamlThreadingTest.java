/*
 */
package com.github.omwah.SDFEconomy;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Ignore;

import org.junit.rules.MethodRule;
import org.junit.rules.TemporaryFolder;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 */

@RunWith(JUnit4.class)
public class YamlThreadingTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final String test_filename = "accounts_test.yml";
    private final int numAccountAccess = 1000;
    private final double accountIncrement = 10.0;
    private final int numThreads = 2;
    
    // Does account access in a seperate thread
    private class AccessStorageRunnable implements Runnable {
        private YamlStorage storage;

        public AccessStorageRunnable(YamlStorage storage) {
            this.storage = storage;
        }

        /*
         * Just iteratively sets a balance on a YamlStorage object
         */
        public void run() { 
            PlayerAccount account;
            if(storage.hasPlayerAccount("Player1", "world")) {
                account = storage.getPlayerAccount("Player1", "world1");
            } else {
                account = storage.createPlayerAccount("Player1", "world1", 0.0);
            }
            for(int idx = 0; idx < numAccountAccess; idx++) {
                account.setBalance(account.getBalance() + accountIncrement);
            }
        }
    }

    /*
     */
    @Test
    public void multiThreadAccess() {
        File out_file = new File(folder.getRoot(), test_filename);
        {
            YamlStorage store = new YamlStorage(out_file);
            store.addObserver((Observer) new StorageCommitEveryN(100));

            ThreadGroup accessGroup = new ThreadGroup("YamlAccess");
            for(int tcount = 0; tcount < this.numThreads; tcount++) {
                (new Thread(accessGroup, new AccessStorageRunnable(store))).start();
            }

            while(accessGroup.activeCount() > 0) {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {
                    break;
                }
            }
            store.commit();
        }

        {
            YamlStorage store = new YamlStorage(out_file);
            PlayerAccount account = store.getPlayerAccount("Player1", "world1");
            assertEquals(this.numAccountAccess * this.accountIncrement * this.numThreads, account.getBalance(), 1e-6);
        }
    } 

}
