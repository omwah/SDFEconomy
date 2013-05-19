package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends TranslatedCommand
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
