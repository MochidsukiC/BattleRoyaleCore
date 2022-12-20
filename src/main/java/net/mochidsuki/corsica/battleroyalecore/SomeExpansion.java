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
            return String.valueOf(v.stime);
        }

        if(params.equalsIgnoreCase("rtime")) {
            return String.valueOf(v.rtime);
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
