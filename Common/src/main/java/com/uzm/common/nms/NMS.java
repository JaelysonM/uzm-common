package com.uzm.common.nms;


import com.uzm.common.libraries.holograms.api.Hologram;
import com.uzm.common.libraries.holograms.api.HologramLine;
import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.common.nms.interfaces.IArmorStand;
import com.uzm.common.nms.interfaces.INMS;
import com.uzm.common.spigot.features.Titles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class NMS {

    private static INMS BRIDGE;

    public static boolean setupNMS() {
        try {
            BRIDGE = (INMS) Class.forName("com.uzm.common.nms.version.v" + getMinecraftRevision() + ".NMSImpl").getConstructor()
                    .newInstance();
            return true;
        } catch (Exception err) {
            return false;
        }

    }

    private static String MINECRAFT_REVISION;

    public static String getMinecraftRevision() {
        if (MINECRAFT_REVISION == null) {
            MINECRAFT_REVISION = Bukkit.getServer().getClass().getPackage().getName();
        }
        return MINECRAFT_REVISION.substring(MINECRAFT_REVISION.lastIndexOf('.') + 2);
    }

    public static ItemStack glow(ItemStack stackToGlow) {
        return BRIDGE.glow(stackToGlow);
    }

    public static void sendTitle(Player player, Titles.TitleType type, String bottom, String top, int fadeIn, int stayTime, int fadeOut) {
        BRIDGE.sendTitle(player, type, bottom, top, fadeIn, stayTime, fadeOut);
    }

    public static void sendActionBar(Player player, String message) {
        BRIDGE.sendActionBar(player, message);
    }

    public static void sendTabColor(Player player, String footer, String bottom) {
        BRIDGE.sendTabColor(player, footer, bottom);
    }

    public static boolean addToWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return BRIDGE.addToWorld(world, entity, reason);
    }

    public static boolean addEntityToWorld(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return BRIDGE.addEntityToWorld(entity, reason);
    }

    public static void setValueAndSignature(Player player, String value, String signature) {
        BRIDGE.setValueAndSignature(player, value, signature);
    }

    public static void registerEntityClass(Class<?> clazz) {
        BRIDGE.registerEntityClass(clazz);
    }


    public static void sendPacket(Player player, Object packet) {
        BRIDGE.sendPacket(player, packet);
    }

    public static void sendTabListAdd(Player player, Player listPlayer) {
        BRIDGE.sendTabListAdd(player, listPlayer);
    }

    public static void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities) {
        BRIDGE.sendTabListRemove(player, skinnableEntities);
    }

    public static void sendTabListRemove(Player player, Player listPlayer) {
        BRIDGE.sendTabListRemove(player, listPlayer);
    }

    public static SkinnableEntity getSkinnable(Entity entity) {
        return BRIDGE.getSkinnable(entity);
    }


    public static void setHeadYaw(Entity entity, float yaw) {
        BRIDGE.setHeadYaw(entity, yaw);
    }

    public static void setBodyYaw(Entity entity, float yaw) {
        BRIDGE.setBodyYaw(entity, yaw);
    }

    public static void setStepHeight(Object entity, float height) {
        BRIDGE.setStepHeight(entity, height);
    }

    public static float getStepHeight(LivingEntity entity) {
        return BRIDGE.getStepHeight(entity);
    }

    public static void replaceTrackerEntry(Player player) {
        BRIDGE.replaceTrackerEntry(player);
    }


    public static void removeFromPlayerList(Player type) {
        BRIDGE.removeFromPlayerList(type);
    }

    public static void updateAI(Object entity) {
        BRIDGE.updateAI(entity);
    }

    public static void updateNavigation(Object navigation) {
        BRIDGE.updateNavigation(navigation);
    }

    public static boolean isNavigationFinished(Object navigation) {
        return BRIDGE.isNavigationFinished(navigation);
    }


    public static void removeFromServerPlayerList(Player player) {
        BRIDGE.removeFromServerPlayerList(player);
    }

    public static void look(Entity entity, float yaw, float pitch) {
        BRIDGE.look(entity, yaw, pitch);
    }

    public static void look(Entity from, Entity to) {
        BRIDGE.look(from, to);
    }

    public static void look(Entity entity, Location to, boolean headOnly, boolean immediate) {
        BRIDGE.look(entity, to, headOnly, immediate);
    }

    public static void removeFromWorld(Entity entity) {
        BRIDGE.removeFromWorld(entity);
    }

    public static void playAnimation(Entity entity, int id) {
        BRIDGE.playAnimation(entity, id);
    }

    public static void sendPacketNearby(Player from, Location location, Object packet, double radius) {
        BRIDGE.sendPacketNearby(from, location, packet, radius);
    }

    public static void sendPacketsNearby(Player from, Location location, Collection<Object> packet, double radius) {
        BRIDGE.sendPacketsNearby(from, location, packet, radius);
    }

    public static void refreshNPCSlot(Entity entity, int slot, ItemStack stack) {
        BRIDGE.refreshNPCSlot(entity, slot, stack);
    }

    public static IArmorStand createArmorStand(Location location, String name, HologramLine line) {
        return BRIDGE.createArmorStand(location, name, line);
    }

    public static Hologram getHologram(Entity entity) {
        return BRIDGE.getHologram(entity);
    }

    public static boolean isHologramEntity(Entity entity) {
        return BRIDGE.isHologramEntity(entity);
    }

    public static String[] getPlayerTextures(Player player) {
        return BRIDGE.getPlayerTextures(player);
    }


    public static void clearPathfinderGoals(Object entity) {
        BRIDGE.clearPathfinderGoals(entity);
    }

    public static void silentEntity(Object entity) {
        BRIDGE.silentEntity(entity);
    }


    public static String getSoundEffect(NPC npc, String snd, String meta) {
        return BRIDGE.getSoundEffect(npc, snd, meta);
    }

    public static void setSize(Object entity, float f, float f1, boolean justCreated) {
        BRIDGE.setSize(entity, f, f1, justCreated);
    }


}
