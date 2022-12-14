package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import static net.mochidsuki.corsica.battleroyalecore.v.rtime;

public class border extends BukkitRunnable {
    double[] speed;
    int radius;


    public border(double[] speed, int radius, int rtime) {
        this.speed = speed;
        this.radius = radius;
        v.rtime = rtime*20;
    }


    @Override
    public void run() {

        if (rtime > 0) {
            rtime = rtime - 1;
            v.now[0] = v.now[0] + speed[0];
            v.now[1] = v.now[1] + speed[1];
            v.now[2] = v.now[2] + speed[2];
            v.now[3] = v.now[3] + speed[3];
            World world = Bukkit.getWorld("world");
            world.getWorldBorder().setCenter(v.now[1] + (v.now[0] - v.now[1])/2,v.now[3] + (v.now[2] - v.now[3])/2);
            world.getWorldBorder().setSize((v.now[0]-v.now[1]));


            /*
            int x= (int)v.now[0];
            int mx= (int)v.now[1];
            int z= (int)v.now[2];
            int mz= (int)v.now[3];
            for(; mx < x; mx = mx+1){
                World world = Bukkit.getWorld("world");
                BlockData block = Material.getMaterial("REDSTONE_BLOCK").createBlockData();
                world.getBlockAt(mx,20,z).setBlockData(block);
                world.getBlockAt(mx,20,mz).setBlockData(block);
            }
            for(; mz < z; mz = mz+1){
                World world = Bukkit.getWorld("world");
                BlockData block = Material.getMaterial("REDSTONE_BLOCK").createBlockData();
                world.getBlockAt(x,20,mz).setBlockData(block);
                world.getBlockAt(mx,20,mz).setBlockData(block);
            }
             */


        }else{
            if(v.gameround < 6){
                Roundsystemc r = new Roundsystemc();
                v.gameround = v.gameround + 1;
                r.Roundsystem();
            }
            cancel();
        }

    }

}
