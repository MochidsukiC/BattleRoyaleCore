package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
    private final Plugin plugin;
    private FileConfiguration config = null;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        // ロードする
        load();
    }
    public void load() {
        // 設定ファイルを保存
        plugin.saveDefaultConfig();
        if (config != null) { // configが非null == リロードで呼び出された
            plugin.reloadConfig();
        }
        config = plugin.getConfig();
        v.mr = config.getInt("MAP.Radius");
        v.mcx = config.getInt("MAP.Center.x");
        v.mcz = config.getInt("MAP.Center.z");
        v.mapScale = config.getInt("MAP.MapScale");
        v.roundstime[1] = config.getInt("Ring.Round1.stime");
        v.roundrtime[1] = config.getInt("Ring.Round1.vtime");
        v.roundstime[2] = config.getInt("Ring.Round2.stime");
        v.roundrtime[2] = config.getInt("Ring.Round2.vtime");
        v.roundstime[3] = config.getInt("Ring.Round3.stime");
        v.roundrtime[3] = config.getInt("Ring.Round3.vtime");
        v.roundstime[4] = config.getInt("Ring.Round4.stime");
        v.roundrtime[4] = config.getInt("Ring.Round4.vtime");
        v.roundstime[5] = config.getInt("Ring.Round5.stime");
        v.roundrtime[5] = config.getInt("Ring.Round5.vtime");
        v.roundstime[6] = config.getInt("Ring.Round6.stime");
        v.roundrtime[6] = config.getInt("Ring.Round6.vtime");
        v.bigmapdata = config.getInt("MAP.MapData");
        v.exVector = config.getDouble("ADVANCED_SETTING.elytra_x_vector");
        v.eyVector = config.getDouble("ADVANCED_SETTING.elytra_y_vector");
        v.esLimit = config.getDouble("ADVANCED_SETTING.elytra_speed_limit");
        v.inv = config.getBoolean("ADVANCED_SETTING.Inventory_Locker");



    }
}
