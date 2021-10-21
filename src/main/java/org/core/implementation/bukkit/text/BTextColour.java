package org.core.implementation.bukkit.text;

import org.core.text.TextColour;

@Deprecated
public class BTextColour implements TextColour {

    private org.bukkit.ChatColor color;

    public BTextColour(org.bukkit.ChatColor color){
        this.color = color;
    }

    @Override
    public String formatChar() {
        return this.color.toString();
    }

    @Override
    public String getId() {
        return "minecraft:" + this.color.name().toLowerCase();
    }

    @Override
    public String getName() {
        return this.color.name();
    }

    @Override
    public String toString(){
        return this.formatChar();
    }
}
