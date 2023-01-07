package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class OriginMapRender extends MapRenderer {

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        int colorsi = 0;
        for(int x = 0;x <= 127;x++){
            for(int y = 0;y <= 127;y++){
                v.colors[colorsi] = mapCanvas.getBasePixelColor(x,y);
                colorsi = colorsi +1;

            }
        }
    }
}
