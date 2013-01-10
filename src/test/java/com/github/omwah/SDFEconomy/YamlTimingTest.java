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

import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * Time the speed of the current commit functionality
 * Grinders + ecoCreature can queue up a large number of writes and cause
 * excessive disk i/o if not handled properly
 */

// Ignore for now since these tests only need for benchmarking, not testing persay 
@Ignore
@RunWith(JUnit4.class)
public class YamlTimingTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    private final String test_filename = "accounts_test.yml";
    private final int numAccountAccess = 1000;
    private final double accountIncrement = 10.0;
    

    @Before
    public void setup() {
    }

    /*
     * Just iteratively sets a balance on a YamlStorage object
     * commits at the end to save all changes
     */
    public void setBalanceBenchmark(YamlStorage storage) {
        PlayerAccount account = storage.createPlayerAccount("Player1", "world1", 0.0);
        for(int idx = 0; idx < this.numAccountAccess; idx++) {
            account.setBalance(account.getBalance() + this.accountIncrement);
        }
        storage.commit();
    }

    /*
     * Checks that the balance set through the benchmark was actually saved to disk
     */
    public void checkBalanceSaving(YamlStorage storage) {
        PlayerAccount account = storage.getPlayerAccount("Player1", "world1");            
        assertEquals(this.numAccountAccess * this.accountIncrement, account.getBalance(), 1e-6);
    }

    /*
     * Benchmark saving once 
     */
    @Test
    public void saveOnce() {
        File out_file = new File(folder.getRoot(), test_filename);
        {
            YamlStorage store = new YamlStorage(out_file);
            setBalanceBenchmark(store);
        }

        {
            YamlStorage store = new YamlStorage(out_file);
            checkBalanceSaving(store);
        }
    } 

    /*
     * Benchmark saving on each update
     */
    @Test
    public void saveEveryUpdate() {
        File out_file = new File(folder.getRoot(), test_filename);
        {
            YamlStorage store = new YamlStorage(out_file);
            store.addObserver((Observer) new StorageCommitEveryUpdate());
            setBalanceBenchmark(store);
        }

        {
            YamlStorage store = new YamlStorage(out_file);
            checkBalanceSaving(store);
        }
    } 

    /*
     * Benchmark saving every N updates.
     * Included to illustrate the performance gain from not 
     * saving on every update
     */
    @Test
    public void saveEveryN() {
        File out_file = new File(folder.getRoot(), test_filename);
        {
            YamlStorage store = new YamlStorage(out_file);
            store.addObserver((Observer) new StorageCommitEveryN(100));
            setBalanceBenchmark(store);
        }

        {
            YamlStorage store = new YamlStorage(out_file);
            checkBalanceSaving(store);
        }
    }
}
