package com.uzm.common.libraries.npclib.npc;

import com.uzm.common.libraries.npclib.api.npc.EntityController;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maxter
 */
public class EntityControllers {
  
  private static final Map<EntityType, Class<? extends EntityController>> controllers = new HashMap<>();
  
  public static void registerEntityController(EntityType type, Class<? extends EntityController> controller) {
    controllers.put(type, controller);
  }
  
  public static EntityController getController(EntityType type) {
    Class<? extends EntityController> clazz = controllers.get(type);
    if (clazz == null) {
      throw new IllegalArgumentException("Tipo nao disponivel de NPC: " + type.name());
    }
    
    try {
      return clazz.newInstance();
    } catch (ReflectiveOperationException e) {
      e.printStackTrace();
      return null;
    }
  }
}
