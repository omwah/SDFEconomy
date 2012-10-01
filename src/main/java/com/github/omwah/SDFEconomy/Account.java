/*
 */
package com.github.omwah.SDFEconomy;

import java.util.Observable;

/**
 * Base class that implements common Account behavior.
 */
public abstract class Account extends Observable {
    String name;
    String location;
    double balance = 0.0;
    
    /*
     * Name of account
     */
    
    public String getName() {
        return name;
    }
    
    /*
     * Where the account is located, i.e. which world
     */
    
    String getLocation() {
        return this.location;
    }
    
    /*
     * How much money is in account
     */
        
    double getBalance() {
        return this.balance;
    }
    
    /*
     * Set balance of account
     */
    
    void setBalance(double amount) {
        this.balance = amount;
        setChanged();
        notifyObservers();
    }

}
