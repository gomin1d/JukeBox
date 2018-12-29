//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music.utils;

import fr.skytasul.music.JukeBox;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
    public static String NEXT_PAGE;
    public static String LATER_PAGE;
    public static String PLAYER;
    public static String RELOAD_MUSIC;
    public static String INV_NAME;
    public static String TOGGLE_PLAYING;
    public static String VOLUME;
    public static String RIGHT_CLICK;
    public static String LEFT_CLICK;
    public static String RANDOM_MUSIC;
    public static String STOP;
    public static String MUSIC_STOPPED;
    public static String ENABLE;
    public static String DISABLE;
    public static String SHUFFLE_MODE;
    public static String CONNEXION_MUSIC;
    public static String PARTICLES;
    public static String MUSIC_PLAYING;
    public static String INCORRECT_SYNTAX;
    public static String RELOAD_LAUNCH;
    public static String RELOAD_FINISH;
    public static String AVAILABLE_COMMANDS;
    public static String INVALID_NUMBER;
    public static String PLAYER_MUSIC_STOPPED;
    public static String IN_PLAYLIST;
    public static String PLAYLIST_ITEM;
    public static String OPTIONS_ITEM;
    public static String MENU_ITEM;
    public static String CLEAR_PLAYLIST;
    public static String NEXT_ITEM;
    public static String CHANGE_PLAYLIST;
    public static String PLAYLIST;
    public static String FAVORITES;

    static {
        NEXT_PAGE = ChatColor.AQUA + "Next page";
        LATER_PAGE = ChatColor.AQUA + "Previous page";
        PLAYER = ChatColor.RED + "You must be a player to do this command.";
        RELOAD_MUSIC = ChatColor.GREEN + "Music reloaded.";
        INV_NAME = ChatColor.LIGHT_PURPLE + "§lJukebox !";
        TOGGLE_PLAYING = ChatColor.GOLD + "Pause/play";
        VOLUME = ChatColor.BLUE + "Music volume : §b";
        RIGHT_CLICK = "§eRight click: decrease by 10%";
        LEFT_CLICK = "§eLeft click: increase by 10%";
        RANDOM_MUSIC = ChatColor.DARK_AQUA + "Random music";
        STOP = ChatColor.RED + "Stop the music";
        MUSIC_STOPPED = ChatColor.GREEN + "Music stopped.";
        ENABLE = "Enable";
        DISABLE = "Disable";
        SHUFFLE_MODE = "the shuffle mode";
        CONNEXION_MUSIC = "music when connecting";
        PARTICLES = "particles";
        MUSIC_PLAYING = ChatColor.GREEN + "Music while playing:";
        INCORRECT_SYNTAX = ChatColor.RED + "Incorrect syntax.";
        RELOAD_LAUNCH = ChatColor.GREEN + "Trying to reload.";
        RELOAD_FINISH = ChatColor.GREEN + "Reload finished.";
        AVAILABLE_COMMANDS = ChatColor.GREEN + "Available commands:";
        INVALID_NUMBER = ChatColor.RED + "Invalid number.";
        PLAYER_MUSIC_STOPPED = ChatColor.GREEN + "Music stopped for player: §b";
        IN_PLAYLIST = ChatColor.BLUE + "§oIn Playlist";
        PLAYLIST_ITEM = ChatColor.LIGHT_PURPLE + "Playlists";
        OPTIONS_ITEM = ChatColor.AQUA + "Options";
        MENU_ITEM = ChatColor.GOLD + "Return to menu";
        CLEAR_PLAYLIST = ChatColor.RED + "Empty the actual playlist";
        NEXT_ITEM = ChatColor.YELLOW + "Next song";
        CHANGE_PLAYLIST = ChatColor.GOLD + "§lSwitch playlist: §r";
        PLAYLIST = ChatColor.DARK_PURPLE + "Playlist";
        FAVORITES = ChatColor.DARK_RED + "Favorites";
    }

    public Lang() {
    }

    public static void saveFile(YamlConfiguration cfg, File file) throws IllegalArgumentException, IllegalAccessException, IOException {
        Field[] var5;
        int var4 = (var5 = Lang.class.getDeclaredFields()).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Field f = var5[var3];
            if (!cfg.contains(f.getName())) {
                cfg.set(f.getName(), f.get((Object)null));
            }
        }

        cfg.save(file);
    }

    public static void loadFromConfig(YamlConfiguration cfg) {
        Iterator var2 = cfg.getValues(false).keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();

            try {
                Lang.class.getDeclaredField(key).set(key, cfg.get(key));
            } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException var4) {
                JukeBox.getInstance().getLogger().warning("Error when loading language value \"" + key + "\".");
                var4.printStackTrace();
            }
        }

    }
}
