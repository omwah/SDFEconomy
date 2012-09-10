/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.omwah.SDFEconomy;

import java.util.Observable;

/**
 *
 * @author Omwah
 */
public abstract class Account extends Observable {
    double balance;
    String location;
    
    double getBalance() {
        return this.balance;
    }
    
    void setBalance(double amount) {
        this.balance = amount;
        setChanged();
        notifyObservers();
    }
    
    String getLocation() {
        return this.location;
    }
    
}
