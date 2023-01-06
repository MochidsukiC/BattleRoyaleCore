package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getMap;

public class GameStart {
    public void player(Player player){
        //ビッグマップ処理
        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();
        /*
        getMap(v.bigmapdata).addRenderer(new BigMapRenderer());
        mapMetaB.setMapId(v.bigmapdata);
         */




        MapView viewB = player.getServer().createMap(player.getWorld());
        viewB.addRenderer(new BigMapRenderer());
        mapMetaB.setMapView(viewB);
        mapItemB.setItemMeta(mapMetaB);
        //ビッグマップ付与
        player.getInventory().setItem(8,mapItemB);
        player.addScoreboardTag("live");
        player.setGameMode(GameMode.SURVIVAL);


        //ミニマップ処理・付与
        ItemStack mapItem = new ItemStack(Material.FILLED_MAP,1);//
        MapMeta mapMeta = (MapMeta)mapItem.getItemMeta();//
        MapView view = player.getServer().createMap(player.getWorld());//
        view.addRenderer(new MiniMapRenderer());//
        mapMeta.setMapView(view);//
        mapItem.setItemMeta(mapMeta);//
        view.setScale(MapView.Scale.CLOSEST);
        view.setTrackingPosition(true);
        player.getInventory().setItem(EquipmentSlot.OFF_HAND,mapItem);
        }

    }

