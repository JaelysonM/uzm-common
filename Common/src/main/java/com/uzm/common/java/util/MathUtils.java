package com.uzm.common.java.util;

import org.bukkit.Location;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public class MathUtils {

    public static float clampYaw(float yaw) {
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }
        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }

        return yaw;
    }

    /**
     * Verifica se uma localização tem a chunk carregada no mundo.
     *
     * @param location localização para verificar
     * @return TRUE caso a localização tenha a chunk carregada, FALSE caso não tenha.
     */
    public static boolean isLoaded(Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }

        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;
        return location.getWorld().isChunkLoaded(chunkX, chunkZ);
    }
}