/*
 */
package com.github.omwah.SDFEconomy;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.Observer;
import java.util.Observable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Implements an Economy storage through a YAML file
 */
public class EconomyYamlStorage implements EconomyStorage, Observer {
    private final String player_prefix = "player";
    private final String bank_prefix = "bank";
    
    private final String filename;
    YamlConfiguration storage;

    private static final Logger log = Logger.getLogger("Minecraft");

    public EconomyYamlStorage(String filename) {
        this.filename = filename;
        this.storage = new YamlConfiguration();
    }
    
    private ConfigurationSection getPlayerSection(String playerName, String location) {
        return this.storage.getConfigurationSection(location + "." + this.player_prefix + "." + playerName);
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
    
    public PlayerAccount createPlayerAccount(String playerName, String location, double begBalance) {
        ConfigurationSection section = this.storage.createSection(this.player_prefix + "." + playerName + "." + location);
        section.set("balance", begBalance);
        return getPlayerAccount(playerName, location);
    }

    private ConfigurationSection getBankSection(String accountName, String location) {
        return this.storage.getConfigurationSection(location + "." + this.bank_prefix + "." + accountName);
    }

     public boolean hasBankAccount(String accountName, String location) {
        return getBankSection(accountName, location) != null;
    }

    public BankAccount getBankAccount(String accountName, String location) {
        ConfigurationSection section = getBankSection(accountName, location);
        BankAccount account = new BankAccount(accountName, location);
        account.setBalance(section.getDouble("balance"));
        account.setMembers(section.getStringList("members"));
        account.addObserver((Observer) this);
        return account;
     }
    
    public BankAccount createBankAccount(String accountName, String location, double begBalance) {
        ConfigurationSection section = getBankSection(accountName, location);
        section.set("balance", begBalance);
        return getBankAccount(accountName, location);
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
            throw new IllegalArgumentException("Account passed not an instance of PlayerAccount or BankAccount");
        }
        ConfigurationSection section = this.storage.createSection(this.player_prefix + "." + account.getName() + "." + account.getLocation());
        section.set("balance", account.getBalance());
        if (account instanceof BankAccount) {
            section.set("members", ((BankAccount) account).getMembers());
        }
    }
    
    @Override
    public void commit() {
        try {
            this.storage.save(this.filename);
        } catch(IOException e) {
            this.log.severe("Error saving YamlStorage to: " + this.filename + "\n" + e);
        }
    }

}
