package com.uzm.common.libraries.npclib.trait;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.nms.NMS;
import com.uzm.common.plugin.Common;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Maxter
 */
public class MakeAnimation extends NPCTrait {

    private Location location = new Location(null, 0, 0, 0);

    public MakeAnimation(NPC npc) {
        super(npc);


    }


    @Override
    public void onSpawn() {
        super.onSpawn();

        new BukkitRunnable() {

            @Override
            public void run() {
                NMS.playAnimation(getNPC().getEntity(), 1);
            }
        }.runTaskLaterAsynchronously(Common.getInstance(), 20 * 2);


    }

}
