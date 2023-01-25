package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

import static org.bukkit.Bukkit.getMap;

public final class BattleRoyaleCore extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        getLogger().info("Battle Royale 2 Pluginが目を覚ました！");
        getServer().getPluginManager().registerEvents(new event(), this);
        getCommand("gameround").setExecutor(new CommandClass()); //gameround
        getCommand("debugerb").setExecutor(new CommandClass()); //debugerb
        getCommand("brc").setExecutor(new CommandClass()); //brgame
        getCommand("mapgenerator").setExecutor(new CommandClass()); //mapgenerator
        v.rtime = 0;
        v.stime =0;
        plugin = this;

        //Config
        Config config = new Config(plugin);
        config.load();

        //PlaceholderAPI
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SomeExpansion(this).register();
        }
        //every ticks
        new everyticks().runTaskTimer(this,0L,1);
        //Big Map Data
        /*
        MapView origin = getMap(v.bigmapdata);
        origin.addRenderer(new MapRenderer() {
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
        });

         */
        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();
        getMap(v.bigmapdata).addRenderer(new OriginMapRender());
        mapMetaB.setMapId(v.bigmapdata);
/*
        MapView viewB = Bukkit.getServer().;
        mapMetaB.setMapView(viewB);

 */
        mapItemB.setItemMeta(mapMetaB);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getLogger().info("Battle Royale 2 Pluginがおねんねした");
        // Plugin shutdown logic
    }
    public static Plugin getPlugin() {
        return plugin;
    }

}

class v{
    static double[] now = new double[4];
    static int gameround;
    static int mr;
    static int mcx;
    static int mcz;
    static long[] roundstime = new long[7];
    static int[] roundrtime = new int[7];
    static long stime;
    static int rtime;
    static int bigmapdata;
    static Color[] colors = new Color[16384];
    static double exVector;
    static double eyVector;
    static double esLimit;
    static int mapScale;
}
class b{
    static int nx;
    static int nmx;
    static int nz;
    static int nmz;
    static int[] target = new int[4];

    static double[] center = new double[3];
}

class m{
    static int[] distance = new int[4];
}
