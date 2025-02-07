package main.elysiaforge;

import main.elysiaforge.command.CommandManager;
import main.elysiaforge.command.CommandTabComplete;
import main.elysiaforge.filemanager.ConfigManager;
import main.elysiaforge.filemanager.FileListener;
import main.elysiaforge.filemanager.FormulaManager;
import main.elysiaforge.guimanager.GuiManager;
import main.elysiaforge.listener.ElysiaForgeListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * @author elysia
 * @date 2024/1/28
 * @project 锻造插件
 **/

public final class ElysiaForge extends JavaPlugin {
    private static ElysiaForge instance;
    private static ConfigManager configManager;
    private static FormulaManager formulaManager;
    private static GuiManager guiManager;
    private static Economy economy;
    public static ElysiaForge getInstance() {
        return instance;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static FormulaManager getFormulaManager() {
        return formulaManager;
    }
    public static GuiManager getGuiManager() {
        return guiManager;
    }
    public static Economy getEconomy() {
        return economy;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configManager = ConfigManager.getInstance();
        formulaManager = FormulaManager.getInstance();
        guiManager = GuiManager.getInstance();
        createFile();
        configManager.loadConfig();
        formulaManager.load();
        Bukkit.getPluginCommand("ElysiaForge").setExecutor(new CommandManager());
        Bukkit.getPluginCommand("ElysiaForge").setTabCompleter(new CommandTabComplete());
        Bukkit.getPluginManager().registerEvents(new ElysiaForgeListener(), this);
        RegisteredServiceProvider< Economy > rsp = getServer().getServicesManager().getRegistration(Economy.class);
        economy = rsp.getProvider();
        FileListener.startWatching(getDataFolder());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    private void createFile() {
        saveDefaultConfig();
        createDefaultFile();
    }
    private void createDefaultFile(){
        saveDefaultConfig();
        Path formulaFolderPath = getDataFolder().toPath().resolve("formula");
        createDirectoryIfNotExists(formulaFolderPath);
        Path filePath = formulaFolderPath.resolve("测试配方.yml");
        if (!Files.exists(filePath)) {
            try (InputStream resourceStream = getResourceAsStream()) {
                Files.copy(resourceStream, filePath);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to create default file.", e);
            }
        }
    }
    private void createDirectoryIfNotExists(Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to create directory.", e);
            }
        }
    }
    private InputStream getResourceAsStream() {
        InputStream resourceStream = getClass().getResourceAsStream("/formula/测试配方.yml");
        if (resourceStream == null) {
            throw new RuntimeException("Resource '/formula/测试配方.yml' not found in classpath.");
        }
        return resourceStream;
    }
}
