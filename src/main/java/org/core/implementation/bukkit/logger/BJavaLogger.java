package org.core.implementation.bukkit.logger;

import org.core.adventureText.AText;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class BJavaLogger implements org.core.logger.Logger {

    private final Logger logger;

    public BJavaLogger(@NotNull Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(@NotNull String log) {
        this.logger.info(log);
    }

    @Override
    public void log(@NotNull AText log) {
        this.logger.info(log.toLegacy());
    }

    @Override
    public void warning(@NotNull String log) {
        this.logger.fine(log);
    }

    @Override
    public void error(@NotNull String log) {
        this.logger.warning(log);
    }
}
