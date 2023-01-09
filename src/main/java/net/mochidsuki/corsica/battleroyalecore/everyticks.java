package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;

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
                Location l = player.getLocation();
                l.setY(player.getLocation().getBlockY() + 1);
                if(player.getLocation().getPitch() > 0) {
                    player.setVelocity(player.getLocation().getDirection().normalize().multiply(v.exVector));
                    Vector v = player.getVelocity();
                    v.add(new Vector(0, net.mochidsuki.corsica.battleroyalecore.v.eyVector,0));
                    player.setVelocity(v);
                }else {
                    player.getLocation().setPitch(-1f);
                    player.setVelocity(player.getLocation().getDirection().normalize().multiply(v.exVector/4));
                    Vector v = player.getVelocity();
                    v.add(new Vector(0, net.mochidsuki.corsica.battleroyalecore.v.eyVector,0));
                    player.setVelocity(v);
                }
                player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL,l,50,0.1,0.1,0.1,0.2);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SMOKE_NORMAL,l,50,0.2,0.2,0.2,0);

            }
            if(player.hasPotionEffect(PotionEffectType.LEVITATION)){
                Location l = player.getLocation();
                l.setY(player.getLocation().getBlockY() + 1);
                player.getWorld().spawnParticle(Particle.SPIT,l,50,0.1,0.1,0.1,0.2);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SMOKE_NORMAL,l,50,0.2,0.2,0.2,0);

            }
        }
    }
}
