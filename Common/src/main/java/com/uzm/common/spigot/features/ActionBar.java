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
public class ActionBar {

    private String message;

    public ActionBar send(Player player) {
        NMS.sendActionBar(player, message);
        return this;
    }

}
