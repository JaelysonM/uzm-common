package com.uzm.common.nms.version.v1_8_R3.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.metadata.PlayerMetadataStore;

/**
 * @author Maxter
 */
public class UUIDMetadataStore extends PlayerMetadataStore {

  @Override
  protected String disambiguate(OfflinePlayer player, String metadataKey) {
    return player.getUniqueId().toString() + ":" + metadataKey;
  }
}