package com.uzm.common.nms.version.v1_12_R1.nbt;


import com.uzm.common.nms.nbt.NBTCompound;
import com.uzm.common.nms.nbt.NBTEntity;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;

public class NBTEntityImpl extends NBTCompoundImpl implements NBTEntity {

    private static Field fieldG;

    private Entity nmsEntity;

    public NBTEntityImpl(NBTTagCompound entityNBT, Entity nmsEntity) {
        super(entityNBT);
        this.nmsEntity = nmsEntity;
    }

    static {
        try {
            fieldG = EntityTypes.class.getDeclaredField("g");
            fieldG.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    @Override
    public org.bukkit.entity.Entity spawn(Location location) {
        String entityType = getNBTObject("entity_type").asString();

        Entity spawned = ItemMonsterEgg.spawnCreature( // Changed since 1.14
                ((CraftWorld) location.getWorld()).getHandle(),
                new MinecraftKey(entityType), // Parameter simplified in 1.10
                location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                CreatureSpawnEvent.SpawnReason.DEFAULT
        );

        if (spawned != null) {
            spawned.f(compound); // This changed from 1.16.1
            org.bukkit.entity.Entity entity = spawned.getBukkitEntity();
            spawned.setLocation(location.getX(), location.getY(), location.getZ(),
                    location.getPitch(), location.getYaw());
            nmsEntity = spawned;
            return entity;
        }
        return null;
    }

    @Override
    public org.bukkit.entity.Entity reSpawn(Location location) {
        nmsEntity.dead = true;
        return spawn(location);
    }

    @Override
    public NBTCompound set(String tag, byte[] b) {
        compound.setByteArray(tag, b);
        return this;
    }

    @Override
    public byte[] getByteArray(String tag) {
        return new byte[0];
    }

    @Override
    public void addExtras() {
        String key = EntityTypes.b(nmsEntity); // Changed in 1.12
        if (key != null)
            compound.setString("entity_type", key); // Changed in 1.13
    }
}