package net.mochidsuki.corsica.battleroyalecore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

import static org.bukkit.Bukkit.getMap;

public final class BattleRoyaleCore extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        getLogger().info("Battle Royale 2 Pluginが目を覚ました！");
        getServer().getPluginManager().registerEvents(new event(), this);
        getCommand("gameround").setExecutor(new CommandClass()); //gameround
        getCommand("debugerb").setExecutor(new CommandClass()); //debugerb
        getCommand("brc").setExecutor(new CommandClass()); //brgame
        getCommand("mapgenerator").setExecutor(new CommandClass()); //mapgenerator
        v.rtime = 0;
        v.stime =0;
        plugin = this;

        //Config
        Config config = new Config(plugin);
        config.load();

        //PlaceholderAPI
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SomeExpansion(this).register();
        }
        //every ticks
        new everyticks().runTaskTimer(this,0L,1);
        //Big Map Data

        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();
        getMap(v.bigmapdata).addRenderer(new OriginMapRender());
        mapMetaB.setMapId(v.bigmapdata);
        mapItemB.setItemMeta(mapMetaB);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getLogger().info("Battle Royale 2 Pluginがおねんねした");
        // Plugin shutdown logic
    }
    public static Plugin getPlugin() {
        return plugin;
    }

}

class v{
    static double[] now = new double[4];//ボーダーの現在位置0=x,1=y,2=-x,3=-y
    static int gameround;//現在のゲームラウンド
    static int mr;//マップ半径
    static int mcx;//マップの中心座標x
    static int mcz;//マップの中心座標y
    static long[] roundstime = new long[7];//各ラウンドの収縮待機時間
    static int[] roundrtime = new int[7];//各ラウンドの収縮時間
    static long stime;//現ラウンドの収縮待機残り時間
    static int rtime;//現ラウンドの収縮残り時間
    static int bigmapdata;//Bigmapの大元mapID
    static Color[] colors = new Color[16384];//マップデータをコピーするための一時保管場所
    static double exVector;//エリトラの推進力
    static double eyVector;//エリトラにかかる重力
    static double esLimit;//エリトラのスピード制限
}
class b{
    static int nx;
    static int nmx;
    static int nz;
    static int nmz;
    static int[] target = new int[4];//ボーダーの収縮目標座標0=x,1=y,2=-x,3=-y

    static double[] center = new double[3];//ボーダーの中心座標0=忘れた,1=x,2=y
}

class m{
    static int[] distance = new int[4];//ボーダーの残り収縮距離0=x,1=y,2=-x,3=-y
}

/*
_________.__-__-__~__~~_____--(((-++J+&u&&&+a&++J(__~____~~_____~__~_~__~_~
~_____._~.__.____~~__(JgXHWWHHXWWXWHWpHHHHWWkbWkHWqHmma,__~_:~~:___~__~__~_
____~_._---_-____(+dHHH@HHmWH@HHkHMHHqHHWHHqkqWHmqHgH@@HHm-:~<~<<~:~~~~_~_~
_<__~-.____~___(uKWWqHHWmHHmM@HHHHHHMMHgHmHHHggHHkHHHHHHYT>:~:_-_:::__<~~~~
_~____.__.-__(dQkHHHHMHHHHMMMMMMH@gHHHggHgHMHHH@gHY71<(_<______((________~_
_~--_.-~-___jdHHNMHHMNHMMMMMM@HHMMMH@MHgHggHHgWY<_~((<__<(_jggH@Ne-~~~~__~_
___-_.-__(<(<THHMMMMMMM@HHMHMMH@HHMM@HH@HH@#YC<<_<((<<((+gWmHHgHMMHm-______
-_(_((:((;<(<<<7WMMM@MMHMMHHM@MM@HHH@HM@HYC(___(_<((jQWMHHgHHHHHHMHkHx___-_
<<(<<(<<><<;<<(<<<?TWHHMMM@MMHMMHMMHMgHY<_((:(<+JgHHHHHM@HHHHNMMMMMHHHe-___
_<(<<(;<(<(~~<(<<(<_<?YUHHMMM@HgHMHHHB<_<_-<((d@MHMMMMHHHMNMHMMMMMMMMMH2<(_
<;;<<<<<<<(<<;<<;jWMN+/~<<?THHHMHMHKC<_<~_+&MMMHMMHMMMgHMMMMMMMMMMHMMMHk<_(
<<<<<<<<<<<~~(<<dHMHMMm+<_~<~?7UWHY(<<_-<jdMHMMMMMH@M@HM@MMMMMMHMMMMHMHHR:(
<<<<<:<~_~<<_<~:<TH@MMMNa+________(__~_(XgHHMMMHHM@HMM@MM@HMHM@HMMMMHMHHH<(
<(<<~<~<<~_<__<___~dHHHHHNJ-~____~__(<(THHHM@HMMHMMMMMMMMHHMHMMMHMMMMMMHH2_
<(<~~~:~~_~~_~~__~__?WHM@MMb__-___<(-___<?TMHHHHMMMMMMMMM@HM@MMHMMMHHMMMMb~
<<<__(_~~__~____-~_(+HHMHMH8_______(_~(_<__<<??THH@MMMMHMMHMMMMMMMHMMMHHMH<
_~~_<~_~_____-~._(JW@@HMTY<~~(_(__<(__<-<__-<(_<((OWHHM@MMMH@MMH@MHM@HM@MHc
~___~__~____.-_(gMHM@M#=~-_~__(:__:(_~__-__~<__<_____<THMHHHMMMMMMMM@@HMMMb
______________(MMHMMHY_____~(&+g+aaJJ&g+J--<~_-~___~_~___?YW@HH@MMMMHH@@MHR
_~__~__~______~(?YY:<___~_<<dHMMMMMMMMHHM@HNx__<_((~_<<_-___~?TWMMM@@MMHMMR
~______~__~~__~_~~__<__-___~?WHMHHHHHHMMHMMH3_-___-___<_-___~__~(~7TWWHHHHS
___~___~__(___~_<<__~((-~__<_~<~_(+++<<?>~_~_.-_-_______-______~__<__~?7UWC
(WHH@MMNHWXNHMHHkHHHHHHWNH<~_(dHHMMMMMMHkWWkXQkQAgHHmgmggMMMNkQea&ag++&j+&+
JHMMMMM@MHMMMMMMMMMMM@HMM#:~?WMMMMMMHH@@HHHWHHHHH@MMMMMHMMMM@HHHWHHWkHKHWW$
(MMHHMH@MHMMMM@MMMMHMMHM@P_:?HMMHHMMMMMHMH@HM@H@M@MMHHHMMHHN@M@MHHMHHHHHqkC
?MHHHMHMMHHMMMMMHHMHMMHMH$<~?@@MM@HMHMMMMMHMMMMM@@MHMMMMHMMMHMMMMHMHHHHHHK<
(WMMMMHMHHMHMMHHHMMHMMMMk$<_~dMMMMMMMMM@HHHH@HgM@@MMMMHMMMMMHMMHMMMHHgHHWr(
(jMHMM@MMHMHHH@MHMHHHHMHNR<~<<MMM@@M@MH@MH@M@@HHMMMHH@M@M@MMMHM@HHMHHHHHS<<
~(W@@MMMMHMHMMHH@HHH@HH@HN+(<<dHH@MMMMHHHMHM@@HMHMMMMMM@HHHHgHMMHHH@HHHWC<~
~_?MHM@MM@M@MHM@HHMHMMHMMMs(<<(WMH@MMMM@MM@MHH@@MMMMMHMMMMM@M@MMMM@HHHH3:<<
~__?MMMMM@HHHHHMHMMHMHHMHM#~~<<qHHMM@MM@MMHMHMMMMMHMMHM@MMHH@HHMHMM@HHf<:<~
_--(?MMMMHHMMHMMHMHHHHHMMM#<~<(<WHMMHMMHMMHMMMMMMM@MMH@MHHN@MHHM@HMHHf<~~~~
__-_(?MM@HHMMHMMH@HMHHMHMMMx~:_~?H@@HMHMMHMMMMMMMMHMMHMMHMMMHMMH@HHH9<~~~~~
:(:<(~<WMMMMNHHHMMHHHH@@MMHb~~_~_XM@@M@MM@MMMMMMMMHMMHM@MM@MHMHHHkKV<~__~_~
((_<((<<(UMMMHMHHMH@H@HMMMHMr____(WMMMMMMM@MMHM@MMMMMHMM@HMHMHHHWUC_____~~_
<(_<(_<(<<?WMMMMMMMHMHMH@MHMMr___~(HM@MMMMMMHHMgMHMMMM@HHHHkWHHW=~_____.___
<<<<((<<<<((?HMMMMMHM@@HMMHMMN_____dMMMMMHNMHMMMMMgHMMgHHWHWX6=_____._ .__-
<<;<((<<-<((+_?WMMMMH@M@MM@HMMh_____WMMMMMHMMHMMHM@HMHHHWWWV<_.___`__` .__
<<(<((<<(-(((_(_<?WMMMMMHM@MMHMm____(HHMMMMMHMMMMHHMHMHWUY>_____-__- _--__.
;;<><((:(<_(<<(<<((<?UHHqH@MMHMNs<~___WMMMMMMMMHH@HMMB9!~____`_  __ ___.__
<+<<<<<;<<<(<<(<<(<<<<<??TYHHHHHHh<__~_?MmHHMHHWWHY=~_-__.~~-___.__--__._..
<+><(<<((<:(<<(<<(<<_<<<~~~~~?1YTHw__~__(WHWH9T<__~~___.__._.__   __._-.-..
<<(((<<<(<<(<;(<<(<:(+<<(<:~~:~~_-:_<____(_~___._~____._____-  _.._  _.-._-
*/
