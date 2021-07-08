package com.uzm.common.spigot.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public class ItemStackUtils {

    public static int getAmountOfItem(Material material, Location location) {
        return getAmountOfItem(material, location, 1);
    }

    public static int getAmountOfItem(Material material, Location location, int distance) {
        int amount = 0;
        for (Entity entity : location.getWorld().getEntities()) {
            if (entity instanceof Item) {
                Item item = (Item) entity;
                if (item.getItemStack().getType().equals(material) && item.getLocation().distance(location) <= distance) {
                    amount += item.getItemStack().getAmount();
                }
            }
        }
        return amount;
    }
}
