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
    public void pin(Player player, Location loc1, Location loc2 , Location loc3,Boolean b1,Boolean b2,Boolean b3) {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();

        EntityType entityType = EntityType.ENDER_SIGNAL;

        PacketContainer packet = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getModifier().writeDefaults();
        packet.getEntityTypeModifier().write(0, entityType);
        packet.getUUIDs().write(0, UUID.randomUUID());
        //packet.getIntegers().write(1, 1);
        packet.getDoubles().write(0, loc1.getX()+0.5);
        packet.getDoubles().write(1, loc1.getY()+1);
        packet.getDoubles().write(2, loc1.getZ()+0.5);

        PacketContainer packet2 = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA); // metadata packet
        packet2.getIntegers().write(0, packet.getIntegers().read(0)); //Set entity id from packet above
        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher.setEntity(player); //Set the new data watcher's target
        watcher.setObject(0, serializer, (byte) (0x20 | 0x40)); //Set status to glowing, found on protocol page
        packet2.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created. This is Line 32




        PacketContainer packet3 = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet3.getModifier().writeDefaults();
        packet3.getEntityTypeModifier().write(0, entityType);
        packet3.getUUIDs().write(0, UUID.randomUUID());
        //packet.getIntegers().write(1, 1);
        packet3.getDoubles().write(0, loc2.getX()+0.5);
        packet3.getDoubles().write(1, loc2.getY()+1);
        packet3.getDoubles().write(2, loc2.getZ()+0.5);

        PacketContainer packet4 = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA); // metadata packet
        packet4.getIntegers().write(0, packet4.getIntegers().read(0)); //Set entity id from packet above
        WrappedDataWatcher watcher4 = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer4 = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher4.setEntity(player); //Set the new data watcher's target
        watcher4.setObject(0, serializer4, (byte) (0x20 | 0x40)); //Set status to glowing, found on protocol page
        packet4.getWatchableCollectionModifier().write(0, watcher4.getWatchableObjects()); //Make the packet's datawatcher the one we created. This is Line 32

        PacketContainer packet5 = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet5.getModifier().writeDefaults();
        packet5.getEntityTypeModifier().write(0, entityType);
        packet5.getUUIDs().write(0, UUID.randomUUID());
        //packet.getIntegers().write(1, 1);
        packet5.getDoubles().write(0, loc3.getX()+0.5);
        packet5.getDoubles().write(1, loc3.getY()+1);
        packet5.getDoubles().write(2, loc3.getZ()+0.5);

        PacketContainer packet6 = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA); // metadata packet
        packet6.getIntegers().write(0, packet6.getIntegers().read(0)); //Set entity id from packet above
        WrappedDataWatcher watcher6 = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer6 = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher6.setEntity(player); //Set the new data watcher's target
        watcher6.setObject(0, serializer6, (byte) (0x20 | 0x40)); //Set status to glowing, found on protocol page
        packet6.getWatchableCollectionModifier().write(0, watcher6.getWatchableObjects()); //Make the packet's datawatcher the one we created. This is Line 32

        try {
            if(b1) {
                pm.sendServerPacket(player, packet);
                pm.sendServerPacket(player, packet2);
            }
            if(b2) {
                pm.sendServerPacket(player, packet3);
                pm.sendServerPacket(player, packet4);
            }
            if(b3) {
                pm.sendServerPacket(player, packet5);
                pm.sendServerPacket(player, packet6);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }





    }
}
