package net.mochidsuki.corsica.battleroyalecore;



import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.Objects;
import java.util.Optional;


public class CommandClass implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("gameround")){
            if(args[0].equalsIgnoreCase("0")){
                return false;
            }
            if(args[0].equalsIgnoreCase("stop")){
                Border.stop = true;
                return true;
            }
            try {
                v.gameround = Integer.parseInt(args[0]);
            }catch (Exception ignored){}

            Roundsystemc r = new Roundsystemc();
            World world = sender.getServer().getWorld("World");
            if(args.length == 1) {

                try {
                    Block b = (Block) sender;
                    world = b.getWorld();
                } catch (Exception e) {
                }

                try {
                    Player player = (Player) sender;
                    world = player.getWorld();
                } catch (Exception e) {
                }
                r.Roundsystem(world);
            }else {
                r.Roundsystem(sender.getServer().getWorld(args[1]));
            }

            return true;
        }
        if(command.getName().equalsIgnoreCase("brc")){
            if(args[0].equalsIgnoreCase("reload")){
                Config config = new Config(BattleRoyaleCore.getPlugin());
                config.load();
                return true;
            }
            if(args[0].equalsIgnoreCase("setstart")){
                switch (args[1]) {
                    case "@a":
                        Player[] players = sender.getServer().getOnlinePlayers().toArray(new Player[0]);
                        for (Player player : players) {
                            GameStart g = new GameStart();
                            g.player(player);
                        }
                        return true;
                    case "@s":
                        GameStart g = new GameStart();
                        g.player((Player) sender);
                        return true;
                    default:
                        GameStart ga = new GameStart();
                        ga.player((Player) sender);
                        return true;
                }
            }
            /*
            if(args[0].equalsIgnoreCase("testpin")){
                Pin pin = new Pin();
                Optional.of(new Location((sender.getServer().getWorld(sender.getName())), 0,0,0));

                Optional<Location> loc1 = Optional.ofNullable(v.pin.get(sender));

                pin.pin((Player) sender,loc1.orElse(new Location(sender.getServer().getWorld(sender.getName()),0,0,0)),(new Location(sender.getServer().getWorld(sender.getName()),0,0,0)),(new Location(sender.getServer().getWorld(sender.getName()),0,0,0)),true,false,false);
            return true;
            }

             */

        }
        if(command.getName().equalsIgnoreCase("openelytra")) {
            switch (args[1]) {
                case "@a":
                    Player[] players = sender.getServer().getOnlinePlayers().toArray(new Player[0]);
                    for (Player player : players) {
                        player.setGliding(true);
                    }
                    return true;
                case "@s":
                    ((Player)sender).setGliding(true);
                    return true;
                default:
                    ((Player)sender).setGliding(true);
                    return true;
            }
        }
        if(command.getName().equalsIgnoreCase("mapgenerator")){
            Player player = (Player) sender;

            ItemStack mapItem = new ItemStack(Material.FILLED_MAP,1);
            MapMeta mapMeta = (MapMeta)mapItem.getItemMeta();
            MapView view = player.getServer().createMap(player.getWorld());
            Objects.requireNonNull(mapMeta).setMapView(view);
            mapItem.setItemMeta(mapMeta);
            switch (args[0]) {
                case "0":
                    view.setScale(MapView.Scale.CLOSEST);
                    break;
                case "1":
                    view.setScale(MapView.Scale.CLOSE);
                    break;
                case "2":
                    view.setScale(MapView.Scale.NORMAL);
                    break;
                case "3":
                    view.setScale(MapView.Scale.FAR);
                    break;
                case "4":
                    view.setScale(MapView.Scale.FARTHEST);
                    break;
            }
            view.setTrackingPosition(true);
            view.setCenterX(v.mcx);
            view.setCenterZ(v.mcz);
            player.getInventory().setItem(8,mapItem);
        }
        if(command.getName().equalsIgnoreCase("debugerb")){
            sender.sendMessage(v.now[0]+","+ v.now[1]+","+ v.now[2]+","+ v.now[3]+","+v.roundstime[1]);
            return true;
        }
        return false;
    }
}
