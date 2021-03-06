package com.uzm.common.nms.version.v1_8_R3.entity.npcs;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.api.event.NPCEnderTeleportEvent;
import com.uzm.common.libraries.npclib.api.event.NPCPushEvent;
import com.uzm.common.libraries.npclib.npc.ai.NPCHolder;
import com.uzm.common.nms.NMS;
import com.uzm.common.nms.utils.Utils;
import com.uzm.common.nms.version.v1_8_R3.NMSImpl;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPigZombie;
import org.bukkit.entity.PigZombie;
import org.bukkit.util.Vector;

public class PigZombieController extends MobEntityController {

    public PigZombieController() {
        super(EntityPigZombieNPC.class);
    }

    @Override
    public PigZombie getBukkitEntity() {
        return (PigZombie) super.getBukkitEntity();
    }

    public static class EntityPigZombieNPC extends EntityPigZombie implements NPCHolder {
        private final NPC npc;

        public EntityPigZombieNPC(World world) {
            this(world, null);
        }

        public EntityPigZombieNPC(World world, NPC npc) {
            super(world);
            this.npc = npc;
            if (npc != null) {
                NMS.clearPathfinderGoals(this);
            }
        }

        @Override
        public void t_() {
            super.t_();
            if (npc != null)
                npc.tick();

        }

        @Override
        protected void a(double d0, boolean flag, Block block, BlockPosition blockposition) {
            if (npc == null || !npc.isFlyable()) {
                super.a(d0, flag, block, blockposition);
            }
        }

        @Override
        protected String bo() {
            return NMS.getSoundEffect(npc, super.bo(), NPC.HURT_SOUND_METADATA);
        }

        @Override
        protected String bp() {
            return NMS.getSoundEffect(npc, super.bp(), NPC.DEATH_SOUND_METADATA);
        }

        @Override
        public boolean cc() {
            if (npc == null)
                return super.cc();
            boolean protectedDefault = npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true);
            if (!protectedDefault || !npc.data().get(NPC.LEASH_PROTECTED_METADATA, protectedDefault))
                return super.cc();
            if (super.cc()) {
                unleash(true, false); // clearLeash with client update
            }
            return false; // shouldLeash
        }

        @Override
        public void collide(Entity entity) {
            // this method is called by both the entities involved - cancelling
            // it will not stop the NPC from moving.
            super.collide(entity);
            if (npc != null)
                Utils.callCollisionEvent(npc, entity.getBukkitEntity());
        }

        @Override
        public boolean d(NBTTagCompound save) {
            return npc == null && super.d(save);
        }

        @Override
        protected void D() {
            if (npc == null) {
                super.D();
            }
        }

        @Override
        public void e(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.e(f, f1);
            }
        }

        @Override
        public void E() {
            super.E();
            if (npc != null)
                npc.update();
        }

        @Override
        public void enderTeleportTo(double d0, double d1, double d2) {
            if (npc == null)
                super.enderTeleportTo(d0, d1, d2);
            NPCEnderTeleportEvent event = new NPCEnderTeleportEvent(npc);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                super.enderTeleportTo(d0, d1, d2);
            }
        }

        @Override
        public void g(double x, double y, double z) {
            if (npc == null) {
                super.g(x, y, z);
                return;
            }
            if (NPCPushEvent.getHandlerList().getRegisteredListeners().length == 0) {
                if (!npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true)) {
                    super.g(x, y, z);
                }
                return;
            }
            Vector vector = new Vector(x, y, z);
            NPCPushEvent event = Utils.callPushEvent(npc, vector);
            if (!event.isCancelled()) {
                vector = event.getCollisionVector();
                super.g(vector.getX(), vector.getY(), vector.getZ());
            }
            // when another entity collides, this method is called to push the
            // NPC so we prevent it from doing anything if the event is
            // cancelled.
        }

        @Override
        public void g(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.g(f, f1);
            } else {
                NMSImpl.flyingMoveLogic(this, f, f1);
            }
        }

        @Override
        public CraftEntity getBukkitEntity() {
            if (npc != null && !(bukkitEntity instanceof NPCHolder))
                bukkitEntity = new PigZombieNPC(this);
            return super.getBukkitEntity();
        }

        @Override
        public NPC getNPC() {
            return npc;
        }

        @Override
        public boolean k_() {
            if (npc == null || !npc.isFlyable()) {
                return super.k_();
            } else {
                return false;
            }
        }

        @Override
        protected String z() {
            return NMS.getSoundEffect(npc, super.z(), NPC.AMBIENT_SOUND_METADATA);
        }
    }

    public static class PigZombieNPC extends CraftPigZombie implements NPCHolder {
        private final NPC npc;

        public PigZombieNPC(EntityPigZombieNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.npc = entity.npc;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}