package sdfeconomy.commands;

import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import org.bukkit.command.CommandSender;
import sdfeconomy.SDFEconomyAPI;

import java.util.ResourceBundle;

public class ReloadCommand extends TranslatedCommand
{
	private final SDFEconomyAPI api;

	public ReloadCommand(SDFEconomyAPI api, ResourceBundle translation)
	{
		super("reload", translation);

		this.api = api;

		setArgumentRange(0, 0);
		setIdentifiers(this.getName());
		setPermission("sdfeconomy.admin");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
	{
		// CommandHandler will ensure correct permissions
		api.forceReload();
		sender.sendMessage(getClassTranslation("reload_success"));

		return true;
	}
}
