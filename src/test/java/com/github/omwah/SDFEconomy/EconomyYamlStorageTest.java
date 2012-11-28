/*
 */
package com.github.omwah.SDFEconomy;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.Rule;

import org.junit.rules.TemporaryFolder;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 *
 */
@RunWith(JUnit4.class)
public class EconomyYamlStorageTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final String test_filename = "accounts_test.yml";

    @Test
    public void testWrite() {
        File out_file = new File(folder.getRoot(), test_filename);

        EconomyYamlStorage storage = new EconomyYamlStorage(out_file, false);
        storage.commit();

        assertTrue("Accounts file was not written", out_file.exists());
    }

    @Test
    public void testPlayerAccount() {
        File out_file = new File(folder.getRoot(), test_filename);

        // Use seperate scopes so underlying storage is deleted each time
        
        // Test creation of new player account
        {
            EconomyYamlStorage stor_save = new EconomyYamlStorage(out_file, false);
            PlayerAccount saved_account = stor_save.createPlayerAccount("player", "world", 10.0);
            stor_save.commit();

            assertEquals("player", saved_account.getName());
            assertEquals("world", saved_account.getLocation());
            assertEquals(10.0, saved_account.getBalance(), 1e-6);
        }

        // Test can be read back in, turn save on update on to test
        {
            EconomyYamlStorage stor_read = new EconomyYamlStorage(out_file, true);
            boolean has_account = stor_read.hasPlayerAccount("player", "world");
            assertTrue("Player account was not created", has_account);

            PlayerAccount read_account = stor_read.getPlayerAccount("player", "world");
            assertEquals("player", read_account.getName());
            assertEquals("world", read_account.getLocation());
            assertEquals(10.0, read_account.getBalance(), 1e-6);
            
            // Update to check observer updates
            read_account.setBalance(50.0);
        }

        // Check that updated balance was written
        {
            EconomyYamlStorage stor_update = new EconomyYamlStorage(out_file, false);
            PlayerAccount updated_account = stor_update.getPlayerAccount("player", "world");

            assertEquals(50.0, updated_account.getBalance(), 1e-6);
        }
     }
}
