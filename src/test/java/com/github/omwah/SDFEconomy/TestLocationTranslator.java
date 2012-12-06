/*
 */
package com.github.omwah.SDFEconomy;

/**
 * Used for testing purposes, always returns the same string
 */
public class TestLocationTranslator implements LocationTranslator {

    public String getLocationName(String playerName) {
        return "world";
    }
    
}
