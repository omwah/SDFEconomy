/*
 */

package sdfeconomy.storage.yaml;

import com.google.common.collect.Lists;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import sdfeconomy.storage.Account;
import sdfeconomy.storage.BankAccount;
import sdfeconomy.storage.EconomyStorage;
import sdfeconomy.storage.PlayerAccount;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Implements an Economy storage through a YAML file
 */
public class YamlStorage extends Observable implements EconomyStorage, Observer
{
	private final String player_prefix = "player_account";
	private final String bank_prefix = "bank_account";

	private final File accounts_file;
	YamlConfiguration storage;

	private static final Logger log = Logger.getLogger("Minecraft");

	public YamlStorage(File accounts_file)
	{
		this.accounts_file = accounts_file;
		this.storage = YamlConfiguration.loadConfiguration(accounts_file);
	}

	private ConfigurationSection getPlayerSection(UUID playerUUID, String location, boolean createIfMissing)
	{
		// Store and search for account names in lower case to make names case insensitive
		ConfigurationSection section = this.storage.getConfigurationSection(location.toLowerCase() + "." + player_prefix + "." + playerUUID.toString());
		if (section == null && createIfMissing)
		{
			section = this.storage.createSection(location.toLowerCase() + "." + player_prefix + "." + playerUUID.toString());
		}
		return section;
	}

	private ConfigurationSection getPlayerSection(UUID playerUUID, String location)
	{
		return getPlayerSection(playerUUID, location, false);
	}

	public List<String> getPlayerAccounts(String location)
	{
		return Lists.newArrayList(storage.getConfigurationSection(location.toLowerCase() + "." + player_prefix).getKeys(false));
	}

	public boolean hasPlayerAccount(UUID playerUUID, String location)
	{
		return getPlayerSection(playerUUID, location) != null;
	}

	public PlayerAccount getPlayerAccount(UUID playerUUID, String location)
	{
		if (!hasPlayerAccount(playerUUID, location))
		{
			log.severe("Player " + playerUUID + " @ " + location + " does not exist.");
			return null;
		}
		else
		{
			ConfigurationSection section = getPlayerSection(playerUUID, location);
			YamlPlayerAccount account = new YamlPlayerAccount(section);
			account.addObserver(this);
			return account;
		}
	}

	public PlayerAccount createPlayerAccount(UUID playerUUID, String location, double begBalance)
	{
		ConfigurationSection section = getPlayerSection(playerUUID, location, true);
		YamlPlayerAccount newAccount = new YamlPlayerAccount(section);
		newAccount.setBalance(begBalance);
		newAccount.setLocation(location);
		newAccount.addObserver(this);
		setChanged();
		notifyObservers();
		return newAccount;
	}

	public void deletePlayerAccount(UUID playerUUID, String location)
	{
		if (hasPlayerAccount(playerUUID, location))
		{
			this.storage.set(location.toLowerCase() + "." + this.player_prefix + "." + playerUUID.toString(), null);

			// Notify observers that accounts have changed
			setChanged();
			notifyObservers();
		}
	}

	public List<String> getBankNames()
	{
		ConfigurationSection bank_section = this.storage.getConfigurationSection(this.bank_prefix);
		if (bank_section != null)
		{
			Set<String> namesSet = bank_section.getKeys(false);
			List<String> nameList = new ArrayList<String>();
			nameList.addAll(namesSet);
			return nameList;
		}
		else
		{
			return Collections.<String>emptyList();
		}
	}

	private ConfigurationSection getBankSection(String accountName, boolean createIfMissing)
	{
		// Store and search for account names in lower case to make names case insensitive
		ConfigurationSection section = storage.getConfigurationSection(bank_prefix + "." + accountName.toLowerCase());
		if (section == null && createIfMissing)
		{
			section = storage.createSection(bank_prefix + "." + accountName.toLowerCase());
		}
		return section;
	}

	private ConfigurationSection getBankSection(String accountName)
	{
		return getBankSection(accountName, false);
	}

	public boolean hasBankAccount(String accountName)
	{
		if (accountName == null)
		{
			return false;
		}
		else
		{
			ConfigurationSection section = getBankSection(accountName);
			return section != null;
		}
	}

	public BankAccount getBankAccount(String accountName)
	{
		ConfigurationSection section = getBankSection(accountName);
		if (section == null)
		{
			log.severe("Bank account " + accountName + " does not exist");
			return null;
		}
		YamlBankAccount account = new YamlBankAccount(section);
		account.addObserver(this);
		return account;
	}

	public BankAccount createBankAccount(String accountName, UUID owner, String location, double begBalance)
	{
		if (hasBankAccount(accountName))
		{
			log.severe("Bank account " + accountName + " already exists");
			return null;
		}
		ConfigurationSection section = getBankSection(accountName, true);
		YamlBankAccount account = new YamlBankAccount(section);
		account.setBalance(begBalance);
		account.setLocation(location);
		account.setOwner(owner);
		account.addObserver(this);
		setChanged();
		notifyObservers();
		return account;
	}

	public void deleteBankAccount(String accountName)
	{
		storage.set(bank_prefix + "." + accountName, null);
		// Notify observers that accounts have changed
		setChanged();
		notifyObservers();
	}

	public void update(Observable o, Object arg)
	{
		if (o instanceof Account)
		{
			// Notify observers that accounts have changed
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public synchronized void reload()
	{
		this.storage = YamlConfiguration.loadConfiguration(this.accounts_file);
	}

	@Override
	public synchronized void commit()
	{
		try
		{
			storage.save(this.accounts_file);
		}
		catch (IOException e)
		{
			log.severe("Error saving YamlStorage to: " + this.accounts_file.getPath() + "\n" + e);
		}
	}

	/*
	 * Intended to be used for debug purposes only
	 */
	@Override
	public String toString()
	{
		return this.storage.saveToString();
	}
}
