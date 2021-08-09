package io.alerium.mobheads.heads.listeners;

import io.alerium.mobheads.MobHeadsPlugin;
import io.alerium.mobheads.heads.HeadsManager;
import io.alerium.mobheads.heads.objects.PlayerHead;
import io.alerium.mobheads.utils.ItemStackCreator;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class HeadListener implements Listener {

    private final MobHeadsPlugin plugin;
    private final HeadsManager manager;

    public HeadListener(MobHeadsPlugin plugin, HeadsManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;

        manager.getHead(event.getEntityType()).ifPresent(head -> {
            if (ThreadLocalRandom.current().nextInt(100) > head.getChangeToDrop())
                return;

            final Player killer = event.getEntity().getKiller();
            final double value = head.getValue(event.getEntity());

            event.getDrops().add(head.createHead(killer, value));

            if (head instanceof PlayerHead playerHead) {
                final Player victim = (Player) event.getEntity();
                victim.sendMessage(playerHead.getMessageToVictim()
                        .replaceText(TextReplacementConfig.builder().match("%killer%").replacement(killer.getName()).build())
                        .replaceText(TextReplacementConfig.builder().match("%value%").replacement(Double.toString(value)).build())
                );

                killer.sendMessage(head.getMessageToKiller()
                        .replaceText(TextReplacementConfig.builder().match("%victim%").replacement(victim.getName()).build())
                        .replaceText(TextReplacementConfig.builder().match("%value%").replacement(Double.toString(value)).build())
                );

                plugin.getEconomy().withdrawPlayer(victim, value);
                return;
            }

            killer.sendMessage(head.getMessageToKiller()
                    .replaceText(TextReplacementConfig.builder().match("%value%").replacement(Double.toString(value)).build())
            );
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        final ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.PLAYER_HEAD)
            return;

        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemStackCreator.MOB_HEAD_TYPE, PersistentDataType.STRING) || !container.has(ItemStackCreator.MOB_HEAD_VALUE, PersistentDataType.DOUBLE))
            return;

        event.setCancelled(true);

        final EntityType type = EntityType.valueOf(container.get(ItemStackCreator.MOB_HEAD_TYPE, PersistentDataType.STRING));
        final double value = container.get(ItemStackCreator.MOB_HEAD_VALUE, PersistentDataType.DOUBLE);
        manager.getHead(type).ifPresent(head -> {
            head.executeActions(event.getPlayer(), value);
            item.setAmount(item.getAmount()-1);
        });
    }

}
