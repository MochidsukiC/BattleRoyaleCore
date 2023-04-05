package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.Objects;


public class BigMapRenderer extends MapRenderer {

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        //Mapコピー
        int mi = 0;
        for(int x = 0;x <= 127;x++) {
            for (int z = 0; z <= 127; z++) {

                canvas.setPixelColor(x, z, v.colors[mi]);
                mi = mi + 1;
            }
        }
        int mapZoom;
        switch (v.mapScale){
            case 0:
                mapZoom = 1;
                break;
            case 1:
                mapZoom = 2;
                break;
            case 2:
                mapZoom = 4;
                break;
            case 3:
                mapZoom = 8;
                break;
            case 4:
                mapZoom = 16;
                break;
            default:
                mapZoom = 8;
        }
            //カーソルクリア
            for(int i = 0; i <= canvas.getCursors().size();i++){
                try {
                    canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
                }catch (Exception ignored){}
            }
                //チームメイト表示
                Team playerteam = player.getScoreboard().getPlayerTeam(player);
                Iterator<String> iterator = playerteam.getEntries().iterator();
                MapCursorCollection cursor = new MapCursorCollection();
                while (iterator.hasNext()){
                    Player teammate = player.getServer().getPlayer(iterator.next());
                    if(teammate != null) {
                        if (!(teammate == player)) {
                            int x = (teammate.getLocation().getBlockX() - v.mcx) / mapZoom;
                            if (x > 64) {
                                x = 64;
                            } else if (x < -64) {
                                x = -64;
                            }
                            int z = (teammate.getLocation().getBlockZ() - v.mcz) / mapZoom;
                            if (z > 64) {
                                z = 64;
                            } else if (z < -64) {
                                z = -64;
                            }
                            cursor.addCursor(new MapCursor((byte) x, (byte) z, (byte) ((teammate.getLocation().getYaw() - teammate.getLocation().getYaw() % 45) / 45), MapCursor.Type.BLUE_POINTER, true));
                        }
                        try {
                            cursor.addCursor(new MapCursor((byte) ((v.pin.get(teammate).getBlockX() - v.mcx) / mapZoom * 2), (byte) ((v.pin.get(teammate).getBlockZ() - v.mcz) / mapZoom * 2), (byte) 0, MapCursor.Type.BANNER_YELLOW, true));
                        }catch (Exception e){}
                        try {
                            cursor.addCursor(new MapCursor((byte) ((v.pinRed.get(teammate).getBlockX() - v.mcx) / mapZoom * 2), (byte) ((v.pinRed.get(teammate).getBlockZ() - v.mcz) / mapZoom * 2), (byte) 0, MapCursor.Type.BANNER_RED, true));
                        }catch (Exception e){}
                    }
                }
                canvas.setCursors(cursor);

            //border予測線
            int[] distance = new int[4];
            distance[0] = (b.target[0] - v.mcx)/mapZoom;
            distance[1] = (b.target[1] - v.mcx)/mapZoom;
            distance[2] = (b.target[3] - v.mcz)/mapZoom;
            distance[3] = (b.target[2] - v.mcz)/mapZoom;
            for (; distance[1] <= distance[0]; distance[1]++) {
                if(distance[1] >= -64 && distance[1] <= 64 && distance[2] >= -64 && distance[2] <= 64 ) {
                    canvas.setPixelColor(distance[1]+64, distance[2]+64, Color.gray);
                }
                if(distance[1] >= -64 && distance[1] <= 64 && distance[3] >= -64 && distance[3] <= 64 ) {
                    canvas.setPixelColor(distance[1]+64, distance[3]+64, Color.gray);
                }
            }
            distance[1] = (b.target[1] - v.mcx)/mapZoom;
            for (; distance[2] <= distance[3]; distance[2]++) {
                if(distance[0] >= -64 && distance[0] <= 64 && distance[2] >= -64 && distance[2] <= 64 ) {
                    canvas.setPixelColor(distance[0]+64, distance[2]+64, Color.gray);
                }
                if(distance[1] >= -64 && distance[1] <= 64 && distance[2] >= -64 && distance[2] <= 64 ) {
                    canvas.setPixelColor(distance[1]+64, distance[2]+64, Color.gray);
                }
            }

            //border現在位置
            int[] distanceNow = new int[4];
            distanceNow[0] = (int)(v.now[0] - v.mcx)/mapZoom;
            distanceNow[1] = (int)(v.now[1] - v.mcx)/mapZoom;
            distanceNow[2] = (int)(v.now[3] - v.mcz)/mapZoom;
            distanceNow[3] = (int)(v.now[2] - v.mcz)/mapZoom;
            for (; distanceNow[1] <= distanceNow[0]; distanceNow[1]++) {
                if(distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[2] >= -64 && distanceNow[2] <= 64 ) {
                    canvas.setPixelColor(distanceNow[1]+64, distanceNow[2]+64, Color.red);
                }
                if(distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[3] >= -64 && distanceNow[3] <= 64 ) {
                    canvas.setPixelColor(distanceNow[1]+64, distanceNow[3]+64, Color.red);
                }
            }
            distanceNow[1] = (int)(v.now[1] - v.mcx)/mapZoom;
            for (; distanceNow[2] <= distanceNow[3]; distanceNow[2]++) {
                if(distanceNow[0] >= -64 && distanceNow[0] <= 64 && distanceNow[2] >= -64 && distanceNow[2] <= 64 ) {
                    canvas.setPixelColor(distanceNow[0]+64, distanceNow[2]+64, Color.red);
                }
                if(distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[2] >= -64 && distanceNow[2] <= 64 ) {
                    canvas.setPixelColor(distanceNow[1]+64, distanceNow[2]+64, Color.red);
                }
            }
        }
    }
