package net.mochidsuki.corsica.battleroyalecore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

public class SomeExpansion extends PlaceholderExpansion {

    private final BattleRoyaleCore plugin;

    public SomeExpansion(BattleRoyaleCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "mochidsuki";
    }

    @Override
    public String getIdentifier() {
        return "BRC";
    }

    @Override
    public String getVersion() {return "1.0.0";    }

    @Override
    public boolean persist() {return true;}

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("stime")){
            return (v.stime-v.stime % 60)/60+":"+v.stime % 60;
        }

        if(params.equalsIgnoreCase("rtime")) {
            int r = v.rtime/20;
            return (r-r % 60)/60+":"+r % 60;
        }
        Team playerteam = player.getPlayer().getScoreboard().getPlayerTeam(player);
        String[] tp = new String[playerteam.getEntries().size()];
        playerteam.getEntries().toArray(tp);
        Player[] teamplayer = new Player[tp.length];
        ChatColor head =ChatColor.STRIKETHROUGH;
        ChatColor chest =ChatColor.STRIKETHROUGH;
        ChatColor legs =ChatColor.STRIKETHROUGH;
        ChatColor feet =ChatColor.STRIKETHROUGH;
        for (int i = 0;i < tp.length;i++) {
            teamplayer[i] = Bukkit.getPlayer(tp[i]);
            if(params.equalsIgnoreCase(player+"_teammate"+i+"_name")){
                if(!(teamplayer[i] == player.getPlayer())){
                    //頭
                    ItemStack headitem = teamplayer[i].getInventory().getItem(EquipmentSlot.HEAD);
                    if(headitem.getType() == Material.LEATHER_HELMET){
                        head = ChatColor.BLACK;
                    }
                    if(headitem.getType() == Material.CHAINMAIL_HELMET){
                        head = ChatColor.DARK_GRAY;
                    }
                    if(headitem.getType() == Material.IRON_HELMET){
                        head = ChatColor.GRAY;
                    }
                    if(headitem.getType() == Material.DIAMOND_HELMET){
                        head = ChatColor.AQUA;
                    }
                    if(headitem.getType() == Material.NETHERITE_HELMET){
                        head = ChatColor.DARK_RED;
                    }
                    //胸
                    ItemStack chestitem = teamplayer[i].getInventory().getItem(EquipmentSlot.CHEST);
                    if(chestitem.getType() == Material.LEATHER_CHESTPLATE){
                        chest = ChatColor.BLACK;
                    }
                    if(chestitem.getType() == Material.CHAINMAIL_CHESTPLATE){
                        chest = ChatColor.DARK_GRAY;
                    }
                    if(chestitem.getType() == Material.IRON_CHESTPLATE){
                        chest = ChatColor.GRAY;
                    }
                    if(chestitem.getType() == Material.DIAMOND_CHESTPLATE){
                        chest = ChatColor.AQUA;
                    }
                    if(chestitem.getType() == Material.NETHERITE_CHESTPLATE){
                        chest = ChatColor.DARK_RED;
                    }
                    //足
                    ItemStack legsitem = teamplayer[i].getInventory().getItem(EquipmentSlot.LEGS);
                    if(legsitem.getType() == Material.LEATHER_LEGGINGS){
                        legs = ChatColor.BLACK;
                    }
                    if(legsitem.getType() == Material.CHAINMAIL_LEGGINGS){
                        legs = ChatColor.DARK_GRAY;
                    }
                    if(legsitem.getType() == Material.IRON_LEGGINGS){
                        legs = ChatColor.GRAY;
                    }
                    if(legsitem.getType() == Material.DIAMOND_LEGGINGS){
                        legs = ChatColor.AQUA;
                    }
                    if(legsitem.getType() == Material.NETHERITE_LEGGINGS){
                        legs = ChatColor.DARK_RED;
                    }
                    //靴
                    ItemStack feetitem = teamplayer[i].getInventory().getItem(EquipmentSlot.FEET);
                    if(feetitem.getType() == Material.LEATHER_BOOTS){
                        feet = ChatColor.BLACK;
                    }
                    if(feetitem.getType() == Material.CHAINMAIL_BOOTS){
                        feet = ChatColor.DARK_GRAY;
                    }
                    if(feetitem.getType() == Material.IRON_BOOTS){
                        feet = ChatColor.GRAY;
                    }
                    if(feetitem.getType() == Material.DIAMOND_BOOTS){
                        feet = ChatColor.AQUA;
                    }
                    if(feetitem.getType() == Material.NETHERITE_BOOTS){
                        feet = ChatColor.DARK_RED;
                    }
                    return teamplayer[i]+" - "+head+"H"+ ChatColor.RESET +chest+"C"+ ChatColor.RESET+legs+"L"+ ChatColor.RESET+feet+"B";
                }
            }
        }


        return null; // Placeholder is unknown by the Expansion
    }
}
