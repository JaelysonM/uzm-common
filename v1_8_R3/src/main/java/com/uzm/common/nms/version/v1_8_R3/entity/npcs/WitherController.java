package com.uzm.common.nms.version.v1_8_R3.entity.npcs;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.api.event.NPCEnderTeleportEvent;
import com.uzm.common.libraries.npclib.api.event.NPCPushEvent;
import com.uzm.common.libraries.npclib.npc.AbstractNPC;
import com.uzm.common.libraries.npclib.npc.ai.NPCHolder;
import com.uzm.common.nms.NMS;
import com.uzm.common.nms.utils.Utils;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWither;
import org.bukkit.entity.Wither;
import org.bukkit.util.Vector;

public class WitherController extends MobEntityController {
    public WitherController() {
        super(EntityWitherNPC.class);
    }

    @Override
    public Wither getBukkitEntity() {
        return (Wither) super.getBukkitEntity();
    }

    public static class EntityWitherNPC extends EntityWither implements NPCHolder {
        private final AbstractNPC npc;

        public EntityWitherNPC(World world) {
            this(world, null);
        }

        public EntityWitherNPC(World world, NPC npc) {
            super(world);
            this.npc = (AbstractNPC) npc;
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
        public void collide(net.minecraft.server.v1_8_R3.Entity entity) {
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
        protected void E() {
            if (npc == null) {
                super.E();
                return;
            }
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
                if (!npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true))
                    super.g(x, y, z);
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
        public CraftEntity getBukkitEntity() {
            if (npc != null && !(bukkitEntity instanceof NPCHolder))
                bukkitEntity = new WitherNPC(this);
            return super.getBukkitEntity();
        }

        @Override
        public NPC getNPC() {
            return npc;
        }

        @Override
        public int s(int i) {
            return npc == null ? super.s(i) : 0;
        }

        @Override
        protected String z() {
            return NMS.getSoundEffect(npc, super.z(), NPC.AMBIENT_SOUND_METADATA);
        }
    }

    public static class WitherNPC extends CraftWither implements NPCHolder {
        private final NPC npc;

        public WitherNPC(EntityWitherNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.npc = entity.npc;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}