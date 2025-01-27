package main.elysiaforge.command.subcommands;

import main.elysiaforge.ElysiaForge;
import org.bukkit.command.CommandSender;

public class OpenGroupCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 4) {
            String playerName = args[1];
            String id = args[2];
            int page = Integer.parseInt(args[3]);
            if (ElysiaForge.getFormulaManager().getGroupList().contains(id))
                ElysiaForge.getGuiManager().openGui(id, playerName, page);
        }
    }
}
