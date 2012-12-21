package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import java.util.SortedSet;
import java.util.TreeSet;

public class TopAccountsCommand extends BasicCommand {
    private SDFEconomyAPI api;
    private int topN;
    
    /*
     * Helper class for sorting players
     */
    private class PlayerBalanceComparable implements Comparable {
        public String playerName;
        public double balance;
        public PlayerBalanceComparable(String playerName, double balance) {
            this.playerName = playerName;
            this.balance = balance;
        }
        public int compareTo(Object t) {
            if(!(t instanceof PlayerBalanceComparable)) {
                throw new UnsupportedOperationException("Can only compare to other PlayerBalanceComparable objects");
            }       
            // Return such that sort is in decreasing balance
            return -(new Double(balance)).compareTo(((PlayerBalanceComparable) t).balance);
        }        
    }
    
    public TopAccountsCommand(SDFEconomyAPI api, int topN) {
        super("top");
        
        this.api = api;
        this.topN = topN;
        
        setDescription("List top account holders for location");
        setUsage(this.getName() + " §8[location]");
        setArgumentRange(0, 1);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_account");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // Try and determine location
        String location_name = null;
        if(args.length > 0) {
            location_name = args[0];
        }
            
        // Use the API's last location for player if none supplied as argument
        if(location_name == null && sender != null && sender instanceof Player) {
            // This will not be executed if the player did not supply a location
            location_name = api.getPlayerLocationName(((Player) sender).getName());
        }
        
        // If arguments are supplied then check for another players balance
        if (location_name == null) {
            sender.sendMessage("Must specify location name when using this command from the console");
            return false;
        }
        
        if(!api.validLocationName(location_name)) {
            sender.sendMessage(location_name + " is not a valid location");
            return false;
        }

        boolean show_balances = false;
        if(handler.hasAdminPermission(sender) || handler.hasPermission(sender, "sdfeconomy.see_top_balance")) {
            show_balances = true;
        }

        // Loop over all players for a location
        // Gathering the topN accounts as we go
        SortedSet<PlayerBalanceComparable> top_players = new TreeSet<PlayerBalanceComparable>();
        for(String player_name : api.getPlayers(location_name)) {
            double balance = api.getBalance(player_name, location_name);
            top_players.add(new PlayerBalanceComparable(player_name, balance));

            // Prune from the bottom if too many players
            if(top_players.size() > this.topN) {
                top_players.remove(top_players.last());
            }
        }

        // Now output top players info
        sender.sendMessage("§c-----[ " + "§f Top " + this.topN + " Richest Players @ " + location_name + " §c ]-----");
        for(PlayerBalanceComparable p : top_players) {
            if(show_balances) {
                sender.sendMessage(p.playerName + " : " + api.format(p.balance));
            } else {
                sender.sendMessage(p.playerName);
            }
        }
        
        return true;
    }
}
