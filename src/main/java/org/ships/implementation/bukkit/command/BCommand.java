package org.ships.implementation.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.core.TranslateCore;
import org.core.command.CommandLauncher;
import org.core.exceptions.NotEnoughArguments;
import org.core.source.command.CommandSource;
import org.ships.implementation.bukkit.platform.BukkitPlatform;

import java.util.List;

public class BCommand implements TabExecutor {

    private CommandLauncher command;

    public BCommand(CommandLauncher command){
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        CommandSource source = ((BukkitPlatform) TranslateCore.getPlatform()).getSource(commandSender);
        try {
            return this.command.run(source, strings);
        } catch (NotEnoughArguments notEnoughArguments) {
            notEnoughArguments.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        CommandSource source = ((BukkitPlatform) TranslateCore.getPlatform()).getSource(commandSender);
        return this.command.tab(source, strings);
    }
}
