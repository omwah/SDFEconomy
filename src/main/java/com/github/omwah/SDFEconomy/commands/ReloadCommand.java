package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;

public class ReloadCommand extends BasicCommand
{
    private final SDFEconomyAPI api;

    public ReloadCommand(SDFEconomyAPI api)
    {
        super("reload");
        
        this.api = api;
        
        setDescription("Reload account data from storage, admin only");
        setUsage(this.getName());
        setArgumentRange(0, 0);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.admin");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // CommandHandler will ensure correct permissions
        api.forceReload();
        sender.sendMessage("Accounts data reloaded from storage");
           
        return true;
    }
}
