package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class pin {
    public void Pin(Player player, Location loc){
        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            PacketContainer slime_spawn = manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            slime_spawn.getEntityTypeModifier().write(0, EntityType.SLIME);
            slime_spawn.getUUIDs().write(0, UUID.randomUUID());
            slime_spawn.getDoubles().write(0, loc.getX()).write(1, loc.getY()).write(2, loc.getZ());

            PacketContainer slime_meta = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

            WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
//what to put here?

            manager.sendServerPacket(player, slime_spawn);
            manager.sendServerPacket(player, slime_meta);
        }catch (Exception e){

        }
    }
}
