package com.uzm.common.database.cache;

import lombok.*;
import org.bukkit.entity.Player;

@Getter
@Setter
@ToString(includeFieldNames = true)
public abstract class PlayerCacheHandler extends CacheHandler {

    private Player player;
    private String uuid;

    public PlayerCacheHandler(Player player, String uuid) {
        this.player = player;
        this.uuid = uuid;
    }

    public void gc() {
        this.player = null;
        this.uuid = null;
    }


}
