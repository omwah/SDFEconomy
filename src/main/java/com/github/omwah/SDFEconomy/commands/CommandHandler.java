/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.github.omwah.SDFEconomy.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler
{

    private final Permission permission;
    protected Map<String, PluginCommand> commands;
    
    String admin_perm_string = null;

    public CommandHandler(Permission permission)
    {
        this.permission = permission;
        this.commands = new LinkedHashMap<String, PluginCommand>();
    }
    
    public CommandHandler(Permission permission, String adminPermString)
    {
        this(permission);
        this.admin_perm_string = adminPermString;
    }

    public void addCommand(PluginCommand command)
    {
        commands.put(command.getName().toLowerCase(), command);
    }

    public void removeCommand(PluginCommand command)
    {
        commands.remove(command.getName().toLowerCase());
    }

    public PluginCommand getCommand(String name)
    {
        return commands.get(name.toLowerCase());
    }

    public List<PluginCommand> getCommands()
    {
        return new ArrayList<PluginCommand>(commands.values());
    }

    public boolean dispatch(CommandSender sender, Command command, String label, String[] arguments)
    {

        for (int argsIncluded = arguments.length; argsIncluded >= 0; argsIncluded--) {
            // Build a string as a possible identifier of the command
            // First use all available arguments as identifers
            // If none of the arguments are matched as identifiers
            // use the command name
            String identifier;
            if(argsIncluded == 0) {
                identifier = command.getName();
            } else {
                StringBuilder identifierBuilder = new StringBuilder();
                for (int i = 0; i < argsIncluded; i++) {
                    identifierBuilder.append(' ').append(arguments[i]);
                }

                identifier = identifierBuilder.toString().trim();
            }

            for (PluginCommand cmd : commands.values()) {
                if (cmd.isIdentifier(sender, identifier)) {
                    // Arguments are the parts of arguments that
                    // were not part of the identifier
                    String[] realArgs = Arrays.copyOfRange(arguments, argsIncluded, arguments.length);

                    if (!cmd.isInProgress(sender)) {
                        // Display help if the wrong number of arguments
                        // or ? is the sole argument
                        if (realArgs.length < cmd.getMinArguments() || realArgs.length > cmd.getMaxArguments()) {
                            displayCommandHelp(label, cmd, sender);
                            return true;
                        } else if (realArgs.length > 0 && "?".equals(realArgs[0])) {
                            displayCommandHelp(label, cmd, sender);
                            return true;
                        }
                    }

                    // Ensure sender has the proper permissions
                    if (!hasPermission(sender, cmd.getPermission())) {
                        sender.sendMessage("Insufficient permission.");
                        return true;
                    }

                    // Finally call the PluginCommand's execute routine
                    cmd.execute(this, sender, label, identifier, realArgs);
                    return true;
                }
            }
        }

        return true;
    }

    private void displayCommandHelp(String label, PluginCommand cmd, CommandSender sender)
    {
        sender.sendMessage("§cCommand:§e " + cmd.getName());
        sender.sendMessage("§cDescription:§e " + cmd.getDescription());
        sender.sendMessage("§cUsage:§e " + cmd.getUsage(label));
        if (cmd.getNotes() != null) {
            for (String note : cmd.getNotes()) {
                sender.sendMessage("§e" + note);
            }
        }
    }

    public boolean hasPermission(CommandSender sender, String permString)
    {
        if (!(sender instanceof Player) || permString == null || permString.isEmpty()) {
            return true;
        }

        Player player = (Player) sender;
        if (permission != null) {
            return permission.has(player, permString);
        }
        return player.hasPermission(permString);
    }
    
    public boolean hasAdminPermission(CommandSender sender) {
        if (sender == null ||
                (sender instanceof Player && ((Player)sender).isOp()) || 
                (admin_perm_string != null && hasPermission(sender, admin_perm_string))) {
            return true;
        } else {
            return false;
        }
    }
}
