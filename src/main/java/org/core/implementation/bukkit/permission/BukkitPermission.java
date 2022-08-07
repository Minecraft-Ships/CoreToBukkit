package org.core.implementation.bukkit.permission;

import org.core.permission.Permission;

public class BukkitPermission implements Permission {

    private final String permission;

    public BukkitPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermissionValue() {
        return this.permission;
    }
}
