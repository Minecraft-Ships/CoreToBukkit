package org.core.implementation.bukkit.world.position.flags;

import org.core.world.position.flags.physics.ApplyPhysicsFlag;

public class BApplyPhysicsFlag implements ApplyPhysicsFlag {

    public static final BApplyPhysicsFlag NONE = new BApplyPhysicsFlag("bukkit:none", "None", false);
    public static final BApplyPhysicsFlag DEFAULT = new BApplyPhysicsFlag("bukkit:default", "Default", true);

    private final String name;
    private final String id;
    private final boolean value;

    public BApplyPhysicsFlag(String id, String name, boolean value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }

    public boolean getBukkitValue() {
        return this.value;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
