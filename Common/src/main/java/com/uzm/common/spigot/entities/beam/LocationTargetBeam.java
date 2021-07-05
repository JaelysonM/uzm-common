package com.uzm.common.spigot.entities.beam;

import com.google.common.base.Preconditions;
import com.uzm.common.spigot.entities.beam.protocol.PacketFactory;
import com.uzm.common.spigot.entities.beam.protocol.WrappedBeamPacket;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationTargetBeam {
    private final WrappedBeamPacket packetSquidSpawn;

    private final WrappedBeamPacket packetSquidMove;

    private final WrappedBeamPacket packetGuardianSpawn;

    private final WrappedBeamPacket packetGuardianMove;

    private final WrappedBeamPacket packetRemoveEntities;

    public LocationTargetBeam(Location startingPosition, Location endingPosition) {
        Preconditions.checkNotNull(startingPosition, "startingPosition cannot be null");
        Preconditions.checkNotNull(endingPosition, "endingPosition cannot be null");
        Preconditions.checkState(startingPosition.getWorld().equals(endingPosition.getWorld()), "startingPosition and endingPosition must be in the same world");
        this.packetSquidSpawn = PacketFactory.createPacketSquidSpawn(startingPosition);
        this.packetSquidMove = PacketFactory.createPacketEntityMove(this.packetSquidSpawn);
        this.packetGuardianSpawn = PacketFactory.createPacketGuardianSpawn(endingPosition, this.packetSquidSpawn);
        this.packetGuardianMove = PacketFactory.createPacketEntityMove(this.packetGuardianSpawn);
        this.packetRemoveEntities = PacketFactory.createPacketRemoveEntities(this.packetSquidSpawn, this.packetGuardianSpawn);
    }

    public void start(Player player) {
        this.packetSquidSpawn.send(player);
        this.packetGuardianSpawn.send(player);
    }

    public void setStartingPosition(Player player, Location location) {
        PacketFactory.modifyPacketEntitySpawn(this.packetSquidSpawn, location);
        PacketFactory.modifyPacketEntityMove(this.packetSquidMove, location).send(player);
    }

    public void setEndingPosition(Player player, Location location) {
        PacketFactory.modifyPacketEntitySpawn(this.packetGuardianSpawn, location);
        PacketFactory.modifyPacketEntityMove(this.packetGuardianMove, location).send(player);
    }

    public void cleanup(Player player) {
        this.packetRemoveEntities.send(player);
    }
}
