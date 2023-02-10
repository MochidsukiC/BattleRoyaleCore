package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PacketFilterManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Pin{
    public void pin(Player player, Location loc) {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getModifier().writeDefaults();
        packet.getEntityTypeModifier().write(0, EntityType.SLIME);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(1, 1); //set the type of the entity,probably the problem?
        packet.getDoubles().write(0, loc.getX());
        packet.getDoubles().write(1, loc.getY());
        packet.getDoubles().write(2, loc.getZ());

        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher.setEntity(player); //Set the new data watcher's target
        watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page

        try {
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            pm.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

         
    }
}
