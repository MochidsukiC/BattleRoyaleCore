package net.mochidsuki.corsica.battleroyalecore;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.Objects;

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
            //タイトル
            try {
                int shieldMax;
                ItemStack chestItem = player.getInventory().getItem(22);
                switch (Objects.requireNonNull(chestItem).getType()) {
                    case CHAINMAIL_CHESTPLATE:
                        shieldMax = 5;
                        break;
                    case IRON_CHESTPLATE:
                    case GOLDEN_CHESTPLATE:
                        shieldMax = 10;
                        break;
                    case DIAMOND_CHESTPLATE:
                        shieldMax = 15;
                        break;
                    case NETHERITE_CHESTPLATE:
                        shieldMax = 20;
                        break;
                    default:
                        shieldMax = 0;
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
                component.setText("[" + shield + half + openings + "]");
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
        }
    }
}
