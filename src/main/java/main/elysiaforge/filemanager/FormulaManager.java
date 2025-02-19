package main.elysiaforge.filemanager;

import main.elysiaforge.ElysiaForge;
import main.elysiaforge.filemanager.data.FormulaData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormulaManager {
    private FormulaManager(){}
    private static final FormulaManager instance = new FormulaManager();
    private final HashMap<String, FormulaData> formulaDataHashMap = new HashMap<>();
    private final HashMap<String, List<String>> formulaDataGroupHashMap = new HashMap<>();
    private final ElysiaForge plugin = ElysiaForge.getInstance();
    public static FormulaManager getInstance(){
        return instance;
    }
    public FormulaData getFormulaData(String id){
        return formulaDataHashMap.get(id);
    }
    public List<String> getFormulaDataGroup(String group){
        return formulaDataGroupHashMap.get(group);
    }
    public List<String> getGroupList(){
        return new ArrayList<>(formulaDataGroupHashMap.keySet());
    }
    public List<String> getFormulaDataList(){
        return new ArrayList<>(formulaDataHashMap.keySet());
    }

    public void load(){
        findAllYmlFiles(new File(plugin.getDataFolder() + "/formula"));
    }
    private void findAllYmlFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹则递归查找
                    findAllYmlFiles(file);
                } else if (file.getName().endsWith(".yml")) {
                    // 如果是YML文件则加入结果列表
                    File formulaDataFolder = new File(folder + "/" + file.getName());
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(formulaDataFolder);
                    loadFormulaData(config);
                }
            }
        }
    }
    private void loadFormulaData(YamlConfiguration config) {
        for (String key : config.getKeys(false)){
            String group = config.getString(key + ".group");
            FormulaData formulaData = new FormulaData(
                    key,
                    group,
                    config.getString(key + ".produce"),
                    config.getInt(key + ".number"),
                    config.getBoolean(key + ".permission"),
                    config.getStringList(key + ".item"),
                    config.getInt(key + ".money")
            );
            formulaDataHashMap.put(key, formulaData);
            List<String> groupList = formulaDataGroupHashMap.get(group);
            if (groupList == null || groupList.isEmpty()) {
                groupList = new ArrayList<>();
                groupList.add(key);
            } else
                groupList.add(key);
            formulaDataGroupHashMap.put(group, groupList);
            logFormulaDataIfDebug(formulaData);
        }
    }
    private void logFormulaDataIfDebug(FormulaData formulaData){
        if (plugin.getConfig().getBoolean("debug")) {
            plugin.getLogger().info("加载配方数据: §a" + formulaData.getId());
            plugin.getLogger().info("§e  组: §a" + formulaData.getGroup());
            plugin.getLogger().info("§e  产物: §a" + formulaData.getProduce());
            plugin.getLogger().info("§e  数量: §a" + formulaData.getNumber());
            plugin.getLogger().info("§e  需要权限: §a" + formulaData.isPermission());
            plugin.getLogger().info("§e  物品: §a" + formulaData.getItem());
            plugin.getLogger().info("§e  金币: §a" + formulaData.getMoney());
        }
    }
}
