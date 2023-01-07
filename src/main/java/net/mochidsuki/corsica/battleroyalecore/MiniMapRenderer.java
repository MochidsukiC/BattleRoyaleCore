package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class MiniMapRenderer extends MapRenderer {
    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        map.setCenterX(player.getLocation().getBlockX());
        map.setCenterZ(player.getLocation().getBlockZ());
        //ピクセルクリア
        for(int x = 0;x <= 128;x++){
            for(int z = 0;z <= 128;z++){
                canvas.setPixelColor(x, z, canvas.getBasePixelColor(x,z));
            }
        }
        //カーソルクリア
        for(int i = 0; i <= canvas.getCursors().size();i++){
            try {
                canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
            }catch (Exception e){}
        }
        try {
            //チームメイト表示
            Team playerteam = player.getScoreboard().getPlayerTeam(player);
            String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
            playerteam.getEntries().toArray(tp);
            Player[] teamplayer = new Player[tp.length];
            MapCursorCollection cursor = new MapCursorCollection();
            for (int i = 0; i < tp.length; i++) {
                teamplayer[i] = Bukkit.getPlayer(tp[i]);
                if (!(teamplayer[i] == player)) {
                    int x = Objects.requireNonNull(teamplayer[i]).getLocation().getBlockX() - player.getLocation().getBlockX();
                    if (x > 64) {
                        x = 64;
                    }
                    int z = teamplayer[i].getLocation().getBlockX() - player.getLocation().getBlockZ();
                    if (z > 64) {
                        z = 64;
                    }

                    cursor.addCursor(new MapCursor((byte) x, (byte) z, (byte) ((teamplayer[i].getLocation().getYaw() - teamplayer[i].getLocation().getYaw() % 45) / 45), MapCursor.Type.BLUE_POINTER, true));

                }
            }
            canvas.setCursors(cursor);
        }catch (Exception e){}

        //中心に対する線
        double[] cDistance = new double[4];
        cDistance[0] = b.center[0] - player.getLocation().getBlockX();
        cDistance[1] = b.center[1] - player.getLocation().getBlockZ();
        for(int x = 0;x <= cDistance[0];x++){

        }



        //border予測線
        int[] distance = new int[4];
        distance[0] = (b.target[0] - player.getLocation().getBlockX());
        distance[1] = (b.target[1] - player.getLocation().getBlockX());
        distance[2] = (b.target[3] - player.getLocation().getBlockZ());
        distance[3] = (b.target[2] - player.getLocation().getBlockZ());
        for (; distance[1] <= distance[0]; distance[1]++) {
            if(distance[1] >= -64 && distance[1] <= 64 && distance[2] >= -64 && distance[2] <= 64 ) {
                canvas.setPixelColor(distance[1]+64, distance[2]+64, Color.gray);
            }
            if(distance[1] >= -64 && distance[1] <= 64 && distance[3] >= -64 && distance[3] <= 64 ) {
                canvas.setPixelColor(distance[1]+64, distance[3]+64, Color.gray);
            }
        }
        distance[1] = b.target[1] - player.getLocation().getBlockX();
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
        distanceNow[0] = (int)(v.now[0] - player.getLocation().getBlockX()) ;
        distanceNow[1] = (int)(v.now[1] - player.getLocation().getBlockX());
        distanceNow[2] = (int)(v.now[3] - player.getLocation().getBlockZ());
        distanceNow[3] = (int)(v.now[2] - player.getLocation().getBlockZ());
        for (; distanceNow[1] <= distanceNow[0]; distanceNow[1]++) {
            if(distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[2] >= -64 && distanceNow[2] <= 64 ) {
                canvas.setPixelColor(distanceNow[1]+64, distanceNow[2]+64, Color.red);
            }
            if(distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[3] >= -64 && distanceNow[3] <= 64 ) {
                canvas.setPixelColor(distanceNow[1]+64, distanceNow[3]+64, Color.red);
            }
        }
        distanceNow[1] = (int)(v.now[1] - player.getLocation().getBlockX());
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
