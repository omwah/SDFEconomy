/*
 */
package com.github.omwah.SDFEconomy;

import java.util.List;
import java.text.DecimalFormat;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.configuration.Configuration;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

/**
 * Provides the interface necessary to implement a Vault Economy.
 */
public class EconomyAPI implements Economy {
    private final String name = "SDFEconomy";
    
    private Server server;
    private EconomyStorage storage;
    private Configuration config;
    private LocationTranslator locTrans;
    
    public EconomyAPI(Server server, Configuration config, EconomyStorage storage, LocationTranslator locationTrans) {
        this.server = server;
        this.config = config;
        this.storage = storage;
        this.locTrans = locationTrans;
        
        this.config.addDefault("api.bank.enabled", true);
        this.config.addDefault("api.bank.initial_balance", 0.00);
        this.config.addDefault("api.player.initial_balance", 10.00);
        this.config.addDefault("api.currency.numerical_format", "#,##0.00");
        this.config.addDefault("api.currency.name.plural", "simoleons");
        this.config.addDefault("api.currency.name.singular", "simoleons");
    }
    
    /*
     * Returns that the economy is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
     * The name of the Economy
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * Whether bank support is enabled
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
        return storage.hasPlayerAccount(playerObj.getName(), locTrans.getLocationName(playerObj));
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance((Player) server.getOfflinePlayer(playerName));
    }
    
    public double getBalance(Player playerObj) {
        PlayerAccount account = storage.getPlayerAccount(playerObj.getName(), locTrans.getLocationName(playerObj));
        return account.getBalance();
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer((Player) server.getOfflinePlayer(playerName), amount);
    }
    
    public EconomyResponse withdrawPlayer(Player playerObj, double amount) {
        PlayerAccount account = storage.getPlayerAccount(playerObj.getName(), locTrans.getLocationName(playerObj));
        account.setBalance(account.getBalance() - amount);
        EconomyResponse response = new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer((Player) server.getOfflinePlayer(playerName), amount);
    }
    
    public EconomyResponse depositPlayer(Player playerObj, double amount) {
        PlayerAccount account = storage.getPlayerAccount(playerObj.getName(), locTrans.getLocationName(playerObj));
        account.setBalance(account.getBalance() + amount);
        EconomyResponse response = new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
    }
    
    @Override
    public EconomyResponse createBank(String name, String playerName) {
        return createBank(name, (Player) server.getOfflinePlayer(playerName));
    }
    
    public EconomyResponse createBank(String name, Player playerObj) {
        double initialBalance = config.getDouble("api.bank.initial_balance");
        BankAccount account = storage.createBankAccount(name, locTrans.getLocationName(playerObj), initialBalance);
        EconomyResponse response = new EconomyResponse(initialBalance, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
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
