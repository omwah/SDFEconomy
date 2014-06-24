/*
 */

package sdfeconomy.storage.yaml;

import org.bukkit.configuration.ConfigurationSection;
import sdfeconomy.storage.BankAccount;

import java.util.List;

/**
 * Specialization of Account for Bank Accounts.
 */
public class YamlBankAccount extends YamlAccount implements BankAccount
{
	/*
	 * Create a new BankAccount
	 */
	public YamlBankAccount(ConfigurationSection section)
	{
		super(section);
	}

	@Override
	public String getOwner()
	{
		return section.getString("owner");
	}

	/*
	 * Sets the owner of the Bank
	 */
	@Override
	public void setOwner(String owner)
	{
		section.set("owner", owner.toLowerCase());
		setChanged();
		notifyObservers();
	}

	/*
	 * Get the members of the Bank
	 */
	@Override
	public List<String> getMembers()
	{
		return section.getStringList("members");
	}

	/*
	 * Set all the members of the Bank
	 */
	@Override
	public void setMembers(List<String> memberList)
	{
		section.set("members", memberList);
		setChanged();
		notifyObservers();
	}

	/*
	 * Add a new Bank member
	 */
	@Override
	public void addMember(String newMember)
	{
		List<String> members = getMembers();
		members.add(newMember.toLowerCase());
		setMembers(members);
		setChanged();
		notifyObservers();
	}

	/*
	 * Remove a Bank member
	 */
	@Override
	public void removeMember(String oldMember)
	{
		// List will use the .equals of String to compare
		List<String> members = getMembers();
		members.remove(oldMember.toLowerCase());
		setMembers(members);
		setChanged();
		notifyObservers();
	}

	/*
	 * Determine if someone is a member of the Bank
	 */
	@Override
	public boolean isMember(String memberName)
	{
		return getMembers().indexOf(memberName.toLowerCase()) >= 0;
	}

	/*
	 * Determine if someone the owner of the bank
	 */
	@Override
	public boolean isOwner(String playerName)
	{
		return getOwner().equalsIgnoreCase(playerName);
	}
}
