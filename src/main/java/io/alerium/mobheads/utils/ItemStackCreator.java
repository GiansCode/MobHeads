package io.alerium.mobheads.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class ItemStackCreator {

    public static final NamespacedKey MOB_HEAD_TYPE = new NamespacedKey("mobheads", "type");
    public static final NamespacedKey MOB_HEAD_VALUE = new NamespacedKey("mobheads", "value");

    public static ItemStack createHead(Component name, List<Component> lore, String texture, EntityType type, double value) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", texture));
        return createHead(name, lore, profile, type, value);
    }

    public static ItemStack createHead(Component name, List<Component> lore, Player player, double value) {
        return createHead(name, lore, player.getPlayerProfile(), EntityType.PLAYER, value);
    }

    private static ItemStack createHead(Component name, List<Component> lore, PlayerProfile profile, EntityType type, double value) {
        final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.displayName(name);
        meta.lore(lore);
        meta.setPlayerProfile(profile);

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(MOB_HEAD_TYPE, PersistentDataType.STRING, type.name());
        container.set(MOB_HEAD_VALUE, PersistentDataType.DOUBLE, value);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
