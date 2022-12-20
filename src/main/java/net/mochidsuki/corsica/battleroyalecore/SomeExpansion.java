package net.mochidsuki.corsica.battleroyalecore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class SomeExpansion extends PlaceholderExpansion {

    private final BattleRoyaleCore plugin;

    public SomeExpansion(BattleRoyaleCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "mochidsuki";
    }

    @Override
    public String getIdentifier() {
        return "BRC";
    }

    @Override
    public String getVersion() {return "1.0.0";    }

    @Override
    public boolean persist() {return true;}

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("stime")){
            return (v.stime-v.stime % 60)/60+":"+v.stime % 60;
        }

        if(params.equalsIgnoreCase("rtime")) {
            int r = v.rtime/20;
            return (r-r % 60)/60+":"+r % 60;
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
