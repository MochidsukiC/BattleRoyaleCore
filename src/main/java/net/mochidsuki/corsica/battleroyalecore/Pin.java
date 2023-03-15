package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Pin{
    public void pushPin(Player player, Location[] location,boolean[] b,EntityType entityType,int entityIdPlus) {
        int entityId = 10000 + entityIdPlus;


        for(int i = 0;i <=2;i++) {

            entityId++;
            PacketContainer packet0 = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);

            packet0.getIntegers().write(0, entityId);
            packet0.getUUIDs().write(0, UUID.randomUUID());
            packet0.getEntityTypeModifier().write(0, entityType);

            packet0.getDoubles()
                    .write(0, location[i].getX())
                    .write(1, location[i].getY())
                    .write(2, location[i].getZ());

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            EasyMetadataPacket metadata = new EasyMetadataPacket(null); // Pass the NMS entity, or null as we're dealing with a client-side entity

            byte bitmask = 0x00; // First bitmask, 0x00 by default
            bitmask |= 0x20; // is invisible
            bitmask |= 0x40; // is glowing

            metadata.write(0, bitmask); // Write the first bitmask


            packet.getIntegers().write(0, entityId);
            packet.getWatchableCollectionModifier().write(0, metadata.export());


            try {
                if(b[i]) {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet0);
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
                }
            } catch (InvocationTargetException ignored) {
            }
        }

    }
}
