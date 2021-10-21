package org.core.implementation.bukkit.world.position.block.entity.sign;

import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.text.Text;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BSignEntitySnapshot implements SignTileEntitySnapshot {

    protected List<AText> lines;

    public BSignEntitySnapshot(SignTileEntity entity) {
        this(entity.getText());
    }

    public BSignEntitySnapshot(AText... lines) {
        this(Arrays.asList(lines));
    }

    public BSignEntitySnapshot(Collection<AText> collection) {
        this.lines = new ArrayList<>();
        this.lines.addAll(collection);
    }

    @Deprecated
    public BSignEntitySnapshot(Text... lines) {
        this.lines = Stream.of(lines).map(Text::toAdventure).collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public Text[] getLines() {
        return lines.stream().map(t -> TranslateCore.buildText(t.toLegacy())).toArray(Text[]::new);
    }

    @Override
    @Deprecated
    public SignTileEntitySnapshot setLines(Text... lines) throws IndexOutOfBoundsException {
        if (lines.length > 4) {
            throw new IndexOutOfBoundsException();
        }
        this.lines = Stream.of(lines).map(Text::toAdventure).collect(Collectors.toList());
        return this;
    }

    @Override
    public List<AText> getText() {
        return this.lines;
    }

    @Override
    public SignTileEntity setText(Collection<AText> text) {
        this.lines.clear();
        this.lines.addAll(text);
        return this;
    }

    @Override
    public LiveSignTileEntity apply(LiveSignTileEntity lste) {
        lste.setText(this.lines);
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
