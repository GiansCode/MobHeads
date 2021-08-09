package io.alerium.mobheads.heads;

import io.alerium.mobheads.MobHeadsPlugin;
import io.alerium.mobheads.heads.listeners.HeadListener;
import io.alerium.mobheads.heads.objects.AbstractHead;
import io.alerium.mobheads.heads.objects.MobHead;
import io.alerium.mobheads.heads.objects.PlayerHead;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HeadsManager {

    private final MobHeadsPlugin plugin;
    private final Map<EntityType, AbstractHead> mobHeads = new HashMap<>();

    public HeadsManager(MobHeadsPlugin plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        loadHeads();
        loadPlayerHead();

        Bukkit.getPluginManager().registerEvents(new HeadListener(plugin, this), plugin);
    }

    public Optional<AbstractHead> getHead(EntityType type) {
        return Optional.ofNullable(mobHeads.get(type));
    }

    private void loadHeads() {
        final ConfigurationSection section = plugin.getConfiguration().getConfig().getConfigurationSection("mob-heads");
        for (String id : section.getKeys(false)) {
            final EntityType type = EntityType.valueOf(id);
            final ConfigurationSection settingsSection = section.getConfigurationSection(id + ".settings");

            final int minValue = settingsSection.getInt("min-value");
            final int maxValue = settingsSection.getInt("max-value");
            final int changeToDrop = settingsSection.getInt("change-to-drop");
            final String texture = settingsSection.getString("head");
            final Component messageOnDrop = plugin.getConfiguration().getMessage("mob-heads." + id + ".settings.message-on-drop.to-killer");

            final Component itemName = plugin.getConfiguration().getMessage("mob-heads." + id + ".item.name");
            final List<Component> itemLore = plugin.getConfiguration().getMessageList("mob-heads." + id + ".item.lore");

            mobHeads.put(type, new MobHead(changeToDrop, section.getStringList(id + ".actions"), messageOnDrop, itemName, itemLore, minValue, maxValue, texture));
        }
    }

    private void loadPlayerHead() {
        final ConfigurationSection section = plugin.getConfiguration().getConfig().getConfigurationSection("player-heads");
        if (section == null || !section.getBoolean("enabled"))
            return;

        final int moneyPercentage = section.getInt("settings.money-percentage");
        final int changeToDrop = section.getInt("settings.chance-to-drop");
        final Component messageToKiller = plugin.getConfiguration().getMessage("player-heads.settings.message-on-drop.to-killer");
        final Component messageToVictim = plugin.getConfiguration().getMessage("player-heads.settings.message-on-drop.to-victim");

        final Component itemName = plugin.getConfiguration().getMessage("player-heads.item.name");
        final List<Component> itemLore = plugin.getConfiguration().getMessageList("player-heads.item.lore");
        final List<String> actions = section.getStringList("actions");

        mobHeads.put(EntityType.PLAYER, new PlayerHead(changeToDrop, actions, messageToKiller, itemName, itemLore, messageToVictim, moneyPercentage));
    }

}
