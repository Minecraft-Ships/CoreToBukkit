package org.ships.implementation.bukkit.world.position.block.entity.sign;

import org.core.text.Text;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;

import java.util.Collection;

public class BSignEntitySnapshot implements SignTileEntitySnapshot {

    protected Text[] lines;

    public BSignEntitySnapshot(SignTileEntity entity) {
        this(entity.getLines());
    }

    @Deprecated
    public BSignEntitySnapshot(Text... lines) {
        this.lines = lines.clone();
    }

    @Override
    public Text[] getLines() {
        return lines;
    }

    @Override
    public SignTileEntitySnapshot setLines(Text... lines) throws IndexOutOfBoundsException {
        if (lines.length > 4) {
            throw new IndexOutOfBoundsException();
        }
        this.lines = lines.clone();
        return this;
    }

    @Override
    public LiveSignTileEntity apply(LiveSignTileEntity lste) {
        lste.setLines(this.lines);
        return lste;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes.OAK_SIGN.getLike();
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new BSignEntitySnapshot(this);
    }
}
