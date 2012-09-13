/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.omwah.SDFEconomy;

import java.util.Observable;

/**
 *
 */
public abstract class Account extends Observable {
    String name;
    String location;
    double balance = 0.0;
    
    public String getName() {
        return name;
    }
    
    String getLocation() {
        return this.location;
    }
        
    double getBalance() {
        return this.balance;
    }
    
    void setBalance(double amount) {
        this.balance = amount;
        setChanged();
        notifyObservers();
    }

}
