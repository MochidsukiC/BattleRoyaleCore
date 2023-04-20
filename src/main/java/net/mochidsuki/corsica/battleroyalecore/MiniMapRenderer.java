package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
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
            }catch (Exception ignored){}
        }
            //チームメイト表示
        Team playerteam = player.getScoreboard().getPlayerTeam(player);
        Iterator<String> iterator = playerteam.getEntries().iterator();
        MapCursorCollection cursor = new MapCursorCollection();
            while (iterator.hasNext()){
                try {
                    Player teammate = player.getServer().getPlayer(iterator.next());
                    if (teammate != null) {
                        if (!(teammate == player)) {
                            int x = teammate.getLocation().getBlockX() - player.getLocation().getBlockX();
                            if (x > 128) {
                                x = 127;
                            } else if (x < -128) {
                                x = -127;
                            }
                            int z = (teammate.getLocation().getBlockZ() - player.getLocation().getBlockZ());
                            if (z > 128) {
                                z = 127;
                            } else if (z < -128) {
                                z = -127;
                            }

                            cursor.addCursor(new MapCursor((byte) x, (byte) z, (byte) ((teammate.getLocation().getYaw() - teammate.getLocation().getYaw() % 45) / 45), MapCursor.Type.BLUE_POINTER, true));

                        }
                        if(v.pin.containsKey(teammate)) {
                            int x = v.pin.get(teammate).getBlockX() - player.getLocation().getBlockX();
                            int z = v.pin.get(teammate).getBlockZ() - player.getLocation().getBlockZ();
                            if (x < 128 && x > -128 && z < 128 && z > -128) {
                                cursor.addCursor(new MapCursor((byte) x, (byte) z, (byte) 0, MapCursor.Type.BANNER_YELLOW, true));
                            }
                        }
                        if(v.pinRed.containsKey(teammate)) {
                            int xR = v.pinRed.get(teammate).getBlockX() - player.getLocation().getBlockX();
                            int zR = v.pinRed.get(teammate).getBlockZ() - player.getLocation().getBlockZ();
                            if (xR < 128 && xR > -128 && zR < 128 && zR > -128) {
                                cursor.addCursor(new MapCursor((byte) xR, (byte) zR, (byte) 0, MapCursor.Type.BANNER_RED, true));
                            }
                        }
                    }
                }catch (Exception e){}
            }
            canvas.setCursors(cursor);

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
        int i = 0;
        for (String entry : playerteam.getEntries()){
            if(BattleRoyaleCore.getPlugin().getServer().getPlayer(entry) != null && BattleRoyaleCore.getPlugin().getServer().getPlayer(entry).isOnline() && BattleRoyaleCore.getPlugin().getServer().getPlayer(entry) != player) {
                Player teammate = Bukkit.getPlayer(entry);
                //下地
                for(int ii = 0;ii < 10;ii++){
                    for(int iii = 0;iii < 128;iii++){
                        if(teammate.hasPotionEffect(PotionEffectType.UNLUCK)) {
                            canvas.setPixelColor(iii,127 - ii - i*10,Color.RED);
                        }else if(teammate.getGameMode() == GameMode.SURVIVAL){
                            canvas.setPixelColor(iii,127 - ii - i*10,Color.YELLOW);
                        }else {
                            canvas.setPixelColor(iii,127 - ii - i*10,Color.GRAY);
                        }
                    }
                }
                //枠
                for(int ii = 0;ii < 128;ii++){
                    if(teammate.hasPotionEffect(PotionEffectType.UNLUCK)) {
                        canvas.setPixelColor(ii, 127 - i * 10, Color.RED.darker());
                        canvas.setPixelColor(ii, 127 - i * 10 - 9, Color.RED.darker());
                        if(ii < 10){
                            canvas.setPixelColor(0,128 - ii,Color.RED.darker());
                            canvas.setPixelColor(1,128 - ii,Color.RED.darker());
                        }
                    }else if(teammate.getGameMode() == GameMode.SURVIVAL){
                        canvas.setPixelColor(ii, 127 - i * 10, Color.YELLOW.darker());
                        canvas.setPixelColor(ii, 127 - i * 10 - 9, Color.YELLOW.darker());
                        if(ii < 10){
                            canvas.setPixelColor(0,128 - ii,Color.YELLOW.darker());
                            canvas.setPixelColor(127,128 - ii,Color.YELLOW.darker());
                        }
                    }else {
                        canvas.setPixelColor(ii, 127 - i * 10, Color.GRAY.darker());
                        canvas.setPixelColor(ii, 127 - i * 10 - 9, Color.GRAY.darker());
                        if(ii < 10){
                            canvas.setPixelColor(0,128 - ii,Color.GRAY.darker());
                            canvas.setPixelColor(127,128 - ii,Color.GRAY.darker());
                        }
                    }
                }

                MinecraftFont font = new MinecraftFont();
                canvas.drawText(2,127 - i * 10 - 8, font, "§30;" + teammate.getName());

                int health = (int) teammate.getHealth();
                for(int ii = 0;ii < 20;ii++){
                    if(ii < health) {
                        for (int iii = 0; iii < 3; iii++) {
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 1, Color.LIGHT_GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 2, Color.WHITE);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 3, Color.WHITE);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 4, Color.WHITE);
                            if (ii == health) {
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 1, Color.LIGHT_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 2, Color.LIGHT_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 3, Color.LIGHT_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 4, Color.LIGHT_GRAY);
                            }
                        }
                    }else {
                        for(int iii = 0; iii < 3;iii++) {
                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 1, Color.DARK_GRAY);
                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 2, Color.GRAY);
                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 3, Color.GRAY);
                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 4, Color.GRAY);
                        if(ii == health) {
                            canvas.setPixelColor(127, 127 - i * 10 - 1, Color.DARK_GRAY);
                            canvas.setPixelColor(127, 127 - i * 10 - 2, Color.DARK_GRAY);
                            canvas.setPixelColor(127, 127 - i * 10 - 3, Color.DARK_GRAY);
                            canvas.setPixelColor(127, 127 - i * 10 - 4, Color.DARK_GRAY);
                        }
                        }
                    }
                }

                if(!teammate.hasPotionEffect(PotionEffectType.UNLUCK)) {
                    if (teammate.getInventory().getItem(22) != null) {
                        ShieldUtil shieldUtil = new ShieldUtil(teammate.getInventory().getItem(22));
                        for (int ii = 0; ii < shieldUtil.getShieldMax(); ii++) {
                            if (ii < shieldUtil.getShieldNow()) {
                                try {
                                    for (int iii = 0; iii < 3; iii++) {
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, shieldUtil.getShieldColor().asBungee().getColor());
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, shieldUtil.getShieldColor().asBungee().getColor());
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, shieldUtil.getShieldColor().asBungee().getColor());
                                        if (ii == shieldUtil.getShieldMax() - 1) {
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 5, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 6, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 7, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 8, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                for (int iii = 0; iii < 3; iii++) {
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, Color.DARK_GRAY);
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, Color.GRAY);
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, Color.GRAY);
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, Color.GRAY);
                                    if (ii == shieldUtil.getShieldMax() - 1) {
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 5, Color.DARK_GRAY);
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 6, Color.DARK_GRAY);
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 7, Color.DARK_GRAY);
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 8, Color.DARK_GRAY);
                                    }
                                }
                            }
                        }
                    }
                } else {
                for(int ii = 0;ii < 20;ii++) {
                    if (ii < health - 20) {
                        for (int iii = 0; iii < 3; iii++) {
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, Color.LIGHT_GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, Color.WHITE);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, Color.WHITE);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, Color.WHITE);
                        }
                    }else {
                        for(int iii = 0; iii < 3;iii++) {
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, Color.DARK_GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, Color.GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, Color.GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, Color.GRAY);
                            if(ii == health - 20) {
                                canvas.setPixelColor(127, 127 - i * 10 - 5, Color.DARK_GRAY);
                                canvas.setPixelColor(127, 127 - i * 10 - 6, Color.DARK_GRAY);
                                canvas.setPixelColor(127, 127 - i * 10 - 7, Color.DARK_GRAY);
                                canvas.setPixelColor(127, 127 - i * 10 - 8, Color.DARK_GRAY);
                            }
                        }
                    }
                }
            }

                i++;
            }
        }




    }
}
