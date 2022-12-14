package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.SUFFOCATION;

public class event implements Listener{
    @EventHandler
    public void EntityDropItemEvent(EntityDropItemEvent event){
        Item item = event.getItemDrop();
        if(item.getType().equals(Material.FIREWORK_ROCKET)){

        }
    }
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        if(cause.equals(SUFFOCATION)){
            switch (v.gameround) {
                case 1:
                case 2:
                    event.setDamage(0.4);
                    break;
                case 3:
                    event.setDamage(1.2);
                    break;
                case 4:
                    event.setDamage(2.4);
                    break;
                case 5:
                case 6:
                    event.setDamage(2.9);
                    break;

            }
        }
    }


}
