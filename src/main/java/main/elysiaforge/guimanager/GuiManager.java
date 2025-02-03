package main.elysiaforge.guimanager;

import main.elysiaforge.ElysiaForge;
import main.elysiaforge.guimanager.gui.FormulaGui;
import main.elysiaforge.guimanager.gui.GroupGui;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Gui创建管理器
 */

public class GuiManager {
    private GuiManager() {}
    private final static GuiManager instance = new GuiManager();
    public static GuiManager getInstance() {
        return instance;
    }
    /**
     * 打开指定组对应页数的展示页面
     * @param id 组id
     * @param playerName 玩家名
     * @param page 页码
     */
    public void openGui(String id,String playerName,int page){
        new BukkitRunnable(){

            @Override
            public void run() {
                GroupGui groupGui = new GroupGui();
                groupGui.createFormulaGui(id, page);
                groupGui.openInventory(playerName);
            }
        }.runTaskAsynchronously(ElysiaForge.getInstance());
    }
    /**
     * 打开指定配方对应的锻造页面
     * @param id 配方id
     * @param playerName 玩家名
     */
    public void openGui(String id,String playerName){
        new BukkitRunnable(){
            @Override
            public void run() {
                FormulaGui formulaGui = new FormulaGui();
                formulaGui.createFormulaGui(id, Bukkit.getPlayer(playerName).getUniqueId());
                formulaGui.openInventory(playerName);
            }
        }.runTaskAsynchronously(ElysiaForge.getInstance());
    }
}
