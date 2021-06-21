package com.uzm.common.protocol;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.uzm.common.plugin.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class HologramAdapter extends PacketAdapter {

    private boolean useOptional = true;


    public HologramAdapter() {
        super(params().plugin(Common.getInstance()).types(PacketType.Play.Server.ENTITY_METADATA).serverSide()
                .listenerPriority(ListenerPriority.NORMAL));

        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String majorVersion = version.split("_")[1];
        if (majorVersion.contains("_")) {
            majorVersion = majorVersion.split("_")[0];
        }

        if (Integer.parseInt(majorVersion) < 13) {
            useOptional = false;
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (!Common.getInstance().getLoader().isPapiHooked()) return;

        PacketContainer packet = event.getPacket();

        if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
            List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getEntityMetadata();

            if (dataWatcherValues == null) {
                return;
            }

            for (WrappedWatchableObject watchableObject : dataWatcherValues) {
                if (watchableObject.getIndex() == 2) {
                    if (replacePlaceholders(watchableObject, event.getPlayer())) {
                        event.setPacket(entityMetadataPacket.getHandle());
                    }

                    return;
                }
            }
        }
    }

    protected boolean replacePlaceholders(WrappedWatchableObject customNameWatchableObject, Player player) {
        if (customNameWatchableObject == null) return true;

        Object customNameWatchableObjectValue = customNameWatchableObject.getValue();
        String customName;

        if (useOptional) { //1.13 or above
            if (!(customNameWatchableObjectValue instanceof Optional)) {
                return false;
            }

            Optional<?> customNameOptional = (Optional<?>) customNameWatchableObjectValue;
            if (!customNameOptional.isPresent()) {
                return false;
            }

            WrappedChatComponent componentWrapper = WrappedChatComponent.fromHandle(customNameOptional.get());
            customName = componentWrapper.getJson();

        } else {
            customName = (String) customNameWatchableObjectValue;
        }

        customName = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, customName);

        if (useOptional) { // 1.13 or above
            customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(customName).getHandle()));
        } else {
            customNameWatchableObject.setValue(customName);
        }

        return true;
    }

}
