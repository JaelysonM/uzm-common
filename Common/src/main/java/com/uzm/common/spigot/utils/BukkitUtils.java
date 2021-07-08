package com.uzm.common.spigot.utils;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.common.reflections.Accessors;
import com.uzm.common.reflections.acessors.FieldAccessor;
import com.uzm.common.reflections.acessors.MethodAccessor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public class BukkitUtils {

    /**
     * Todas as cores prontas da classe {@link Color}
     */
    public static final List<FieldAccessor<Color>> COLORS;
    public static final MethodAccessor GET_PROFILE;
    public static final FieldAccessor<GameProfile> SKULL_META_PROFILE;

    static {
        COLORS = new ArrayList<>();
        for (Field field : Color.class.getDeclaredFields()) {
            if (field.getType().equals(Color.class)) {
                COLORS.add(new FieldAccessor<>(field));
            }
        }

        GET_PROFILE = Accessors.getMethod(MinecraftReflection.getCraftBukkitClass("entity.CraftPlayer"), GameProfile.class, 0);
        SKULL_META_PROFILE = Accessors.getField(MinecraftReflection.getCraftBukkitClass("inventory.CraftMetaSkull"), "profile", GameProfile.class);
    }

    public static boolean replaceItem(Inventory inventory, String equals, ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || !stack.getType().name().contains(equals)) {
                continue;
            }

            inventory.setItem(i, item);
            return true;
        }

        return false;
    }

    public static int removeItem(Inventory inventory, Material mat, int quantity) {
        int rest = quantity;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || stack.getType() != mat) {
                continue;
            }

            if (rest >= stack.getAmount()) {
                rest -= stack.getAmount();
                inventory.clear(i);
            } else if (rest > 0) {
                stack.setAmount(stack.getAmount() - rest);
                rest = 0;
            } else {
                break;
            }
        }

        return quantity - rest;
    }

    public static boolean isSimilar(ItemStack main, ItemStack toCompare) {
        if (main == null) {
            return false;
        } else if (toCompare == null) {
            return false;
        } else {
            return main.getTypeId() == toCompare.getTypeId() && main.getDurability() == toCompare.getDurability() && main.hasItemMeta() == toCompare.hasItemMeta() && main.getItemMeta().hasDisplayName() == toCompare.getItemMeta().hasDisplayName() && (!main.hasItemMeta() || main.getItemMeta().getDisplayName().equalsIgnoreCase(toCompare.getItemMeta().getDisplayName()));
        }
    }

    public static int removeItem(Inventory inventory, ItemStack mat, int quantity) {
        int rest = quantity;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || !isSimilar(mat, stack)) {
                continue;
            }

            if (rest >= stack.getAmount()) {
                rest -= stack.getAmount();
                inventory.clear(i);
            } else if (rest > 0) {
                stack.setAmount(stack.getAmount() - rest);
                rest = 0;
            } else {
                break;
            }
        }

        return quantity - rest;
    }

    public static int getCountFromMaterial(Inventory inv, Material mat) {
        int count = 0;
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType() == mat) {
                count += item.getAmount();
            }
        }

        return count;
    }

    public static int getCountFromItemStack(Inventory inv, ItemStack stack) {
        int count = 0;
        for (ItemStack item : inv.getContents()) {
            if (item != null && isSimilar(stack, item)) {
                count += item.getAmount();
            }
        }

        return count;
    }

    public static boolean isBedBlock(Block isBed) {
        return isBed != null && isBed.getType().name().contains("BED") && !isBed.getType().name().contains("ROCK");
    }

    public static Block getBedNeighbor(Block head) {
        if (isBedBlock(head.getRelative(BlockFace.EAST))) {
            return head.getRelative(BlockFace.EAST);
        } else if (isBedBlock(head.getRelative(BlockFace.WEST))) {
            return head.getRelative(BlockFace.WEST);
        } else if (isBedBlock(head.getRelative(BlockFace.SOUTH))) {
            return head.getRelative(BlockFace.SOUTH);
        } else {
            return head.getRelative(BlockFace.NORTH);
        }
    }

    /**
     * Seta a partir da {@code Reflection} o perfil de um {@link ItemStack} do tipo Cabeça.
     *
     * @param player O jogador para requisitar o {@link GameProfile}.
     * @param head   O {@link ItemStack} do tipo Cabeça.
     * @return O {@link ItemStack} modificado com o Perfil do jogador.
     */
    public static ItemStack putProfileOnSkull(Player player, ItemStack head) {
        if (head == null || !(head.getItemMeta() instanceof SkullMeta)) {
            return head;
        }

        ItemMeta meta = head.getItemMeta();
        SKULL_META_PROFILE.set(meta, (GameProfile) GET_PROFILE.invoke(player));
        head.setItemMeta(meta);
        return head;
    }

    /**
     * Seta a partir da {@code Reflection} o perfil de um {@link ItemStack} do tipo Cabeça.
     *
     * @param profile O {@link GameProfile} perfil para colocar na Cabeça.
     * @param head    O {@link ItemStack} do tipo Cabeça.
     * @return O {@link ItemStack} modificado com o Perfil.
     */
    public static ItemStack putProfileOnSkull(GameProfile profile, ItemStack head) {
        if (head == null || !(head.getItemMeta() instanceof SkullMeta)) {
            return head;
        }

        ItemMeta meta = head.getItemMeta();
        SKULL_META_PROFILE.set(meta, profile);
        head.setItemMeta(meta);
        return head;
    }

    /**
     * Seta a partir da {@code Reflection} o perfil de um {@link ItemStack} do tipo Cabeça.
     *
     * @param value O {@link String} value para ser colcoado na textures
     * @param head  O {@link ItemStack} do tipo Cabeça.
     * @return O {@link ItemStack} modificado com o Perfil.
     */
    public static ItemStack putProfileOnSkull(String value, ItemStack head) {
        if (head == null || !(head.getItemMeta() instanceof SkullMeta)) {
            return head;
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));

        ItemMeta meta = head.getItemMeta();
        SKULL_META_PROFILE.set(meta, profile);
        head.setItemMeta(meta);
        return head;
    }

    /**
     * Recria o teleporte interno do Spigot, usado para burlar sistemas que travam o teleporte.
     *
     * @param player O {@link Player} a ser teleportado
     * @param from   O {@link Location} a localização de saída.
     * @param to     O {@link Location} a localização de chegada.
     */

    public static void copyInternalTeleport(Player player, Location from, Location to) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        CraftServer cs = ((CraftServer) player.getServer());
        ep.mount(null);
        WorldServer fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        WorldServer toWorld = ((CraftWorld) to.getWorld()).getHandle();
        if (ep.activeContainer != ep.defaultContainer) {
            ep.closeInventory();
        }
        if (fromWorld == toWorld) {
            ep.playerConnection.teleport(to);
        } else {
            cs.getHandle().moveToWorld(ep, toWorld.dimension, true, to, true);
        }
    }

    /**
     * Distância básica entre duas localizações.
     *
     * @param from A localização inicial
     * @param to   A localização final.
     * @return A distância.
     */
    public static double distance(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Retorna um item indestrutível.
     *
     * @param itemStack O item a ser colocado com indestrutível
     * @return O {@link ItemStack} retorna um item indestrutível.
     */

    public static ItemStack unbreakableItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


}
