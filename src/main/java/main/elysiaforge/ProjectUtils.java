package main.elysiaforge;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.inventory.ItemStack;

public class ProjectUtils {
    public static boolean checkMythicItem(ItemStack itemStack, String id){
        return MythicMobs.inst().getItemManager().getItemStack(id).isSimilar(itemStack);
    }
    public static ItemStack getMythicItem(String id){
        return MythicMobs.inst().getItemManager().getItemStack(id);
    }
    public static double getPlayerMoney(String playerName){
        return new XConomyAPI().getPlayerData(playerName).getBalance().doubleValue();
    }
    public static void setPlayerMoney(String playerName, double money){
        new XConomyAPI().getPlayerData(playerName).setBalance(new XConomyAPI().formatdouble(String.valueOf(money)));
    }
}
