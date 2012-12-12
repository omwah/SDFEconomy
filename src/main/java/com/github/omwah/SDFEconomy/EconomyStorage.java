/*
 */
package com.github.omwah.SDFEconomy;

import java.util.List;

/**
 * Interface for storage of Economy data
 */
public interface EconomyStorage {
    /*
     * Get the name of all player accounts in a section
     */
    public List<String> getPlayerNames(String location);
    /*
     * Does the player have an account in a given location
     */
    boolean hasPlayerAccount(String playerName, String location);
    /*
     * Get a Player account for a given location
     */
    PlayerAccount getPlayerAccount(String playerName, String location);
    /*
     * Create a new Player account in a given location with an initial balance
     */
    PlayerAccount createPlayerAccount(String playerName, String location, double begBalance);
    /*
     * Get a list of all bank names
     */
    List<String> getBankNames();
    /*
     * Get a Bank account, account should be unique to a certain location
     */
    BankAccount getBankAccount(String accountName);
    /*
     * Create a new Bank account, Bank is tied to location during creation
     */
    BankAccount createBankAccount(String accountName, String ownerName, String location, double begBalance);
     /*
     * Create a new Bank account
     */
    void deleteBankAccount(String accountName);
    /*
     * Updates the storage values for an Account
     */
    void updateAccount(Account account);
    /*
     * Commits any unsaved changes to the underlying storage
     */
    public void commit();
}
