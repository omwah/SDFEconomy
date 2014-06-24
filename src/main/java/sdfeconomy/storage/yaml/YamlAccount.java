/*
 */

package sdfeconomy.storage.yaml;

import org.bukkit.configuration.ConfigurationSection;
import sdfeconomy.storage.PlayerAccount;

import java.util.Observable;

/**
 * Implementation of Account that utilizes the YamlFileConfiguration backend
 */
public abstract class YamlAccount extends Observable implements PlayerAccount
{
	protected final ConfigurationSection section;

	public YamlAccount(ConfigurationSection section)
	{
		this.section = section;
	}

	@Override
	public String getName()
	{
		return section.getName();
	}

	@Override
	public String getLocation()
	{
		return section.getString("location");
	}

	@Override
	public void setLocation(String location)
	{
		section.set("location", location.toLowerCase());
		setChanged();
		notifyObservers();
	}

	@Override
	public double getBalance()
	{
		synchronized (section)
		{
			return section.getDouble("balance");
		}
	}

	@Override
	public void setBalance(double amount)
	{
		synchronized (section)
		{
			section.set("balance", amount);
		}
		setChanged();
		notifyObservers();
	}

	@Override
	public double deposit(double amount)
	{
		synchronized (section)
		{
			double newBalance = section.getDouble("balance") + amount;
			section.set("balance", newBalance);
			setChanged();
			notifyObservers();
			return newBalance;
		}
	}

	@Override
	public double withdraw(double amount)
	{
		synchronized (section)
		{
			double newBalance = section.getDouble("balance") - amount;
			section.set("balance", newBalance);
			setChanged();
			notifyObservers();
			return newBalance;
		}
	}
}
