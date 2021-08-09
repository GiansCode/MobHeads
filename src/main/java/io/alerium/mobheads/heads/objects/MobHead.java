package io.alerium.mobheads.heads.objects;

import io.alerium.mobheads.utils.ItemStackCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MobHead extends AbstractHead {

    private final int minValue;
    private final int maxValue;
    private final String texture;

    public MobHead(int changeToDrop, List<String> actions, Component messageToKiller, Component headName, List<Component> headLore, int minValue, int maxValue, String texture) {
        super(changeToDrop, actions, messageToKiller, headName, headLore);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.texture = texture;
    }

    @Override
    public ItemStack createHead(LivingEntity entity, double value) {
        final TextReplacementConfig textReplacement = TextReplacementConfig.builder().match("%value%").replacement(Double.toString(value)).build();

        return ItemStackCreator.createHead(
                headName,
                headLore.stream().map(component -> component.replaceText(textReplacement)).collect(Collectors.toList()),
                texture,
                entity.getType(),
                value
        );
    }

    @Override
    public double getValue(LivingEntity entity) {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue);
    }

}
