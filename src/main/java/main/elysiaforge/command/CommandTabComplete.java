package main.elysiaforge.command;

import main.elysiaforge.ElysiaForge;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> subCommands = new ArrayList<>();
        if (strings.length == 1) {
            if (strings[0].startsWith("openf"))
                subCommands.add("openformula");
            else if (strings[0].startsWith("openg"))
                subCommands.add("opengroup");
            else if (strings[0].startsWith("o")){
                subCommands.add("openformula");
                subCommands.add("opengroup");
            }
            else {
                subCommands.add("help");
                subCommands.add("reload");
                subCommands.add("openformula");
                subCommands.add("opengroup");
            }
        }
        else if (strings.length == 2){
            if (strings[0].equalsIgnoreCase("openformula")) {
                for (Player player : Bukkit.getOnlinePlayers())
                    subCommands.add(player.getName());
            }
            else if (strings[0].equalsIgnoreCase("opengroup")) {
                for (Player player : Bukkit.getOnlinePlayers())
                    subCommands.add(player.getName());
            }
        }
        else if (strings.length == 3){
            if (strings[0].equalsIgnoreCase("openformula")) {
                subCommands.addAll(ElysiaForge.getFormulaManager().getFormulaDataList());
            }
            else if (strings[0].equalsIgnoreCase("opengroup")) {
                subCommands.addAll(ElysiaForge.getFormulaManager().getGroupList());
            }
        }
        return subCommands;
    }
}
