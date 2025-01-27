package main.elysiaforge.override;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class FormulaGuiHolder implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }
}
