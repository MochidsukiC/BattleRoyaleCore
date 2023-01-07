package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class everyticks extends BukkitRunnable {
    int s = 19;

    @Override
    public void run() {
        s = s + 1;
        if(s >= 20){
            //毎秒処理
            if(v.stime >0) {
                v.stime = v.stime - 1;
            }
            s = 0;
        }



        //everyticks
        Player[] players = Bukkit.getServer().getOnlinePlayers().toArray((new Player[0]));
        for(int i = 0;i < players.length;i++){
            Player player = players[i];
            if(player.hasPotionEffect(PotionEffectType.SLOW_FALLING)){
                player.setVelocity(player.getLocation().getDirection().normalize().multiply(1));
            }
        }
    }
}
