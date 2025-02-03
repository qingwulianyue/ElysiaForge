package main.elysiaforge;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.inventory.ItemStack;

public class ProjectUtils {
    /**
     * 返回指定的MythicMobs物品
     * @param id  MythicMobs 物品ID
     **/
    public static ItemStack getMythicItem(String id){
        return MythicMobs.inst().getItemManager().getItemStack(id);
    }
    /**
     * 返回指定的玩家余额
     * @param playerName  玩家名
     **/
    public static double getPlayerMoney(String playerName){
        return new XConomyAPI().getPlayerData(playerName).getBalance().doubleValue();
    }
    /**
     * 设置指定的玩家余额
     * @param playerName  玩家名
     * @param money  X余额
     **/
    public static void setPlayerMoney(String playerName, double money){
        new XConomyAPI().getPlayerData(playerName).setBalance(new XConomyAPI().formatdouble(String.valueOf(money)));
    }
}
