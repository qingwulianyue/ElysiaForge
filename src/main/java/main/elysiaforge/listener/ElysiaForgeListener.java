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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 玩家容器交互事件管理
 */

public class ElysiaForgeListener implements Listener {
    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof FormulaGuiHolder) && !(inventory.getHolder() instanceof GroupGuiHolder)) return;
        event.setCancelled(true);
        if (inventory.getHolder() instanceof FormulaGuiHolder) onPlayerClickFormulaGui(inventory, event.getWhoClicked().getUniqueId(), event.getSlot());
        if (inventory.getHolder() instanceof GroupGuiHolder) onPlayerClickGroupGui(inventory, event.getWhoClicked().getUniqueId(), event.getSlot());
    }

    /**
     * 玩家点击配方锻造页面
     * @param inventory 玩家点击的容器
     * @param uuid 玩家uuid
     * @param slot 玩家点击的槽位
     */
    private void onPlayerClickFormulaGui(Inventory inventory, UUID uuid, int slot){
        //获取当前配方id
        String id = inventory.getTitle();
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        Player player = Bukkit.getPlayer(uuid);
        if (slot == 44)
            //当玩家点击返回时，打开对应组的显示页面
            ElysiaForge.getGuiManager().openGui(formulaData.getGroup(), player.getName(), 1);
        else if (slot == 22) {
            //当玩家点击锻造时
            onPlayerStartForge(uuid, id);
        }
    }
    /**
     * 玩家点击配方组页面
     * @param inventory 玩家点击的容器
     * @param uuid 玩家uuid
     * @param slot 玩家点击的槽位
     */
    private void onPlayerClickGroupGui(Inventory inventory, UUID uuid, int slot){
        //获取当前展示组
        String group = inventory.getTitle();
        Player player = Bukkit.getPlayer(uuid);
        //获取当前组下的所有配方的id
        List<String> formulaDataList = ElysiaForge.getFormulaManager().getFormulaDataGroup(group);
        //计算本组的最大页数
        int max = formulaDataList.size() / 21 + 1;
        //获取当前页码
        String page = inventory.getItem(40).getItemMeta().getDisplayName();
        //正则匹配获取页码数字
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(page);
        int number = 1;
        if (matcher.find())
            number = Integer.parseInt(matcher.group());
        //当玩家点击上一页时，判断当前页码是否为第一页，是则返回，否则打开上一页
        if (slot == 36){
            if (number == 1) return;
            ElysiaForge.getGuiManager().openGui(group, player.getName(), number - 1);
        }
        //当玩家点击下一页时，判断当前页码是否为最后一页，是则返回，否则打开下一页
        if (slot == 44){
            if (number == max) return;
            ElysiaForge.getGuiManager().openGui(group, player.getName(), number + 1);
        }
        //当玩家点击展示配方范围时
        if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34))
            onPlayerClickGroupFormula(uuid, inventory.getItem(slot).getItemMeta().getDisplayName());
    }
    /**
     * 玩家点击配方组页面中的配方
     * @param uuid 玩家uuid
     * @param id 配方id
     */
    private void onPlayerClickGroupFormula(UUID uuid, String id){
        Player player = Bukkit.getPlayer(uuid);
        ElysiaForge.getGuiManager().openGui(id, player.getName());
    }
    /**
     * 玩家点击开始锻造按钮
     * @param uuid 玩家uuid
     * @param id 配方id
     */
    private void onPlayerStartForge(UUID uuid, String id){
        Player player = Bukkit.getPlayer(uuid);
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        //判断玩家是否有足够的钱
        if (ProjectUtils.getPlayerMoney(player.getName()) < formulaData.getMoney()){
            player.sendMessage(
                    ElysiaForge.getConfigManager().getConfigData().getPrefix() +
                            ElysiaForge.getConfigManager().getConfigData().getMessages().get("no_money")
            );
            return;
        }
        List<String> itemList = formulaData.getItem();
        //判断玩家是否有足够的物品，遍历配方所需的物品列表，异步运行
        new BukkitRunnable(){
            @Override
            public void run() {
                for (String item : itemList) {
                    String[] itemData = item.split(" ");
                    ItemStack itemStack = ProjectUtils.getMythicItem(itemData[0]);
                    String displayName = itemStack.getItemMeta().getDisplayName();
                    int amount = 0;
                    //遍历玩家背包
                    for (ItemStack inventoryItem : player.getInventory().getContents()){
                        if (inventoryItem == null || !inventoryItem.hasItemMeta() || inventoryItem.getType() != itemStack.getType()) continue;
                        if (inventoryItem.getItemMeta().getDisplayName().equals(displayName))
                            //当该物品显示名满足条件，则累加数量
                            amount += inventoryItem.getAmount();
                    }
                    //当该物品累计数量少于所需数量则锻造失败
                    if (amount < Integer.parseInt(itemData[1])){
                        player.sendMessage(
                                ElysiaForge.getConfigManager().getConfigData().getPrefix() +
                                        ElysiaForge.getConfigManager().getConfigData().getMessages().get("no_item")
                        );
                        return;
                    }
                }
                //玩家通过了全部的检定则进入锻造成功步骤
                onPlayerForgeSuccess(uuid, id);
            }
        }.runTaskAsynchronously(ElysiaForge.getInstance());
    }

    /**
     * 玩家完成锻造
     * @param uuid 玩家uuid
     * @param id 配方id
     */
    private void onPlayerForgeSuccess(UUID uuid, String id){
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        Player player = Bukkit.getPlayer(uuid);
        Inventory inventory = player.getInventory();
        //修改玩家余额
        ProjectUtils.setPlayerMoney(player.getName(), ProjectUtils.getPlayerMoney(player.getName()) - formulaData.getMoney());
        List<String> itemList = formulaData.getItem();
        //遍历锻造所需物品列表，异步运行
        new BukkitRunnable(){

            @Override
            public void run() {
                for (String item : itemList) {
                    String[] itemData = item.split(" ");
                    ItemStack itemStack = ProjectUtils.getMythicItem(itemData[0]);
                    String displayName = itemStack.getItemMeta().getDisplayName();
                    int amount = Integer.parseInt(itemData[1]);
                    //遍历玩家背包
                    for (ItemStack inventoryItem : inventory.getContents()){
                        if (inventoryItem == null || !inventoryItem.hasItemMeta() || inventoryItem.getType() != itemStack.getType()) continue;
                        if (inventoryItem.getItemMeta().getDisplayName().equals(displayName)){
                            int number = inventoryItem.getAmount();
                            //当该物品显示名满足条件，则扣除所需数量，最少扣至0
                            inventoryItem.setAmount(Math.max(0, inventoryItem.getAmount() - amount));
                            //扣除剩余所需数量
                            amount -= number;
                            //当剩余所需数量为0，则跳出循环
                            if (amount <= 0)
                                break;
                        }
                    }
                }
                updateGuiItemLore(uuid, id);
            }
        }.runTaskAsynchronously(ElysiaForge.getInstance());
        //给予锻造物品
        ItemStack produce = ProjectUtils.getMythicItem(formulaData.getProduce());
        produce.setAmount(formulaData.getNumber());
        inventory.addItem(produce);
        player.sendMessage(
                ElysiaForge.getConfigManager().getConfigData().getPrefix() +
                        ElysiaForge.getConfigManager().getConfigData().getMessages().get("forge_success")
                        .replace("%name%", formulaData.getProduce())
                        .replace("%number%", String.valueOf(formulaData.getNumber()))
        );
    }

    /**
     * 更新页面显示
     * @param uuid 玩家uuid
     * @param id 配方id
     */
    private void updateGuiItemLore(UUID uuid, String id){
        FormulaData formulaData = ElysiaForge.getFormulaManager().getFormulaData(id);
        Inventory inventory = Bukkit.getPlayer(uuid).getOpenInventory().getTopInventory();
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
