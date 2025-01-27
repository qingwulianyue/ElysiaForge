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
        for (int i = 0; i < 45; i++){
            if ((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) ||i == 44)
                continue;
            inventory.setItem(i, glass);
        }
        inventory.setItem(44, back);
    }
    public void openInventory(String name){
        Bukkit.getPlayer(name).openInventory(inventory);
    }
}
