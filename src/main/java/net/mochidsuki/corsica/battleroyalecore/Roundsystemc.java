package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.Random;

import static net.mochidsuki.corsica.battleroyalecore.v.rtime;
import static net.mochidsuki.corsica.battleroyalecore.v.stime;


public class Roundsystemc {
    //ラウンドシステム
    public void Roundsystem(){
        int[] center = new int[3];
        double[] speed = new double[4];
        int radius=0;
        double radiusk=0;
        center[1] = (int)(v.now[1] + (v.now[0]-v.now[1])/2);
        center[2] = (int)(v.now[3] + (v.now[2]-v.now[3])/2);
        switch (v.gameround){
            case 1:
                radius = v.mr;
                radiusk = 0.5;
                center[1] = v.mcx;
                center[2] = v.mcz;
                rtime = v.roundrtime[1];
                stime = v.roundstime[1];
                v.now[0] = v.mcx+v.mr;
                v.now[1] = v.mcx-v.mr;
                v.now[2] = v.mcz+v.mr;
                v.now[3] = v.mcz-v.mr;
                break;
            case 2:
                radius = (int)(v.mr * 0.5);
                radiusk = 0.65;
                rtime = v.roundrtime[2];
                stime = v.roundstime[2];
                break;
            case 3:
                radius = (int)(v.mr * 0.5*0.65);
                radiusk = 0.6;
                rtime = v.roundrtime[3];
                stime = v.roundstime[3];
                break;
            case 4:
                radius = (int)(v.mr * 0.5*0.65*0.6);
                radiusk = 0.5;
                rtime = v.roundrtime[4];
                stime = v.roundstime[4];
                break;
            case 5:
                radius = (int)(v.mr * 0.5*0.65*0.6*0.5);
                radiusk = 0.5;
                rtime = v.roundrtime[5];
                stime = v.roundstime[5];
                break;
            case 6:
                radius = (int)(v.mr * 0.5*0.65*0.6*0.5*0.5);
                radiusk = 0;
                rtime = v.roundrtime[6];
                stime = v.roundstime[6];
                break;
        }
        Random rnd = new Random();
        center[0] = rnd.nextInt(9);
        switch (center[0]){
            case 0:
                center[1] = (int)(center[1] - radius*(1-radiusk));
                center[2] = (int)(center[2] + radius*(1-radiusk));
                break;
            case 1:
                center[2] = (int)(center[2] + radius*(1-radiusk));
                break;
            case 2:
                center[1] = (int)(center[1] + radius*(1-radiusk));
                center[2] = (int)(center[2] + radius*(1-radiusk));
                break;
            case 3:
                center[1] = (int)(center[1] - radius*(1-radiusk));
                break;
            case 4:
                break;
            case 5:
                center[1] = (int)(center[1] + radius*(1-radiusk));
                break;
            case 6:
                center[1] = (int)(center[1] - radius*(1-radiusk));
                center[2] = (int)(center[2] - radius*(1-radiusk));
                break;
            case 7:
                center[2] = (int)(center[2] - radius*(1-radiusk));
                break;
            case 8:
                center[1] = (int)(center[1] + radius*(1-radiusk));
                center[2] = (int)(center[2] - radius*(1-radiusk));
                break;
        }
        int[] target = new int[4];
        target[0] = (int)(center[1] + radius*radiusk);
        target[1] = (int)(center[1] - radius*radiusk);
        target[2] = (int)(center[2] + radius*radiusk);
        target[3] = (int)(center[2] - radius*radiusk);

        speed[0] = (target[0] - v.now[0])/ rtime/20;
        speed[1] = (target[1] - v.now[1])/rtime/20;
        speed[2] = (target[2] - v.now[2])/rtime/20;
        speed[3] = (target[3] - v.now[3])/rtime/20;

        new fillborder(target).runTaskAsynchronously(BattleRoyaleCore.getPlugin());

        new border(speed,radius,rtime).runTaskTimer(BattleRoyaleCore.getPlugin(), stime*20, 1L);

    }


}
