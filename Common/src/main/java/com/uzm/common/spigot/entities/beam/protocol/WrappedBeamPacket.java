package com.uzm.common.spigot.entities.beam.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class WrappedBeamPacket {
  private final PacketContainer handle;
  
  public WrappedBeamPacket(PacketContainer container) {
    this.handle = container;
  }
  
  public void send(Player receiver) {
    try {
      ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, this.handle);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException("Failed to send beam packet to player.", ex);
    } 
  }
  
  public PacketContainer getHandle() {
    return this.handle;
  }
}
