package net.mochidsuki.corsica.battleroyalecore;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;


import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.SUFFOCATION;

public class event implements Listener{
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event){
        Item item = event.getItemDrop();
        if(item.getItemStack().getType() == Material.FIREWORK_ROCKET){//ドロップシップからの降下
            Team playerteam = event.getPlayer().getScoreboard().getPlayerTeam(event.getPlayer());
            String[] tp = new String[playerteam.getEntries().size()];
            playerteam.getEntries().toArray(tp);
            Player[] teamplayer = new Player[tp.length];
            for (int i = 0;i < tp.length;i++) {
                teamplayer[i] = Bukkit.getPlayer(tp[i]);
            }

            for (int i = 0; i < teamplayer.length; i++) {//teamplayer全員に実行
                GameStart g = new GameStart();
                g.player(teamplayer[i]);
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
            if(event.getItem().getType() == Material.FIRE_CHARGE){
                Fireball fireball = event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation(), Fireball.class);
                fireball.setShooter(event.getPlayer());
                fireball.setVelocity(event.getPlayer().getLocation().getDirection().normalize().multiply(1.5));
                event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() -1 );
            }
        }
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            player.setLevel((int) event.getDamage());
        }
    }
}
