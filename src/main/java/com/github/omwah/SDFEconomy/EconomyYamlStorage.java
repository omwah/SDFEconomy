/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.omwah.SDFEconomy;

import java.util.Observer;
import java.util.Observable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Omwah
 */
public class EconomyYamlStorage extends Observer implements EconomyStorage {
    private final String player_prefix = "player";
    private final String bank_prefix = "bank";
    
    private final String filename;
    YamlConfiguration storage;

    public EconomyYamlStorage(String filename) {
        this.filename = filename;
        this.storage = new YamlConfiguration();
    }
    
    private ConfigurationSection getPlayerSection(String playerName, String location) {
        return this.storage.getConfigurationSection(this.player_prefix + "." + playerName + "." + location);
    }

    public boolean hasPlayerAccount(String playerName, String location) {
        return getPlayerSection(playerName, location) != null;
    }

    public PlayerAccount getPlayerAccount(String playerName, String location) {
        ConfigurationSection section = getPlayerSection(playerName, location);
        PlayerAccount account = new PlayerAccount(playerName, location);
        account.setBalance(section.getDouble("balance"));
        account.addObserver((Observer) this);
        return account;
    }
    
    public void createPlayerAccount(String playerName, String location, double begBalance) {
        ConfigurationSection section = this.storage.createSection(this.player_prefix + "." + playerName + "." + location);
        section.set("balance", begBalance);
    }

    public boolean hasBankAccount(String accountName, String location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BankAccount getBankAccount(String accountName, String location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public BankAccount createBankAccount(String accountName, String location, double begBalance) {
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
        String section_prefix;
        if(account instanceof PlayerAccount) {
            section_prefix = this.player_prefix;
        } else if (account instanceof BankAccount) {
            section_prefix = this.bank_prefix;
        } else {
            throw IllegalArgumentException("Account passed not an instance of PlayerAccount or BankAccount");
        }
        ConfigurationSection section = this.storage.createSection(this.player_prefix + "." + account.getName() + "." + account.getLocation());
        section.set("balance", account.getBalance());
        if (account instanceof BankAccount) {
            section.set("members", ((BankAccount) account).getMembers());
        }
    }
    
    @Override
    public void commit() {
        this.storage.save(this.filename);
    }

}
