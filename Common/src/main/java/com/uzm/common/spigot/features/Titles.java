package com.uzm.common.spigot.features;

import com.uzm.common.nms.NMS;
import org.bukkit.entity.Player;

/**
 * @author JotaMPê (UzmStudio)
 */

public class Titles {

    private Player player;
    private TitleType type;

    private String topMessage;
    private String bottomMessage;

    public Titles(Player player, TitleType type) {
        this.player = player;
        this.type = type;
    }

    public Titles setTopMessage(String topMessage) {
        this.topMessage = topMessage;
        return this;
    }

    public Titles setBottomMessage(String bottomMessage) {
        this.bottomMessage = bottomMessage;
        return this;
    }

    public Titles setMessages(String topMessage, String bottomMessage) {
        this.topMessage = bottomMessage;
        this.bottomMessage = bottomMessage;
        return this;
    }

    public TitleType getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }


    public Titles send(int fadeIn, int stayTime, int fadeOut) {
        NMS.sendTitle(player, type, bottomMessage, topMessage, fadeIn, stayTime, fadeOut);
        destroy();
        return this;
    }


    public enum TitleType {
        BOTH,
        SUBTILE,
        TITLE;
    }

    public void destroy() {
        this.player = null;
        this.bottomMessage = null;
        this.topMessage = null;
        this.type = null;
    }
}
