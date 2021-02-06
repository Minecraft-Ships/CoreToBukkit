package org.ships.implementation.bukkit.utils.entry;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AbstractSnapshotValue<O, V> implements SnapshotValue<O, V>{

    protected V value;
    protected final Function<O, V> getter;
    protected final BiConsumer<O, V> setter;

    public AbstractSnapshotValue(V value, Function<O, V> getter, BiConsumer<O, V> setter){
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public SnapshotValue<O, V> setValue(V value) {
        this.value = value;
        return this;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V storeValue(O obj) {
        this.value = this.getter.apply(obj);
        return this.value;
    }

    @Override
    public void applyValue(O obj) {
        this.setter.accept(obj, this.value);
    }

    @Override
    public AbstractSnapshotValue<O, V> clone(){
        throw new IllegalStateException("Clone requires to be implemented");
    }
}
