package net.mochidsuki.corsica.battleroyalecore;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;


import java.util.Objects;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.SUFFOCATION;

public class event implements Listener{
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event){
        Item item = event.getItemDrop();
        if(item.getItemStack().getType() == Material.FIREWORK_ROCKET){//ドロップシップからの降下
            Team playerteam = event.getPlayer().getScoreboard().getPlayerTeam(event.getPlayer());
            String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
            playerteam.getEntries().toArray(tp);
            Player[] teamplayer = new Player[tp.length];
            for (int i = 0;i < tp.length;i++) {
                teamplayer[i] = Bukkit.getPlayer(tp[i]);
            }

            for (Player player : teamplayer) {//teamplayer全員に実行
                GameStart g = new GameStart();
                g.player(player);
            }



        }
    }
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        if(cause.equals(SUFFOCATION)){//ボーダー外ダメージ
            switch (v.gameround) {
                case 1:
                case 2:
                    event.setDamage(0.4);
                    break;
                case 3:
                    event.setDamage(1.2);
                    break;
                case 4:
                    event.setDamage(2.4);
                    break;
                case 5:
                case 6:
                    event.setDamage(2.9);
                    break;

            }
        }
    }

    @EventHandler
    public void EntityToggleGlideEvent(EntityToggleGlideEvent event){
        if(event.isGliding()){
            Player player = (Player) event.getEntity();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,1000000,0,true,true));
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(Objects.requireNonNull(event.getItem()).getType() == Material.FIRE_CHARGE){
                Fireball fireball = event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation(), Fireball.class);
                fireball.setShooter(event.getPlayer());
                fireball.setVelocity(event.getPlayer().getLocation().getDirection().normalize().multiply(1.5));
                event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() -1 );
            }
        }
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        Player damager = null;
        Player player = (Player) event.getEntity();
        boolean damagerType = true;
        try {
            damager = (Player) event.getDamager();
        }catch (Exception e){
            damagerType = false;
        }

        if(event.getEntity().getType().equals(EntityType.PLAYER)) {
            if(damagerType) {
                damager.setLevel((int) event.getDamage());//ダメージを経験値に変換
            }


            //シールド
            int shieldMax;
            int shieldNow;
            int damage = 0;
            ItemStack chestItem = player.getInventory().getItem(EquipmentSlot.CHEST);
            switch (Objects.requireNonNull(chestItem).getType()){
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
            Damageable d = (Damageable)chestItem.getItemMeta();
            shieldNow = (chestItem.getType().getMaxDurability()-Objects.requireNonNull(d).getDamage())/chestItem.getType().getMaxDurability()*shieldMax;
            if(shieldNow>0){
                damage = (int) (event.getDamage() - shieldNow);
                shieldNow = (int) (shieldNow - event.getDamage());
                if(shieldNow <= 0){
                    shieldNow = 0;
                    if(damagerType) {
                        damager.playSound(damager.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 0);
                    }
                }
            d.setDamage(shieldNow/shieldMax*chestItem.getType().getMaxDurability());
            chestItem.setItemMeta(d);
            }
            if(damage <= 0){
                event.setCancelled(true);
            }else {
                event.setDamage(damage);
            }
        }
    }
}