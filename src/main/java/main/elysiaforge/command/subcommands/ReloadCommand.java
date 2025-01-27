package main.elysiaforge.command.subcommands;

import main.elysiaforge.ElysiaForge;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            ElysiaForge.getConfigManager().loadConfig();
            ElysiaForge.getFormulaManager().load();
            sender.sendMessage("重载完成");
        }
    }
}
