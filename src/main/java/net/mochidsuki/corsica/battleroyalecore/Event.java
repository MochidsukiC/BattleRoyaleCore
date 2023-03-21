package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.inventivetalent.glow.GlowAPI;


import java.util.Objects;
import java.util.Optional;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

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
        /*
        if(item.getItemStack().getType() == Material.FILLED_MAP){
            v.pin.put(event.getPlayer(),null);
            event.setCancelled(true);
        }

         */
    }
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        double damage = event.getDamage();
        if(cause.equals(SUFFOCATION)){//ボーダー外ダメージ
            switch (v.gameround) {
                case 1:
                case 2:
                    damage = 0.1;

                    break;
                case 3:
                    damage = 0.8;
                    break;
                case 4:
                    damage = 1.8;
                    break;
                case 5:
                case 6:
                    damage = 2.9;
                    break;

            }
        }
        if(event.getEntity().getType() == EntityType.PLAYER){
            Player player = (Player) event.getEntity();
            if(!(player.hasPotionEffect(PotionEffectType.UNLUCK))){
                if(!(cause.equals(ENTITY_ATTACK) || cause.equals(ENTITY_SWEEP_ATTACK) || cause.equals(SONIC_BOOM))){
                    if((player.getHealth() < damage)){
                        ItemStack[] itemStacks = new ItemStack[36];
                        for(int i = 0;i < itemStacks.length;i++){
                            itemStacks[i] = player.getInventory().getItem(i);
                        }
                        v.knockDownBU.put(player,itemStacks);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK,999999999,0,true,true));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST,999999999,4,true,true));
                        player.setHealth(40);
                        player.setFoodLevel(0);
                        event.setCancelled(true);
                        player.getInventory().clear();
                        player.updateInventory();

                        Team playerteam = player.getScoreboard().getPlayerTeam(player);
                        String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
                        playerteam.getEntries().toArray(tp);
                        Player[] teamplayer = new Player[tp.length];
                        int allDeath = 0;
                        for (int i = 0; i < tp.length; i++) {
                            teamplayer[i] = Bukkit.getPlayer(tp[i]);
                            if (teamplayer[i].hasPotionEffect(PotionEffectType.UNLUCK) || teamplayer[i].getGameMode() == GameMode.SPECTATOR){
                                allDeath++;
                            }
                        }
                        if(allDeath == tp.length){
                            for (int i = 0; i < tp.length; i++) {
                                teamplayer[i].setHealth(0);
                            }
                        }
                    }
                }
            }
        }
        event.setDamage(damage);
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
        if(!(event.getPlayer().hasPotionEffect(PotionEffectType.UNLUCK))) {
            try {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    switch (Objects.requireNonNull(event.getMaterial())) {
                        case FIRE_CHARGE:
                            Fireball fireball = event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation(), Fireball.class);
                            fireball.setShooter(event.getPlayer());
                            fireball.setVelocity(event.getPlayer().getLocation().getDirection().normalize().multiply(1.5));

                            event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                            break;
                        case FILLED_MAP:
                            v.pin.put(event.getPlayer(), Objects.requireNonNull(event.getPlayer().getTargetBlockExact(400)).getLocation());

                            Team playerteam = event.getPlayer().getScoreboard().getPlayerTeam(event.getPlayer());
                            String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
                            playerteam.getEntries().toArray(tp);
                            Player[] teamplayer = new Player[tp.length];
                            for (int i = 0; i < tp.length; i++) {
                                teamplayer[i] = Bukkit.getPlayer(tp[i]);
                            }

                            for (Player player : teamplayer) {//teamplayer全員に実行
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 100, 0);
                            }


                            event.setCancelled(true);
                            break;
                    }
                }
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    switch (Objects.requireNonNull(event.getMaterial())) {
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

                        case ENDER_PEARL:
                            if (!(event.getPlayer().hasPotionEffect(PotionEffectType.SLOW))) {
                                if (!(event.getPlayer().getInventory().getItem(22) == null || Objects.equals(event.getPlayer().getInventory().getItem(22), new ItemStack(Material.LEATHER_CHESTPLATE)))) {
                                    Damageable d = (Damageable) event.getPlayer().getInventory().getItem(22).getItemMeta();
                                    if (d.getDamage() != 0) {
                                        new LongPress(event.getPlayer(), "shieldmini", event.getMaterial(), 40, null).runTaskTimer(BattleRoyaleCore.getPlugin(), 0L, 1L);
                                    } else {
                                        event.getPlayer().sendTitle("", "シールドは新品同様です", 10, 10, 20);
                                    }
                                } else {
                                    event.getPlayer().sendTitle("", ChatColor.RED + "シールドがありません", 10, 10, 20);
                                }
                            }
                            event.setCancelled(true);
                            break;
                        case MUSIC_DISC_5:
                            if (!(event.getPlayer().hasPotionEffect(PotionEffectType.SLOW))) {
                                if (!(event.getPlayer().getInventory().getItem(22) == null || Objects.equals(event.getPlayer().getInventory().getItem(22), new ItemStack(Material.LEATHER_CHESTPLATE)))) {
                                    Damageable d = (Damageable) event.getPlayer().getInventory().getItem(22).getItemMeta();
                                    if (d.getDamage() != 0) {
                                        new LongPress(event.getPlayer(), "shieldmax", event.getMaterial(), 100, null).runTaskTimer(BattleRoyaleCore.getPlugin(), 0L, 1L);
                                    } else {
                                        event.getPlayer().sendTitle("", "シールドは新品同様です", 10, 10, 20);
                                    }
                                } else {
                                    event.getPlayer().sendTitle("", ChatColor.RED + "シールドがありません", 10, 10, 20);
                                }
                            }
                            event.setCancelled(true);
                            break;
                        case FILLED_MAP:
                            if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.FILLED_MAP) {
                                v.pinRed.put(event.getPlayer(), Objects.requireNonNull(event.getPlayer().getTargetBlockExact(400)).getLocation());

                                Team playerteam = event.getPlayer().getScoreboard().getPlayerTeam(event.getPlayer());
                                String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
                                playerteam.getEntries().toArray(tp);
                                Player[] teamplayer = new Player[tp.length];
                                for (int i = 0; i < tp.length; i++) {
                                    teamplayer[i] = Bukkit.getPlayer(tp[i]);
                                }

                                for (Player player : teamplayer) {//teamplayer全員に実行
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 50, 0);
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 50, 0.3F);
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 50, 0.6F);
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 50, 1);
                                }
                            }
                            break;

                    }

                }
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = event.getClickedBlock();
                    if (block.getType() == Material.OAK_DOOR || block.getType() == Material.SPRUCE_DOOR || block.getType() == Material.BIRCH_DOOR || block.getType() == Material.JUNGLE_DOOR || block.getType() == Material.ACACIA_DOOR || block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.MANGROVE_DOOR || block.getType() == Material.CRIMSON_DOOR || block.getType() == Material.WARPED_DOOR) {

                        Player[] players = event.getPlayer().getServer().getOnlinePlayers().toArray(new Player[0]);

                        if ((block.getBlockData().isFaceSturdy(BlockFace.NORTH, BlockSupport.FULL))) {
                            for (Player player : players) {
                                Location location = player.getLocation();
                                if (Math.abs(block.getX() + 0.5 - location.getX()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getZ() - location.getZ() < 0.8 && block.getZ() - location.getZ() >= -0.6) {
                                    if (player.isSneaking()) {
                                        event.setCancelled(true);
                                    }
                                }

                            }
                        }
                        if ((block.getBlockData().isFaceSturdy(BlockFace.EAST, BlockSupport.FULL))) {
                            for (Player player : players) {
                                Location location = player.getLocation();
                                if (Math.abs(block.getZ() + 0.5 - location.getZ()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getX() - location.getX() < -0.4 && block.getX() - location.getX() >= -1.6) {
                                    if (player.isSneaking()) {
                                        event.setCancelled(true);
                                    }
                                }

                            }
                        }
                        if ((block.getBlockData().isFaceSturdy(BlockFace.WEST, BlockSupport.FULL))) {
                            for (Player player : players) {
                                Location location = player.getLocation();
                                if (Math.abs(block.getZ() + 0.5 - location.getZ()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getX() - location.getX() < 0.8 && block.getX() - location.getX() >= -0.6) {
                                    if (player.isSneaking()) {
                                        event.setCancelled(true);
                                    }
                                }

                            }
                        }
                        if ((block.getBlockData().isFaceSturdy(BlockFace.SOUTH, BlockSupport.FULL))) {
                            for (Player player : players) {
                                Location location = player.getLocation();
                                if (Math.abs(block.getX() + 0.5 - location.getX()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getZ() - location.getZ() < -0.4 && block.getZ() - location.getZ() >= -1.6) {
                                    if (player.isSneaking()) {
                                        event.setCancelled(true);
                                    }
                                }

                            }
                        }

                    }
                }
            } catch (Exception ignored) {
            }
        }else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerInteractEntityEvent(PlayerInteractEntityEvent event){
        if(event.getRightClicked().getType() == EntityType.PLAYER) {
            Team team = event.getPlayer().getScoreboard().getPlayerTeam(event.getPlayer());

            if (team.hasPlayer((OfflinePlayer) event.getRightClicked()) && event.getPlayer().getLocation().distance(event.getRightClicked().getLocation()) < 2 && ((Player)event.getRightClicked()).hasPotionEffect(PotionEffectType.UNLUCK) && !(event.getPlayer().hasPotionEffect(PotionEffectType.SLOW))) {
                    new LongPress(event.getPlayer(), "fenix", null, 100, (Player) event.getRightClicked()).runTaskTimer(BattleRoyaleCore.getPlugin(), 0L, 1L);
            }
        }
    }


    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        Player damager = null;

        if(event.getDamager().getType().equals(EntityType.PLAYER)){
            if(((Player)event.getDamager()).hasPotionEffect(PotionEffectType.UNLUCK)){
                event.setCancelled(true);
                return;
            }
        }



        if(event.getEntity().getType().equals(EntityType.PLAYER)) {


            Player player = (Player) event.getEntity();
            if(event.getDamager().getType() == EntityType.PLAYER) {
                damager = (Player) event.getDamager();
                damager.setLevel((int) event.getDamage()+damager.getLevel());//ダメージを経験値に変換
                int i;
                if(ui.damage.get(damager) == null){
                    i = 0;
                }else {
                    i=ui.damage.get(damager);
                }
                ui.damage.put(damager,(int)(i + event.getDamage()));
            }


            //シールド
            double damage = event.getDamage();
            if(player.getInventory().getItem(22) == new ItemStack(Material.LEATHER_CHESTPLATE ) || player.getInventory().getItem(22) == new ItemStack(Material.CHAINMAIL_CHESTPLATE ) || player.getInventory().getItem(22) == new ItemStack(Material.IRON_CHESTPLATE ) || player.getInventory().getItem(22) == new ItemStack(Material.GOLDEN_CHESTPLATE ) || player.getInventory().getItem(22) == new ItemStack(Material.DIAMOND_CHESTPLATE ) || player.getInventory().getItem(22) == new ItemStack(Material.NETHERITE_CHESTPLATE )) {
                int shieldNow;


                ShieldUtil shieldUtil = new ShieldUtil(player.getInventory().getItem(22));

                shieldNow = shieldUtil.getShieldNow();
                if (shieldNow > 0) {
                    damage = (int) (event.getDamage() - shieldNow);
                    shieldNow = (int) (shieldNow - event.getDamage());
                    if (shieldNow <= 0) {
                        shieldNow = 0;
                        if (event.getDamager().getType() == EntityType.PLAYER) {
                            damager.playSound(damager.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 0);
                        }
                    }
                }
                if (damage <= 0) {
                    damage = 0;
                }
                event.setDamage(damage);
                double da = (shieldUtil.getShieldMax() - shieldNow) / shieldUtil.getShieldMax() * shieldUtil.getShieldMaxDurability();
                Damageable damageable = (Damageable) player.getInventory().getItem(22).getItemMeta();
                damageable.setDamage((int) da);
                player.getInventory().getItem(22).setItemMeta(damageable);


            }
            if (!(player.hasPotionEffect(PotionEffectType.UNLUCK))) {
                if ((player.getHealth() < damage)) {
                    ItemStack[] itemStacks = new ItemStack[36];
                    for (int i = 0; i < itemStacks.length; i++) {
                        itemStacks[i] = player.getInventory().getItem(i);
                    }
                    v.knockDownBU.put(player, itemStacks);
                    player.updateInventory();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 999999999, 0, true, true));
                    player.setSaturation(-20);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999999, 4, true, true));
                    player.setHealth(40);
                    event.setCancelled(true);
                    player.getInventory().clear();

                    Team playerteam = player.getScoreboard().getPlayerTeam(player);
                    String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
                    playerteam.getEntries().toArray(tp);
                    Player[] teamplayer = new Player[tp.length];
                    int allDeath = 0;
                    for (int i = 0; i < tp.length; i++) {
                        teamplayer[i] = Bukkit.getPlayer(tp[i]);
                        if (teamplayer[i].hasPotionEffect(PotionEffectType.UNLUCK) || teamplayer[i].getGameMode() == GameMode.SPECTATOR) {
                            allDeath++;
                        }
                    }
                    if (allDeath == tp.length) {
                        for (int i = 0; i < tp.length; i++) {
                            teamplayer[i].setHealth(0);
                        }
                    }


                }
            }

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

            if (slot >= 11 && slot <= 17) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            if (slot == 20) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            if (slot >= 24 && slot <= 26) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            if (slot >= 29 && slot <= 34) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            switch (slot) {
                case 21:
                    if (!(cursor.orElse(Material.LEATHER_HELMET) == Material.LEATHER_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.CHAINMAIL_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.GOLDEN_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.DIAMOND_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_HELMET || event.getAction() == InventoryAction.DROP_ONE_SLOT || event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
                case 22:
                    if (!(cursor.orElse(Material.LEATHER_CHESTPLATE) == Material.LEATHER_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.CHAINMAIL_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.GOLDEN_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.DIAMOND_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_CHESTPLATE || event.getAction() == InventoryAction.DROP_ONE_SLOT|| event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
                case 23:
                    if (!(cursor.orElse(Material.LEATHER_BOOTS) == Material.LEATHER_BOOTS || cursor.orElse(Material.LEATHER_BOOTS) == Material.CHAINMAIL_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.GOLDEN_BOOTS || cursor.orElse(Material.LEATHER_BOOTS) == Material.DIAMOND_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_BOOTS || event.getAction() == InventoryAction.DROP_ONE_SLOT|| event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event){
        Entity entity = event.getEntity().getWorld().spawn(event.getEntity().getLocation(),EntityType.MINECART_CHEST.getEntityClass());
        entity.setGlowing(true);
        entity.setInvulnerable(true);
        StorageMinecart deathCart = (StorageMinecart)entity;
        for (int i = 0; i <= 10;i++){
            if(v.knockDownBU.get(event.getEntity())[i] != null) {
                if (v.knockDownBU.get(event.getEntity())[i].getType() != Material.FILLED_MAP) {
                    deathCart.getInventory().setItem(i, v.knockDownBU.get(event.getEntity())[i]);
                }
            }
        }
        deathCart.getInventory().setItem(11, v.knockDownBU.get(event.getEntity())[18]);
        deathCart.getInventory().setItem(18, v.knockDownBU.get(event.getEntity())[19]);
        deathCart.getInventory().setItem(19, v.knockDownBU.get(event.getEntity())[27]);
        deathCart.getInventory().setItem(20, v.knockDownBU.get(event.getEntity())[28]);

        deathCart.getInventory().setItem(24, v.knockDownBU.get(event.getEntity())[21]);
        deathCart.setCustomName(event.getEntity().getName());
        try {
            ItemStack chest = v.knockDownBU.get(event.getEntity())[22];
            Damageable chestD = (Damageable) chest.getItemMeta();
            chestD.setDamage(0);
            chest.setItemMeta(chestD);
            deathCart.getInventory().setItem(25, chest);
        }catch (Exception e){}
        deathCart.getInventory().setItem(26, v.knockDownBU.get(event.getEntity())[23]);
        event.getEntity().getInventory().clear();

        //kill counter



    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        //Delay the update by a few ticks until the player is actually on the server
        Bukkit.getScheduler().runTaskLater(BattleRoyaleCore.getPlugin(), new Runnable() {
            @Override
            public void run() {
                //Set the event's player glowing in DARK_AQUA for all online players
                GlowAPI.setGlowing(event.getPlayer(), GlowAPI.Color.DARK_AQUA, Bukkit.getOnlinePlayers());
            }
        }, 10);
    }


}