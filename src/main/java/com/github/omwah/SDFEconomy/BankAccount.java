/*
 */
package com.github.omwah.SDFEconomy;

import java.util.List;
import java.util.ArrayList;

/**
 * Specialization of Account for Bank Accounts.
 */
public class BankAccount extends Account {
    private ArrayList<String> members;
    
    /*
     * Create a new BankAccount
     */

    public BankAccount(String name, String location) {
        this.name = name;
        this.location = location;
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
    }
    
    /*
     * Add a new Bank member
     */
     
    public void addMember(String newMember) {
        members.add(newMember);
    }
    
    /*
     * Remove a Bank member
     */
    
    public void removeMember(String oldMember) {
        // ArrayList will use the .equals of String to compare
        members.remove(oldMember);    
    }
    
    /*
     * Determine if someone is a member of the Bank
     */
    
    public boolean isMember(String memberName) {
       return members.indexOf(memberName) >= 0;
    }
    
}
