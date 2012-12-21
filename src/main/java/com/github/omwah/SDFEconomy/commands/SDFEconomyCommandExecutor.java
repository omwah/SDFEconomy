package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

/*
 * CommandExectuor that dispatches commands to CommandHandler classes
 */
public class SDFEconomyCommandExecutor implements CommandExecutor {
    private CommandHandler commandHandler;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public SDFEconomyCommandExecutor(Command cmd, Permission permission, SDFEconomyAPI api, Configuration config) {
        // Set up sub commands
        Map<String, PluginCommand> sub_commands = getSubCommands(api, config);

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

    private Map<String, PluginCommand> getSubCommands(SDFEconomyAPI api, Configuration config) {
        // Set up which subcommands of the main command are available
        ArrayList<PluginCommand> sub_cmd_list = new ArrayList<PluginCommand>();
        sub_cmd_list.add(new BalanceCommand(api));
        sub_cmd_list.add(new PayCommand(api));
        
        sub_cmd_list.add(new BankListCommand(api));
        sub_cmd_list.add(new BankInfoCommand(api));

        sub_cmd_list.add(new BankDepositCommand(api));
        sub_cmd_list.add(new BankWithdrawCommand(api));

        sub_cmd_list.add(new BankCreateCommand(api));
        sub_cmd_list.add(new BankRemoveCommand(api));
        sub_cmd_list.add(new BankRenameCommand(api));        
        
        sub_cmd_list.add(new BankAddMemberCommand(api));
        sub_cmd_list.add(new BankRemoveMemberCommand(api));

        config.addDefault("commands.topN", 5);
        sub_cmd_list.add(new TopAccountsCommand(api, config.getInt("commands.topN")));

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
