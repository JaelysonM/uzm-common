package com.uzm.common.nms.version.v1_12_R1.network;


import com.uzm.common.nms.version.v1_12_R1.entity.EntityNPCPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PlayerConnection;

/**
 * @author Maxter
 */
public class EmptyNetHandler extends PlayerConnection {

  public EmptyNetHandler(EntityNPCPlayer entityplayer) {
    super(entityplayer.server, new EmptyNetworkManager(), entityplayer);
  }

  @Override
  public void sendPacket(Packet packet) {
    // nao envie packets para um NPC.
  }
}