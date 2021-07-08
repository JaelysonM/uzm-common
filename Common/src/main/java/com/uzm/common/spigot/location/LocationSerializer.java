package com.uzm.common.spigot.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public class LocationSerializer {


    private String locationString;
    private Location location;

    public LocationSerializer(Location location) {
        this.location = location;
    }

    public LocationSerializer(String locationString) {
        this.locationString = locationString;
    }

    public Location unserialize() {
        String[] sp = locationString.split(" : ");
        Location loc = new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]), Double.parseDouble(sp[3]));
        loc.setPitch(Float.parseFloat(sp[4]));
        loc.setYaw(Float.parseFloat(sp[5]));
        destroy();
        return loc;
    }

    public void destroy() {
        locationString = null;
        location = null;
    }


    public String serialize() {
        String serialized =
                location.getWorld().getName() + " : " + location.getX() + " : " + location.getY() + " : " + location.getZ() + " : " + location.getPitch() + " : " + location.getYaw();
        destroy();
        return serialized;
    }
}
