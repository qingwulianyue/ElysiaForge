package main.elysiaforge.command.subcommands;

import org.bukkit.command.CommandSender;

public class HelpCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage("/ElysiaForge help   -   获取帮助");
            sender.sendMessage("/ElysiaForge reload   -   重载插件");
            sender.sendMessage("/ElysiaForge openformula {player} {id}   -   为玩家开启对应配方的锻造页面");
            sender.sendMessage("/ElysiaForge opengroup {player} {group}   -   为玩家开启对应分组的展示页面");
        }
    }
}
