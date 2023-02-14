package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PacketFilterManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.UUID;

public class Pin{
    public void pin(Player player, Location[] locations,boolean[] b) {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        EntityType entityType = EntityType.DRAGON_FIREBALL;

        PacketContainer[] packet = new PacketContainer[locations.length];
        PacketContainer[] packetmeta = new PacketContainer[locations.length]; // metadata packet

        for (int i = 0;i<locations.length;i++) {
            try {
                Location loc = new Location(player.getWorld(),0,0,0);
                double x = locations[i].getX() - player.getLocation().getX();
                double y = locations[i].getY() - player.getLocation().getY();
                double z = locations[i].getZ() - player.getLocation().getZ();
                double d =Math.sqrt(Math.abs(y * y + (x * x + z * z)));

                if (d > 40) {
                    loc.setX(player.getLocation().getX()+x*40/d);
                    loc.setY(player.getLocation().getY()+y*40/d);
                    loc.setZ(player.getLocation().getZ()+z*40/d);
                }else {
                    loc = locations[i];
                }

                packet[i] = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);

                packet[i].getModifier().writeDefaults();
                packet[i].getEntityTypeModifier().write(0, entityType);
                packet[i].getUUIDs().write(0, UUID.randomUUID());
                //packet.getIntegers().write(1, 1);
                packet[i].getDoubles().write(0, loc.getX()+0.5);
                packet[i].getDoubles().write(1, loc.getY()+1);
                packet[i].getDoubles().write(2, loc.getZ()+0.5);

                packetmeta[i] = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);

                packetmeta[i].getIntegers().write(0, packet[i].getIntegers().read(0)); //Set entity id from packet above
                WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
                WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
                watcher.setEntity(player); //Set the new data watcher's target
                watcher.setObject(0, serializer, (byte) (0x20 | 0x40)); //Set status to glowing, found on protocol page
                packetmeta[i].getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created. This is Line 32

            }catch (Exception ignored){}


        }



        try {
            for(int i = 0; i < locations.length; i++){
                if (b[i]) {
                    pm.sendServerPacket(player, packet[i]);
                    pm.sendServerPacket(player, packetmeta[i]);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }





    }
}
