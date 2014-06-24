/*
 */

package sdfeconomy.storage;

import java.util.List;

/**
 * Specialization of Account for Bank Accounts.
 */
public interface BankAccount extends Account
{
	/*
	 * Get the owner of the Bank
	 */
	public String getOwner();

	/*
	 * Sets the owner of the Bank
	 */
	public void setOwner(String owner);

	/*
	 * Get the members of the Bank
	 */
	public List<String> getMembers();

	/*
	 * Set all the members of the Bank
	 */
	public void setMembers(List<String> memberList);

	/*
	 * Add a new Bank member
	 */
	public void addMember(String newMember);

	/*
	 * Remove a Bank member
	 */
	public void removeMember(String oldMember);

	/*
	 * Determine if someone is a member of the Bank
	 */
	public boolean isMember(String memberName);

	/*
	 * Determine if someone the owner of the bank
	 */
	public boolean isOwner(String playerName);
}
