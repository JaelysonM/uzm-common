package com.uzm.common.libraries.holograms.api;

import com.uzm.common.nms.NMS;
import com.uzm.common.nms.interfaces.IArmorStand;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */


@Getter
@Setter
public class HologramLine {

    private Location location;
    private IArmorStand armor;
    private String line;
    private Hologram hologram;

    public HologramLine(Hologram hologram, Location location, String line) {
        this.line = ChatColor.translateAlternateColorCodes('&', line);
        this.location = location;
        this.armor = null;
        this.hologram = hologram;
    }

    public void spawn() {
        if (this.armor == null) {
            this.armor = NMS.createArmorStand(location, line, this);
        }
    }

    public void despawn() {
        if (this.armor != null) {
            this.armor.killEntity();
            this.armor = null;
        }
    }

    public void setLocation(Location location) {
        if (this.armor != null) {
            this.armor.setLocation(location.getX(), location.getY(), location.getZ());
        }
    }

    public void setLine(String line) {
        if (this.line.equals(ChatColor.translateAlternateColorCodes('&', line))) {
            this.armor.setName(this.line);
            return;
        }

        this.line = ChatColor.translateAlternateColorCodes('&', line);
        if (armor == null) {
            if (hologram.isSpawned()) {
                this.spawn();
            }

            return;
        }

        this.armor.setName(this.line);
    }
}