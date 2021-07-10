package com.uzm.common.spigot.items;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

public class ItemSerializer {

    private ItemStack toSerialize;

    public ItemSerializer(ItemStack toSerialize) {
        this.toSerialize = toSerialize;
    }

    public ItemStack getItemStack() {
        return this.toSerialize;
    }

    public String serializeToString() {
        StringBuilder sb = new StringBuilder(toSerialize.getType().name() + ":" + toSerialize.getDurability() + " : " + toSerialize.getAmount());

        if (toSerialize.hasItemMeta()) {
            if (toSerialize.getItemMeta().hasDisplayName())
                sb.append(": name=").append(toSerialize.getItemMeta().getDisplayName().replace("§", "&"));

            if (toSerialize.getItemMeta().hasLore() && toSerialize.getItemMeta().getLore().size() > 0) {
                sb.append(" : lore=");

                for (int i = 0; i < toSerialize.getItemMeta().getLore().size(); i++) {
                    sb.append((i + 1) == toSerialize.getItemMeta().getLore().size() ? toSerialize.getItemMeta().getLore().get(i).replace("&", "§") : toSerialize.getItemMeta().getLore().get(i).replace("&", "§") + "/");
                }
            }
            if (toSerialize.getItemMeta().hasEnchants()) {
                for (Map.Entry<Enchantment, Integer> e : toSerialize.getItemMeta().getEnchants().entrySet()) {
                    sb.append(" : enchant=").append(e.getKey().getName().toUpperCase()).append(":").append(e.getValue());
                }
            }
        }


        destroy();
        return sb.toString();
    }

    public void destroy() {
        this.toSerialize = null;
    }
}
