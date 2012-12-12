/*
 */
package com.github.omwah.SDFEconomy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;

import org.junit.rules.TemporaryFolder;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import org.bukkit.configuration.MemoryConfiguration;

/**
 * Tests the SDFEconomyAPI class
 */
@RunWith(JUnit4.class)
public class SDFEconomyAPITest {
    private SDFEconomyAPI api;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
        
    @Before
    public void SetupAPI() {
        // Create an API object using a memory only configuration and a test
        // location translator that returns known values
        File storage_file = new File(folder.getRoot(), "api_test_accounts.yml");
        
        // Add test date using storage, not API
        EconomyYamlStorage storage = new EconomyYamlStorage(storage_file, false);
        
        // These will both match up, w/o specifying world name
        storage.createPlayerAccount("Player1", "World1", 10.0);
        storage.createPlayerAccount("Player2", "World2", 40.0);
        
        // This player is not in his default location per the test location translator
        storage.createPlayerAccount("Player3", "World1", 50.0);
        
        storage.createBankAccount("Bank1", "Player1", "World1", 101.00);
        BankAccount bank = storage.getBankAccount("Bank1");
        bank.addMember("Player2");
        
        this.api = new SDFEconomyAPI(new MemoryConfiguration(), storage, new TestLocationTranslator());
    }
    
    @Test
    public void format() {
        assertEquals("1.00 simoleon", api.format(1.0));
        assertEquals("10.00 simoleons", api.format(10.0));
    }
    
    @Test
    public void getPlayerLocationName() {
        // This really just ends up testing that we can pass the right
        // string from the unit test location translator
        assertEquals("World1", api.getPlayerLocationName("Player1"));
        assertEquals("World2", api.getPlayerLocationName("Player2"));
    }
    
    @Test
    public void playerNames() {
        // Player names returned as lower case only
        ArrayList<String> expected_names = new ArrayList<String>();
        expected_names.add("player1");
        expected_names.add("player3");
        assertEquals(expected_names, api.getPlayers("World1"));
    }
    
    @Test
    public void hasAccount() {
        assertTrue(api.hasAccount("Player1"));
        assertTrue(api.hasAccount("Player2"));
        
        assertFalse(api.hasAccount("Player3"));
        assertTrue(api.hasAccount("Player3", "World1"));
    }
    
    @Test
    public void getBalance() {
        assertEquals(10.0, api.getBalance("Player1"), 1e-6);
        assertEquals(40.0, api.getBalance("Player2"), 1e-6);
        assertEquals(50.0, api.getBalance("Player3", "World1"), 1e-6);
    }
    
    @Test
    public void has() {
        assertTrue("Player1 should have 15.00", api.has("Player1", 15.0));
        assertFalse("Player1 should not have negative 15.00", api.has("Player1", -15.0));
        assertTrue("Player2 should have 45.00", api.has("Player2", 45.0));
        assertFalse("Player2 should not have negative 45.00", api.has("Player2", -45.0));
    }
}
