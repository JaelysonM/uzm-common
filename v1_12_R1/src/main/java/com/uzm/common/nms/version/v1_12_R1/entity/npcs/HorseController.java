package com.uzm.common.nms.version.v1_12_R1.entity.npcs;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.api.event.NPCEnderTeleportEvent;
import com.uzm.common.libraries.npclib.api.event.NPCPushEvent;
import com.uzm.common.libraries.npclib.npc.AbstractNPC;
import com.uzm.common.libraries.npclib.npc.ai.NPCHolder;
import com.uzm.common.libraries.npclib.trait.HorseModifiers;
import com.uzm.common.nms.NMS;
import com.uzm.common.nms.utils.Utils;
import com.uzm.common.nms.version.v1_12_R1.NMSImpl;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHorse;
import org.bukkit.entity.Horse;
import org.bukkit.util.Vector;

import java.util.Objects;

public class HorseController extends MobEntityController {
    public HorseController() {
        super(EntityHorseNPC.class);
    }

    @Override
    public Horse getBukkitEntity() {
        return (Horse) super.getBukkitEntity();
    }

    @Override
    public void spawn(Location at, NPC npc) {
        npc.getTrait(HorseModifiers.class);
        super.spawn(at, npc);
    }

    public static class EntityHorseNPC extends EntityHorse implements NPCHolder {
        private final AbstractNPC npc;

        public EntityHorseNPC(World world) {
            this(world, null);
        }

        public EntityHorseNPC(World world, NPC npc) {
            super(world);
            this.npc = (AbstractNPC) npc;
            if (npc != null) {
                NMS.clearPathfinderGoals(this);
                ((Horse) getBukkitEntity()).setDomestication(((Horse) getBukkitEntity()).getMaxDomestication());
            }
        }


        @Override
        public void a(boolean flag) {
            float oldw = width;
            float oldl = length;
            super.a(flag);
            if (oldw != width || oldl != length) {
                this.setPosition(locX - 0.01, locY, locZ - 0.01);
                this.setPosition(locX + 0.01, locY, locZ + 0.01);
            }
        }


        @Override
        protected void a(double d0, boolean flag, IBlockData block, BlockPosition blockposition) {
            if (npc == null || !npc.isFlyable()) {
                super.a(d0, flag, block, blockposition);
            }
        }


        @Override
        protected SoundEffect d(DamageSource damagesource) {
            return SoundEffect.a.get(new MinecraftKey(NMS.getSoundEffect(npc, Objects.requireNonNull(super.d(damagesource)).toString(), NPC.HURT_SOUND_METADATA)));
        }

        @Override
        protected SoundEffect cf() {
            return SoundEffect.a.get(new MinecraftKey(NMS.getSoundEffect(npc, Objects.requireNonNull(super.cf()).toString(), NPC.DEATH_SOUND_METADATA)));
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
            if (npc != null) {
                Utils.callCollisionEvent(npc, entity.getBukkitEntity());
            }
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
        public void B_() {
            if (npc == null) {
                super.B_();
            } else {
                NMS.setStepHeight(this, 1);
                npc.update();
                npc.tick();
            }
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
        public void a(float f, float f1, float f2) {
            if (npc == null || !npc.isFlyable()) {
                super.a(f, f1, f2);
            } else {
                NMSImpl.flyingMoveLogic(this, f, f1, f2);
            }
        }

        @Override
        public CraftEntity getBukkitEntity() {
            if (npc != null && !(bukkitEntity instanceof NPCHolder))
                bukkitEntity = new HorseNPC(this);
            return super.getBukkitEntity();
        }

        @Override
        public NPC getNPC() {
            return npc;
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
        protected SoundEffect F() {
            return SoundEffect.a.get(new MinecraftKey(NMS.getSoundEffect(npc, Objects.requireNonNull(super.F()).toString(), NPC.AMBIENT_SOUND_METADATA)));
        }
    }

    public static class HorseNPC extends CraftHorse implements NPCHolder {
        private final AbstractNPC npc;

        public HorseNPC(EntityHorseNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.npc = entity.npc;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}