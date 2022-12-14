package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

public class fillborder extends BukkitRunnable {
    int[] target;
    public fillborder(int[] target) {
    this.target=target;
    }

    @Override
    public void run() {
        try {
            //元枠クリア
            int x = (int) v.now[0] + 1;
            int mx = (int) v.now[1] - 1;
            int z = (int) v.now[2] + 1;
            int mz = (int) v.now[3] - 1;
            for (; mx < x; mx = mx + 1) {
                World world = Bukkit.getWorld("world");
                BlockData block = Material.getMaterial("AIR").createBlockData();
                world.getBlockAt(mx, 20, z + 1).setBlockData(block);
                world.getBlockAt(mx, 20, mz + 1).setBlockData(block);
                world.getBlockAt(mx, 20, z).setBlockData(block);
                world.getBlockAt(mx, 20, mz).setBlockData(block);
                world.getBlockAt(mx, 20, z - 1).setBlockData(block);
                world.getBlockAt(mx, 20, mz - 1).setBlockData(block);
            }
            mx = (int) v.now[1] - 1;
            for (; mz < z; mz = mz + 1) {
                World world = Bukkit.getWorld("world");
                BlockData block = Material.getMaterial("AIR").createBlockData();
                world.getBlockAt(x, 20, mz).setBlockData(block);
                world.getBlockAt(mx, 20, mz).setBlockData(block);
                world.getBlockAt(x + 1, 20, mz).setBlockData(block);
                world.getBlockAt(mx + 1, 20, mz).setBlockData(block);
                world.getBlockAt(x - 1, 20, mz).setBlockData(block);
                world.getBlockAt(mx - 1, 20, mz).setBlockData(block);
            }
            mz = (int) v.now[3] - 1;
            //新規枠
            int nx = target[0] + 1;
            int nmx = target[1] - 1;
            int nz = target[2] + 1;
            int nmz = target[3] - 1;
            for (; nmx < nx; nmx = nmx + 1) {
                World world = Bukkit.getWorld("world");
                BlockData block = Material.getMaterial("REDSTONE_BLOCK").createBlockData();
                world.getBlockAt(nmx, 20, nz).setBlockData(block);
                world.getBlockAt(nmx, 20, nmz).setBlockData(block);
            }
            nmx = target[1] - 1;
            for (; nmz < nz; nmz = nmz + 1) {
                World world = Bukkit.getWorld("world");
                BlockData block = Material.getMaterial("REDSTONE_BLOCK").createBlockData();
                world.getBlockAt(nx, 20, nmz).setBlockData(block);
                world.getBlockAt(nmx, 20, nmz).setBlockData(block);
            }
            nmz = target[3] - 1;
        }catch (Exception e){

        }

    }
}
