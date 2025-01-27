package main.elysiaforge.command.subcommands;

import main.elysiaforge.ElysiaForge;
import org.bukkit.command.CommandSender;

public class OpenFormulaCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 3) {
            String playerName = args[1];
            String id = args[2];
            if (ElysiaForge.getFormulaManager().getFormulaDataList().contains(id))
                ElysiaForge.getGuiManager().openGui(id, playerName);
        }
    }
}
