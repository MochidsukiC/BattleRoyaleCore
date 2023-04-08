package net.mochidsuki.corsica.battleroyalecore;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.mochidsuki.corsica.battleroyalecore.v.rtime;

public class Border extends BukkitRunnable {
    static boolean stop;
    double[] speed;
    double radius;
    World world;


    public Border(double[] speed, double radius, int rtime,World world) {
        this.speed = speed;
        this.radius = radius;
        v.rtime = rtime*20;
        this.world = world;
    }


    @Override
    public void run() {

        if (rtime > 0) {
            rtime = rtime - 1;
            v.now[0] = v.now[0] + speed[0];
            v.now[1] = v.now[1] + speed[1];
            v.now[2] = v.now[2] + speed[2];
            v.now[3] = v.now[3] + speed[3];
            world.getWorldBorder().setCenter(v.now[1] + (v.now[0] - v.now[1])/2,v.now[3] + (v.now[2] - v.now[3])/2);
            world.getWorldBorder().setSize((v.now[0]-v.now[1]));



        }else{
            if(v.gameround < 6){
                Roundsystemc r = new Roundsystemc();
                v.gameround = v.gameround + 1;
                r.Roundsystem(world);


            }else {
                v.gameround = 0;
            }
            cancel();
        }
        if(stop){
            stop = false;
            v.gameround = 0;
            cancel();
        }
    }

}
