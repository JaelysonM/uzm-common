package com.uzm.common.nms.version.v1_12_R1.network;

import net.minecraft.server.v1_12_R1.EnumProtocolDirection;
import net.minecraft.server.v1_12_R1.NetworkManager;

import java.net.SocketAddress;

/**
 * @author Maxter
 */
public class EmptyNetworkManager extends NetworkManager {

  public EmptyNetworkManager() {
    super(EnumProtocolDirection.CLIENTBOUND);
    this.channel = new EmptyChannel();
    this.l = new SocketAddress() {
      private static final long serialVersionUID = 7794407580553892140L;
    };
  }
}