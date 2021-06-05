package com.uzm.common.spigot.features;

import com.uzm.common.nms.NMS;
import org.bukkit.entity.Player;

/**
 * @author JotaMPê (UzmStudio)
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
