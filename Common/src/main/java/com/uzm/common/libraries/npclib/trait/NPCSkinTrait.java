package com.uzm.common.libraries.npclib.trait;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.npc.skin.Skin;
import com.uzm.common.libraries.npclib.npc.skin.SkinnableEntity;

public class NPCSkinTrait extends NPCTrait {


    private Skin skin;

    public NPCSkinTrait(NPC npc, String value, String signature) {
        super(npc);
        this.skin = Skin.fromData(value, signature);

    }

    public NPCSkinTrait(NPC npc, String name) {
        super(npc);
        this.skin = Skin.fromName(name);


    }

   public Skin getSkin() {
        return skin;
   }
    @Override
    public void onSpawn() {
        this.skin.apply((SkinnableEntity) this.getNPC().getEntity());
    }
}
