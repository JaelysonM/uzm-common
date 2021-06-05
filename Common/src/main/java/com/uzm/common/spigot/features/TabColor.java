package com.uzm.common.spigot.features;

import com.uzm.common.nms.NMS;
import org.bukkit.entity.Player;

/**
 * @author JotaMPê (UzmStudio)
 */

public class TabColor {

    private Player player;

    private String header;
    private String bottom;


    public TabColor(Player player) {
        this.player = player;
        this.bottom = "";
        this.header = "";
    }

    public TabColor(Player player, String header, String bottom) {
        this.player = player;
        this.header = header;
        this.bottom = bottom;

    }

    public TabColor setHeader(String header) {
        this.header = header;
        return this;
    }

    public TabColor setBottom(String bottom) {
        this.bottom = bottom;
        return this;
    }


    public TabColor send() {
        NMS.sendTabColor(player, header, bottom);
        destroy();
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public void destroy() {
        this.bottom = null;
        this.header = null;
        this.player = null;
    }

}
