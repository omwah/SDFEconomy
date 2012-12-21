package com.github.omwah.SDFEconomy.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

public class HelpCommand extends BasicCommand
{
    private String pluginName;
    private static final int CMDS_PER_PAGE = 8;

    public HelpCommand(String pluginName)
    {
        super("help");
        
        this.pluginName = pluginName;
        
        setDescription("Displays the help menu");
        setUsage(this.getName() + " §8[page#]");
        setArgumentRange(0, 1);
        
        // Respond to /<label> help
        // as well as any of the aliases for the plugin commands
        setIdentifiers(this.getName(), "?");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        int page = 0;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            }
            catch (NumberFormatException e) {
            }
        }

        List<PluginCommand> sortCommands = handler.getCommands();
        List<PluginCommand> commands = new ArrayList<PluginCommand>();

        // Build list of permitted commands
        for (PluginCommand command : sortCommands) {
            if (command.isShownOnHelpMenu()) {
                if (handler.hasPermission(sender, command.getPermission())) {
                    commands.add(command);
                }
            }
        }

        int numPages = commands.size() / CMDS_PER_PAGE;
        if (commands.size() % CMDS_PER_PAGE != 0) {
            numPages++;
        }

        if (page >= numPages || page < 0) {
            page = 0;
        }
        sender.sendMessage("§c-----[ " + "§f" + this.pluginName + " Help <" + (page + 1) + "/" + numPages + ">§c ]-----");
        int start = page * CMDS_PER_PAGE;
        int end = start + CMDS_PER_PAGE;
        if (end > commands.size()) {
            end = commands.size();
        }
        for (int c = start; c < end; c++) {
            PluginCommand cmd = commands.get(c);
            sender.sendMessage("  §a" + cmd.getUsage(label));
        }

        sender.sendMessage("§cFor more info on a particular command, type §f/" + label + " <command> ?");

        return true;
    }

}
