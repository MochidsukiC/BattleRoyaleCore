package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BorderShiver extends BukkitRunnable {
    static boolean stop;
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
                i = 2;
            }else {
                i= -2;
            }
            world.getWorldBorder().setSize(world.getWorldBorder().getSize() + i);
        }else {
            cancel();
            List<Player> players = (List<Player>) BattleRoyaleCore.getPlugin().getServer().getOnlinePlayers();
            for(Player player : players){
                player.sendMessage("ラウンド" + v.gameround);
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD+"ボーダーの収縮を開始");
                player.playSound(player, Sound.BLOCK_BELL_USE,1,0);
            }
        }
        if(stop){
            stop = false;
            v.gameround = 0;
            cancel();
        }
    }
}
