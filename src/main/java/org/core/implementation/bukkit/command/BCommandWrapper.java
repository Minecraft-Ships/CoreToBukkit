package org.core.implementation.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BCommandWrapper extends Command {

    private final BCommand command;

    public BCommandWrapper(BCommand command) {
        super(command.getWrapper().getName(), command.getWrapper().getDescription(), command.getWrapper().getName(),
                Collections.emptyList());
        this.command = command;
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return this.command.onCommand(sender, this, commandLabel, args);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws
            IllegalArgumentException {
        return this.command.onTabComplete(sender, this, alias, args);
    }

}
