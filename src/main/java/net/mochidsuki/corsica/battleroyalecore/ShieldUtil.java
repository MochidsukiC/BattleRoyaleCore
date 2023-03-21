package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Objects;

public class ShieldUtil {

    ItemStack chest;
    public ShieldUtil(ItemStack itemStack){
        chest = itemStack;
    }

    public double getShieldMax(){
        if(chest != null) {
            switch (Objects.requireNonNull(chest).getType()) {
                case CHAINMAIL_CHESTPLATE:
                    return 5;
                case IRON_CHESTPLATE:
                case GOLDEN_CHESTPLATE:
                    return 10;
                case DIAMOND_CHESTPLATE:
                    return 15;
                case NETHERITE_CHESTPLATE:
                    return 20;
                default:
                    return 0;
            }
        }return 0;
    }

    public int getShieldNow(){
        if(chest != null) {
            Damageable d = (Damageable) chest.getItemMeta();

            double dDamage = Objects.requireNonNull(d).getDamage();
            double durableValue = getShieldMaxDurability() - dDamage;
            return (int) (durableValue / getShieldMaxDurability() * getShieldMax());
        }else {
            return 0;
        }
    }

    public ChatColor getShieldColor(){
        if(chest != null) {
            switch (Objects.requireNonNull(chest).getType()) {
                case CHAINMAIL_CHESTPLATE:
                    return ChatColor.GRAY;
                case IRON_CHESTPLATE:
                    return ChatColor.DARK_GRAY;
                case GOLDEN_CHESTPLATE:
                    return ChatColor.YELLOW;
                case DIAMOND_CHESTPLATE:
                    return ChatColor.AQUA;
                case NETHERITE_CHESTPLATE:
                    return ChatColor.DARK_RED;
                default:
                    return ChatColor.RESET;
            }
        }else {
            return ChatColor.RESET;
        }
    }
    public double getShieldMaxDurability(){
        if(chest != null) {
            return chest.getType().getMaxDurability();
        }else {
            return 0;
        }
    }

}
