package main.elysiaforge.guimanager;

import main.elysiaforge.guimanager.gui.FormulaGui;
import main.elysiaforge.guimanager.gui.GroupGui;

public class GuiManager {
    private GuiManager() {}
    private final static GuiManager instance = new GuiManager();
    public static GuiManager getInstance() {
        return instance;
    }
    public void openGui(String id,String playerName,int page){
        GroupGui groupGui = new GroupGui();
        groupGui.createFormulaGui(id, page);
        groupGui.openInventory(playerName);
    }
    public void openGui(String id,String playerName){
        FormulaGui formulaGui = new FormulaGui();
        formulaGui.createFormulaGui(id);
        formulaGui.openInventory(playerName);
    }
}
