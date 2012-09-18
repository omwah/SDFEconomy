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
 */
public class EconomyAPI implements Economy {
    private final String name = "SDFEconomy";
    
    private Server server;
    private EconomyStorage storage;
    private Configuration config;
    private LocationTranslator locationTrans;
    
    public EconomyAPI(Server server, Configuration config, EconomyStorage storage, LocationTranslator locationTrans) {
        this.server = server;
        this.config = config;
        this.storage = storage;
        this.locationTrans = locationTrans;
        
        this.config.addDefault("api.bank.enabled", true);
        this.config.addDefault("api.currency.numerical_format", "#,##0.00");
        this.config.addDefault("api.currency.name.plural", "simoleons");
        this.config.addDefault("api.currency.name.singular", "simoleons");
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
        return this.config.getBoolean("api.bank.enabled");
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
        String pattern = this.config.getString("api.currency.numerical_format");
        DecimalFormat formatter = new DecimalFormat(pattern);
        String formatted = formatter.format(amount);
        return formatted;
    }

    @Override
    public String currencyNamePlural() {
        return this.config.getString("api.currency.name.plural");
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
