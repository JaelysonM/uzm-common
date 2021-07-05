package com.uzm.common.spigot.entities.beam.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;

public class PacketFactory {
  public static WrappedBeamPacket createPacketSquidSpawn(Location location) {
    PacketContainer container = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
    container.getIntegers().write(0, EIDGen.generateEID());
    container.getIntegers().write(1, 94);
    container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0D));
    container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0D));
    container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0D));
    container.getBytes().write(0, (byte) (int) (location.getYaw() * 256.0F / 360.0F));
    container.getBytes().write(1, (byte) (int) (location.getPitch() * 256.0F / 360.0F));
    WrappedDataWatcher watcher = new WrappedDataWatcher();
    watcher.setObject(0, (byte) 32);
    container.getDataWatcherModifier().write(0, watcher);
    return new WrappedBeamPacket(container);
  }
  
  public static WrappedBeamPacket createPacketGuardianSpawn(Location location, WrappedBeamPacket squidPacket) {
    PacketContainer container = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
    container.getIntegers().write(0, EIDGen.generateEID());
    container.getIntegers().write(1, 68);
    container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0D));
    container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0D));
    container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0D));
    container.getBytes().write(0, (byte) (int) (location.getYaw() * 256.0F / 360.0F));
    container.getBytes().write(1, (byte) (int) (location.getPitch() * 256.0F / 360.0F));
    WrappedDataWatcher watcher = new WrappedDataWatcher();
    watcher.setObject(0, (byte) 32);
    watcher.setObject(16, 0);
    watcher.setObject(17, squidPacket.getHandle().getIntegers().read(0));
    container.getDataWatcherModifier().write(0, watcher);
    return new WrappedBeamPacket(container);
  }
  
  public static WrappedBeamPacket modifyPacketEntitySpawn(WrappedBeamPacket entitySpawnPacket, Location location) {
    PacketContainer container = entitySpawnPacket.getHandle();
    container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0D));
    container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0D));
    container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0D));
    container.getBytes().write(0, (byte) (int) (location.getYaw() * 256.0F / 360.0F));
    container.getBytes().write(1, (byte) (int) (location.getPitch() * 256.0F / 360.0F));
    return entitySpawnPacket;
  }
  
  public static WrappedBeamPacket createPacketEntityMove(WrappedBeamPacket entityPacket) {
    PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
    container.getIntegers().write(0, entityPacket.getHandle().getIntegers().read(0));
    return new WrappedBeamPacket(container);
  }
  
  public static WrappedBeamPacket modifyPacketEntityMove(WrappedBeamPacket entityMovePacket, Location location) {
    PacketContainer container = entityMovePacket.getHandle();
    container.getIntegers().write(1, (int) Math.floor(location.getX() * 32.0D));
    container.getIntegers().write(2, (int) Math.floor(location.getY() * 32.0D));
    container.getIntegers().write(3, (int) Math.floor(location.getZ() * 32.0D));
    container.getBytes().write(0, (byte) (int) (location.getYaw() * 256.0F / 360.0F));
    container.getBytes().write(1, (byte) (int) (location.getPitch() * 256.0F / 360.0F));
    return entityMovePacket;
  }
  
  public static WrappedBeamPacket createPacketRemoveEntities(WrappedBeamPacket squidPacket, WrappedBeamPacket guardianPacket) {
    PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
    container.getIntegerArrays().write(0, new int[] {squidPacket.getHandle().getIntegers().read(0), ((Integer)guardianPacket.getHandle().getIntegers().read(0)).intValue() });
    return new WrappedBeamPacket(container);
  }
}
