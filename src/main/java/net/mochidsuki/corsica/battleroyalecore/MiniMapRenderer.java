package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MiniMapRenderer extends MapRenderer {
    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        map.setCenterX(player.getLocation().getBlockX());
        map.setCenterZ(player.getLocation().getBlockZ());

        for(int x = 0;x <= 128;x++){
            for(int z = 0;z <= 128;z++){
                canvas.setPixelColor(x, z, canvas.getBasePixelColor(x,z));
            }
        }

        //border予測線
        int[] distance = new int[4];
        distance[0] = b.target[0] - player.getLocation().getBlockX() - 64;
        distance[1] = b.target[1] - player.getLocation().getBlockX() - 64;
        distance[2] = -1 * (b.target[3] - player.getLocation().getBlockZ() - 64);
        distance[3] = -1 * (b.target[2] - player.getLocation().getBlockZ() - 64);
        for (; distance[1] < distance[0]; distance[1] = distance[1] + 1) {
            if(distance[1] >= 0 && distance[1] <= 128 && distance[2] >= 0 && distance[2] <= 128 ) {
                canvas.setPixelColor(distance[1], distance[2], Color.red);
            }
            if(distance[1] >= 0 && distance[1] <= 128 && distance[3] >= 0 && distance[3] <= 128 ) {
                canvas.setPixelColor(distance[1], distance[3], Color.red);
            }
        }
        distance[1] = b.target[1] - player.getLocation().getBlockX() - 64;
        for (; distance[3] < distance[2]; distance[3] = distance[3] + 1) {
            if(distance[0] >= 0 && distance[0] <= 128 && distance[3] >= 0 && distance[3] <= 128 ) {
                canvas.setPixelColor(distance[0], distance[3], Color.red);
            }
            if(distance[1] >= 0 && distance[1] <= 128 && distance[3] >= 0 && distance[3] <= 128 ) {
                canvas.setPixelColor(distance[1], distance[3], Color.red);
            }
        }
    }
}
