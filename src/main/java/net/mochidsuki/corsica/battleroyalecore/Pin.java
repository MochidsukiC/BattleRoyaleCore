package net.mochidsuki.corsica.battleroyalecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Pin {
    public void Pin(Player player, Location loc){
        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            PacketContainer slime_spawn = manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            slime_spawn.getIntegers().write(0, slime_id).write(1, (int) EntityType.SLIME.getTypeId()); //doesn't spawn a slime
            slime_spawn.getUUIDs().write(0, UUID.randomUUID());
            slime_spawn.getDoubles().write(0, loc.getX()).write(1, loc.getY()).write(2, loc.getZ());
            slime_id--;

            PacketContainer slime_meta = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
//what to put here?

            manager.sendServerPacket(player, slime_spawn);
            manager.sendServerPacket(player, slime_meta);
        }catch (Exception e){

        }
    }
}
