/*
 */
package com.github.omwah.SDFEconomy;

/**
 * Interface for storage of Economy data
 */
public interface EconomyStorage {
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
     * Is there a Bank account in a given location
     */
    boolean hasBankAccount(String accountName, String location);
    /*
     * Get a Bank account for a given location
     */
    BankAccount getBankAccount(String accountName, String location);
    /*
     * Create a new Bank account
     */
    BankAccount createBankAccount(String accountName, String location, double begBalance);
    /*
     * Updates the storage values for an Account
     */
    void updateAccount(Account account);
    /*
     * Commits any unsaved changes to the underlying storage
     */
    public void commit();
}
