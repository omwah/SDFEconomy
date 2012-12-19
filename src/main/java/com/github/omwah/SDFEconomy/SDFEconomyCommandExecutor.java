package com.github.omwah.SDFEconomy;

import com.github.omwah.SDFEconomy.commands.CommandHandler;
import com.github.omwah.SDFEconomy.commands.PluginCommand;
import com.github.omwah.SDFEconomy.commands.HelpCommand;
import com.github.omwah.SDFEconomy.commands.BalanceCommand;
import com.github.omwah.SDFEconomy.commands.PayCommand;
import com.github.omwah.SDFEconomy.commands.BankCreateCommand;
import com.github.omwah.SDFEconomy.commands.BankListCommand;
import com.github.omwah.SDFEconomy.commands.ReloadCommand;
import com.github.omwah.SDFEconomy.commands.SetCommand;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/*
 * CommandExectuor that dispatches commands to CommandHandler classes
 */
public class SDFEconomyCommandExecutor implements CommandExecutor {
    private CommandHandler commandHandler;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public SDFEconomyCommandExecutor(Command cmd, Permission permission, SDFEconomyAPI api) {
        // Set up sub commands
        Map<String, PluginCommand> sub_commands = getSubCommands(api);

        this.commandHandler = new CommandHandler(permission, "sdfeconomy.admin");
        if (sub_commands.containsKey(cmd.getName())) {
            // Set up the command handler with the sole sub command
            // that has been promoted to a top level command
            this.commandHandler.addCommand((PluginCommand) sub_commands.get(cmd.getName()));
        } else {
            // Set up sub commands under a plugin top level command with a help command
            
            // Add help commmand along with aliases to make it respond to command and
            // its aliases when no arguments are supplied
            HelpCommand help_cmd = new HelpCommand("SDFEconomy");
            help_cmd.addIdentifier(cmd.getName());
            for (Iterator alias_iter = cmd.getAliases().iterator(); alias_iter.hasNext();) {
                help_cmd.addIdentifier((String) alias_iter.next());
            }             
            
            this.commandHandler.addCommand(help_cmd);
            for (Iterator sub_cmd_iter = sub_commands.values().iterator(); sub_cmd_iter.hasNext();) {
                this.commandHandler.addCommand((PluginCommand) sub_cmd_iter.next());
            }
        }
    }

    /*
     * Helper routine for defining and instantiating sub commands
     * Which may or may not be promoted to top level commands
     * based on what is present in plugin.yml
     */

    private Map<String, PluginCommand> getSubCommands(SDFEconomyAPI api) {
        // Set up which subcommands of the main command are available
        ArrayList<PluginCommand> sub_cmd_list = new ArrayList<PluginCommand>();
        sub_cmd_list.add(new BalanceCommand(api));
        sub_cmd_list.add(new PayCommand(api));
        
        sub_cmd_list.add(new BankListCommand(api));
        sub_cmd_list.add(new BankCreateCommand(api));

        sub_cmd_list.add(new ReloadCommand(api));
        sub_cmd_list.add(new SetCommand(api));
       
        // Use LinkedHashMap so values are in the order they were inserted
        Map<String, PluginCommand> sub_cmd_map = new LinkedHashMap<String, PluginCommand>();
        for (Iterator sub_cmd_iter = sub_cmd_list.iterator(); sub_cmd_iter.hasNext();) {
            PluginCommand sub_cmd = (PluginCommand) sub_cmd_iter.next();
            sub_cmd_map.put(sub_cmd.getName(), sub_cmd);
        }

        return sub_cmd_map;
    }

    /*
     * Dispatch commands through CommandHandler 
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandHandler.dispatch(sender, command, label, args);
    }

    /*
     * Returns the command handler
     */

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

}
