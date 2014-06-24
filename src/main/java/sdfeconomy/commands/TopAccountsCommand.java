package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sdfeconomy.SDFEconomyAPI;
import sdfeconomy.storage.BankAccount;

import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

public class TopAccountsCommand extends TranslatedCommand
{
	private final SDFEconomyAPI api;
	private final int topN;
	private final boolean includeBanks;

	/*
	 * Helper class for sorting players
	 */
	private class PlayerBalanceComparable implements Comparable
	{
		public String playerName;
		public double balance;

		public PlayerBalanceComparable(String playerName, double balance)
		{
			this.playerName = playerName;
			this.balance = balance;
		}

		public int compareTo(Object t)
		{
			if (!(t instanceof PlayerBalanceComparable))
			{
				throw new UnsupportedOperationException("Can only compare to other PlayerBalanceComparable objects");
			}
			// Return such that sort is in decreasing wealth
			return -(new Double(balance)).compareTo(((PlayerBalanceComparable) t).balance);
		}
	}

	public TopAccountsCommand(SDFEconomyAPI api, int topN, boolean includeBanks, ResourceBundle translation)
	{
		super("top", translation);

		this.api = api;
		this.topN = topN;
		this.includeBanks = includeBanks;

		setArgumentRange(0, 2);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.use_account");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		// Try and determine location
		String location_name = null;
		if (args.length > 0)
		{
			location_name = args[0];
		}

		// Use the API's last location for player if none supplied as argument
		if (location_name == null && sender != null && sender instanceof Player)
		{
			// This will not be executed if the player did not supply a location
			location_name = api.getPlayerLocationName(((Player) sender).getName());
		}

		// If arguments are supplied then check for another players wealth
		if (location_name == null)
		{
			sender.sendMessage(getClassTranslation("specify_location"));
			return false;
		}

		if (!api.validLocationName(location_name))
		{
			sender.sendMessage(getTranslation("AccountCommon-invalid_location", location_name));
			return false;
		}

		boolean show_balances = false;
		if (handler.hasAdminPermission(sender) || handler.hasPermission(sender, "sdfeconomy.see_top_balance"))
		{
			show_balances = true;
		}

		int top_count;
		if (args.length > 1 && handler.hasAdminPermission(sender))
		{
			try
			{
				top_count = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				// Quietly ignore the invalid number
				top_count = this.topN;
			}
		}
		else
		{
			top_count = this.topN;
		}

		// Loop over all players for a location
		// Gathering the top accounts as we go
		SortedSet<PlayerBalanceComparable> top_players = new TreeSet<PlayerBalanceComparable>();
		for (String player_name : api.getPlayers(location_name))
		{
			double wealth = api.getBalance(player_name, location_name);
			if (includeBanks)
			{
				List<BankAccount> player_banks = api.getPlayerBanks(player_name, location_name);
				for (BankAccount bank : player_banks)
				{
					wealth += bank.getBalance();
				}
			}
			top_players.add(new PlayerBalanceComparable(player_name, wealth));

			// Prune from the bottom if too many players
			if (top_players.size() > top_count)
			{
				top_players.remove(top_players.last());
			}
		}

		// Now output top players info
		sender.sendMessage(getClassTranslation("banner", top_count, location_name));
		for (PlayerBalanceComparable p : top_players)
		{
			if (show_balances)
			{
				sender.sendMessage(getClassTranslation("line_with_balance", p.playerName, api.format(p.balance)));
			}
			else
			{
				sender.sendMessage(getClassTranslation("line_no_balance", p.playerName));
			}
		}

		return true;
	}
}
