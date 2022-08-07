package org.core.implementation.bukkit;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.Optional;

public interface VaultService {

    static Optional<Double> getBalance(OfflinePlayer user) {
        Optional<Economy> opEco = getEconomy();
        return opEco.map(economy -> economy.getBalance(user));
    }

    static void setBalance(OfflinePlayer user, BigDecimal price) {
        setBalance(user, price.doubleValue());
    }

    static void setBalance(OfflinePlayer user, double price) {
        Optional<Economy> opEco = getEconomy();
        if (!opEco.isPresent()) {
            return;
        }
        double bal = opEco.get().getBalance(user);
        double diff = (-bal) + price;
        EconomyResponse response;
        if (diff < 0) {
            response = opEco.get().withdrawPlayer(user, -diff);
        } else {
            response = opEco.get().depositPlayer(user, price);
        }
        if (!response.transactionSuccess()) {
            throw new IllegalStateException(response.errorMessage);
        }
    }

    static Optional<Economy> getEconomy() {
        if (Bukkit.getPluginManager().getPlugin("vault") == null) {
            return Optional.empty();
        }
        return Optional.of(Bukkit.getServicesManager().getRegistration(Economy.class).getProvider());
    }
}
