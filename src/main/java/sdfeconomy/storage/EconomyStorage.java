/*
 */

package sdfeconomy.storage;

import java.util.List;
import java.util.UUID;

/**
 * Interface for storage of Economy data
 */
public interface EconomyStorage
{
	/*
	 * Get the name of all player accounts in a section
	 */
	public List<String> getPlayerAccounts(String location);

	/*
	 * Does the player have an account in a given location
	 */
	boolean hasPlayerAccount(UUID uuid, String location);

	/*
	 * Get a Player account for a given location
	 */
	PlayerAccount getPlayerAccount(UUID uuid, String location);

	/*
	 * Create a new Player account in a given location with an initial balance
	 */
	PlayerAccount createPlayerAccount(UUID uuid, String location, double begBalance);

	/*
	 * Delete a new Player account in a given location
	 */
	void deletePlayerAccount(UUID uuid, String location);

	/*
	 * Get a list of all bank names
	 */
	List<String> getBankNames();

	/*
	 * Does the named bank account exist
	 */
	public boolean hasBankAccount(String name);

	/*
	 * Get a Bank account, account should be unique to a certain location
	 */
	BankAccount getBankAccount(String accountName);

	/*
	 * Create a new Bank account, Bank is tied to location during creation
	 */
	BankAccount createBankAccount(String accountName, UUID ownerUUID, String location, double begBalance);

	/*
	 * Create a new Bank account
	 */
	void deleteBankAccount(String accountName);

	/*
	 * Reload account data, purging any pending changes
	 */
	public void reload();

	/*
	 * Commits any unsaved changes to the underlying storage
	 */
	public void commit();
}
