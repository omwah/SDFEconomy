/*
 */

package sdfeconomy.storage;

import java.util.List;
import java.util.UUID;

/**
 * Specialization of Account for Bank Accounts.
 */
public interface BankAccount extends Account
{
	/*
	 * Get the owner of the Bank
	 */
	public UUID getOwner();

	/*
	 * Sets the owner of the Bank
	 */
	public void setOwner(UUID owner);

	/*
	 * Get the members of the Bank
	 */
	public List<UUID> getMembers();

	/*
	 * Set all the members of the Bank
	 */
	public void setMembers(List<UUID> memberList);

	/*
	 * Add a new Bank member
	 */
	public void addMember(UUID newMember);

	/*
	 * Remove a Bank member
	 */
	public void removeMember(UUID oldMember);

	/*
	 * Determine if someone is a member of the Bank
	 */
	public boolean isMember(UUID memberUUID);

	/*
	 * Determine if someone the owner of the bank
	 */
	public boolean isOwner(UUID playerUUID);
}
