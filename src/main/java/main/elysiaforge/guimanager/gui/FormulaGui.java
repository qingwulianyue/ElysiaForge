package main.elysiaforge.guimanager.gui;

import main.elysiaforge.ElysiaForge;
import main.elysiaforge.ProjectUtils;
import main.elysiaforge.filemanager.data.FormulaData;
import main.elysiaforge.override.FormulaGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 锻造配方gui
 */

public class FormulaGui {
    private Inventory inventory;
    public void createFormulaGui(String id, UUID uuid){
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
        ItemStack produce = ProjectUtils.getMythicItem(formulaData.getProduce());
        for (int i = 0; i < 45; i++){
            if ((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) ||i == 44)
                continue;
            inventory.setItem(i, glass);
        }
        inventory.setItem(44, back);
        inventory.setItem(22, start);
        inventory.setItem(31, money);
        inventory.setItem(20, produce);
        int i = 14;
        for (String item : formulaData.getItem()){
            String [] itemData = item.split(" ");
            ItemStack itemStack = ProjectUtils.getMythicItem(itemData[0]);
            itemStack.setAmount(Integer.parseInt(itemData[1]));
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore;
            if (itemMeta.hasLore())
                lore = itemMeta.getLore();
            else
                lore = new ArrayList<>();

            lore.add(ElysiaForge.getConfigManager().getConfigData().getTips()
                    .get("number")
                    .replaceAll("%need%", itemData[1])
                    .replaceAll("%have%", String.valueOf(getPlayerItemAmount(uuid, itemData[0]))));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i, itemStack);
            if (i == 16) i = 23;
            else if(i == 25) i = 32;
            else if(i == 34)break;
            i++;
        }
    }
    public void openInventory(String name){
        Bukkit.getPlayer(name).openInventory(inventory);
    }
    private int getPlayerItemAmount(UUID uuid, String id){
        Player player = Bukkit.getPlayer(uuid);
        ItemStack itemStack = ProjectUtils.getMythicItem(id);
        String displayName = itemStack.getItemMeta().getDisplayName();
        Inventory inventory = player.getInventory();
        int amount = 0;
        for (ItemStack item : inventory.getContents()){
            if (item == null || item.getType() != itemStack.getType()) continue;
            if (item.getItemMeta().getDisplayName().equals(displayName))
                amount += item.getAmount();
        }
        return amount;
    }
}
