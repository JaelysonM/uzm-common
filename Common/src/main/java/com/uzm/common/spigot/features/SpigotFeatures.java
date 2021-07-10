package com.uzm.common.spigot.features;

import com.uzm.common.nms.NMS;
import org.bukkit.entity.Player;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */


public class SpigotFeatures {

    public static void sendTabColor(Player player, String header, String bottom) {
        if (player != null)
            NMS.sendTabColor(player, header, bottom);
    }

    public static void sendActionBar(Player player, String message) {
        if (player != null)
            NMS.sendActionBar(player, message);
    }

    public static void sendTitle(Player player, Titles.TitleType type, String topMessage, String bottomMessage, int fadeIn, int stayTime, int fadeOut) {
        if (player != null)
            NMS.sendTitle(player, type, bottomMessage, topMessage, fadeIn, stayTime, fadeOut);


    }

}
