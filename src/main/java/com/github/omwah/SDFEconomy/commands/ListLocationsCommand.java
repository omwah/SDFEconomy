package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class ListLocationsCommand extends BasicCommand
{
    private final SDFEconomyAPI api;
    private final Server server;

    public ListLocationsCommand(SDFEconomyAPI api, Server server)
    {
        super("listlocations");
        
        this.api = api;
        this.server = server;
        
        setDescription("List all valid economy location names");
        setUsage(this.getName());
        setArgumentRange(0, 0);
        setIdentifiers(this.getName());
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // Get all unique location names based on worlds
        Set<String> location_names = new TreeSet<String>();
        for(World world : server.getWorlds()) {
            location_names.add(api.getLocationTranslated(world.getSpawnLocation()));
        }
        
        // Display world names
        sender.sendMessage("§c-----[ " + "§f Economy Location Names §c ]-----");
        for(String curr_name : location_names) {
            sender.sendMessage(curr_name);
        }
        return true;
    }
}
