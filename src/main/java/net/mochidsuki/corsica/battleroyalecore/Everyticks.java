package net.mochidsuki.corsica.battleroyalecore;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;


public class Everyticks extends BukkitRunnable {
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

        Player[] players = Bukkit.getServer().getOnlinePlayers().toArray((new Player[0]));
        for (Player player : players) {//全プレイヤーに適応


            try {
                if (player.hasPotionEffect(PotionEffectType.UNLUCK)) {
                    for (int i = 0; i <= 2; i++) {
                        for (int ii = 0; ii <= 2; ii++) {
                            for (int iii = 0; iii <= 2; iii++) {
                                player.sendBlockChange(player.getLocation().add(i - 1, iii, ii - 1), player.getLocation().add(i - 1, iii, ii - 1).getBlock().getBlockData());
                            }
                        }
                    }


                    if (player.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                        player.sendBlockChange(player.getLocation().add(0, 1, 0), Material.BARRIER.createBlockData());
                    }
                }
            }catch (Exception e){e.printStackTrace();}


            //アクションバー
            try {
                Optional<ItemStack> headItem = Optional.ofNullable(player.getInventory().getItem(21));
                Optional<ItemStack> bootsItem = Optional.ofNullable(player.getInventory().getItem(23));
                ChatColor colorH = ChatColor.RESET;
                ChatColor colorB = ChatColor.RESET;

                switch (headItem.orElse(new ItemStack(Material.LEATHER_HELMET)).getType()) {
                    case CHAINMAIL_HELMET:
                        colorH = ChatColor.GRAY;
                        break;
                    case IRON_HELMET:
                        colorH = ChatColor.DARK_GRAY;
                        break;
                    case GOLDEN_HELMET:
                        colorH = ChatColor.YELLOW;
                        break;
                    case DIAMOND_HELMET:
                        colorH = ChatColor.AQUA;
                        break;
                    case NETHERITE_HELMET:
                        colorH = ChatColor.DARK_RED;
                        break;
                    default:
                }

                switch (bootsItem.orElse(new ItemStack(Material.LEATHER_BOOTS)).getType()) {
                    case CHAINMAIL_BOOTS:
                        colorB = ChatColor.GRAY;
                        break;
                    case IRON_BOOTS:
                        colorB = ChatColor.DARK_GRAY;
                        break;
                    case GOLDEN_BOOTS:
                        colorB = ChatColor.YELLOW;
                        break;
                    case DIAMOND_BOOTS:
                        colorB = ChatColor.AQUA;
                        break;
                    case NETHERITE_BOOTS:
                        colorB = ChatColor.DARK_RED;
                        break;
                    default:
                }


                TextComponent component = new net.md_5.bungee.api.chat.TextComponent();
                String shield;
                String half;
                String openings;

                ShieldUtil shieldUtil = new ShieldUtil(player.getInventory().getItem(22));
                shield = String.join("", Collections.nCopies((int) (shieldUtil.getShieldNow() / 2), "■"));

                if (!(shieldUtil.getShieldNow() % 2 == 0)) {
                    half = "□";
                } else {
                    half = "";
                }
                openings = String.join("", Collections.nCopies((int) ((shieldUtil.getShieldMax() - shieldUtil.getShieldNow()) / 2), "-"));
                component.setText(colorH + "[" + shieldUtil.getShieldColor() + shield + half + openings + colorB + "]");
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }catch (Exception e){e.printStackTrace();}






            ItemStack chestPlate = player.getInventory().getItem(22);
            try {
                //エリトラのエフェクト


                if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                    Location l = player.getLocation();
                    l.setY(player.getLocation().getBlockY() + 1);
                    if (player.getLocation().getPitch() > 0) {
                        player.setVelocity(player.getLocation().getDirection().normalize().multiply(v.exVector));
                        Vector v = player.getVelocity();
                        v.add(new Vector(0, net.mochidsuki.corsica.battleroyalecore.v.eyVector, 0));
                        player.setVelocity(v);
                    } else {
                        player.getLocation().setPitch(-1f);
                        player.setVelocity(player.getLocation().getDirection().normalize().multiply(v.exVector / 4));
                        Vector v = player.getVelocity();
                        v.add(new Vector(0, net.mochidsuki.corsica.battleroyalecore.v.eyVector, 0));
                        player.setVelocity(v);
                    }
                    player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, l, 50, 0.1, 0.1, 0.1, 0.2);
                    player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 50, 0.2, 0.2, 0.2, 0);
                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, l, 50, 0.2, 0.2, 0.2, 0);
                    player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, l, 50, 0.2, 0.2, 0.2, 0);

                    chestPlate = new ItemStack(Material.ELYTRA);//エリトラインベントリ同期の判定

                }
                if (player.hasPotionEffect(PotionEffectType.LEVITATION)) {
                    Location l = player.getLocation();
                    l.setY(player.getLocation().getBlockY() + 1);
                    player.getWorld().spawnParticle(Particle.SPIT, l, 50, 0.1, 0.1, 0.1, 0.2);
                    player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 50, 0.2, 0.2, 0.2, 0);
                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, l, 50, 0.2, 0.2, 0.2, 0);
                    player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, l, 50, 0.2, 0.2, 0.2, 0);

                    chestPlate = new ItemStack(Material.ELYTRA);//エリトラインベントリ同期の判定
                }
                if (player.hasPotionEffect(PotionEffectType.LUCK)) {
                    chestPlate = new ItemStack(Material.ELYTRA);
                }//エリトラインベントリ同期の判定
            }catch (Exception e){e.printStackTrace();}

            //Pin
            try {

                Protocol protocol = new Protocol();
                Team team = player.getScoreboard().getEntryTeam(player.getName());
                if(team != null){
                    Location[] location = new Location[team.getEntries().size()];
                    Location[] locationR = new Location[team.getEntries().size()];
                    int i = 0;
                    for(String playerName : team.getEntries()){

                        if(Bukkit.getOfflinePlayer(playerName).isOnline()) {

                            Player teammate = BattleRoyaleCore.getPlugin().getServer().getPlayer(playerName);
                            location[i] = v.pin.get(teammate);
                            locationR[i] = v.pinRed.get(teammate);

                            protocol.setGlowing(teammate, player);

                            i++;
                        }
                    }
                    protocol.pushPin(player,location, EntityType.DRAGON_FIREBALL,0);
                    protocol.pushPin(player,locationR,EntityType.FIREBALL,team.getEntries().size());
                }else {

                }



            }catch (Exception e){
                e.printStackTrace();
            }



            //Armor Synchronizer
            if(v.inv) {
                if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    player.getInventory().setItem(EquipmentSlot.HEAD, player.getInventory().getItem(21));


                    if (!Objects.equals(player.getInventory().getItem(EquipmentSlot.CHEST), new ItemStack(Material.ELYTRA))) {
                        player.getInventory().setItem(EquipmentSlot.CHEST, chestPlate);
                    }
                    player.getInventory().setItem(EquipmentSlot.FEET, player.getInventory().getItem(23));
                    player.getInventory().setItem(EquipmentSlot.LEGS, player.getInventory().getItem(35));
                    //player.updateInventory();
                } else {
                    player.getInventory().setItem(EquipmentSlot.HEAD, new ItemStack(Material.AIR));
                    player.getInventory().setItem(EquipmentSlot.CHEST, new ItemStack(Material.AIR));
                    player.getInventory().setItem(EquipmentSlot.FEET, new ItemStack(Material.AIR));
                    player.getInventory().setItem(EquipmentSlot.LEGS, new ItemStack(Material.AIR));
                    //player.updateInventory();
                }
            }
            //BossBar
            b.bossBar.addPlayer(player);
            double d = 1;
            if(v.gameround != 0) {
                if (v.stime > 0) {
                    b.bossBar.setTitle(ChatColor.YELLOW + "ラウンド" + v.gameround + ChatColor.GREEN + " - ボーダー収縮待機中・・・" + ChatColor.AQUA + (v.stime - v.stime % 60) / 60 + ":" + v.stime % 60 + ChatColor.GRAY + " - 残り部隊数 :" + player.getScoreboard().getObjective("teams").getScore("system").getScore());
                    b.bossBar.setColor(BarColor.GREEN);
                    b.bossBar.setProgress(d * v.stime / v.roundstime[v.gameround]);
                } else {
                    int r = v.rtime / 20;
                    b.bossBar.setTitle(ChatColor.YELLOW + "ラウンド" + v.gameround + ChatColor.RED + " - ボーダー収縮中・・・" + ChatColor.AQUA + (r - r % 60) / 60 + ":" + r % 60 + ChatColor.GRAY + " - 残り部隊数 :" + player.getScoreboard().getObjective("teams").getScore("system").getScore());
                    b.bossBar.setColor(BarColor.RED);
                    b.bossBar.setProgress(d * v.rtime / v.roundrtime[v.gameround] / 20);
                }
            }else {
                b.bossBar.setTitle("開始までお待ちください。。。");
                b.bossBar.setColor(BarColor.WHITE);
                b.bossBar.setProgress(1);
            }
        }
    }
}
