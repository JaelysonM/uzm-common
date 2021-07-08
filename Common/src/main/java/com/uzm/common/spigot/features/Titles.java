package com.uzm.common.spigot.features;

import com.uzm.common.nms.NMS;
import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

@Builder
@Data
public class Titles {

    private Player player;
    private TitleType type;

    private String topMessage;
    private String bottomMessage;


    public Titles send(int fadeIn, int stayTime, int fadeOut) {
        NMS.sendTitle(player, type, bottomMessage, topMessage, fadeIn, stayTime, fadeOut);
        return this;
    }


    public enum TitleType {
        BOTH,
        SUBTILE,
        TITLE;
    }

}
