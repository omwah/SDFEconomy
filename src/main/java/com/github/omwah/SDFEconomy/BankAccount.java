/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.omwah.SDFEconomy;

import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class BankAccount extends Account {
    private ArrayList<String> members;

    public BankAccount(String name, String location) {
        this.name = name;
        this.location = location;
    }
    
    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> memberList) {
        members.addAll(memberList); 
    }
     
    public void addMember(String newMember) {
        members.add(newMember);
    }
    
    public void removeMember(String oldMember) {
        // ArrayList will use the .equals of String to compare
        members.remove(oldMember);    
    }
    
    public boolean isMember(String memberName) {
       return members.indexOf(memberName) >= 0;
    }
    
}
