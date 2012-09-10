/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.omwah.SDFEconomy;

import java.util.Observer;
import java.util.Observable;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Omwah
 */
public class EconomyYamlStorage extends Observer implements EconomyStorage {
    private final String player_prefix = "player";
    private final String bank_prefix = "bank";
    
    private final String filename;
    FileConfiguration storage;

    public EconomyYamlStorage(String filename) {
        this.filename = filename;
        this.storage = new YamlConfiguration();
    }

    public boolean hasPlayerAccount(String playerName, String location) {
        return this.storage.getConfigurationSection(this.player_prefix + "." + playerName) != null;
    }

    public PlayerAccount getPlayerAccount(String playerName, String location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasBankAccount(String accountName, String location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BankAccount getBankAccount(String accountName, String location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(Observable o, Object arg) {
        if (o instanceof Account) {
            updateAccount((Account) o);
        }
        this.commit();
    }
            
    @Override
    public void updateAccount(Account account) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void commit() {
        this.storage.save(this.filename);
    }

}
