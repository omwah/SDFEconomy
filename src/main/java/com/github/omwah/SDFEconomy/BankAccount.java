/*
 */
package com.github.omwah.SDFEconomy;

import java.util.List;
import java.util.ArrayList;

/**
 * Specialization of Account for Bank Accounts.
 */
public class BankAccount extends Account {
    private String owner;
    private ArrayList<String> members;
    
    /*
     * Create a new BankAccount
     */

    public BankAccount(String name, String owner, String location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.members = new ArrayList<String>();
    }
    
    /*
     * Get the owner of the Bank
     */
    
    public String getOwner() {
        return owner;
    }
    
    /*
     * Get the members of the Bank
     */
    
    public ArrayList<String> getMembers() {
        return members;
    }
    
    /*
     * Set all the members of the Bank
     */

    public void setMembers(List<String> memberList) {
        members.addAll(memberList);
        setChanged();
        notifyObservers();
    }
    
    /*
     * Add a new Bank member
     */
     
    public void addMember(String newMember) {
        members.add(newMember);
        setChanged();
        notifyObservers();
    }
    
    /*
     * Remove a Bank member
     */
    
    public void removeMember(String oldMember) {
        // ArrayList will use the .equals of String to compare
        members.remove(oldMember);
        setChanged();
        notifyObservers();
    }
    
    /*
     * Determine if someone is a member of the Bank
     */
    
    public boolean isMember(String memberName) {
       return members.indexOf(memberName) >= 0;
    }
    
    /*
     * Determine if someone the owner of the bank
     */
    
    public boolean isOwner(String playerName) {
       return this.owner.equals(playerName);
    }
    
    
}
