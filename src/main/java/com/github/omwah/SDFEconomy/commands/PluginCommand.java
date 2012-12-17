package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;

/*
 * Interface that this plugin's commmands implement. 
 * Named PluginCommand to avoid name clashing with org.bukkit.command.Command
 */

public interface PluginCommand {

    public void cancelInteraction(CommandSender executor);

    public boolean execute(CommandHandler handler, CommandSender executor, String label, String identifier, String[] args);

    public String getDescription();

    public String[] getIdentifiers();

    public int getMaxArguments();

    public int getMinArguments();

    public String getName();

    public String[] getNotes();

    public String getPermission();

    public String getUsage(String identifier);

    public boolean isIdentifier(CommandSender executor, String input);

    public boolean isInProgress(CommandSender executor);

    public boolean isInteractive();

    public boolean isShownOnHelpMenu();

}
