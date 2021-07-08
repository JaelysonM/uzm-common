package com.uzm.common.database.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */


@Getter
@Setter
@ToString(includeFieldNames = true)
@AllArgsConstructor
public abstract class PlayerCacheHandler extends CacheHandler {

    private Player player;
    private String uuid;

    public void gc() {
        this.player = null;
        this.uuid = null;
    }


}
