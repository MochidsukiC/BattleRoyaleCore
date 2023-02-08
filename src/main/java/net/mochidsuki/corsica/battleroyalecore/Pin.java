package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Pin {
    public void pin(Player player, Location loc) {
        /*
        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            PacketContainer slime_spawn = manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            slime_spawn.getEntityTypeModifier().write(0, EntityType.SLIME);
            slime_spawn.getUUIDs().write(0, UUID.randomUUID());
            slime_spawn.getDoubles().write(0, loc.getX()).write(1, loc.getY()).write(2, loc.getZ());


            WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
            watcher.setEntity(player);
            watcher.setObject(0, serializer, (byte) (0x40));
//what to put here?
            slime_spawn.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
            try {
                manager.sendServerPacket(player, slime_spawn);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            manager.sendServerPacket(player, slime_spawn);
        }catch (Exception e){

        }
         */
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, player.getEntityId()); //Set packet's entity id
        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher.setEntity(player); //Set the new data watcher's target
        watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
        try {
            pm.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
