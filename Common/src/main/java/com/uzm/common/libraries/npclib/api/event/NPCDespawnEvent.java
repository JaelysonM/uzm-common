package com.uzm.common.libraries.npclib.api.event;

import com.uzm.common.libraries.npclib.api.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * @author Maxter
 */
public class NPCDespawnEvent extends NPCEvent implements Cancellable {

  private boolean cancelled;

  public NPCDespawnEvent(NPC npc) {
    super(npc);
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public boolean isCancelled() {
    return cancelled;
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
