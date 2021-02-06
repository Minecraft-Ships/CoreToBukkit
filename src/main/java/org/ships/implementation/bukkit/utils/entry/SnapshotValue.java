package org.ships.implementation.bukkit.utils.entry;

public interface SnapshotValue <O, V> {

    boolean canApplyTo(Object obj);
    V getValue();
    V storeValue(O obj);
    SnapshotValue<O, V> setValue(V value);
    void applyValue(O obj);
    SnapshotValue<O, V> clone();
}
