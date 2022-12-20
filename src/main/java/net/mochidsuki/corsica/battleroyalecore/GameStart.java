package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.scoreboard.Team;

import static org.bukkit.Bukkit.getServer;

public class GameStart {
    public void player(Player player){
        try {
            Team playerteam = player.getScoreboard().getPlayerTeam(player);
            String[] tp = new String[playerteam.getEntries().size()];
            playerteam.getEntries().toArray(tp);
            Player[] teamplayer = new Player[tp.length];
            for (int i = 0;i < tp.length;i++) {
                teamplayer[i] = Bukkit.getPlayer(tp[i]);
            }
            //ビッグマップ処理
            ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
            MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();
            MapView viewB = player.getServer().createMap(player.getWorld());
            viewB.addRenderer(new BigMapRenderer());
            mapMetaB.setMapView(viewB);
            mapItemB.setItemMeta(mapMetaB);
            viewB.setScale(MapView.Scale.FAR);
            viewB.setTrackingPosition(true);
            for (int i = 0; i < teamplayer.length; i++){//teamplayer全員に実行
                //ビッグマップ付与
                teamplayer[i].getInventory().setItem(8,mapItemB);

                teamplayer[i].addScoreboardTag("live");
                teamplayer[i].setGameMode(GameMode.SURVIVAL);

                //ミニマップ処理・付与
                ItemStack mapItem = new ItemStack(Material.FILLED_MAP,1);
                MapMeta mapMeta = (MapMeta)mapItem.getItemMeta();
                MapView view = teamplayer[i].getServer().createMap(player.getWorld());
                view.addRenderer(new MiniMapRenderer());
                mapMeta.setMapView(view);
                mapItem.setItemMeta(mapMeta);
                view.setScale(MapView.Scale.CLOSEST);
                view.setTrackingPosition(true);
                teamplayer[i].getInventory().setItem(EquipmentSlot.OFF_HAND,mapItem);
                for (int ii=0;ii < teamplayer.length;ii++){
                    if(!(ii == i)){
                        teamplayer[ii].getInventory().setItem(9+ii,mapItem);
                    }
                }

            }

        }catch (Exception e){}
    }

}
