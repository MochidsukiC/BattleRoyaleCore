package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Protocol{
    public void pushPin(Player player, Location[] location,boolean[] b,EntityType entityType,int entityIdPlus) {
        int entityId = 10000 + entityIdPlus;


        for(int i = 0;i <=2;i++) {

            Location loc = new Location(player.getWorld(),0,0,0);
            double x = location[i].getX() - player.getLocation().getX();
            double y = location[i].getY() - player.getLocation().getY();
            double z = location[i].getZ() - player.getLocation().getZ();
            double d =Math.sqrt(Math.abs(y * y + (x * x + z * z)));
            if (d > 160) {
                loc.setX(player.getLocation().getX()+x*160/d);
                loc.setY(player.getLocation().getY()+y*160/d);
                loc.setZ(player.getLocation().getZ()+z*160/d);
            }else {
                loc = location[i];
            }



            entityId++;
            PacketContainer packet0 = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);

            packet0.getIntegers().write(0, entityId);
            packet0.getUUIDs().write(0, UUID.randomUUID());
            packet0.getEntityTypeModifier().write(0, entityType);

            packet0.getDoubles()
                    .write(0, loc.getX())
                    .write(1, loc.getY())
                    .write(2, loc.getZ());

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            EasyMetadataPacket metadata = new EasyMetadataPacket(null); // Pass the NMS entity, or null as we're dealing with a client-side entity

            byte bitmask = 0x00; // First bitmask, 0x00 by default
            bitmask |= 0x20; // is invisible
            if(b[i]) {
                bitmask |= 0x40; // is glowing
            }

            metadata.write(0, bitmask); // Write the first bitmask


            packet.getIntegers().write(0, entityId);
            packet.getWatchableCollectionModifier().write(0, metadata.export());


            try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet0);
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (InvocationTargetException ignored) {
            }
        }

    }

    public void setGlowing(Player glowingPlayer, Player sendPacket) {

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, glowingPlayer.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setEntity(glowingPlayer);
        watcher.setObject(0, serializer, (byte) (0x40));
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(sendPacket, packet);
        } catch (InvocationTargetException e) {
            System.out.println("There was an issue with one of the glowing enchants!");
        }

    }

}
