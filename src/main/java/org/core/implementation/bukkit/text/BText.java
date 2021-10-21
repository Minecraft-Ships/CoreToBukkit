package org.core.implementation.bukkit.text;

import org.array.utils.ArrayUtils;
import org.bukkit.ChatColor;
import org.core.adventureText.AText;
import org.core.text.Text;

@Deprecated
public class BText implements Text {

    protected String text;

    public BText(String text) {
        this.text = text;
    }

    public String toBukkitString() {
        return this.text;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BText)) {
            return false;
        }
        BText text = (BText) obj;
        return text.toPlain().equals(toPlain());
    }

    @Override
    public boolean equalsExact(String chars) {
        return this.text.equals(chars);
    }

    @Override
    public String toPlain() {
        return ChatColor.stripColor(this.text);
    }

    @Override
    @Deprecated
    public Text append(Text... text) {
        return new BText(this.text + ArrayUtils.toString("", t -> ((BText) t).text, text));
    }

    @Override
    public AText toAdventure() {
        return AText.ofLegacy(this.text);
    }
}
