/*
 */
package com.github.omwah.SDFEconomy;

import java.io.File;
import java.util.Observer;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
    
    // Does balance access in a seperate thread
    private class StorageDepositRunnable implements Runnable {
        private YamlStorage storage;
        private String name;

        public StorageDepositRunnable(YamlStorage storage, String name) {
            this.name = name;
            this.storage = storage;
        }

        /*
         * Just iteratively sets a balance on a YamlStorage object
         */
        public void run() { 
            PlayerAccount account = storage.getPlayerAccount("Player1", "world1");
            for(int idx = 0; idx < numAccountAccess; idx++) {
                account.deposit(accountIncrement);
            }
        }
    }
    
    /*
     * Tests multiple threads all hitting the same player's account for desposits
     */
    @Test
    public void balanceDeposit() {
        File out_file = new File(folder.getRoot(), test_filename);
        {
            YamlStorage store = new YamlStorage(out_file);
            store.addObserver((Observer) new StorageCommitEveryN(100));
            store.createPlayerAccount("Player1", "world1", 0.0);

            ThreadGroup accessGroup = new ThreadGroup("YamlAccess");
            for(int tcount = 0; tcount < this.numThreads; tcount++) {
                String tname = "Thread" + (tcount+1);
                (new Thread(accessGroup, new StorageDepositRunnable(store, tname))).start();
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

    private class StorageWithdrawRunnable implements Runnable {
        private YamlStorage storage;
        private String name;

        public StorageWithdrawRunnable(YamlStorage storage, String name) {
            this.storage = storage;
            this.name = name;
        }

        /*
         * Just iteratively sets a balance on a YamlStorage object
         */
        public void run() { 
            PlayerAccount account = storage.getPlayerAccount("Player1", "world1");
            for(int idx = 0; idx < numAccountAccess; idx++) {
                account.withdraw(accountIncrement);
            }
        }
    }
        
    /*
     * Tests multiple threads all hitting the same player's account for withdraws
     */
    @Test
    public void balanceWithdraw() {
        File out_file = new File(folder.getRoot(), test_filename);
        {
            YamlStorage store = new YamlStorage(out_file);
            store.addObserver((Observer) new StorageCommitEveryN(100));
            
            // Create a player with a large amount of money which will be
            // reduced to 0 by the threads
            store.createPlayerAccount("Player1", "world1", 
                    this.numAccountAccess * this.accountIncrement * this.numThreads);
        
            ThreadGroup accessGroup = new ThreadGroup("YamlAccess");
            for(int tcount = 0; tcount < this.numThreads; tcount++) {
                String tname = "Thread" + (tcount + 1); 
                (new Thread(accessGroup, new StorageWithdrawRunnable(store, tname))).start();
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
            assertEquals(0.0, account.getBalance(), 1e-6);
        }
    } 
    
}
