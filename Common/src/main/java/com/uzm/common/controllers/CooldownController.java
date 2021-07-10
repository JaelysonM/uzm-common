package com.uzm.common.controllers;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

@Getter
public class CooldownController {

    private HashMap<String, Long> cooldowns = new HashMap<>();

    /**
     * Method used to create a cooldown with {@param seconds} seconds saving by key {@param key}
     *
     * @param key     Cooldown code.
     * @param seconds Time in seconds to be set as a cooldown
     */

    public void createCooldown(String key, int seconds) {
        this.getCooldowns().put(key, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
    }

    /**
     * Delete cooldown with the given {@param key}
     *
     * @param key Cooldown code.
     */

    public void deleteCooldown(String key) {
        this.getCooldowns().remove(key);
    }

    /**
     * Get the cooldown with the given {@param key}
     *
     * @param key Cooldown code.
     */

    public long getCooldown(String key) {
        return this.getCooldowns().getOrDefault(key, 0L);
    }

    /**
     * Check that there is a cooldown rolling from key {@param key}
     *
     * @param key Cooldown code.
     */

    public boolean isInCooldown(String key) {
        return this.getCooldown(key) >= System.currentTimeMillis();
    }


    /**
     * Remove controller from player {@param player}
     *
     * @param player Player to remove the controller.
     */

    public static void removeCooldownController(Player player) {
        CONTROLLER.remove(player.getUniqueId());
    }

    /**
     * Get controller from player {@param player}
     *
     * @param player Player to get the controller.
     */

    public static CooldownController getCooldownController(Player player) {
        CONTROLLER.putIfAbsent(player.getUniqueId(), new CooldownController());
        return CONTROLLER.get(player.getUniqueId());
    }

    private static final Map<UUID, CooldownController> CONTROLLER = new HashMap<>();
}