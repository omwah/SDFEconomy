package com.github.omwah.SDFEconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.omwah.SDFEconomy.commands.CommandHandler;
import com.github.omwah.SDFEconomy.commands.HelpCommand;
import com.github.omwah.SDFEconomy.commands.BalanceCommand;


/*
 * CommandExectuor that dispatches commands to CommandHandler classes
 */
public class SDFEconomyCommandExecutor implements CommandExecutor {
    private CommandHandler commandHandler;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public SDFEconomyCommandExecutor(SDFEconomy plugin, CommandHandler handler) {
        this.commandHandler = handler;
        this.commandHandler.addCommand(new HelpCommand(plugin));
        this.commandHandler.addCommand(new BalanceCommand(plugin));
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
