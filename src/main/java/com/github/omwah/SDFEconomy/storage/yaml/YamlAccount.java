/*
 */
package com.github.omwah.SDFEconomy;

import java.util.Observable;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Implementation of Account that utilizes the YamlFileConfiguration backend
 */
public abstract class YamlAccount extends Observable implements PlayerAccount {
    protected ConfigurationSection section;

    public YamlAccount(ConfigurationSection section) {
        this.section = section;
    }

    @Override
    public String getName() {
        return section.getName();
    }
    
    @Override
    public String getLocation() {
        return section.getString("location");
    }
    
    @Override
    public void setLocation(String location) {
        section.set("location", location.toLowerCase());
        setChanged();
        notifyObservers();
    }
    
    @Override
    public double getBalance() {
        return section.getDouble("balance");
    }
   
    @Override
    public void setBalance(double amount) {
        section.set("balance", amount);
        setChanged();
        notifyObservers();
     }
}
