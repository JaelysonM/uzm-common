package com.uzm.common.nms.version.v1_12_R1;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.common.java.util.MathUtils;
import com.uzm.common.libraries.holograms.api.Hologram;
import com.uzm.common.libraries.holograms.api.HologramLine;
import com.uzm.common.libraries.npclib.api.NPC;
import com.uzm.common.libraries.npclib.npc.EntityControllers;
import com.uzm.common.libraries.npclib.npc.ai.NPCHolder;
import com.uzm.common.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.common.nms.NMS;
import com.uzm.common.nms.interfaces.IArmorStand;
import com.uzm.common.nms.interfaces.INMS;
import com.uzm.common.nms.utils.Utils;
import com.uzm.common.nms.version.v1_12_R1.entity.EntityArmorStand;
import com.uzm.common.nms.version.v1_12_R1.entity.EntityNPCPlayer;
import com.uzm.common.nms.version.v1_12_R1.entity.EntityStand;
import com.uzm.common.nms.version.v1_12_R1.entity.HumanController;
import com.uzm.common.nms.version.v1_12_R1.entity.npcs.*;
import com.uzm.common.nms.version.v1_12_R1.utils.PlayerlistTrackerEntry;
import com.uzm.common.nms.version.v1_12_R1.utils.UUIDMetadataStore;
import com.uzm.common.plugin.Common;
import com.uzm.common.plugin.logger.CustomLogger;
import com.uzm.common.reflections.Accessors;
import com.uzm.common.reflections.acessors.FieldAccessor;
import com.uzm.common.spigot.features.Titles;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.metadata.PlayerMetadataStore;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NMSImpl implements INMS {
    private final FieldAccessor<Set> SET_TRACKERS;


    private static final Location FROM_LOCATION = new Location(null, 0, 0, 0);
    private static final Set<EntityType> BAD_CONTROLLER_LOOK =
            EnumSet.of(EntityType.SILVERFISH, EntityType.ENDERMITE, EntityType.ENDER_DRAGON, EntityType.BAT, EntityType.SLIME, EntityType.MAGMA_CUBE, EntityType.HORSE, EntityType.GHAST);


    private static Field TRACKED_ENTITY_SET;
    private static Map<Class<?>, Integer> ENTITY_CLASS_TO_INT;
    private static Map<Class<?>, String> ENTITY_CLASS_TO_NAME;
    private static FieldAccessor<List> PATHFINDERGOAL_B;
    private static FieldAccessor<List> PATHFINDERGOAL_C;


    static {
        PATHFINDERGOAL_B = Accessors.getField(PathfinderGoalSelector.class, 0, List.class);
        PATHFINDERGOAL_C = Accessors.getField(PathfinderGoalSelector.class, 1, List.class);

        try {
            Field field = Accessors.getField(EntityTypes.class, "f").getHandle();
            ENTITY_CLASS_TO_INT = (Map<Class<?>, Integer>) field.get(null);
            field = Accessors.getField(EntityTypes.class, "d").getHandle();
            ENTITY_CLASS_TO_NAME = (Map<Class<?>, String>) field.get(null);
        } catch (Exception e) {
            ((CustomLogger) Common.getInstance().getLogger()).getModule("NMS").log(Level.SEVERE, "uzm-common.nms-errors.getting-id-mapping", e);
        }
        try {
            TRACKED_ENTITY_SET = Accessors.getField(EntityTracker.class, "c").getHandle();
        } catch (Exception e) {
            ((CustomLogger) Common.getInstance().getLogger()).getModule("NMS").log(Level.SEVERE, "uzm-common.nms-errors.getting-track-entity-set", e);

        }
    }

    public NMSImpl() {
        SET_TRACKERS = Accessors.getField(EntityTracker.class, "c", Set.class);

        FieldAccessor<PlayerMetadataStore> metadatastore = Accessors.getField(CraftServer.class, "playerMetadata", PlayerMetadataStore.class);
        if (!(metadatastore.get(Bukkit.getServer()) instanceof UUIDMetadataStore)) {
            metadatastore.set(Bukkit.getServer(), new UUIDMetadataStore());
        }

        loadEntityTypes();
    }


    protected void loadEntityTypes() {
        EntityControllers.registerEntityController(EntityType.PLAYER, HumanController.class);
        EntityControllers.registerEntityController(EntityType.BLAZE, BlazeController.class);
        EntityControllers.registerEntityController(EntityType.ZOMBIE, ZombieController.class);
        EntityControllers.registerEntityController(EntityType.WITHER, WitherController.class);
        EntityControllers.registerEntityController(EntityType.WITCH, WitchController.class);
        EntityControllers.registerEntityController(EntityType.VILLAGER, VillagerController.class);
        EntityControllers.registerEntityController(EntityType.SKELETON, SkeletonController.class);
        EntityControllers.registerEntityController(EntityType.CREEPER, CreeperController.class);
        EntityControllers.registerEntityController(EntityType.ENDERMAN, EndermanController.class);
        EntityControllers.registerEntityController(EntityType.GUARDIAN, GuardianController.class);
        EntityControllers.registerEntityController(EntityType.HORSE, HorseController.class);
        EntityControllers.registerEntityController(EntityType.IRON_GOLEM, IronGolemController.class);
        EntityControllers.registerEntityController(EntityType.MAGMA_CUBE, MagmaCubeController.class);
        EntityControllers.registerEntityController(EntityType.MUSHROOM_COW, MushroomCowController.class);
        EntityControllers.registerEntityController(EntityType.PIG, PigController.class);
        EntityControllers.registerEntityController(EntityType.PIG_ZOMBIE, PigZombieController.class);
        EntityControllers.registerEntityController(EntityType.SLIME, SlimeController.class);
    }

    @Override
    public void registerEntityClass(Class<?> clazz) {
        if (ENTITY_CLASS_TO_INT == null || ENTITY_CLASS_TO_INT.containsKey(clazz))
            return;
        Class<?> search = clazz;
        while ((search = search.getSuperclass()) != null && Entity.class.isAssignableFrom(search)) {
            if (!ENTITY_CLASS_TO_INT.containsKey(search))
                continue;
            int code = ENTITY_CLASS_TO_INT.get(search);
            ENTITY_CLASS_TO_INT.put(clazz, code);
            ENTITY_CLASS_TO_NAME.put(clazz, ENTITY_CLASS_TO_NAME.get(search));
            return;
        }
        throw new IllegalArgumentException("unable to find valid entity superclass for class " + clazz.toString());
    }


    @Override
    public void look(org.bukkit.entity.Entity entity, float yaw, float pitch) {
        Entity handle = getHandle(entity);
        if (handle == null)
            return;
        yaw = Utils.clampYaw(yaw);
        handle.yaw = yaw;
        setHeadYaw(entity, yaw);
        handle.pitch = pitch;
    }

    @Override
    public void silentEntity(Object entity) {
        getHandle((org.bukkit.entity.Entity) entity).b(true);
    }


    @Override
    public void look(org.bukkit.entity.Entity entity, Location to, boolean headOnly, boolean immediate) {
        Entity handle = getHandle(entity);
        if (immediate || headOnly || BAD_CONTROLLER_LOOK
                .contains(handle.getBukkitEntity().getType()) || (!(handle instanceof EntityInsentient) && !(handle instanceof EntityNPCPlayer))) {
            Location fromLocation = entity.getLocation(FROM_LOCATION);
            double xDiff, yDiff, zDiff;
            xDiff = to.getX() - fromLocation.getX();
            yDiff = to.getY() - fromLocation.getY();
            zDiff = to.getZ() - fromLocation.getZ();

            double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
            double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

            double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
            double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90;
            if (zDiff < 0.0)
                yaw += Math.abs(180 - yaw) * 2;
            if (handle instanceof EntityEnderDragon) {
                yaw = getDragonYaw(handle, to.getX(), to.getZ());
            } else {
                yaw = yaw - 90;
            }
            if (headOnly) {
                setHeadYaw(entity, (float) yaw);
            } else {
                look(entity, (float) yaw, (float) pitch);
            }
            return;
        }
        if (handle instanceof EntityInsentient) {
            ((EntityInsentient) handle).getControllerLook().a(to.getX(), to.getY(), to.getZ(), 10, ((EntityInsentient) handle).N());
            while (((EntityInsentient) handle).aK >= 180F) {
                ((EntityInsentient) handle).aK -= 360F;
            }
            while (((EntityInsentient) handle).aK < -180F) {
                ((EntityInsentient) handle).aK += 360F;
            }
        } else {
            ((EntityNPCPlayer) handle).setTargetLook(to);
        }
    }

    @Override
    public void look(org.bukkit.entity.Entity from, org.bukkit.entity.Entity to) {
        Entity handle = getHandle(from), target = getHandle(to);
        if (BAD_CONTROLLER_LOOK.contains(handle.getBukkitEntity().getType())) {
            if (to instanceof LivingEntity) {
                look(from, ((LivingEntity) to).getEyeLocation(), false, true);
            } else {
                look(from, to.getLocation(), false, true);
            }
        } else if (handle instanceof EntityInsentient) {
            ((EntityInsentient) handle).getControllerLook().a(target, 10, ((EntityInsentient) handle).N());
            while (((EntityLiving) handle).aK >= 180F) {
                ((EntityLiving) handle).aK -= 360F;
            }
            while (((EntityLiving) handle).aK < -180F) {
                ((EntityLiving) handle).aK += 360F;
            }
        } else if (handle instanceof EntityNPCPlayer) {
            ((EntityNPCPlayer) handle).setTargetLook(target, 10F, 40F);
        }
    }

    public void updateAI(Object entity) {
        ((EntityNPCPlayer) entity).updateAI();

    }

    public String getSoundEffect(NPC npc, String snd, String meta) {
        return npc == null || !npc.data().has(meta) ? snd : npc.data().get(meta, snd == null ? "" : snd);
    }

    @Override
    public void clearPathfinderGoals(Object entity) {
        if (entity instanceof org.bukkit.entity.Entity) {
            entity = ((CraftEntity) entity).getHandle();
        }

        Entity handle = (Entity) entity;
        if (handle instanceof EntityInsentient) {
            EntityInsentient entityInsentient = (EntityInsentient) handle;
            PATHFINDERGOAL_B.get(entityInsentient.goalSelector).clear();
            PATHFINDERGOAL_C.get(entityInsentient.targetSelector).clear();
        }
    }

    @Override
    public void refreshPlayer(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();

        int entId = ep.getId();

        PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);
        PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);
        PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(player.getItemInHand()));
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.FEET, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
        PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players instanceof NPCHolder) {
                continue;
            }
            EntityPlayer eps = ((CraftPlayer) players).getHandle();

            PlayerConnection con = eps.playerConnection;
            if (players.equals(player)) {
                con.sendPacket(removeInfo);
                con.sendPacket(addInfo);
                con.sendPacket(slot);
                ((CraftPlayer) players).updateScaledHealth();
                eps.triggerHealthUpdate();
                if (players.isOp()) {
                    players.setOp(false);
                    player.setOp(true);
                }
                players.updateInventory();
                Bukkit.getScheduler().runTask(Common.getInstance(), ep::updateAbilities);
            } else {
                if (players.canSee(player) && players.getWorld().equals(player.getWorld())) {
                    con.sendPacket(removeEntity);
                    con.sendPacket(removeInfo);
                    con.sendPacket(addInfo);
                    con.sendPacket(addNamed);
                    con.sendPacket(itemhand);
                    con.sendPacket(helmet);
                    con.sendPacket(chestplate);
                    con.sendPacket(leggings);
                    con.sendPacket(boots);
                } else if (players.canSee(player)) {
                    con.sendPacket(removeInfo);
                    con.sendPacket(addInfo);
                }
            }
        }
    }

    @Override
    public void updateNavigation(Object navigation) {
        ((NavigationAbstract) navigation).k();
    }


    @Override
    public ItemStack glow(ItemStack stackToGlow) {
        if (stackToGlow == null)
            return null;
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stackToGlow);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null)
            tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public void sendTitle(Player player, Titles.TitleType type, String bottomMessage, String topMessage, int fadeIn, int stayTime, int fadeOut) {
        PacketPlayOutTitle top =
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(topMessage), fadeIn, stayTime, fadeOut);
        PacketPlayOutTitle bottom =
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(bottomMessage), fadeIn, stayTime, fadeOut);

        switch (type) {
            case BOTH:
                if (player != null && ((CraftPlayer) player).getHandle().playerConnection != null) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bottom);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(top);
                }

                break;
            case TITLE:
                if (player != null && ((CraftPlayer) player).getHandle().playerConnection != null)
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(top);
                break;
            default:
                if (player != null && ((CraftPlayer) player).getHandle().playerConnection != null)
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bottom);
                break;

        }

    }

    @Override
    public void sendActionBar(Player player, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        if (player != null && ((CraftPlayer) player).getHandle().playerConnection != null)
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
    }

    @Override
    public void sendTabColor(Player player, String footer, String bottom) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        IChatBaseComponent headerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        IChatBaseComponent footerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + bottom + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, headerJSON);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footerJSON);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception ignored) {
        }
        if (craftplayer != null && craftplayer.getHandle().playerConnection != null)
            craftplayer.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public boolean addToWorld(World world, org.bukkit.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.spawnIn(((CraftWorld) world).getHandle());
        return ((CraftWorld) world).getHandle().addEntity(nmsEntity, reason);
    }

    @Override
    public boolean addEntityToWorld(org.bukkit.entity.Entity entity, CreatureSpawnEvent.SpawnReason custom) {
        return getHandle(entity).world.addEntity(getHandle(entity), custom);
    }


    @Override
    public void setValueAndSignature(Player player, String value, String signature) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        if (value != null && signature != null) {
            profile.getProperties().clear();
            profile.getProperties().put("textures", new Property("textures", value, signature));
        }
    }

    @Override
    public void sendTabListAdd(Player player, Player listPlayer) {
        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) listPlayer).getHandle()));
    }


    @Override
    public void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities) {
        SkinnableEntity[] skinnables = skinnableEntities.toArray(new SkinnableEntity[0]);
        EntityPlayer[] entityPlayers = new EntityPlayer[skinnableEntities.size()];

        for (int i = 0; i < skinnables.length; i++) {
            entityPlayers[i] = (EntityPlayer) skinnables[i];
        }

        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayers));
    }

    @Override
    public void sendTabListRemove(Player player, Player listPlayer) {
        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) listPlayer).getHandle()));
    }

    @Override
    public void sendPacket(Player player, Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public SkinnableEntity getSkinnable(org.bukkit.entity.Entity entity) {
        Preconditions.checkNotNull(entity);
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof SkinnableEntity) {
            return (SkinnableEntity) nmsEntity;
        }

        return null;
    }

    @Override
    public void setBodyYaw(org.bukkit.entity.Entity entity, float yaw) {
        getHandle(entity).yaw = yaw;
    }

    @Override
    public void setHeadYaw(org.bukkit.entity.Entity entity, float yaw) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) nmsEntity;
            yaw = MathUtils.clampYaw(yaw);
            living.aK = yaw;
            if (living instanceof EntityHuman) {
                living.aN = yaw;
            }
            living.aP = yaw;
        }
    }

    @Override
    public void setStepHeight(Object entity, float height) {
        ((CraftLivingEntity) entity).getHandle().P = height;
    }

    @Override
    public float getStepHeight(Object entity) {
        return ((CraftLivingEntity) entity).getHandle().P;
    }

    @Override
    public void replaceTrackerEntry(Player player) {
        WorldServer server = ((CraftWorld) player.getWorld()).getHandle();
        EntityTrackerEntry entry = server.getTracker().trackedEntities.get(player.getEntityId());

        if (entry != null) {
            PlayerlistTrackerEntry replace = new PlayerlistTrackerEntry(entry);
            server.getTracker().trackedEntities.a(player.getEntityId(), replace);
            if (SET_TRACKERS != null) {
                Set<Object> set = SET_TRACKERS.get(server.getTracker());
                set.remove(entry);
                set.add(replace);
            }
        }
    }

    @Override
    public void removeFromPlayerList(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        ep.world.players.remove(ep);
    }

    public void setSize(Object e, float f, float f1, boolean justCreated) {
        Entity entity = (Entity) e;
        if ((f != entity.width) || (f1 != entity.length)) {
            float f2 = entity.width;

            entity.width = f;
            entity.length = f1;
            entity.a(new AxisAlignedBB(entity.getBoundingBox().a, entity.getBoundingBox().b, entity.getBoundingBox().c,
                    entity.getBoundingBox().a + entity.width, entity.getBoundingBox().b + entity.length,
                    entity.getBoundingBox().c + entity.width));
            if ((entity.width > f2) && (!justCreated) && (!entity.world.isClientSide))
                entity.move(EnumMoveType.SELF, (f2 - entity.width) / 2, 0.0D, (f2 - entity.width) / 2);
        }
    }

    public static void flyingMoveLogic(EntityLiving entity, float f, float f1, float f2) {
        if ((entity.cC()) || (entity.bI())) {
            if ((entity.isInWater())) {
                double d2 = entity.locY;
                float f4 = entity instanceof EntityPolarBear ? 0.98F : 0.8F;
                float f3 = 0.02F;
                float f5 = EnchantmentManager.e(entity);
                if (f5 > 3.0F) {
                    f5 = 3.0F;
                }
                if (!entity.onGround) {
                    f5 *= 0.5F;
                }
                if (f5 > 0.0F) {
                    f4 += (0.54600006F - f4) * f5 / 3.0F;
                    f3 += (entity.cy() - f3) * f5 / 3.0F;
                }
                entity.b(f, f1, f2, f3);
                entity.move(EnumMoveType.SELF, entity.motX, entity.motY, entity.motZ);
                entity.motX *= f4;
                entity.motY *= 0.800000011920929D;
                entity.motZ *= f4;
                if (!entity.isNoGravity()) {
                    entity.motY -= 0.02D;
                }
                if ((entity.positionChanged)
                        && (entity.c(entity.motX, entity.motY + 0.6000000238418579D - entity.locY + d2, entity.motZ))) {
                    entity.motY = 0.30000001192092896D;
                }
            } else if (entity.au()) {
                double d2 = entity.locY;
                entity.b(f, f1, f2, 0.02F);
                entity.move(EnumMoveType.SELF, entity.motX, entity.motY, entity.motZ);
                entity.motX *= 0.5D;
                entity.motY *= 0.5D;
                entity.motZ *= 0.5D;
                if (!entity.isNoGravity()) {
                    entity.motY -= 0.02D;
                }
                if ((entity.positionChanged)
                        && (entity.c(entity.motX, entity.motY + 0.6000000238418579D - entity.locY + d2, entity.motZ))) {
                    entity.motY = 0.30000001192092896D;
                }
            } else if (entity.cP()) {
                if (entity.motY > -0.5D) {
                    entity.fallDistance = 1.0F;
                }
                Vec3D vec3d = entity.aJ();
                float f6 = entity.pitch * 0.017453292F;

                double d0 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                double d1 = Math.sqrt(entity.motX * entity.motX + entity.motZ * entity.motZ);
                double d3 = vec3d.b();
                float f7 = MathHelper.cos(f6);

                f7 = (float) (f7 * f7 * Math.min(1.0D, d3 / 0.4D));
                entity.motY += -0.08D + f7 * 0.06D;
                if ((entity.motY < 0.0D) && (d0 > 0.0D)) {
                    double d4 = entity.motY * -0.1D * f7;
                    entity.motY += d4;
                    entity.motX += vec3d.x * d4 / d0;
                    entity.motZ += vec3d.z * d4 / d0;
                }
                if (f6 < 0.0F) {
                    double d4 = d1 * -MathHelper.sin(f6) * 0.04D;
                    entity.motY += d4 * 3.2D;
                    entity.motX -= vec3d.x * d4 / d0;
                    entity.motZ -= vec3d.z * d4 / d0;
                }
                if (d0 > 0.0D) {
                    entity.motX += (vec3d.x / d0 * d1 - entity.motX) * 0.1D;
                    entity.motZ += (vec3d.z / d0 * d1 - entity.motZ) * 0.1D;
                }
                entity.motX *= 0.9900000095367432D;
                entity.motY *= 0.9800000190734863D;
                entity.motZ *= 0.9900000095367432D;
                entity.move(EnumMoveType.SELF, entity.motX, entity.motY, entity.motZ);
                if ((entity.positionChanged) && (!entity.world.isClientSide)) {
                    double d4 = Math.sqrt(entity.motX * entity.motX + entity.motZ * entity.motZ);
                    double d5 = d1 - d4;
                    float f8 = (float) (d5 * 10.0D - 3.0D);
                    if (f8 > 0.0F) {
                        entity.a(f8 > 4 ? SoundEffects.bQ : SoundEffects.bY, 1.0F, 1.0F);
                        entity.damageEntity(DamageSource.FLY_INTO_WALL, f8);
                    }
                }
                if ((entity.onGround) && (!entity.world.isClientSide) && (entity.getFlag(7))
                        && (!CraftEventFactory.callToggleGlideEvent(entity, false).isCancelled())) {
                    entity.setFlag(7, false);
                }
            } else {
                float f9 = 0.91F;
                BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition
                        .d(entity.locX, entity.getBoundingBox().b - 1.0D, entity.locZ);
                if (entity.onGround) {
                    f9 = entity.world.getType(blockposition_pooledblockposition).getBlock().frictionFactor * 0.91F;
                }
                float f4 = 0.16277136F / (f9 * f9 * f9);
                float f3;
                if (entity.onGround) {
                    f3 = entity.cy() * f4;
                } else {
                    f3 = entity.aR;
                }
                entity.b(f, f1, f2, f3);
                f9 = 0.91F;
                if (entity.onGround) {
                    f9 = entity.world.getType(blockposition_pooledblockposition.e(entity.locX,
                            entity.getBoundingBox().b - 1.0D, entity.locZ)).getBlock().frictionFactor * 0.91F;
                }
                if (entity.m_()) {
                    entity.motX = MathHelper.a(entity.motX, -0.15000000596046448D, 0.15000000596046448D);
                    entity.motZ = MathHelper.a(entity.motZ, -0.15000000596046448D, 0.15000000596046448D);
                    entity.fallDistance = 0.0F;
                    if (entity.motY < -0.15D) {
                        entity.motY = -0.15D;
                    }
                    boolean flag = (entity.isSneaking());
                    if ((flag) && (entity.motY < 0.0D)) {
                        entity.motY = 0.0D;
                    }
                }
                entity.move(EnumMoveType.SELF, entity.motX, entity.motY, entity.motZ);
                if ((entity.positionChanged) && (entity.m_())) {
                    entity.motY = 0.2D;
                }
                if (entity.hasEffect(MobEffects.LEVITATION)) {
                    entity.motY += (0.05D * (entity.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - entity.motY)
                            * 0.2D;
                } else {
                    blockposition_pooledblockposition.e(entity.locX, 0.0D, entity.locZ);
                    if ((entity.world.isClientSide) && ((!entity.world.isLoaded(blockposition_pooledblockposition))
                            || (!entity.world.getChunkAtWorldCoords(blockposition_pooledblockposition).p()))) {
                        if (entity.locY > 0.0D) {
                            entity.motY = -0.1D;
                        } else {
                            entity.motY = 0.0D;
                        }
                    } else if (!entity.isNoGravity()) {
                        entity.motY -= 0.08D;
                    }
                }
                entity.motY *= 0.9800000190734863D;
                entity.motX *= f9;
                entity.motZ *= f9;
                blockposition_pooledblockposition.t();
            }
        }
        entity.aF = entity.aG;
        double d2 = entity.locX - entity.lastX;
        double d0 = entity.locZ - entity.lastZ;
        double d1 = (entity instanceof EntityBird) ? entity.locY - entity.lastY : 0.0D;
        float f10 = MathHelper.sqrt(d2 * d2 + d1 * d1 + d0 * d0) * 4.0F;
        if (f10 > 1.0F) {
            f10 = 1.0F;
        }
        entity.aG += (f10 - entity.aG) * 0.4F;
        entity.aH += entity.aG;
    }

    @Override
    public void removeFromServerPlayerList(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        ((CraftServer) Bukkit.getServer()).getHandle().players.remove(ep);
    }

    @Override
    public void removeFromWorld(org.bukkit.entity.Entity entity) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.world.removeEntity(nmsEntity);
    }


    @Override
    public void playAnimation(org.bukkit.entity.Entity entity, int id) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(nmsEntity, id);
        NMS.sendPacketNearby(null, entity.getLocation(), packet, 15);
    }

    @Override
    public void sendPacketsNearby(Player from, Location location, Collection<Object> packets, double radius) {
        radius *= radius;
        final World world = location.getWorld();
        for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
            if (ply == null || world != ply.getWorld() || (from != null && !ply.canSee(from))) {
                continue;
            }
            if (location.distanceSquared(ply.getLocation()) > radius) {
                continue;
            }
            for (Packet<?> packet : Collections.singleton((Packet<?>) packets)) {
                sendPacket(ply, packet);
            }
        }
    }

    @Override
    public void sendPacketNearby(Player from, Location location, Object packet, double radius) {
        radius *= radius;
        final World world = location.getWorld();
        for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
            if (ply == null || world != ply.getWorld() || (from != null && !ply.canSee(from))) {
                continue;
            }
            if (location.distanceSquared(ply.getLocation()) > radius) {
                continue;
            }

            sendPacket(ply, packet);
        }
    }

    @Override
    public void refreshNPCSlot(org.bukkit.entity.Entity entity, int slot, ItemStack stack) {

        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(stack);

        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entity.getEntityId(), EnumItemSlot.values()[slot], nmsItemStack);
        sendPacketNearby(null, entity.getLocation(), packet, 10);
    }


    @Override
    public Hologram getHologram(org.bukkit.entity.Entity entity) {
        if (entity == null) {
            return null;
        }

        if (!(entity instanceof EntityArmorStand.CraftArmorStand)) {
            return null;
        }

        Entity en = ((CraftEntity) entity).getHandle();
        if (!(en instanceof EntityArmorStand)) {
            return null;
        }

        HologramLine e = ((com.uzm.common.nms.version.v1_12_R1.entity.EntityArmorStand) en).getLine();
        return e != null ? e.getHologram() : null;
    }

    @Override
    public IArmorStand createArmorStand(Location location, String name, HologramLine line) {

        IArmorStand armor =
                line == null ? new EntityStand(location) : new com.uzm.common.nms.version.v1_12_R1.entity.EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), line);

        Entity entity = (Entity) armor;

        armor.setLocation(location.getX(), location.getY(), location.getZ());

        entity.yaw = location.getYaw();
        entity.pitch = location.getPitch();
        armor.setName(name);
        if (this.addEntity(entity)) {
            return armor;
        }

        return null;
    }


    @Override
    public boolean isHologramEntity(org.bukkit.entity.Entity entity) {
        return this.getHologram(entity) != null;
    }


    private boolean addEntity(Entity entity) {
        try {
            return entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean isNavigationFinished(Object navigation) {
        return ((NavigationAbstract) navigation).o();
    }

    @Override
    public String[] getPlayerTextures(Player player) {
        EntityPlayer playerNMS = ((CraftPlayer) player).getHandle();
        GameProfile profile = playerNMS.getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.getValue();
        String signature = property.getSignature();
        return new String[]{texture, signature};
    }

    public static Entity getHandle(org.bukkit.entity.Entity entity) {
        if (!(entity instanceof CraftEntity))
            return null;
        return ((CraftEntity) entity).getHandle();
    }

    private float getDragonYaw(Entity handle, double tX, double tZ) {
        if (handle.locZ > tZ)
            return (float) (-Math.toDegrees(Math.atan((handle.locX - tX) / (handle.locZ - tZ))));
        if (handle.locZ < tZ) {
            return (float) (-Math.toDegrees(Math.atan((handle.locX - tX) / (handle.locZ - tZ)))) + 180.0F;
        }
        return handle.yaw;
    }


}
