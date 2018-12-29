//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music;

import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import fr.skytasul.music.utils.Lang;
import fr.skytasul.music.utils.Placeholders;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class JukeBox extends JavaPlugin implements Listener {
    private static JukeBox instance;
    private boolean disable = false;
    private static File playersFile;
    public static FileConfiguration players;
    public static File songsFolder;
    public static LinkedList<Song> songs;
    public static int maxPage;
    public static boolean sendMessages = true;
    public static boolean async = false;
    public static boolean autoJoin = false;
    public static PlayerData defaultPlayer = null;
    public static List<World> worldsEnabled;
    public static boolean worlds;
    public static boolean particles;
    public static boolean actionBar;
    public ItemStack jukeboxItem;
    private static Random random = new Random();

    public JukeBox() {
    }

    public void onEnable() {
        if (!this.getServer().getPluginManager().isPluginEnabled("NoteBlockAPI")) {
            this.getLogger().severe("NoteBlockAPI isn't loaded. Please install it on your server and restart it.");
            this.disable = true;
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                Placeholders.registerPlaceholders();
            }

            instance = this;
            this.saveDefaultConfig();
            this.initAll();
        }

    }

    public void onDisable() {
        if (!this.disable) {
            this.disableAll();
        }

    }

    public void disableAll() {
        List<Map<String, Object>> list = new ArrayList();
        Iterator var3 = PlayerData.players.values().iterator();

        while(var3.hasNext()) {
            PlayerData pdata = (PlayerData)var3.next();
            if (pdata.songPlayer != null) {
                pdata.stopPlaying(true);
            }

            if (!pdata.isDefault(defaultPlayer)) {
                list.add(pdata.serialize());
            }
        }

        players.set("players", list);
        players.set("item", this.jukeboxItem == null ? null : this.jukeboxItem.serialize());

        try {
            players.save(playersFile);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        try {
            JukeBoxInventory.inventories.clear();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        HandlerList.unregisterAll((Plugin) this);
    }

    public void initAll() {
        this.reloadConfig();
        this.loadLang();
        sendMessages = this.getConfig().getBoolean("sendMessages");
        async = this.getConfig().getBoolean("asyncLoading");
        autoJoin = this.getConfig().getBoolean("forceJoinMusic");
        defaultPlayer = PlayerData.deserialize(this.getConfig().getConfigurationSection("defaultPlayerOptions").getValues(false), (Map)null);
        particles = this.getConfig().getBoolean("noteParticles");
        actionBar = this.getConfig().getBoolean("actionBar");
        if (JukeBoxInventory.version == 8) {
            particles = false;
        }

        worldsEnabled = new ArrayList();
        Iterator var2 = this.getConfig().getStringList("enabledWorlds").iterator();

        while(var2.hasNext()) {
            String name = (String)var2.next();
            World world = Bukkit.getWorld(name);
            if (world != null) {
                worldsEnabled.add(world);
            }
        }

        worlds = !worldsEnabled.isEmpty();
        if (async) {
            (new BukkitRunnable() {
                public void run() {
                    JukeBox.this.loadDatas();
                    JukeBox.this.finishEnabling();
                }
            }).runTaskAsynchronously(this);
        } else {
            this.loadDatas();
            this.finishEnabling();
        }

    }

    private void finishEnabling() {
        this.getCommand("music").setExecutor(new CommandMusic());
        this.getCommand("adminmusic").setExecutor(new CommandAdmin());
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private void loadDatas() {
        songs = new LinkedList();
        songsFolder = new File(this.getDataFolder(), "songs");
        if (!songsFolder.exists()) {
            songsFolder.mkdirs();
        }

        Map<String, Song> tmpSongs = new HashMap();
        File[] var5;
        int var4 = (var5 = songsFolder.listFiles()).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            File file = var5[var3];
            if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("nbs")) {
                Song song = NBSDecoder.parse(file);
                if (song != null) {
                    String n = getInternal(song);
                    if (tmpSongs.containsKey(n)) {
                        this.getLogger().warning("Song \"" + n + "\" is duplicated. Please delete one from the songs directory. File name: " + file.getName());
                    } else {
                        tmpSongs.put(n, song);
                    }
                }
            }
        }

        this.getLogger().info(tmpSongs.size() + " songs loadeds. Sorting by name... ");
        List<String> names = new ArrayList(tmpSongs.keySet());
        Collections.sort(names, Collator.getInstance());
        Iterator var13 = names.iterator();

        while(var13.hasNext()) {
            String str = (String)var13.next();
            songs.add((Song)tmpSongs.get(str));
        }

        this.setMaxPage();
        this.getLogger().info("Songs sorted ! " + songs.size() + " songs. Number of pages : " + maxPage);
        PlayerData.players = new HashMap();

        try {
            playersFile = new File(this.getDataFolder(), "datas.yml");
            boolean b = false;
            if (!playersFile.exists()) {
                b = true;
                playersFile.createNewFile();
            }

            players = YamlConfiguration.loadConfiguration(playersFile);
            boolean last = this.getConfig().contains("players");
            if (!b || last) {
                FileConfiguration tmpPlayers = last ? this.getConfig() : players;
                Iterator var18 = tmpPlayers.getMapList("players").iterator();

                while(var18.hasNext()) {
                    Map<?, ?> m = (Map)var18.next();
                    PlayerData pdata = PlayerData.deserialize((Map)m, tmpSongs);
                    PlayerData.players.put(pdata.getID(), pdata);
                }

                if (tmpPlayers.get("item") != null) {
                    this.jukeboxItem = ItemStack.deserialize(tmpPlayers.getConfigurationSection("item").getValues(false));
                }

                if (last) {
                    this.getLogger().info("Player datas were saved into config.yml; moved to players.yml");
                    this.getConfig().set("players", (Object)null);
                    this.saveConfig();
                }
            }
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        var13 = Bukkit.getOnlinePlayers().iterator();

        while(var13.hasNext()) {
            Player p = (Player)var13.next();
            this.onJoin(new PlayerJoinEvent(p, ""));
        }

    }

    void setMaxPage() {
        maxPage = (int)StrictMath.ceil((double)songs.size() * 1.0D / 45.0D);
    }

    private YamlConfiguration loadLang() {
        String s = "en.yml";
        if (this.getConfig().getString("lang") != null) {
            s = this.getConfig().getString("lang") + ".yml";
        }

        File lang = new File(this.getDataFolder(), s);
        if (!lang.exists()) {
            try {
                this.getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource(s);
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
                    defConfig.save(lang);
                    Lang.loadFromConfig(defConfig);
                    this.getLogger().info("Created language file " + s);
                    return defConfig;
                }
            } catch (IOException var6) {
                var6.printStackTrace();
                this.getLogger().severe("Couldn't create language file.");
                this.getLogger().severe("This is a fatal error. Now disabling.");
                this.disable = true;
                this.setEnabled(false);
            }
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);

        try {
            Lang.saveFile(conf, lang);
        } catch (IllegalAccessException | IOException | IllegalArgumentException var5) {
            this.getLogger().warning("Failed to save lang.yml.");
            this.getLogger().warning("Report this stack trace to SkytAsul on SpigotMC.");
            var5.printStackTrace();
        }

        Lang.loadFromConfig(conf);
        this.getLogger().info("Loaded language file " + s);
        return conf;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        PlayerData pdata = (PlayerData)PlayerData.players.get(id);
        if (pdata == null) {
            pdata = PlayerData.create(id);
            PlayerData.players.put(id, pdata);
        }

        pdata.playerJoin(p, worlds ? worldsEnabled.contains(p.getWorld()) : true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && this.jukeboxItem != null && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem().equals(this.jukeboxItem)) {
            CommandMusic.open(e.getPlayer());
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (worlds && e.getFrom().getWorld() != e.getTo().getWorld() && worldsEnabled.contains(e.getTo().getWorld())) {
            PlayerData pdata = (PlayerData)PlayerData.players.get(e.getPlayer().getUniqueId());
            if (pdata.songPlayer != null) {
                pdata.stopPlaying(true);
            }
        }

    }

    public static JukeBox getInstance() {
        return instance;
    }

    public static Song randomSong() {
        return (Song)songs.get(random.nextInt(songs.size() - 1));
    }

    public static String getInternal(Song s) {
        return s.getTitle() != null && !s.getTitle().isEmpty() ? s.getTitle() : s.getPath().getName();
    }

    public static String getItemName(Song s, boolean unknown) {
        boolean noName = s.getTitle() != null ? s.getTitle().isEmpty() : true;
        return (noName ? (unknown ? "unknown" : s.getPath().getName()) : s.getTitle()) + "    | " + songs.indexOf(s);
    }

    public static String getSongName(Song song) {
        boolean noName = song.getTitle().isEmpty();
        return "\"" + (noName ? song.getPath().getName() + "\"" : song.getTitle() + "\", " + song.getAuthor());
    }

    public static boolean sendMessage(Player p, String msg) {
        if (sendMessages) {
            if (actionBar) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
            } else {
                p.sendMessage(msg);
            }

            return true;
        } else {
            return false;
        }
    }
}
