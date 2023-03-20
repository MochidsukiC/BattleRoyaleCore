package net.mochidsuki.corsica.battleroyalecore;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
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
    Player fenixPlayer;

    public LongPress(Player p, String t, Material i,double ti,Player player1){
        player = p;
        type = t;
        item = i;
        time = ti;
        fenixPlayer = player1;
    }

    @Override
    public void run() {
        switch (type) {
            case "shieldmini":
            case "shieldmax":
            if (player.getInventory().getItemInMainHand().getType() == item) {
                use = use + 1;
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, 4, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2, 200, true, false));

                ShieldUtil shieldUtil = new ShieldUtil(player.getInventory().getItem(22));


                String bar = String.join("", Collections.nCopies((int) (use / time * 10), "■"));
                String barM = String.join("", Collections.nCopies((int) ((time - use) / time * 10), "-"));
                String half;

                if (use % (time / 10) != 0) {
                    half = "□";
                } else {
                    half = "";
                }
                player.sendTitle("", "[" + shieldUtil.getShieldColor() + bar + half + barM + ChatColor.RESET + "]", 0, 2, 20);

                if (use >= time) {


                    use = 0;

                    if (player.getInventory().getItem(22) != null) {
                        Damageable damageable = (Damageable) player.getInventory().getItem(22).getItemMeta();
                        switch (type) {
                            case "shieldmini":
                                double d = damageable.getDamage() - (2 / shieldUtil.getShieldMax() * shieldUtil.getShieldMaxDurability());
                                damageable.setDamage((int) d);
                                player.getInventory().getItem(22).setItemMeta(damageable);
                                use = 0;
                                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                                cancel();
                                break;

                            case "shieldmax":
                                damageable.setDamage(0);
                                player.getInventory().getItem(22).setItemMeta(damageable);
                                use = 0;
                                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                                cancel();
                                break;
                        }
                    }
                }
            } else {
                use = 0;
                cancel();
            }
            case "fenix":
                if (player.isSneaking()) {
                    use = use + 1;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, 10, true, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2, 200, true, false));


                    String bar = String.join("", Collections.nCopies((int) (use / time * 10), "■"));
                    String barM = String.join("", Collections.nCopies((int) ((time - use) / time * 10), "-"));
                    String half;

                    if (use % (time / 10) != 0) {
                        half = "□";
                    } else {
                        half = "";
                    }
                    player.sendTitle("", "["+ ChatColor.LIGHT_PURPLE + bar + half + barM + ChatColor.RESET + "]", 0, 2, 20);

                    fenixPlayer.getWorld().spawnParticle(Particle.HEART,fenixPlayer.getLocation(),100,1,1,1);

                    if (use >= time) {
                        use = 0;
                        fenixPlayer.removePotionEffect(PotionEffectType.UNLUCK);
                        fenixPlayer.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                        fenixPlayer.setHealth(2);
                        fenixPlayer.setFoodLevel(10);
                    }
                } else {
                    use = 0;
                    cancel();
                }
        }
    }
}
