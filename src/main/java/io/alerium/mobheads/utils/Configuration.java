package io.alerium.mobheads.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Configuration {

    private final Plugin plugin;
    private final File file;

    @Getter private FileConfiguration config;

    public Configuration(Plugin plugin, String name) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name + ".yml");

        if (!file.exists())
            plugin.saveResource(file.getName(), false);

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while saving the config file.");
        }
    }

    public Component getMessage(String path, String... placeholders) {
        return parsePlaceholders(config.getString(path), placeholders);
    }

    public List<Component> getMessageList(String path, String... placeholders) {
        return config.getStringList(path).stream().map(s -> parsePlaceholders(s, placeholders)).collect(Collectors.toList());
    }

    private Component parsePlaceholders(String s, String... placeholders) {
        for (int i = 0; i < placeholders.length; i += 2)
            s = s.replaceAll(placeholders[i], placeholders[i+1]);
        return MiniMessage.get().parse(s);
    }

}
