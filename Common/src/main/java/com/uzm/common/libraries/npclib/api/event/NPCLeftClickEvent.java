package com.uzm.common.libraries.npclib.api.event;

import com.uzm.common.libraries.npclib.api.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * @author Maxter
 */
public class NPCLeftClickEvent extends NPCEvent {

  private Player player;

  public NPCLeftClickEvent(NPC npc, Player clicked) {
    super(npc);
    this.player = clicked;
  }


  public Player getPlayer() {
    return player;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
