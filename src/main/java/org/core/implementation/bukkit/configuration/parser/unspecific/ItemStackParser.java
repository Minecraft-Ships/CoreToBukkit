package org.core.implementation.bukkit.configuration.parser.unspecific;

import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.core.config.parser.unspecific.UnspecificParser;
import org.core.implementation.bukkit.configuration.YAMLConfigurationFile;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.item.stack.ItemStack;

import java.util.Optional;

public class ItemStackParser implements UnspecificParser<ItemStack> {

    @Override
    public void set(ConfigurationStream file, ItemStack value, ConfigurationNode node) {
        ((YAMLConfigurationFile) file)
                .getYaml()
                .set(
                        String.join(".", node.getPath()),
                        ((BAbstractItemStack) value).getBukkitItem());
    }

    @Override
    public Optional<ItemStack> parse(ConfigurationStream file, ConfigurationNode node) {
        org.bukkit.inventory.ItemStack stack = ((YAMLConfigurationFile) file)
                .getYaml()
                .getItemStack(String.join(".", node.getPath()));
        if (stack == null) {
            return Optional.empty();
        }
        return Optional.of(new BLiveItemStack(stack));
    }

    @Override
    public String getId() {
        return "itemstack";
    }

    @Override
    public String getName() {
        return "Item Stack";
    }
}
