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
        for (Player player : players) {
            if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                //player.setVelocity(player.getLocation().getDirection().normalize().multiply(v.exVector));
                Vector v = player.getVelocity();
                v.add(new Vector(0, net.mochidsuki.corsica.battleroyalecore.v.eyVector,0));
                if(player.getLocation().getDirection().equals(player.getLocation().getDirection().normalize().multiply(net.mochidsuki.corsica.battleroyalecore.v.esLimit))) {
                    v.add(player.getLocation().getDirection().normalize().multiply(net.mochidsuki.corsica.battleroyalecore.v.exVector));
                }
                player.setVelocity(v);
            }
        }
    }
}
