package com.uzm.common.nms.interfaces;

import com.uzm.common.libraries.holograms.api.Hologram;
import com.uzm.common.libraries.holograms.api.HologramLine;
import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.common.spigot.features.Titles;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface INMS {

    ItemStack glow(ItemStack stackToGlow);

    void sendTitle(Player player, Titles.TitleType type, String bottom, String top, int fadeIn, int stayTime, int fadeOut);

    void sendActionBar(Player player, String message);

    void sendTabColor(Player player, String footer, String bottom);

    boolean addToWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason);

    boolean addEntityToWorld(Entity entity, CreatureSpawnEvent.SpawnReason custom);

    void registerEntityClass(Class<?> clazz);


    void setValueAndSignature(Player player, String value, String signature);

    void sendTabListAdd(Player player, Player listPlayer);

    void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities);

    void sendTabListRemove(Player player, Player listPlayer);

    void sendPacket(Player player, Object packet);

    SkinnableEntity getSkinnable(Entity entity);

    void setHeadYaw(Entity entity, float yaw);

    void setBodyYaw(Entity entity, float yaw);

    void setStepHeight(Object entity, float height);

    float getStepHeight(Object entity);

    void replaceTrackerEntry(Player player);

    void removeFromPlayerList(Player type);

    void removeFromServerPlayerList(Player player);

    void setSize(Object entity, float f, float f1, boolean justCreated);


    void removeFromWorld(Entity entity);


    void playAnimation(Entity entity, int id);

    void sendPacketNearby(Player from, Location location, Object packet, double radius);

    void sendPacketsNearby(Player from, Location location, Collection<Object> packets, double radius);

    void refreshNPCSlot(Entity entity, int slot, ItemStack stack);

    void look(Entity entity, float yaw, float pitch);

    boolean isHologramEntity(Entity entity);

    boolean isNavigationFinished(Object navigation);


    Hologram getHologram(Entity entity);

    IArmorStand createArmorStand(Location location, String name, HologramLine line);

    String[] getPlayerTextures(Player player);

    void look(Entity entity, Location to, boolean headOnly, boolean immediate);

    void look(Entity from, Entity to);

    void updateNavigation(Object navigation);

    void updateAI(Object entity);

    void clearPathfinderGoals(Object entity);

    void silentEntity(Object entity);

    void refreshPlayer(Player player);

    String getSoundEffect(NPC npc, String snd, String meta);


}

