package org.ships.implementation.bukkit.world.position.block;

import org.core.CorePlugin;
import org.core.inventory.item.ItemType;
import org.core.utils.Identifiable;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.grouptype.BlockGroup;
import org.ships.implementation.bukkit.inventory.item.BItemType;
import org.ships.implementation.bukkit.world.position.block.details.blocks.BBlockDetails;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BBlockType implements BlockType {

    protected org.bukkit.Material material;

    public BBlockType(org.bukkit.Material material){
        if(material == null){
            throw new NullPointerException();
        }
        this.material = material;
    }

    public org.bukkit.Material getBukkitMaterial(){
        return this.material;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Identifiable){
            Identifiable type = (Identifiable)object;
            return type.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public BlockDetails getDefaultBlockDetails() {
        return new BBlockDetails(this.material.createBlockData(), false);
    }

    @Override
    public Set<BlockGroup> getGroups() {
        return CorePlugin
                .getPlatform()
                .getBlockGroups()
                .stream()
                .filter(b -> Arrays.asList(b.getGrouped()).contains(BBlockType.this))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<ItemType> getItemType() {
        if (this.material.isItem()){
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
