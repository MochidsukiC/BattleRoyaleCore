package net.mochidsuki.corsica.battleroyalecore;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;


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



        //everyticks
        Player[] players = Bukkit.getServer().getOnlinePlayers().toArray((new Player[0]));
        for (Player player : players) {//全プレイヤーに適応
            //アクションバー
            try {
                int shieldMax;
                ItemStack chestItem = player.getInventory().getItem(22);
                Optional<ItemStack> headItem = Optional.ofNullable(player.getInventory().getItem(21));
                Optional<ItemStack> bootsItem = Optional.ofNullable(player.getInventory().getItem(23));
                ChatColor colorH = ChatColor.RESET;
                ChatColor colorC = ChatColor.RESET;
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

                switch (Objects.requireNonNull(chestItem).getType()) {
                    case CHAINMAIL_CHESTPLATE:
                        shieldMax = 5;
                        colorC = ChatColor.GRAY;
                        break;
                    case IRON_CHESTPLATE:
                        shieldMax = 10;
                        colorC = ChatColor.DARK_GRAY;
                        break;
                    case GOLDEN_CHESTPLATE:
                        shieldMax = 10;
                        colorC = ChatColor.YELLOW;
                        break;
                    case DIAMOND_CHESTPLATE:
                        shieldMax = 15;
                        colorC = ChatColor.AQUA;
                        break;
                    case NETHERITE_CHESTPLATE:
                        shieldMax = 20;
                        colorC = ChatColor.DARK_RED;
                        break;
                    default:
                        shieldMax = 0;
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
                Damageable d = (Damageable) chestItem.getItemMeta();
                double maxDurability = chestItem.getType().getMaxDurability();
                double dDamage = Objects.requireNonNull(d).getDamage();
                double durableValue = maxDurability - dDamage;
                double shieldNow = (int) (durableValue / maxDurability * shieldMax);
                shield = String.join("", Collections.nCopies((int) (shieldNow / 2), "■"));

                if (!(shieldNow % 2 == 0)) {
                    half = "□";
                } else {
                    half = "";
                }
                openings = String.join("", Collections.nCopies((int) ((shieldMax - shieldNow) / 2), "-"));
                component.setText(colorH + "[" + colorC + shield + half + openings + colorB + "]");
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }catch (Exception ignored){}







            //エリトラのエフェクト
            if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                Location l = player.getLocation();
                l.setY(player.getLocation().getBlockY() + 1);
                if(player.getLocation().getPitch() > 0) {
                    player.setVelocity(player.getLocation().getDirection().normalize().multiply(v.exVector));
                    Vector v = player.getVelocity();
                    v.add(new Vector(0, net.mochidsuki.corsica.battleroyalecore.v.eyVector,0));
                    player.setVelocity(v);
                }else {
                    player.getLocation().setPitch(-1f);
                    player.setVelocity(player.getLocation().getDirection().normalize().multiply(v.exVector/4));
                    Vector v = player.getVelocity();
                    v.add(new Vector(0, net.mochidsuki.corsica.battleroyalecore.v.eyVector,0));
                    player.setVelocity(v);
                }
                player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL,l,50,0.1,0.1,0.1,0.2);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SMOKE_NORMAL,l,50,0.2,0.2,0.2,0);

            }
            if(player.hasPotionEffect(PotionEffectType.LEVITATION)){
                Location l = player.getLocation();
                l.setY(player.getLocation().getBlockY() + 1);
                player.getWorld().spawnParticle(Particle.SPIT,l,50,0.1,0.1,0.1,0.2);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,l,50,0.2,0.2,0.2,0);
                player.getWorld().spawnParticle(Particle.SMOKE_NORMAL,l,50,0.2,0.2,0.2,0);
            }


            //Pin

            Team playerteam = player.getScoreboard().getPlayerTeam(player);
            String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
            playerteam.getEntries().toArray(tp);
            Player[] teamplayer = new Player[tp.length+3];
            for (int i = 0;i < tp.length;i++) {
                teamplayer[i] = Bukkit.getPlayer(tp[i]);
            }

            Pin pin = new Pin();
            Optional.of(new Location(player.getWorld(), 0,0,0));

            Optional<Location>[] loc = new Optional[3];

            loc[0] = Optional.ofNullable(v.pin.get(teamplayer[0]));
            loc[1] = Optional.ofNullable(v.pin.get(teamplayer[1]));
            loc[2] = Optional.ofNullable(v.pin.get(teamplayer[2]));

            Location[] location = new Location[3];
            boolean[] booleans = new boolean[3];

            for(int i = 0;i < loc.length; i++) {
                location[i] = loc[i].orElse(new Location(player.getWorld(),0,0,0));
                booleans[i] = !(v.pin.get(teamplayer[i]) == null);
            }

            pin.pin(player, location,booleans);



            //Armor Synchronizer

            if(player.getPotionEffect(PotionEffectType.INVISIBILITY) == null) {
                player.getInventory().setItem(EquipmentSlot.HEAD, player.getInventory().getItem(21));
                player.getInventory().setItem(EquipmentSlot.CHEST, player.getInventory().getItem(22));
                player.getInventory().setItem(EquipmentSlot.FEET, player.getInventory().getItem(23));
                player.getInventory().setItem(EquipmentSlot.LEGS, player.getInventory().getItem(35));
                //player.updateInventory();
            }else {
                player.getInventory().setItem(EquipmentSlot.HEAD, new ItemStack(Material.AIR));
                player.getInventory().setItem(EquipmentSlot.CHEST, new ItemStack(Material.AIR));
                player.getInventory().setItem(EquipmentSlot.FEET, new ItemStack(Material.AIR));
                player.getInventory().setItem(EquipmentSlot.LEGS, new ItemStack(Material.AIR));
                //player.updateInventory();
            }
        }
    }
}
