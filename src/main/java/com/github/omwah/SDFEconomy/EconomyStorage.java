/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.omwah.SDFEconomy;

/**
 *
 * @author Omwah
 */
public interface EconomyStorage {
    /*
     * 
     */
    boolean hasPlayerAccount(String playerName, String location);
    /*
     * 
     */
    PlayerAccount getPlayerAccount(String playerName, String location);
    /*
     * 
     */
    void createPlayerAccount(String playerName, String location, double begBalance);
    /*
     * 
     */
    boolean hasBankAccount(String accountName, String location);
    /*
     * 
     */
    BankAccount getBankAccount(String accountName, String location);
    /*
     * 
     */
    BankAccount createBankAccount(String accountName, String location, double begBalance);
    /*
     * 
     */
    void updateAccount(Account account);
    /*
     * Commits any unsaved changes to the underlying storage
     */
    public void commit();
}
