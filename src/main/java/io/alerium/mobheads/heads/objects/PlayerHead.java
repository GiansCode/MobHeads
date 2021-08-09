package io.alerium.mobheads.heads.objects;

import io.alerium.mobheads.MobHeadsPlugin;
import io.alerium.mobheads.utils.ItemStackCreator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerHead extends AbstractHead {

    @Getter private final Component messageToVictim;
    private final int moneyPercentage;

    public PlayerHead(int changeToDrop, List<String> actions, Component messageToKiller, Component headName, List<Component> headLore, Component messageToVictim, int moneyPercentage) {
        super(changeToDrop, actions, messageToKiller, headName, headLore);
        this.messageToVictim = messageToVictim;
        this.moneyPercentage = moneyPercentage;
    }

    @Override
    public ItemStack createHead(LivingEntity entity, double value) {
        final Player player = (Player) entity;
        final TextReplacementConfig victimReplacement = TextReplacementConfig.builder().match("%victim%").replacement(player.getName()).build();
        final TextReplacementConfig killerReplacement = TextReplacementConfig.builder().match("%killer%").replacement(player.getKiller().getName()).build();
        final TextReplacementConfig valueReplacement = TextReplacementConfig.builder().match("%value%").replacement(Double.toString(value)).build();

        return ItemStackCreator.createHead(
                headName.replaceText(victimReplacement).replaceText(killerReplacement).replaceText(valueReplacement),
                headLore.stream().map(component -> component.replaceText(victimReplacement).replaceText(killerReplacement).replaceText(valueReplacement)).collect(Collectors.toList()),
                player,
                value
        );
    }

    @Override
    public double getValue(LivingEntity entity) {
        final Player player = (Player) entity;
        return (MobHeadsPlugin.getInstance().getEconomy().getBalance(player) / 100) * moneyPercentage;
    }
}
