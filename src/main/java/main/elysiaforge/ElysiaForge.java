package main.elysiaforge;

import main.elysiaforge.command.CommandManager;
import main.elysiaforge.filemanager.ConfigManager;
import main.elysiaforge.filemanager.FormulaManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ElysiaForge extends JavaPlugin {
    private static ElysiaForge instance;
    private static ConfigManager configManager;
    private static FormulaManager formulaManager;
    public static ElysiaForge getInstance() {
        return instance;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static FormulaManager getFormulaManager() {
        return formulaManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configManager = ConfigManager.getInstance();
        formulaManager = FormulaManager.getInstance();
        createFile();
        configManager.loadConfig();
        formulaManager.load();
        Bukkit.getPluginCommand("ElysiaForge").setExecutor(new CommandManager());
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
