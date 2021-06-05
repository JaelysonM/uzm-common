package com.uzm.common.libraries.npclib.api.event;

import com.uzm.common.libraries.npclib.api.NPC;
import org.bukkit.event.HandlerList;

/**
 * @author Maxter
 */
public class NPCNeedsRespawnEvent extends NPCEvent {


  public NPCNeedsRespawnEvent(NPC npc) {
    super(npc);
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
