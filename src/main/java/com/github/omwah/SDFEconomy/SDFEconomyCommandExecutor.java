package com.github.omwah.SDFEconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.milkbowl.vault.permission.Permission;

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
    public SDFEconomyCommandExecutor(Permission permission, SDFEconomyAPI api) {
        this.commandHandler = new CommandHandler(permission);
        this.commandHandler.addCommand(new HelpCommand(this.commandHandler));
        this.commandHandler.addCommand(new BalanceCommand(api));
    }

    /*
     * Dispatch commands through CommandHandler 
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("sender: " + sender + ", command: " + command + ", args:" + args + " label: " + label);
        return commandHandler.dispatch(sender, command, label, args);
    }

    /*
     * Returns the command handler
     */

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

}
