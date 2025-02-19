package main.elysiaforge.guimanager.gui;

import main.elysiaforge.ElysiaForge;
import main.elysiaforge.ProjectUtils;
import main.elysiaforge.filemanager.data.FormulaData;
import main.elysiaforge.override.GroupGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * 分组展示gui
 */
public class GroupGui {
    private Inventory inventory;
    public void createFormulaGui(String id, int page, String playerName){
        Player player = Bukkit.getPlayer(playerName);
        List<String> formulaDataList = ElysiaForge.getFormulaManager().getFormulaDataGroup(id);
        inventory = Bukkit.createInventory(new GroupGuiHolder(), 45, id);
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName("下一页");
        next.setItemMeta(nextMeta);
        ItemStack last = new ItemStack(Material.ARROW);
        ItemMeta lastMeta = last.getItemMeta();
        lastMeta.setDisplayName("上一页");
        last.setItemMeta(lastMeta);
        ItemStack pageItem = new ItemStack(Material.PAPER);
        ItemMeta pageMeta = pageItem.getItemMeta();
        pageMeta.setDisplayName("第" + page + "页");
        pageItem.setItemMeta(pageMeta);
        for (int i = 0; i < 45; i++){
            if ((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) || i == 44 || i == 40 || i == 36)
                continue;
            inventory.setItem(i, glass);
        }
        inventory.setItem(44, next);
        inventory.setItem(40, pageItem);
        inventory.setItem(36, last);
        for (int start = ( page - 1 ) * 21,i = 10;start < formulaDataList.size() && i <= 34;start++){
            FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(formulaDataList.get(start));
            if (formulaData.isPermission() && !player.hasPermission("elysiaforge." + formulaData.getId()))
                continue;
            ItemStack formulaItem = ProjectUtils.getMythicItem(formulaData.getProduce());
            inventory.setItem(i, formulaItem);
            if (i == 16)
                i = 19;
            else if (i == 25)
                i = 28;
            i++;
        }
    }
    public void openInventory(String name){
        Bukkit.getPlayer(name).openInventory(inventory);
    }
}
