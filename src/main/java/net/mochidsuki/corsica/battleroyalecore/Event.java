package net.mochidsuki.corsica.battleroyalecore;


import net.mochidsuki.corsica.battleroyalecore.BukkitRunnableUtils.DistanceKiller;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;


import java.util.*;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public class Event implements Listener{
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event){
        Item item = event.getItemDrop();
    }
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        double damage = event.getFinalDamage();
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
            if(event.getEntityType() == EntityType.PLAYER) {
                if (((Player) event.getEntity()).hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                    int duration = ((Player) event.getEntity()).getPotionEffect(PotionEffectType.FIRE_RESISTANCE).getDuration();
                    ((Player) event.getEntity()).removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                    ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (int) (duration - damage), 0));
                    damage = 0;
                    event.setCancelled(true);
                    ((Player) event.getEntity()).sendTitle("", ChatColor.RED + "耐火の効果によりダメージ無効化中", 0, 2, 0);
                } else {
                    ((Player) event.getEntity()).sendTitle("", ChatColor.RED + "ボーダー外!!", 3, 4, 3);
                }
            }
        }




        if(event.getEntity().getType() == EntityType.PLAYER){
            Player player = (Player) event.getEntity();
            if(!(player.hasPotionEffect(PotionEffectType.UNLUCK))){
                if(!(cause.equals(ENTITY_ATTACK) || cause.equals(ENTITY_SWEEP_ATTACK) || cause.equals(SONIC_BOOM))){
                    if((player.getHealth() < damage)){
                        ItemStack[] itemStacks = new ItemStack[41];
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

                        //部隊全滅
                        Team playerTeam = player.getScoreboard().getEntryTeam(player.getName());
                        int livers = 0;
                        for (String entry : playerTeam.getEntries()){
                            if(player.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(entry))){
                                Player teammate = Bukkit.getPlayer(entry);
                                if(teammate.getGameMode().equals(GameMode.SURVIVAL)&& !teammate.hasPotionEffect(PotionEffectType.UNLUCK)){
                                    livers++;
                                }
                            }
                        }
                        if(livers == 0){
                            for(String entry : playerTeam.getEntries()){
                                if(player.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(entry)) && Bukkit.getPlayer(entry).getGameMode().equals(GameMode.SURVIVAL)) {
                                    Bukkit.getPlayer(entry).setHealth(0);
                                }
                                Bukkit.getPlayer(entry).sendTitle(ChatColor.RED + "部隊全滅","",20,40,10);
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
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    switch (Objects.requireNonNull(event.getMaterial())) {
                        case FIRE_CHARGE:
                            Fireball fireball = event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation(), Fireball.class);
                            //fireball.setShooter(event.getPlayer());
                            fireball.setVelocity(event.getPlayer().getLocation().getDirection().normalize().multiply(1.5));
                            event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);

                            break;
                        case FILLED_MAP:
                            if(event.getPlayer().getTargetBlockExact(400) != null) {
                                v.pin.put(event.getPlayer(),event.getPlayer().getTargetBlockExact(400).getLocation());

                                Team playerteam = event.getPlayer().getScoreboard().getPlayerTeam(event.getPlayer());
                                String[] tp = new String[Objects.requireNonNull(playerteam).getEntries().size()];
                                playerteam.getEntries().toArray(tp);
                                Player[] teamplayer = new Player[tp.length];
                                for (int i = 0; i < tp.length; i++) {
                                    teamplayer[i] = Bukkit.getPlayer(tp[i]);
                                }

                                for (Player player : teamplayer) {//teamplayer全員に実行
                                    if(player != null) {
                                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 100, 0);
                                    }
                                }

                            }else {
                                v.pin.remove(event.getPlayer());
                                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.BLOCK_FIRE_EXTINGUISH,0.5F,1);
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
                                if(event.getPlayer().getTargetBlockExact(400) != null) {
                                    v.pinRed.put(event.getPlayer(), event.getPlayer().getTargetBlockExact(400).getLocation());

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
                                }else {
                                    v.pinRed.remove(event.getPlayer());
                                    event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.BLOCK_FIRE_EXTINGUISH,0.5F,1);
                                }
                            }
                            break;

                        case SPYGLASS:
                            Bukkit.getScheduler().scheduleSyncDelayedTask(BattleRoyaleCore.getPlugin(), () -> v.useSniper.add(event.getPlayer()),5L);
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
        }else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerInteractEntityEvent(PlayerInteractEntityEvent event){
        if(event.getRightClicked().getType() == EntityType.PLAYER) {
            Team team = event.getPlayer().getScoreboard().getPlayerTeam(event.getPlayer());

            if (team.hasPlayer((OfflinePlayer) event.getRightClicked()) && event.getPlayer().getLocation().distance(event.getRightClicked().getLocation()) < 2 && ((Player)event.getRightClicked()).hasPotionEffect(PotionEffectType.UNLUCK) && !(event.getPlayer()).hasPotionEffect(PotionEffectType.UNLUCK) && !(event.getPlayer().hasPotionEffect(PotionEffectType.SLOW))) {
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
            if(event.getDamager().getType() == EntityType.PLAYER || event.getDamager().getType() == EntityType.ARROW) {
                if(event.getDamager().getType() == EntityType.PLAYER) {
                    damager = (Player) event.getDamager();
                }else {
                    damager = (Player) ((Arrow)event.getDamager()).getShooter();
                }
                if(v.gameround != 0) {
                    damager.setLevel((int) event.getFinalDamage() + damager.getLevel());//ダメージを経験値に変換
                }
                int i;
                if(ui.damage.get(damager) == null){
                    i = 0;
                }else {
                    i=ui.damage.get(damager);
                }

                try {
                ui.damage.put(damager,(int)(i + event.getFinalDamage()));
                if(!ui.assisted.isEmpty()){
                        ui.assisted.get(player).add(damager);
                }else {
                    HashSet<Player> players = new HashSet<>();
                    players.add(damager);
                    ui.assisted.put(player,players);
                }

                }catch (Exception e){
                }
            }


            //シールド
            double damage = event.getFinalDamage();
            if(player.getInventory().getItem(22) != null) {
                if (player.getInventory().getItem(22).getType() == Material.LEATHER_CHESTPLATE || player.getInventory().getItem(22).getType() == Material.CHAINMAIL_CHESTPLATE || player.getInventory().getItem(22).getType() == Material.IRON_CHESTPLATE || player.getInventory().getItem(22).getType() == Material.GOLDEN_CHESTPLATE || player.getInventory().getItem(22).getType() == Material.DIAMOND_CHESTPLATE || player.getInventory().getItem(22).getType() == Material.NETHERITE_CHESTPLATE) {
                    ShieldUtil shieldUtil = new ShieldUtil(player.getInventory().getItem(22));
                    if(shieldUtil.getShieldNow() > 0) {
                        int shieldNow;



                        shieldNow = shieldUtil.getShieldNow();
                        if (shieldNow > 0) {
                            damage = (int) (event.getFinalDamage() - shieldNow);
                            shieldNow = (int) (shieldNow - event.getFinalDamage());
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
                }
            }
            if (!(player.hasPotionEffect(PotionEffectType.UNLUCK))) {
                if ((player.getHealth() < damage)) {
                    ItemStack[] itemStacks = new ItemStack[41];
                    for (int i = 0; i < itemStacks.length; i++) {
                        itemStacks[i] = player.getInventory().getItem(i);
                    }
                    v.knockDownBU.put(player, itemStacks);
                    player.updateInventory();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 999999999, 0, true, true));
                    player.setFoodLevel(0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999999, 4, true, true));
                    player.setHealth(40);
                    event.setCancelled(true);
                    player.getInventory().clear();
                    damager.sendMessage(player.getName() + "をノックダウン!");
                    damager.playSound(event.getDamager(), Sound.BLOCK_ANVIL_PLACE, 100, 0);
                    if (ui.knockDown.containsKey(damager)) {
                        ui.knockDown.put(damager, ui.knockDown.get(damager) + 1);
                    } else {
                        ui.knockDown.put(damager, 1);
                    }
                    ui.killed.put(player, damager);

                    //部隊全滅
                    Team playerTeam = player.getScoreboard().getEntryTeam(player.getName());
                    int livers = 0;
                    for (String entry : playerTeam.getEntries()){
                        if(player.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(entry))){
                            Player teammate = Bukkit.getPlayer(entry);
                            if(teammate.getGameMode().equals(GameMode.SURVIVAL)&& !teammate.hasPotionEffect(PotionEffectType.UNLUCK)){
                                livers++;
                            }
                        }
                    }
                    if(livers == 0){
                        for(String entry : playerTeam.getEntries()){
                            if(player.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(entry)) && Bukkit.getPlayer(entry).getGameMode().equals(GameMode.SURVIVAL)) {
                                Bukkit.getPlayer(entry).setHealth(0);
                            }
                            Bukkit.getPlayer(entry).sendTitle(ChatColor.RED + "部隊全滅","",20,40,10);
                            ui.ranking.put(playerTeam, player.getScoreboard().getObjective("teams").getScore("system").getScore());
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
            if(event.getClickedInventory() != null) {
                if (!(Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.PLAYER)) {
                    slot = 0;
                }
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
            if (slot >= 29 && slot <= 39) {
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
        deathCart.getInventory().setItem(21,v.knockDownBU.get(event.getEntity())[40]);
        event.getEntity().getInventory().clear();




        event.getEntity().sendMessage("死んでしまった!!");
        event.getEntity().sendMessage("数字ボタンを押すとほかの人のところにTPできるぞ!!");

        //kill counter
        if(ui.killed.containsKey(event.getEntity())) {
            if (ui.kill.containsKey(ui.killed.get(event.getEntity()))) {
                ui.kill.put(ui.killed.get(event.getEntity()), ui.kill.get(ui.killed.get(event.getEntity())) + 1);
            } else {
                ui.kill.put(ui.killed.get(event.getEntity()), 1);
            }
        }

        for (Player player : ui.assisted.get(event.getEntity())) {
            if(player.getScoreboard().getPlayerTeam(player) == event.getEntity().getScoreboard().getPlayerTeam(event.getEntity()) && player != ui.killed.get(event.getEntity())){
                if(ui.assist.containsKey(player)){
                    ui.assist.put(player,ui.assist.get(player) + 1);
                }else {
                    ui.assist.put(player,1);
                }
            }
        }


    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent event){
        Block block = event.getBlock();
        if (!(block.getType() == Material.FIRE || block.getType() == Material.OAK_DOOR || block.getType() == Material.SPRUCE_DOOR || block.getType() == Material.BIRCH_DOOR || block.getType() == Material.JUNGLE_DOOR || block.getType() == Material.ACACIA_DOOR || block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.MANGROVE_DOOR || block.getType() == Material.CRIMSON_DOOR || block.getType() == Material.WARPED_DOOR)) {
            if(!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent event){
        if(event.getEntity().getType() == EntityType.FIREBALL){
            event.setCancelled(true);
            event.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE,event.getLocation(),1,0,0,0,0);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(),Sound.ENTITY_GENERIC_EXPLODE,1,1);


        }
    }
    @EventHandler
    public void EntityPickupItemEvent(EntityPickupItemEvent event){
        if(event.getEntity().hasPotionEffect(PotionEffectType.UNLUCK)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityPotionEffectEvent(EntityPotionEffectEvent event){
        ((Player)event.getEntity()).updateInventory();
    }

    @EventHandler
    public void PlayerToggleSneakEvent(PlayerToggleSneakEvent event){

        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SPYGLASS) && event.isSneaking() && ((Entity)event.getPlayer()).isOnGround()){
            if(v.useSniper.contains(event.getPlayer())){
                if(event.getPlayer().getCooldown(Material.SPYGLASS) <= 0) {
                    if(event.getPlayer().getInventory().contains(Material.ARROW)) {
                        event.getPlayer().getInventory().removeItem(new ItemStack(Material.ARROW, 1));


                        Arrow ammo = event.getPlayer().getWorld().spawnArrow(event.getPlayer().getLocation().add(0, 1.65, 0), event.getPlayer().getLocation().getDirection(), 50, 1);
                        ammo.setShooter(event.getPlayer());
                        ammo.setCritical(true);
                        ammo.setColor(Color.GRAY);
                        ammo.setPierceLevel(3);
                        ammo.setDamage(0.15);
                        ammo.setShooter(event.getPlayer());
                        new DistanceKiller(ammo, event.getPlayer().getLocation(), 40).runTaskTimer(BattleRoyaleCore.getPlugin(), 0L, 1L);
                        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 0);
                        event.getPlayer().setCooldown(Material.SPYGLASS, 50);
                    }else {
                        event.getPlayer().sendTitle("" ,"弾切れ!",10,10,20);
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent event){
        v.useSniper.remove(event.getPlayer());
    }
    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent event){
        event.setResult(PlayerLoginEvent.Result.ALLOWED);
        if(v.gameround != 0 && !event.getPlayer().isOp()) {
            event.setKickMessage("試合中であるため入室できません");
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

}