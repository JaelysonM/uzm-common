package com.uzm.common.spigot.types;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public final class RayTrace {

    private final Vector origin;
    private final Vector direction;

    public RayTrace(final Vector origin, final Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public RayTrace(final Player player) {
        this.origin = player.getEyeLocation().toVector();
        this.direction = player.getEyeLocation().getDirection();
    }

    public double origin(int i) {
        switch (i) {
            case 0:
                return origin.getX();
            case 1:
                return origin.getY();
            case 2:
                return origin.getZ();
            default:
                return 0;
        }
    }

    public double direction(int i) {
        switch (i) {
            case 0:
                return direction.getX();
            case 1:
                return direction.getY();
            case 2:
                return direction.getZ();
            default:
                return 0;
        }
    }
}