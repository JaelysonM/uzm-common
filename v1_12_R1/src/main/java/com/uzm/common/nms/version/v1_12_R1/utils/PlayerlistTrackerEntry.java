package com.uzm.common.nms.version.v1_12_R1.utils;

import com.uzm.common.nms.NMS;
import com.uzm.common.plugin.Common;
import com.uzm.common.reflections.Accessors;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityTrackerEntry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PlayerlistTrackerEntry extends EntityTrackerEntry {
    private EntityPlayer lastUpdatedPlayer;

    public PlayerlistTrackerEntry(Entity entity, int i, int j, int k, boolean flag) {
        super(entity, i, j, k, flag);
    }

    public PlayerlistTrackerEntry(EntityTrackerEntry entry) {
        this(getTracker(entry), getE(entry), getF(entry), getG(entry), getU(entry));
    }

    public boolean isUpdating() {
        return lastUpdatedPlayer != null;
    }


    @Override
    public void updatePlayer(final EntityPlayer entityplayer) {
        // prevent updates to NPC "viewers"
        if (entityplayer instanceof Entity)
            return;
        lastUpdatedPlayer = entityplayer;
        super.updatePlayer(entityplayer);
        lastUpdatedPlayer = null;
    }

    private static int getE(EntityTrackerEntry entry) {
        try {
            return (Integer) E.get(entry);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getF(EntityTrackerEntry entry) {
        try {
            return (Integer) F.get(entry);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getG(EntityTrackerEntry entry) {
        try {
            return (Integer) G.get(entry);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static Entity getTracker(EntityTrackerEntry entry) {
        try {
            return (Entity) TRACKER.get(entry);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean getU(EntityTrackerEntry entry) {
        try {
            return (Boolean) U.get(entry);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Field E = Accessors.getField(EntityTrackerEntry.class, "e").getHandle();
    private static Field F = Accessors.getField(EntityTrackerEntry.class, "f").getHandle();
    private static Field G = Accessors.getField(EntityTrackerEntry.class, "g").getHandle();
    private static Field TRACKER = Accessors.getField(EntityTrackerEntry.class, "tracker").getHandle();
    private static Field U = Accessors.getField(EntityTrackerEntry.class, "u").getHandle();
}