package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderShiver extends BukkitRunnable {

    int time;
    World world;

    public BorderShiver(int stime, World world) {
        time = stime;
        this.world =world;
    }

    @Override
    public void run() {
        if(time > 0) {
            time = time - 1;
            int i;
            if(time % 2 == 1){
                i = 1;
            }else {
                i=-1;
            }
            world.getWorldBorder().setSize(world.getWorldBorder().getSize() - i);
        }
    }
}
