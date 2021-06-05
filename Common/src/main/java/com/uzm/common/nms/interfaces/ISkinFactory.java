package com.uzm.common.nms.interfaces;

import org.bukkit.entity.Player;

public interface ISkinFactory {

  void applySkin(Player p, Object props);

  void removeSkin(Player p);

  void updateSkin(Player p);




}

