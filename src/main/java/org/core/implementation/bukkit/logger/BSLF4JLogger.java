package org.core.implementation.bukkit.logger;

import org.core.adventureText.AText;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class BSLF4JLogger implements org.core.logger.Logger {

    private final Logger logger;

    /**
     * Do not use unless needed. This is only for casting to the object for instances where Paper is not active
     *
     * @param logger The SLF4J logger
     */
    @Deprecated
    public BSLF4JLogger(@NotNull Object logger) {
        this((Logger) logger);
    }

    public BSLF4JLogger(@NotNull Logger logger) {
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
        this.logger.warn(log);
    }

    @Override
    public void error(@NotNull String log) {
        this.logger.error(log);
    }
}
