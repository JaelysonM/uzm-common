package com.uzm.common.libraries.npclib.npc;

import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.api.npc.EntityController;
import com.uzm.common.nms.NMS;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * @author Maxter
 */
public abstract class AbstractEntityController implements EntityController{

  private Entity bukkitEntity;

  public AbstractEntityController() {
  }

  public AbstractEntityController(Class<?> clazz) {
    NMS.registerEntityClass(clazz);
  }

  protected abstract Entity createEntity(Location location, NPC npc);

  @Override
  public void spawn(Location location, NPC npc) {
    this.bukkitEntity = createEntity(location, npc);
  }

  @Override
  public void remove() {
    if (this.bukkitEntity != null) {
      this.bukkitEntity.remove();
      this.bukkitEntity = null;
    }
  }

  @Override
  public Entity getBukkitEntity() {
    return this.bukkitEntity;
  }
}
