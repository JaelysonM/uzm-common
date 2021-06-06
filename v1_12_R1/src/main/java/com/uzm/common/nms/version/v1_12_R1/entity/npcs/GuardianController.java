package com.uzm.common.nms.version.v1_12_R1.entity.npcs;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.api.event.NPCEnderTeleportEvent;
import com.uzm.common.libraries.npclib.api.event.NPCPushEvent;
import com.uzm.common.libraries.npclib.npc.AbstractNPC;
import com.uzm.common.libraries.npclib.npc.ai.NPCHolder;
import com.uzm.common.nms.NMS;
import com.uzm.common.nms.utils.Utils;
import com.uzm.common.nms.version.v1_12_R1.NMSImpl;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGuardian;
import org.bukkit.entity.Guardian;
import org.bukkit.util.Vector;

import java.util.Objects;

public class GuardianController extends MobEntityController {
    public GuardianController() {
        super(EntityGuardianNPC.class);
    }

    @Override
    public Guardian getBukkitEntity() {
        return (Guardian) super.getBukkitEntity();
    }

    public static class EntityGuardianNPC extends EntityGuardian implements NPCHolder {
        private final AbstractNPC npc;

        public EntityGuardianNPC(World world) {
            this(world, null);
        }

        public EntityGuardianNPC(World world, NPC npc) {
            super(world);
            this.npc = (AbstractNPC) npc;
            if (npc != null) {
                NMS.clearPathfinderGoals(this);
            }
        }

        @Override
        protected void a(double d0, boolean flag, IBlockData block, BlockPosition blockposition) {
            if (npc == null || !npc.isFlyable()) {
                super.a(d0, flag, block, blockposition);
            }
        }

        @Override
        public void a(float f, float f1, float f2) {
            if (npc == null || !npc.isFlyable()) {
                super.a(f, f1, f2);
            } else {
                NMSImpl.flyingMoveLogic(this, f, f1, f2);
            }
        }

        @Override
        protected SoundEffect cf() {
            return SoundEffect.a.get(new MinecraftKey(NMS.getSoundEffect(npc, Objects.requireNonNull(super.cf()).toString(), NPC.DEATH_SOUND_METADATA)));
        }

        @Override
        public void collide(net.minecraft.server.v1_12_R1.Entity entity) {
            // this method is called by both the entities involved - cancelling
            // it will not stop the NPC from moving.
            super.collide(entity);
            if (npc != null)
                Utils.callCollisionEvent(npc, entity.getBukkitEntity());
        }

        @Override
        protected SoundEffect d(DamageSource damagesource) {
            return SoundEffect.a.get(new MinecraftKey(NMS.getSoundEffect(npc, Objects.requireNonNull(super.d(damagesource)).toString(), NPC.HURT_SOUND_METADATA)));
        }

        @Override
        public boolean d(NBTTagCompound save) {
            return npc == null && super.d(save);
        }

        @Override
        public void e(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.e(f, f1);
            }
        }

        @Override
        public void enderTeleportTo(double d0, double d1, double d2) {
            if (npc == null) {
                super.enderTeleportTo(d0, d1, d2);
                return;
            }
            NPCEnderTeleportEvent event = new NPCEnderTeleportEvent(npc);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                super.enderTeleportTo(d0, d1, d2);
            }
        }

        @Override
        public void f(double x, double y, double z) {
            if (npc == null) {
                super.f(x, y, z);
                return;
            }
            if (NPCPushEvent.getHandlerList().getRegisteredListeners().length == 0) {
                if (!npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true))
                    super.f(x, y, z);
                return;
            }
            Vector vector = new Vector(x, y, z);
            NPCPushEvent event = Utils.callPushEvent(npc, vector);
            if (!event.isCancelled()) {
                vector = event.getCollisionVector();
                super.f(vector.getX(), vector.getY(), vector.getZ());
            }
        }

        @Override
        protected SoundEffect F() {
            return SoundEffect.a.get(new MinecraftKey(NMS.getSoundEffect(npc, Objects.requireNonNull(super.F()).toString(), NPC.AMBIENT_SOUND_METADATA)));
        }

        @Override
        public CraftEntity getBukkitEntity() {
            if (npc != null && !(bukkitEntity instanceof NPCHolder))
                bukkitEntity = new GuardianNPC(this);
            return super.getBukkitEntity();
        }

        @Override
        public NPC getNPC() {
            return npc;
        }

        @Override
        public boolean isLeashed() {
            if (npc == null)
                return super.isLeashed();
            boolean protectedDefault = npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true);
            if (!protectedDefault || !npc.data().get(NPC.LEASH_PROTECTED_METADATA, protectedDefault))
                return super.isLeashed();
            if (super.isLeashed()) {
                unleash(true, false); // clearLeash with client update
            }
            return false; // shouldLeash
        }

        @Override
        protected void L() {
            if (npc == null) {
                super.L();
            }
        }

        @Override
        public void M() {
            super.M();
            if (npc != null) {
                npc.update();
            }
        }

        @Override
        public boolean m_() {
            if (npc == null || !npc.isFlyable()) {
                return super.m_();
            } else {
                return false;
            }
        }

        @Override
        public void onLightningStrike(EntityLightning entitylightning) {
            if (npc == null)
                super.onLightningStrike(entitylightning);
        }

        @Override
        public void setSize(float f, float f1) {
            if (npc == null) {
                super.setSize(f, f1);
            } else {
                NMS.setSize(this, f, f1, justCreated);
            }
        }
    }

    public static class GuardianNPC extends CraftGuardian implements NPCHolder {
        private final AbstractNPC npc;

        public GuardianNPC(EntityGuardianNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.npc = entity.npc;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}