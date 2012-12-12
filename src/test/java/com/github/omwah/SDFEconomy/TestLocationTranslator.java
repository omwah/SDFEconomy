/*
 */
package com.github.omwah.SDFEconomy;

/**
 * Used for testing purposes, return predictable strings
 */
public class TestLocationTranslator implements LocationTranslator {

    public String getLocationName(String playerName) {
        int name_len = playerName.length();
        return "World" + playerName.substring(name_len-1,name_len);
    }
    
}
