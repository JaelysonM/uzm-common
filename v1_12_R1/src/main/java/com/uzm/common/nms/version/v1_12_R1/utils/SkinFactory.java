package com.uzm.common.nms.version.v1_12_R1.utils;

import com.uzm.common.java.util.JavaReflections;
import com.uzm.common.nms.interfaces.ISkinFactory;
import com.uzm.common.plugin.Common;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;


public class SkinFactory implements ISkinFactory {

    @Override
    public void applySkin(Player p, Object props) {
        try {
            Object ep = JavaReflections.invokeMethod(p.getClass(), p, "getHandle");
            Object profile = JavaReflections.invokeMethod(ep.getClass(), ep, "getProfile");
            Object propmap = JavaReflections.invokeMethod(profile.getClass(), profile, "getProperties");
            JavaReflections.invokeMethod(propmap, "clear");
            JavaReflections.invokeMethod(propmap.getClass(), propmap, "put", new Class[]{Object.class, Object.class}, "textures", props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSkin(Player p) {
        try {
            CraftPlayer cp = (CraftPlayer) p;
            EntityPlayer ep = cp.getHandle();
            int entId = ep.getId();

            PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);

            PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);

            for (Player inWorld : p.getWorld().getPlayers()) {
                final CraftPlayer craftOnline = (CraftPlayer) inWorld;
                PlayerConnection con = craftOnline.getHandle().playerConnection;
                con.sendPacket(removeEntity);
                con.sendPacket(removeInfo);
            }
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void updateSkin(Player p) {
        try {
            if (!p.isOnline()) {
                return;
            }
            CraftPlayer cp = (CraftPlayer) p;
            EntityPlayer ep = cp.getHandle();
            int entId = ep.getId();

            PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);

            PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);

            PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);

            PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);

            PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ep.getWorld().worldProvider.getDimensionManager().getDimensionID(),
                    ep.getWorld().getDifficulty(), ep.getWorld().worldData.getType(),
                    EnumGamemode.getById(p.getGameMode().getValue()));

            PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.MAINHAND,
                    CraftItemStack.asNMSCopy(p.getItemInHand()));

            PacketPlayOutEntityEquipment offhand = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.MAINHAND,
                    CraftItemStack.asNMSCopy(p.getInventory().getItemInOffHand()));

            PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.HEAD,
                    CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));

            PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.CHEST,
                    CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));

            PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.LEGS,
                    CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));

            PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, EnumItemSlot.FEET,
                    CraftItemStack.asNMSCopy(p.getInventory().getBoots()));

            PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(p.getInventory().getHeldItemSlot());

            for (Player inWorld : p.getWorld().getPlayers()) {
                final CraftPlayer craftOnline = (CraftPlayer) inWorld;
                PlayerConnection con = craftOnline.getHandle().playerConnection;
                if (inWorld.equals(p)) {
                    con.sendPacket(removeInfo);
                    con.sendPacket(addInfo);
                    con.sendPacket(respawn);
                    con.sendPacket(slot);
                    craftOnline.updateScaledHealth();
                    craftOnline.getHandle().triggerHealthUpdate();
                    craftOnline.updateInventory();
                    Bukkit.getScheduler().runTask(Common.getInstance(), () -> craftOnline.getHandle().updateAbilities());
                    continue;
                }
                con.sendPacket(removeEntity);
                con.sendPacket(removeInfo);
                if (inWorld.canSee(p)) {
                    con.sendPacket(addInfo);
                    con.sendPacket(addNamed);
                    con.sendPacket(itemhand);
                    con.sendPacket(helmet);
                    con.sendPacket(offhand);
                    con.sendPacket(chestplate);
                    con.sendPacket(leggings);
                    con.sendPacket(boots);
                }
            }
        } catch (Exception ignored) {
        }

    }

}