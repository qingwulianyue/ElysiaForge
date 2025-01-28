package main.elysiaforge.listener;

import main.elysiaforge.ElysiaForge;
import main.elysiaforge.ProjectUtils;
import main.elysiaforge.filemanager.data.FormulaData;
import main.elysiaforge.override.FormulaGuiHolder;
import main.elysiaforge.override.GroupGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElysiaForgeListener implements Listener {
    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof FormulaGuiHolder) && !(inventory.getHolder() instanceof GroupGuiHolder)) return;
        event.setCancelled(true);
        if (inventory.getHolder() instanceof FormulaGuiHolder) onPlayerClickFormulaGui(inventory, event.getWhoClicked().getUniqueId(), event.getSlot());
        if (inventory.getHolder() instanceof GroupGuiHolder) onPlayerClickGroupGui(inventory, event.getWhoClicked().getUniqueId(), event.getSlot());
    }
    private void onPlayerClickFormulaGui(Inventory inventory, UUID uuid, int slot){
        String id = inventory.getTitle();
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        Player player = Bukkit.getPlayer(uuid);
        if (slot == 44)
            ElysiaForge.getGuiManager().openGui(formulaData.getGroup(), player.getName(), 1);
        else if (slot == 22) {
            onPlayerStartForge(uuid, id);
        }
    }
    private void onPlayerClickGroupGui(Inventory inventory, UUID uuid, int slot){
        String group = inventory.getTitle();
        Player player = Bukkit.getPlayer(uuid);
        List<String> formulaDataList = ElysiaForge.getFormulaManager().getFormulaDataGroup(group);
        int max = formulaDataList.size() / 21 + 1;
        String page = inventory.getItem(40).getItemMeta().getDisplayName();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(page);
        int number = 1;
        if (matcher.find())
            number = Integer.parseInt(matcher.group());
        if (slot == 36){
            if (number == 1) return;
            ElysiaForge.getGuiManager().openGui(group, player.getName(), number - 1);
        }
        if (slot == 44){
            if (number == max) return;
            ElysiaForge.getGuiManager().openGui(group, player.getName(), number + 1);
        }
        if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34))
            onPlayerClickGroupFormula(uuid, inventory.getItem(slot).getItemMeta().getDisplayName());
    }
    private void onPlayerClickGroupFormula(UUID uuid, String id){
        Player player = Bukkit.getPlayer(uuid);
        ElysiaForge.getGuiManager().openGui(id, player.getName());
    }
    private void onPlayerStartForge(UUID uuid, String id){
        Player player = Bukkit.getPlayer(uuid);
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        if (ProjectUtils.getPlayerMoney(player.getName()) < formulaData.getMoney()){
            player.sendMessage(
                    ElysiaForge.getConfigManager().getConfigData().getPrefix() +
                            ElysiaForge.getConfigManager().getConfigData().getMessages().get("no_money")
            );
            return;
        }
        List<String> itemList = formulaData.getItem();
        for (String item : itemList) {
            String[] itemData = item.split(" ");
            ItemStack itemStack = ProjectUtils.getMythicItem(itemData[0]);
            String displayName = itemStack.getItemMeta().getDisplayName();
            int amount = 0;
            for (ItemStack inventoryItem : player.getInventory().getContents()){
                if (inventoryItem == null || !inventoryItem.hasItemMeta() || inventoryItem.getType() != itemStack.getType()) continue;
                if (inventoryItem.getItemMeta().getDisplayName().equals(displayName))
                    amount += inventoryItem.getAmount();
            }
            if (amount < Integer.parseInt(itemData[1])){
                player.sendMessage(
                        ElysiaForge.getConfigManager().getConfigData().getPrefix() +
                                ElysiaForge.getConfigManager().getConfigData().getMessages().get("no_item")
                );
                return;
            }
        }
        onPlayerForgeSuccess(uuid, id);
    }
    private void onPlayerForgeSuccess(UUID uuid, String id){
        System.out.println("onPlayerForgeSuccess");
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        Player player = Bukkit.getPlayer(uuid);
        Inventory inventory = player.getInventory();
        ProjectUtils.setPlayerMoney(player.getName(), ProjectUtils.getPlayerMoney(player.getName()) - formulaData.getMoney());
        List<String> itemList = formulaData.getItem();
        for (String item : itemList) {
            String[] itemData = item.split(" ");
            System.out.println(Arrays.toString(itemData));
            ItemStack itemStack = ProjectUtils.getMythicItem(itemData[0]);
            String displayName = itemStack.getItemMeta().getDisplayName();
            System.out.println(displayName);
            int amount = Integer.parseInt(itemData[1]);
            for (ItemStack inventoryItem : inventory.getContents()){
                if (inventoryItem == null || !inventoryItem.hasItemMeta() || inventoryItem.getType() != itemStack.getType()) continue;
                if (inventoryItem.getItemMeta().getDisplayName().equals(displayName)){
                    int number = inventoryItem.getAmount();
                    inventoryItem.setAmount(Math.max(0, inventoryItem.getAmount() - amount));
                    amount -= number;
                    if (amount <= 0)
                        break;
                }
            }
        }
        ItemStack produce = ProjectUtils.getMythicItem(formulaData.getProduce());
        produce.setAmount(formulaData.getNumber());
        inventory.addItem(produce);
        System.out.println(produce);
        player.sendMessage(
                ElysiaForge.getConfigManager().getConfigData().getPrefix() +
                        ElysiaForge.getConfigManager().getConfigData().getMessages().get("forge_success")
                        .replace("%name%", formulaData.getProduce())
                        .replace("%number%", String.valueOf(formulaData.getNumber()))
        );
    }
}
