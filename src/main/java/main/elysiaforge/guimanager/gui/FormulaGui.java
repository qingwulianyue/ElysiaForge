package main.elysiaforge.guimanager.gui;

import main.elysiaforge.ElysiaForge;
import main.elysiaforge.filemanager.data.FormulaData;
import main.elysiaforge.override.FormulaGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FormulaGui {
    private Inventory inventory;
    public void createFormulaGui(String id){
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        inventory = Bukkit.createInventory(new FormulaGuiHolder(), 45 ,formulaData.getId());
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("返回");
        back.setItemMeta(backMeta);
        ItemStack start = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta startMeta = start.getItemMeta();
        startMeta.setDisplayName(ElysiaForge.getConfigManager().getConfigData().getTips().get("forge"));
        start.setItemMeta(startMeta);
        ItemStack money = new ItemStack(Material.GOLD_INGOT);
        ItemMeta moneyMeta = money.getItemMeta();
        moneyMeta.setDisplayName("所需花费:" + formulaData.getMoney());
        money.setItemMeta(moneyMeta);
        for (int i = 0; i < 45; i++){
            if ((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) ||i == 44)
                continue;
            inventory.setItem(i, glass);
        }
        inventory.setItem(44, back);
        inventory.setItem(22, start);
        inventory.setItem(31, money);
    }
    public void openInventory(String name){
        Bukkit.getPlayer(name).openInventory(inventory);
    }
}
