package net.mochidsuki.corsica.battleroyalecore;


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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;


import java.util.Objects;
import java.util.Optional;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.SUFFOCATION;

public class Event implements Listener{
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
        try {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                switch (Objects.requireNonNull(event.getItem()).getType()) {
                    case FIRE_CHARGE:
                        Fireball fireball = event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation(), Fireball.class);
                        fireball.setShooter(event.getPlayer());
                        fireball.setVelocity(event.getPlayer().getLocation().getDirection().normalize().multiply(1.5));
                        event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                        break;
                    case FILLED_MAP:
                        v.pin.put(event.getPlayer(), Objects.requireNonNull(event.getPlayer().getTargetBlockExact(400)).getLocation());
                        event.setCancelled(true);
                        break;
                }
            }
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                switch (Objects.requireNonNull(event.getItem()).getType()) {
                    case LEATHER_HELMET:
                    case CHAINMAIL_HELMET:
                    case IRON_HELMET:
                    case GOLDEN_HELMET:
                    case DIAMOND_HELMET:
                    case NETHERITE_HELMET:
                        ItemStack originItemH = event.getPlayer().getInventory().getItem(21);
                        ItemStack newItemH = event.getItem();
                        event.getPlayer().getInventory().setItem(21, newItemH);
                        event.getPlayer().getInventory().setItemInMainHand(originItemH);
                        break;
                    case LEATHER_CHESTPLATE:
                    case CHAINMAIL_CHESTPLATE:
                    case IRON_CHESTPLATE:
                    case GOLDEN_CHESTPLATE:
                    case DIAMOND_CHESTPLATE:
                    case NETHERITE_CHESTPLATE:
                        ItemStack originItemC = event.getPlayer().getInventory().getItem(22);
                        ItemStack newItemC = event.getItem();
                        event.getPlayer().getInventory().setItem(22, newItemC);
                        event.getPlayer().getInventory().setItemInMainHand(originItemC);
                        break;
                    case LEATHER_BOOTS:
                    case CHAINMAIL_BOOTS:
                    case IRON_BOOTS:
                    case GOLDEN_BOOTS:
                    case DIAMOND_BOOTS:
                    case NETHERITE_BOOTS:
                        ItemStack originItemB = event.getPlayer().getInventory().getItem(23);
                        ItemStack newItemB = event.getItem();
                        event.getPlayer().getInventory().setItem(23, newItemB);
                        event.getPlayer().getInventory().setItemInMainHand(originItemB);
                        break;
                }

            }
        }catch (Exception ignored){}
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
            double shieldMax;
            int shieldNow;
            double damage = event.getDamage();
            ItemStack chestItem = player.getInventory().getItem(22);
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
            double maxDurability = chestItem.getType().getMaxDurability();
            double dDamage = Objects.requireNonNull(d).getDamage();
            double durableValue = maxDurability - dDamage;
            shieldNow = (int) (durableValue/maxDurability*shieldMax);
            if(shieldNow>0){
                damage = (int) (event.getDamage() - shieldNow);
                shieldNow = (int) (shieldNow - event.getDamage());
                if(shieldNow <= 0){
                    shieldNow = 0;
                    if(damagerType) {
                        damager.playSound(damager.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 0);
                    }
                }
            }
            if(damage <= 0){
                damage = 0;
            }
            event.setDamage(damage);
            double da = (shieldMax - shieldNow)/shieldMax*maxDurability;
            d.setDamage((int) da);
            chestItem.setItemMeta(d);
        }
    }
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        //Inventory Locker
        if (v.inv) {
            Optional<Material> cursor = Optional.of(Objects.requireNonNull(event.getCursor()).getType());
            int slot = event.getSlot();
            if (!(Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.PLAYER)) {
                slot = 0;
            }

            if (slot >= 24) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            if (slot >= 10 && slot <= 20) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            switch (slot) {
                case 21:
                    if (!(cursor.orElse(Material.LEATHER_HELMET) == Material.LEATHER_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.CHAINMAIL_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.DIAMOND_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_HELMET || event.getAction() == InventoryAction.DROP_ONE_SLOT || event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
                case 22:
                    if (!(cursor.orElse(Material.LEATHER_CHESTPLATE) == Material.LEATHER_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.CHAINMAIL_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.DIAMOND_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_CHESTPLATE || event.getAction() == InventoryAction.DROP_ONE_SLOT|| event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
                case 23:
                    if (!(cursor.orElse(Material.LEATHER_BOOTS) == Material.LEATHER_BOOTS || cursor.orElse(Material.LEATHER_BOOTS) == Material.CHAINMAIL_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_BOOTS || cursor.orElse(Material.LEATHER_BOOTS) == Material.DIAMOND_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_BOOTS || event.getAction() == InventoryAction.DROP_ONE_SLOT|| event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
            }
        }
    }


}