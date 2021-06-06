package com.uzm.common.nms.version.v1_12_R1.utils.player;

import com.uzm.common.nms.version.v1_12_R1.entity.EntityNPCPlayer;

public class PlayerControllerJump {
    private final EntityNPCPlayer a;
    private boolean b;

    public PlayerControllerJump(EntityNPCPlayer entityinsentient) {
        this.a = entityinsentient;
    }

    public void a() {
        this.b = true;
    }

    public void b() {
        this.a.l(this.b);
        this.b = false;
    }
}