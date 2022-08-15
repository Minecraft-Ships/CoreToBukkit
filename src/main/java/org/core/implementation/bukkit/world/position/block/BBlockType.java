package org.core.implementation.bukkit.world.position.block;

import org.core.TranslateCore;
import org.core.implementation.bukkit.inventory.item.BItemType;
import org.core.implementation.bukkit.world.position.block.details.blocks.BBlockDetails;
import org.core.inventory.item.ItemType;
import org.core.utils.Identifiable;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.grouptype.BlockGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BBlockType implements BlockType {

    protected final @NotNull org.bukkit.Material material;

    public BBlockType(@NotNull org.bukkit.Material material) {
        this.material = material;
    }

    public @NotNull org.bukkit.Material getBukkitMaterial() {
        return this.material;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Identifiable type) {
            return type.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public BlockDetails getDefaultBlockDetails() {
        return new BBlockDetails(this.material.createBlockData(), false);
    }

    @Override
    public Set<BlockGroup> getGroups() {
        return TranslateCore
                .getPlatform()
                .getBlockGroups()
                .stream()
                .filter(b -> Arrays.asList(b.getGrouped()).contains(BBlockType.this))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<ItemType> getItemType() {
        if (this.material.isItem()) {
            return Optional.of(new BItemType(this.material));
        }
        return Optional.empty();
    }

    @Override
    public String getId() {
        return this.material.getKey().toString();
    }

    @Override
    public String getName() {
        return this.material.name();
    }
}
