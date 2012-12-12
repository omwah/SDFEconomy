/*
 */
package com.github.omwah.SDFEconomy;

import java.text.DecimalFormat;
import java.util.List;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.configuration.Configuration;

/**
 * Provides the interface necessary to implement a Vault Economy.
 * Implements most of Vault.Economy interface but does not declare
 * itself as implementing this interface because there is no easy
 * way in Vault to use this class directly without a proxy class.
 */
public class SDFEconomyAPI {
    private EconomyStorage storage;
    private Configuration config;
    private LocationTranslator locTrans;
    
    public SDFEconomyAPI(Configuration config, EconomyStorage storage, LocationTranslator locationTrans) {
        this.config = config;
        this.storage = storage;
        this.locTrans = locationTrans;
        
        this.config.addDefault("api.bank.enabled", true);
        this.config.addDefault("api.bank.initial_balance", 0.00);
        this.config.addDefault("api.player.initial_balance", 10.00);
        this.config.addDefault("api.currency.numerical_format", "#,##0.00");
        this.config.addDefault("api.currency.name.plural", "simoleons");
        this.config.addDefault("api.currency.name.singular", "simoleon");
    }
    
    /*
     * Whether bank support is enabled
     */

    public boolean hasBankSupport() {
        return this.config.getBoolean("api.bank.enabled");
    }

    /*
     * Returns -1 since no rounding occurs.
     */

    public int fractionalDigits() {
        return -1;
    }

    public String format(double amount) {
        String pattern = this.config.getString("api.currency.numerical_format");
        DecimalFormat formatter = new DecimalFormat(pattern);
        String formatted = formatter.format(amount);
        if(amount == 1.0) {
            formatted += " " + currencyNameSingular();
        } else {
            formatted += " " + currencyNamePlural();
        }
        return formatted;
    }

    public String currencyNamePlural() {
        return this.config.getString("api.currency.name.plural");
    }

    public String currencyNameSingular() {
         return this.config.getString("api.currency.name.singular");
    }
    
    public String getPlayerLocationName(String playerName) {
        return locTrans.getLocationName(playerName);
    }
    
    public List<String> getPlayers(String locationName) {
        return storage.getPlayerNames(locationName);
    }
    
    public boolean hasAccount(String playerName) {
        return hasAccount(playerName, getPlayerLocationName(playerName));
    }
    
    public boolean hasAccount(String playerName, String locationName) {
        return storage.hasPlayerAccount(playerName, locationName);
    }

    public double getBalance(String playerName) {
        return getBalance(playerName, getPlayerLocationName(playerName));
    }
    
    public double getBalance(String playerName, String locationName) {
        PlayerAccount account = storage.getPlayerAccount(playerName, locationName);
        return account.getBalance();
    }

    public boolean has(String playerName, double amount) {
        return has(playerName, getPlayerLocationName(playerName), amount);
    }

    public boolean has(String playerName, String locationName, double amount) {
        return amount >= 0.0 && getBalance(playerName, locationName) <= amount;
    }

    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        PlayerAccount account = storage.getPlayerAccount(playerName, getPlayerLocationName(playerName));
        account.setBalance(account.getBalance() - amount);
        EconomyResponse response = new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
    }

    public EconomyResponse depositPlayer(String playerName, double amount) {
        PlayerAccount account = storage.getPlayerAccount(playerName, getPlayerLocationName(playerName));
        account.setBalance(account.getBalance() + amount);
        EconomyResponse response = new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
    }
    
    public EconomyResponse createBank(String name, String playerName) {
        String locationName = getPlayerLocationName(playerName);
        
        // Make sure a bank can not be created without a location
        EconomyResponse response;
        if(locationName != null) {
            double initialBalance = config.getDouble("api.bank.initial_balance");
            BankAccount account = storage.createBankAccount(name, playerName, locationName, initialBalance);
            response = new EconomyResponse(initialBalance, account.getBalance(), ResponseType.SUCCESS, "");
        } else {
            response = new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "Can not create a bank with an unknown location");
        }
        return response;
    }

    public EconomyResponse deleteBank(String name) {
        storage.deleteBankAccount(name);
        EconomyResponse response = new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
        return response;
    }

    public EconomyResponse bankBalance(String name) {
        BankAccount account = storage.getBankAccount(name);
        EconomyResponse response = new EconomyResponse(0, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
    }

    public EconomyResponse bankHas(String name, double amount) {
        BankAccount account = storage.getBankAccount(name);
        ResponseType result;
        if(account.getBalance() > amount) {
            result = ResponseType.SUCCESS;
        } else {
            result = ResponseType.FAILURE;
        }
        EconomyResponse response = new EconomyResponse(0, account.getBalance(), result, "");
        return response;
    }

    public EconomyResponse bankWithdraw(String name, double amount) {
        BankAccount account = storage.getBankAccount(name);
        account.setBalance(account.getBalance() - amount);
        EconomyResponse response = new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
    }

    public EconomyResponse bankDeposit(String name, double amount) {
        BankAccount account = storage.getBankAccount(name);
        account.setBalance(account.getBalance() + amount);
        EconomyResponse response = new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, "");
        return response;
    }

    public EconomyResponse isBankOwner(String name, String playerName) {
        BankAccount account = storage.getBankAccount(name);
        String location = getPlayerLocationName(playerName);
        
        ResponseType result;
        if(account.getLocation().compareTo(location) == 0 && account.isOwner(playerName)) {
            result = ResponseType.SUCCESS;
        } else {
            result = ResponseType.FAILURE;
        }
        EconomyResponse response = new EconomyResponse(0, account.getBalance(), result, "");
        return response;    
    }

    public EconomyResponse isBankMember(String name, String playerName) {
        BankAccount account = storage.getBankAccount(name);
        String location = getPlayerLocationName(playerName);
        
        ResponseType result;
        if(account.getLocation().compareTo(location) == 0 && account.isMember(playerName)) {
            result = ResponseType.SUCCESS;
        } else {
            result = ResponseType.FAILURE;
        }
        EconomyResponse response = new EconomyResponse(0, account.getBalance(), result, "");
        return response;   
    }

    public List<String> getBanks() {
        return storage.getBankNames();
    }

    public boolean createPlayerAccount(String playerName) {
        String locationName = getPlayerLocationName(playerName);
        
        // Make sure an account can not be created without a location
        if(locationName != null) {
            double initialBalance = config.getDouble("api.player.initial_balance");
            PlayerAccount account = storage.createPlayerAccount(playerName, locationName, initialBalance);
            return true;
        } else {
            return false;
        }
    }
     
}
