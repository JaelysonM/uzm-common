package com.uzm.common.libraries.npclib.trait;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.api.trait.Trait;


/**
 * @author Maxter
 */
public abstract class NPCTrait implements Trait {

  private NPC npc;

  public NPCTrait(NPC npc) {
    this.npc = npc;
  }

  public NPC getNPC() {
    return npc;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onSpawn() {}

  @Override
  public void onDespawn() {}

  @Override
  public void run() {}


  @Override
  public void onRemove() {}
}
