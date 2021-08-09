package io.alerium.mobheads.heads.objects;

import io.alerium.mobheads.MobHeadsPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractHead {

    @Getter protected final int changeToDrop;
    protected final List<String> actions;

    @Getter protected final Component messageToKiller;
    protected final Component headName;
    protected final List<Component> headLore;

    public abstract ItemStack createHead(LivingEntity entity, double value);

    public abstract double getValue(LivingEntity entity);

    public void executeActions(Player player, double value) {
        MobHeadsPlugin.getInstance().getActionUtil().executeActions(player, actions.stream().map(s -> s.replaceAll("%value%", Double.toString(value))).collect(Collectors.toList()));
    }

}
