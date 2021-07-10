package com.uzm.common.spigot.features;

import com.uzm.common.nms.NMS;
import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

@Builder
@Data
public class TabColor {

    private Player player;

    private String header = "";
    private String bottom = "";

    public TabColor send() {
        NMS.sendTabColor(player, header, bottom);
        return this;
    }


}
