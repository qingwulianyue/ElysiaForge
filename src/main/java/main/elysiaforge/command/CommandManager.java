package main.elysiaforge.command;

import main.elysiaforge.command.subcommands.HelpCommand;
import main.elysiaforge.command.subcommands.OpenFormulaCommand;
import main.elysiaforge.command.subcommands.OpenGroupCommand;
import main.elysiaforge.command.subcommands.ReloadCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            new HelpCommand().execute(commandSender, strings);
            return true;
        }
        String subCommand = strings[0].toLowerCase();
        switch (subCommand) {
            case "openformula":
                new OpenFormulaCommand().execute(commandSender, strings);
                return true;
            case "reload":
                new ReloadCommand().execute(commandSender, strings);
                return true;
            case "opengroup":
                new OpenGroupCommand().execute(commandSender, strings);
                return true;
            default:
                new HelpCommand().execute(commandSender, strings);
                return true;
        }
    }
}
