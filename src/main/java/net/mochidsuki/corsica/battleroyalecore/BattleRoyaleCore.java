package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getMap;

public final class BattleRoyaleCore extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        getLogger().info("Battle Royale 2 Pluginが目を覚ました！");
        getServer().getPluginManager().registerEvents(new Event(), this);
        getCommand("gameround").setExecutor(new CommandClass()); //gameround
        getCommand("debugerb").setExecutor(new CommandClass()); //debugerb
        getCommand("brc").setExecutor(new CommandClass()); //brgame
        getCommand("mapgenerator").setExecutor(new CommandClass()); //mapgenerator
        getCommand("openelytra").setExecutor(new CommandClass()); //openelytra
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

        //BossBar
        b.bossBar = this.getServer().createBossBar("開始までお待ちください。。。", BarColor.WHITE, BarStyle.SEGMENTED_10);
        b.bossBar.setVisible(true);


        //every ticks
        new Everyticks().runTaskTimer(this,0L,1);

        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();
        getMap(v.bigmapdata).addRenderer(new OriginMapRender());
        mapMetaB.setMapId(v.bigmapdata);

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
    static boolean inv;
    static HashMap<Player, Location> pin = new HashMap<>();
    static HashMap<Player, Location> pinRed = new HashMap<>();
    static HashMap<Player, ItemStack[]> knockDownBU = new HashMap<>();

}
class b{
    static int[] target = new int[4];

    static double[] center = new double[3];

    static BossBar bossBar;
}

class ui{
    static HashMap<Player , Integer> kill = new HashMap<>();
    static HashMap<Player , Integer> damage = new HashMap<>();
}

