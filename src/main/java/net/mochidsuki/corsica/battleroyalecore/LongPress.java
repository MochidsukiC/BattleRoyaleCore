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
    int use = 0;
    Player player;
    String type;
    Material item;
    int time;

    public LongPress(Player p, String t, Material i,int ti){
        player = p;
        type = t;
        item = i;
        time = ti;
    }

    @Override
    public void run() {
        if(player.getInventory().getItemInMainHand().getType() == item){
            use++;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,2,0,true,true));

            double shieldMax;
            ChatColor colorC = ChatColor.RESET;
            switch (Objects.requireNonNull(item)) {
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



            TextComponent component = new net.md_5.bungee.api.chat.TextComponent();
            String bar = String.join("", Collections.nCopies(time/use*10,"■"));
            String barM = String.join("", Collections.nCopies((use - time)/use*10,"-"));
            String half;

            if(!(use % 2 == 0)){
                half = "□";
            }else {
                half = "";
            }
            component.setText("["+ colorC+bar+half+barM+ChatColor.RESET+"]");
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);

            if(use >= time){


                use =0;

                if(player.getInventory().getItem(22) != null) {
                    Damageable damageable = (Damageable) player.getInventory().getItem(22).getItemMeta();
                    switch (type) {
                        case "shieldmini":
                            double d = damageable.getDamage() - 2 / shieldMax * item.getMaxDurability();
                            damageable.setDamage((int) d);
                            player.getInventory().getItem(22).setItemMeta(damageable);
                            break;

                        case "shieldmax":
                            damageable.setDamage(0);
                            player.getInventory().getItem(22).setItemMeta(damageable);
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
