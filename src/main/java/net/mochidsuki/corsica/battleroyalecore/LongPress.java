package net.mochidsuki.corsica.battleroyalecore;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Objects;


public class LongPress extends BukkitRunnable {
    double use = 0;
    Player player;
    String type;
    Material item;
    double time;

    public LongPress(Player p, String t, Material i,double ti){
        player = p;
        type = t;
        item = i;
        time = ti;
    }

    @Override
    public void run() {
        if(player.getInventory().getItemInMainHand().getType() == item){
            use = use + 1;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,2,4,true,false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2,200,true,false));

            ShieldUtil shieldUtil = new ShieldUtil(player.getInventory().getItem(22));



            String bar = String.join("", Collections.nCopies((int) (use/time*10),"■"));
            String barM = String.join("", Collections.nCopies((int) ((time - use)/time*10),"-"));
            String half;

            if(use % (time/10) != 0){
                half = "□";
            }else {
                half = "";
            }
            player.sendTitle("","["+ shieldUtil.getShieldColor() +bar+half+barM+ChatColor.RESET+"]",0,2,20);

            if(use >= time){


                use =0;

                if(player.getInventory().getItem(22) != null) {
                    switch (type) {
                        case "shieldmini":
                            double d = shieldUtil.getShieldMeta().getDamage() - (2 / shieldUtil.getShieldMax() * shieldUtil.getShieldMaxDurability());
                            shieldUtil.getShieldMeta().setDamage((int) d);
                            player.getInventory().getItem(22).setItemMeta(shieldUtil.getShieldMeta());
                            use = 0;
                            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                            cancel();
                            break;

                        case "shieldmax":
                            shieldUtil.getShieldMeta().setDamage(0);
                            player.getInventory().getItem(22).setItemMeta(shieldUtil.getShieldMeta());
                            use = 0;
                            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                            cancel();
                            break;
                    }
                }
            }
        }else {
            use = 0;
            cancel();
        }
    }
}
