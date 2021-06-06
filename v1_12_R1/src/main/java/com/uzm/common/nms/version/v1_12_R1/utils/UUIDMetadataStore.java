package com.uzm.common.nms.version.v1_12_R1.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.metadata.PlayerMetadataStore;

/**
 * @author Maxter
 */
public class UUIDMetadataStore extends PlayerMetadataStore {

  @Override
  protected String disambiguate(OfflinePlayer player, String metadataKey) {
    return player.getUniqueId().toString() + ":" + metadataKey;
  }
}