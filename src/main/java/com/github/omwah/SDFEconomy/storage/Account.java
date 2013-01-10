/*
 */
package com.github.omwah.SDFEconomy;

/**
 * Base class with common Account methods 
 */
public interface Account { 
    /*
     * Name of account
     */
    public String getName();
    
    /*
     * Where the account is located, i.e. which world
     */
    public String getLocation();
    
    /*
     * Set where the account is located, i.e. which world
     */
    public void setLocation(String location);
    
    /*
     * How much money is in account
     */
    public double getBalance();
    
    /*
     * Set balance of account
     */
    public void setBalance(double amount);

}
