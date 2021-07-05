package com.uzm.common.spigot.entities.beam;

import com.google.common.base.Preconditions;
import com.uzm.common.plugin.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class Beam {
  private final UUID worldUID;
  
  private final double viewingRadiusSquared;
  
  private final long updateDelay;
  
  private boolean isActive;
  
  private final LocationTargetBeam beam;
  
  private Location startingPosition;
  
  private Location endingPosition;
  
  private final Set<UUID> viewers;
  
  private BukkitRunnable runnable;
  
  public Beam(Location startingPosition, Location endingPosition) {
    this(startingPosition, endingPosition, 100.0D, 5L);
  }
  
  public Beam(Location startingPosition, Location endingPosition, double viewingRadius, long updateDelay) {
    Preconditions.checkNotNull(startingPosition, "startingPosition cannot be null");
    Preconditions.checkNotNull(endingPosition, "endingPosition cannot be null");
    Preconditions.checkState(startingPosition.getWorld().equals(endingPosition.getWorld()), "startingPosition and endingPosition must be in the same world");
    Preconditions.checkArgument((viewingRadius > 0.0D), "viewingRadius must be positive");
    Preconditions.checkArgument((updateDelay >= 1L), "viewingRadius must be a natural number");
    this.worldUID = startingPosition.getWorld().getUID();
    this.viewingRadiusSquared = viewingRadius * viewingRadius;
    this.updateDelay = updateDelay;
    this.isActive = false;
    this.beam = new LocationTargetBeam(startingPosition, endingPosition);
    this.startingPosition = startingPosition;
    this.endingPosition = endingPosition;
    this.viewers = new HashSet<>();
  }
  
  public void start() {
    Preconditions.checkState(!this.isActive, "The beam must be disabled in order to start it");
    this.isActive = true;
    (this.runnable = new BeamUpdater()).runTaskTimer(Common.getInstance(), 0L, this.updateDelay);
  }
  
  public void stop() {
    Preconditions.checkState(this.isActive, "The beam must be enabled in order to stop it");
    this.isActive = false;
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player != null && player.getWorld().getUID().equals(this.worldUID) && isCloseEnough(player.getLocation()))
        this.beam.cleanup(player); 
    } 
    this.viewers.clear();
    this.runnable.cancel();
    this.runnable = null;
  }
  
  public void setStartingPosition(Location location) {
    Preconditions.checkArgument(location.getWorld().getUID().equals(this.worldUID), "location must be in the same world as this beam");
    this.startingPosition = location;
    Iterator<UUID> iterator = this.viewers.iterator();
    while (iterator.hasNext()) {
      UUID uuid = iterator.next();
      Player player = Bukkit.getPlayer(uuid);
      if (player == null || !player.isOnline() || !player.getWorld().getUID().equals(this.worldUID) || !isCloseEnough(player.getLocation())) {
        iterator.remove();
        continue;
      } 
      this.beam.setStartingPosition(player, location);
    } 
  }
  
  public void setEndingPosition(Location location) {
    Preconditions.checkArgument(location.getWorld().getUID().equals(this.worldUID), "location must be in the same world as this beam");
    this.endingPosition = location;
    Iterator<UUID> iterator = this.viewers.iterator();
    while (iterator.hasNext()) {
      UUID uuid = iterator.next();
      Player player = Bukkit.getPlayer(uuid);
      if (!player.isOnline() || !player.getWorld().getUID().equals(this.worldUID) || !isCloseEnough(player.getLocation())) {
        iterator.remove();
        continue;
      } 
      this.beam.setEndingPosition(player, location);
    } 
  }
  
  public void update() {
    if (this.isActive)
      for (Player player : Bukkit.getOnlinePlayers()) {
        UUID uuid = player.getUniqueId();
        if (!player.getWorld().getUID().equals(this.worldUID)) {
          this.viewers.remove(uuid);
          return;
        } 
        if (isCloseEnough(player.getLocation())) {
          if (!this.viewers.contains(uuid)) {
            this.beam.start(player);
            this.viewers.add(uuid);
          } 
          continue;
        } 
        if (this.viewers.contains(uuid)) {
          this.beam.cleanup(player);
          this.viewers.remove(uuid);
        } 
      }  
  }
  
  public boolean isActive() {
    return this.isActive;
  }
  
  public boolean isViewing(Player player) {
    return this.viewers.contains(player.getUniqueId());
  }
  
  private boolean isCloseEnough(Location location) {
    return (this.startingPosition.distanceSquared(location) <= this.viewingRadiusSquared || this.endingPosition.distanceSquared(location) <= this.viewingRadiusSquared);
  }
  
  private class BeamUpdater extends BukkitRunnable {
    private BeamUpdater() {}
    
    public void run() {
      Beam.this.update();
    }
  }
}
