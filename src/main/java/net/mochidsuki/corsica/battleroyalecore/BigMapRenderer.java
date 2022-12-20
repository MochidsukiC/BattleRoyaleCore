package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class BigMapRenderer extends MapRenderer {

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        for(int x = 0;x <= 128;x++){
            for(int z = 0;z <= 128;z++){
                canvas.setPixelColor(x, z, canvas.getBasePixelColor(x,z));
            }
        }
    }
}
