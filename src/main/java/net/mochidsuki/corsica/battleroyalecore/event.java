package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.scoreboard.Team;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.SUFFOCATION;

public class event implements Listener{
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event){
        Item item = event.getItemDrop();
        if(item.getItemStack().getType() == Material.FIREWORK_ROCKET){//ドロップシップからの降下
            Player player = event.getPlayer();
            try {
                Team playerteam = player.getScoreboard().getPlayerTeam(player);
                String[] tp = new String[playerteam.getEntries().size()];
                playerteam.getEntries().toArray(tp);
                Player[] teamplayer = new Player[tp.length];
                for (int i = 0;i < tp.length;i++) {
                    teamplayer[i] = Bukkit.getPlayer(tp[i]);
                }
                for (int i = 0; i < teamplayer.length; i++){//teamplayer全員に実行
                    teamplayer[i].addScoreboardTag("live");
                    teamplayer[i].setGameMode(GameMode.SURVIVAL);
                }
                ItemStack mapItem = new ItemStack(Material.FILLED_MAP,1);
                MapMeta mapMeta = (MapMeta)mapItem.getItemMeta();

                MapView view = player.getServer().createMap(player.getWorld());

                view.addRenderer(new);
                mapMeta.setMapView(view);
                mapItem.setItemMeta(mapMeta);

            }catch (Exception e){}

        }
    }
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        if(cause.equals(SUFFOCATION)){//ボーダー外ダメージ
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
