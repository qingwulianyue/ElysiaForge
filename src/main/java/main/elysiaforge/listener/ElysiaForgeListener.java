package main.elysiaforge.listener;

import main.elysiaforge.ElysiaForge;
import main.elysiaforge.filemanager.data.FormulaData;
import main.elysiaforge.override.FormulaGuiHolder;
import main.elysiaforge.override.GroupGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

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
            ElysiaForge.getGuiManager().openGui(formulaData.getGroup(), player.getName(), 0);
        else if (slot == 22) {

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
            if (number == 1)
                return;
            ElysiaForge.getGuiManager().openGui(group, player.getName(), number - 1);
        }
        if (slot == 44){
            if (number == max)
                return;
            ElysiaForge.getGuiManager().openGui(group, player.getName(), number + 1);
        }
    }
}
