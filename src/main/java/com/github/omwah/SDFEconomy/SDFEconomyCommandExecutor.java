package com.github.omwah.SDFEconomy;

import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.milkbowl.vault.permission.Permission;

import com.github.omwah.SDFEconomy.commands.CommandHandler;
import com.github.omwah.SDFEconomy.commands.HelpCommand;
import com.github.omwah.SDFEconomy.commands.BalanceCommand;
import com.github.omwah.SDFEconomy.commands.SetCommand;

/*
 * CommandExectuor that dispatches commands to CommandHandler classes
 */
public class SDFEconomyCommandExecutor implements CommandExecutor {
    private CommandHandler commandHandler;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public SDFEconomyCommandExecutor(Command cmd, Permission permission, SDFEconomyAPI api) {
        this.commandHandler = new CommandHandler(permission);
        
        // Add help commmand along with aliases to make it respond to command and
        // its aliases when no arguments are supplied
        HelpCommand help_cmd = new HelpCommand("SDFEconomy", this.commandHandler);
        help_cmd.addIdentifier(cmd.getName());
        for (Iterator alias_iter = cmd.getAliases().iterator(); alias_iter.hasNext();) {
            help_cmd.addIdentifier((String) alias_iter.next());
        }             
        
        this.commandHandler.addCommand(help_cmd);
        this.commandHandler.addCommand(new BalanceCommand(api, this.commandHandler));
        this.commandHandler.addCommand(new SetCommand(api, this.commandHandler));
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
