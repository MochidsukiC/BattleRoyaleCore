package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public class GameStart {
    public void player(Player player){
        //ビッグマップ処理
        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();

        MapView viewB = player.getServer().createMap(player.getWorld());
        viewB.addRenderer(new BigMapRenderer());
        mapMetaB.setMapView(viewB);
        mapItemB.setItemMeta(mapMetaB);
        switch (v.mapScale) {
            case 0:
                viewB.setScale(MapView.Scale.CLOSEST);
                break;
            case 1:
                viewB.setScale(MapView.Scale.CLOSE);
                break;
            case 2:
                viewB.setScale(MapView.Scale.NORMAL);
                break;
            case 3:
                viewB.setScale(MapView.Scale.FAR);
                break;
            case 4:
                viewB.setScale(MapView.Scale.FARTHEST);
                break;
        }
        viewB.setTrackingPosition(true);
        viewB.setCenterX(v.mcx);
        viewB.setCenterZ(v.mcz);
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


        //TeamColorレギンスを付与
        Color c = Color.fromRGB(player.getScoreboard().getPlayerTeam(player).getColor().asBungee().getColor().getRed(),player.getScoreboard().getPlayerTeam(player).getColor().asBungee().getColor().getGreen(),player.getScoreboard().getPlayerTeam(player).getColor().asBungee().getColor().getBlue());
        ItemStack i = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
        meta.setColor(c);
        i.setItemMeta(meta);
        player.getInventory().setItem(35,i);
        player.getInventory().setItem(21,new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setItem(22,new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setItem(23,new ItemStack(Material.LEATHER_BOOTS));
        }


    }

