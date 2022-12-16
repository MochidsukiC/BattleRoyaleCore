package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class MiniMapRenderer extends MapRenderer {
    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        map.setCenterX(player.getLocation().getBlockX());
        map.setCenterZ(player.getLocation().getBlockZ());

    }
}
