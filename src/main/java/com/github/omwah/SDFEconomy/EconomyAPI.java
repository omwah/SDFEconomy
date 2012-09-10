/*
 * 
 */
package com.github.omwah.SDFEconomy;

import java.util.List;
import java.text.DecimalFormat;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.configuration.Configuration;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

/**
 *
 * @author Omwah
 */
public class EconomyAPI implements Economy {
    private final String name = "SDFEconomy";
    
    private Server server;
    private EconomyStorage storage;
    private Configuration config;
    
    public EconomyAPI(Server server, EconomyStorage storage, Configuration config) {
        this.server = server;
        this.storage = storage;
        this.config = config;
        
        this.config.addDefault("bank.enabled", true);
        this.config.addDefault("currency.numerical_format", "#,##0.00");
        this.config.addDefault("currency.name.plural", "simoleons");
        this.config.addDefault("currency.name.singular", "simoleons");
    }
    
    /*
     *
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
     * 
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * 
     */
    @Override
    public boolean hasBankSupport() {
        return this.config.getBoolean("bank.enabled");
    }

    /*
     * Returns -1 since no rounding occurs.
     */
    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        String pattern = this.config.getString("currency.numerical_format");
        DecimalFormat formatter = new DecimalFormat(pattern);
        String formatted = formatter.format(amount);
        return formatted;
    }

    @Override
    public String currencyNamePlural() {
        return this.config.getString("currency.name.plural");
    }

    @Override
    public String currencyNameSingular() {
         return this.config.getString("currency.name.singular");
    }

    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(server.getPlayer(playerName));
    }
    
    public boolean hasAccount(Player playerObj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public double getBalance(String playerName) {
        return getBalance((Player) server.getOfflinePlayer(playerName));
    }
    
    public double getBalance(Player playerObj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getBanks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
