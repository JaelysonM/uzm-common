package com.uzm.common.spigot.inventories;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

@AllArgsConstructor
@Data
public class ProgressBarMenu {

    private Inventory base;

    private double current;

    private double maxreach;

    private int segments;
    private int[] setup;

    private ItemStack[] stacks;

    public void build() {
        int value = (int) ((current / maxreach * 100) / (100 / segments));

        for (int x = 0; x < segments; x++) {
            int slot = setup[0] + x;
            if (x <= (value - 1)) {

                base.setItem(slot, stacks[0]);
            } else {
                base.setItem(slot, stacks[1]);
            }
        }
    }

}
