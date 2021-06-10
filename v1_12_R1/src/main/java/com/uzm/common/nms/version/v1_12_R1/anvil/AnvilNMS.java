package com.uzm.common.nms.version.v1_12_R1.anvil;

import com.uzm.common.nms.interfaces.IAnvilNMS;
import com.uzm.common.spigot.inventories.anvil.AnvilMenu;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AnvilNMS implements IAnvilNMS {
    
    @Override
    public void open(Player player, AnvilMenu anvilMenu) {
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(p);

        //Set the items to the items from the inventory given
        anvilMenu.setInventory( container.getBukkitView().getTopInventory());

        anvilMenu.getContents().forEach((key, value) -> anvilMenu.getInventory().setItem(key.getSlot(), value));


        //Counter stuff that the game uses to keep track of inventories
        int c = p.nextContainerCounter();

        //Send the packet
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing"), 0));

        //Set their active container to the container
        p.activeContainer = container;

        //Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        //Add the slot listener
        p.activeContainer.addSlotListener(p);
    }

    private static class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean canUse(EntityHuman entityhuman) {
            return true;
        }
    }

}
