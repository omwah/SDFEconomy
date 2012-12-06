/*
 */
package com.github.omwah.SDFEconomy;

import java.io.File;
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
        storage.createPlayerAccount("Player1", "world", 10.0);
        
        this.api = new SDFEconomyAPI(new MemoryConfiguration(), storage, new TestLocationTranslator());
    }
    
    @Test
    public void checkSomething() {
    }
}
