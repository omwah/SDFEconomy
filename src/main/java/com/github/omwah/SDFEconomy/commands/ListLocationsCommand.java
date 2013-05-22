package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class ListLocationsCommand extends TranslatedCommand
{
    private final SDFEconomyAPI api;
    private final Server server;

    public ListLocationsCommand(SDFEconomyAPI api, Server server)
    {
        super("listlocations");
        
        this.api = api;
        this.server = server;
        
        setArgumentRange(0, 0);
        setIdentifiers(this.getName());
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // Get all unique location names based on worlds, store the list of worlds
        // associated with the locations
        Map<String, List<String>> location_names = new TreeMap<String, List<String>>();
        for(World world : server.getWorlds()) {
            String world_loc = api.getLocationTranslated(world.getSpawnLocation());
            if(!location_names.containsKey(world_loc)) {
                location_names.put(world_loc, new ArrayList<String>());
            }
            location_names.get(world_loc).add(world.getName());
        }
        
        // Display locations along with world names
        sender.sendMessage(getClassTranslation("banner"));
        for(String curr_loc_name : location_names.keySet()) {
            Joiner joiner = Joiner.on(", ");
            sender.sendMessage(getClassTranslation("location_line", curr_loc_name, joiner.join(location_names.get(curr_loc_name))));
        }
        return true;
    }
}
