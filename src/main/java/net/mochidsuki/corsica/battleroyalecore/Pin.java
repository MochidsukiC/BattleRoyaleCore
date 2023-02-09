package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Pin{
    public void pin(Player player, Location loc) {
        /*
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getModifier().writeDefaults();
        packet.getEntityTypeModifier().write(0, EntityType.SLIME);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(1, 1); //set the type of the entity,probably the problem?
        packet.getDoubles().write(0, loc.getX());
        packet.getDoubles().write(1, loc.getY());
        packet.getDoubles().write(2, loc.getZ());
        */
        WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class); //Serializer for data watcher object's value
        WrappedDataWatcher.WrappedDataWatcherObject entityBitmask = new WrappedDataWatcher.WrappedDataWatcherObject(0, byteSerializer); //Creating a data watcher object with it's index and serializer for expected value type

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(entityBitmask, (byte) 0x40); //Enable glow effect

        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata();
        metadataPacket.setEntityID(83);
        metadataPacket.setMetadata(watcher.getWatchableObjects());
        metadataPacket.sendPacket(player);
        /*
        try {
            pm.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

         */
    }
}