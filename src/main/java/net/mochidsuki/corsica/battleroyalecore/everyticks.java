package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.scheduler.BukkitRunnable;

public class everyticks extends BukkitRunnable {
    int s = 19;

    @Override
    public void run() {
        s = s + 1;
        if(s == 20){
            //毎秒処理
            v.stime = v.stime - 1;
        }
    }
}
