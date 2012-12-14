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

import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

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
        assertEquals(null, api.getPlayerLocationName("NullPlayer"));
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
    public void createPlayerAccount() {
        assertFalse("Player1 account should not be creatable in World1", api.createPlayerAccount("Player1"));
        assertFalse("Player2 account should not be creatable in World2", api.createPlayerAccount("Player2"));
        assertFalse("Player3 account should not be creatable in World1", api.createPlayerAccount("Player3", "World1"));
        
        assertFalse("Player1 account should not be creatable with null location", api.createPlayerAccount("Player1", null));

        // Since Player3 has no account in World3 yet
        assertTrue("Player1 account should be creatable in World3", api.createPlayerAccount("Player3"));
    }
    
    @Test
    public void hasPlayerAccount() {
        assertTrue(api.hasAccount("Player1"));
        assertTrue(api.hasAccount("Player2"));
        
        assertFalse(api.hasAccount("Player3"));
        assertTrue(api.hasAccount("Player3", "World1"));
        
        assertFalse(api.hasAccount("NullPlayer"));
    }
    
    @Test
    public void getPlayerBalance() {
        assertEquals(10.0, api.getBalance("Player1"), 1e-6);
        assertEquals(40.0, api.getBalance("Player2"), 1e-6);
        assertEquals(50.0, api.getBalance("Player3", "World1"), 1e-6);
        
        assertEquals(0.0, api.getBalance("NullPlayer"), 1e-6);
        assertEquals(0.0, api.getBalance("NullPlayer", "World1"), 1e-6);

    }
    
    @Test
    public void playerHasAmount() {
        assertTrue("Player1 should have 15.00", api.has("Player1", 9.0));
        assertFalse("Player1 should not have negative 15.00", api.has("Player1", -9.0));
        assertTrue("Player2 should have 45.00", api.has("Player2", 39.0));
        assertFalse("Player2 should not have negative 45.00", api.has("Player2", -39.0));
        
        assertTrue("NullPlayer should have 0.00", api.has("NullPlayer", 0.0));
        assertFalse("NullPlayer should not have 1.00", api.has("NullPlayer", 1.0));
    }
    
    @Test
    public void withdrawPlayer() {
        assertTrue("Withdraw of 10.0 from Player1 should succeeed", api.withdrawPlayer("Player1", 10).type == ResponseType.SUCCESS);
        assertTrue("Withdraw of 1.0 from Player1 should not succeeed", api.withdrawPlayer("Player1", 1).type == ResponseType.FAILURE);
 
        assertTrue("Withdraw of 40.0 from Player2 should succeeed", api.withdrawPlayer("Player2", 40).type == ResponseType.SUCCESS);
        assertTrue("Withdraw of 1.0 from Player2 should not succeeed", api.withdrawPlayer("Player2", 1).type == ResponseType.FAILURE);
 
        assertTrue("Withdraw of 1.0 from NullPlayer should not succeeed", api.withdrawPlayer("NullPlayer", 1).type == ResponseType.FAILURE);
    }
    
    @Test
    public void depositPlayer() {
        assertTrue("Deposit of 10.0 to Player1 should succeeed", api.depositPlayer("Player1", 10).type == ResponseType.SUCCESS);
        assertTrue("Deposit of 40.0 to Player2 should succeeed", api.depositPlayer("Player2", 40).type == ResponseType.SUCCESS);
 
        assertTrue("Deposit of 1.0 to NullPlayer should not succeeed", api.depositPlayer("NullPlayer", 1).type == ResponseType.FAILURE);
    }
}
