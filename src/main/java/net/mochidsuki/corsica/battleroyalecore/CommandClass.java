package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;



public class CommandClass implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("gameround")){
            v.gameround = Integer.parseInt(args[0]);
            Roundsystemc r = new Roundsystemc();
            r.Roundsystem();
            return true;
        }
        if(command.getName().equalsIgnoreCase("brgame")){
            if(args[0].equalsIgnoreCase("reload")){
                Config config = new Config(BattleRoyaleCore.getPlugin());
                config.load();
                return true;
            }
        }
        if(command.getName().equalsIgnoreCase("forceloadchanks")){
            int distance = Integer.parseInt(args[0]);
            try {
                int x = v.mcx / 16 - distance;
                int z = v.mcz / 16 - distance;
                for (; x < x + distance * 2; x = x + 1) {
                    z = z + 1;
                    sender.getServer().getWorld("world").setChunkForceLoaded(x, z, true);
                }
            }catch (Exception e){}

            return true;
        }
        if(command.getName().equalsIgnoreCase("debugerb")){
            sender.sendMessage(v.now[0]+","+ v.now[1]+","+ v.now[2]+","+ v.now[3]+","+v.roundstime[1]);
            return true;
        }
        return false;
    }
}
