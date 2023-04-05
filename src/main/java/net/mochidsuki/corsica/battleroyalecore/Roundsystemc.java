package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.World;

import java.util.Random;

import static net.mochidsuki.corsica.battleroyalecore.b.center;
import static net.mochidsuki.corsica.battleroyalecore.v.rtime;
import static net.mochidsuki.corsica.battleroyalecore.v.stime;


public class Roundsystemc {
    //ラウンドシステム
    public void Roundsystem(World world){
        double[] speed = new double[4];
        double radius=0;
        double radiusk=0;
        center[1] = (int)(v.now[1] + (v.now[0]-v.now[1])/2);
        center[2] = (int)(v.now[3] + (v.now[2]-v.now[3])/2);
        int rnd = 9;
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
                rnd = 5;
                break;
            case 4:
                radius = (int)(v.mr * 0.5*0.65*0.6);
                radiusk = 0.3;
                rtime = v.roundrtime[4];
                stime = v.roundstime[4];
                rnd = 5;
                break;
            case 5:
                radius = (int)(v.mr * 0.5*0.65*0.6*0.5);
                radiusk = 0.3;
                rtime = v.roundrtime[5];
                stime = v.roundstime[5];
                rnd = 5;
                break;
            case 6:
                radius = (int)(v.mr * 0.5*0.65*0.6*0.5*0.5);
                radiusk = 0;
                rtime = v.roundrtime[6];
                stime = v.roundstime[6];
                rnd = 5;
                break;
        }
        Random random = new Random();
        center[0] = random.nextInt(rnd);
        switch ((int)center[0]){
            case 0:
                break;
            case 1:
                center[1] = (int)(center[1] - radius*(1-radiusk));
                break;
            case 2:

                center[2] = (int)(center[2] + radius*(1-radiusk));
                break;
            case 3:
                center[1] = (int)(center[1] + radius*(1-radiusk));
                break;
            case 4:
                center[2] = (int)(center[2] - radius*(1-radiusk));
                break;
            case 5:
                center[1] = (int)(center[1] - radius*(1-radiusk));
                center[2] = (int)(center[2] + radius*(1-radiusk));
                break;
            case 6:
                center[1] = (int)(center[1] + radius*(1-radiusk));
                center[2] = (int)(center[2] + radius*(1-radiusk));

                break;
            case 7:
                center[1] = (int)(center[1] + radius*(1-radiusk));
                center[2] = (int)(center[2] - radius*(1-radiusk));
                break;
            case 8:
                center[1] = (int)(center[1] - radius*(1-radiusk));
                center[2] = (int)(center[2] - radius*(1-radiusk));
                break;
        }
        b.target[0] = (int)(center[1] + radius*radiusk);
        b.target[1] = (int)(center[1] - radius*radiusk);
        b.target[2] = (int)(center[2] + radius*radiusk);
        b.target[3] = (int)(center[2] - radius*radiusk);

        speed[0] = (b.target[0] - v.now[0])/ rtime/20;
        speed[1] = (b.target[1] - v.now[1])/rtime/20;
        speed[2] = (b.target[2] - v.now[2])/rtime/20;
        speed[3] = (b.target[3] - v.now[3])/rtime/20;



        new Border(speed,radius,rtime,world).runTaskTimer(BattleRoyaleCore.getPlugin(), stime*20, 1L);
        new BorderShiver((int) (stime*20),world).runTaskTimer(BattleRoyaleCore.getPlugin(),0L,1L);

    }


}
