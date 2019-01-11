//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music;

import com.xxmicloxx.NoteBlockAPI.Song;
import fr.skytasul.music.utils.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JukeBoxInventory implements Listener {
    public static HashMap<UUID, JukeBoxInventory> inventories = new HashMap();
    public static int version = Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].split("_")[1]);
    public static List<String> discs13 = new ArrayList(Arrays.asList("MUSIC_DISC_11", "MUSIC_DISC_13", "MUSIC_DISC_BLOCKS", "MUSIC_DISC_CAT", "MUSIC_DISC_CHIRP", "MUSIC_DISC_FAR", "MUSIC_DISC_MALL", "MUSIC_DISC_MELLOHI", "MUSIC_DISC_STAL", "MUSIC_DISC_STRAD", "MUSIC_DISC_WAIT", "MUSIC_DISC_WARD"));
    public static List<String> discs8 = new ArrayList(Arrays.asList("RECORD_10", "RECORD_11", "RECORD_12", "RECORD_3", "RECORD_4", "RECORD_5", "RECORD_6", "RECORD_7", "RECORD_8", "RECORD_9", "GOLD_RECORD", "GREEN_RECORD"));
    private static ItemStack stopItem;
    private static ItemStack laterItem;
    private static ItemStack nextItem;
    private static ItemStack menuItem;
    private static ItemStack toggleItem;
    private static ItemStack randomItem;
    private static ItemStack playlistMenuItem;
    private static ItemStack optionsMenuItem;
    private static ItemStack nextSongItem;
    private static ItemStack clearItem;
    private static Material particles;
    private static List<String> playlistLore;
    private static ItemStack playlistItem;
    private static ItemStack favoritesItem;
    private UUID id;
    public PlayerData pdata;
    private int page = 0;
    private JukeBoxInventory.ItemsMenu menu;
    private Inventory inv;

    static {
        stopItem = item(Material.BARRIER, Lang.STOP);
        laterItem = item(Material.ARROW, Lang.LATER_PAGE);
        nextItem = item(Material.ARROW, Lang.NEXT_PAGE);
        menuItem = item(Material.TRAPPED_CHEST, Lang.MENU_ITEM);
        toggleItem = item(version < 9 ? Material.STONE_BUTTON : Material.valueOf("END_CRYSTAL"), Lang.TOGGLE_PLAYING);
        randomItem = item(Material.valueOf(version > 12 ? "FIRE_CHARGE" : "FIREBALL"), Lang.RANDOM_MUSIC);
        playlistMenuItem = item(Material.CHEST, Lang.PLAYLIST_ITEM);
        optionsMenuItem = item(Material.valueOf(version > 12 ? "COMPARATOR" : "REDSTONE_COMPARATOR"), Lang.OPTIONS_ITEM);
        nextSongItem = item(Material.FEATHER, Lang.NEXT_ITEM);
        clearItem = item(Material.LAVA_BUCKET, Lang.CLEAR_PLAYLIST);
        particles = version < 13 ? Material.valueOf("FIREWORK") : Material.valueOf("FIREWORK_ROCKET");
        playlistLore = Arrays.asList("", Lang.IN_PLAYLIST);
        playlistItem = item(Material.JUKEBOX, Lang.CHANGE_PLAYLIST + Lang.PLAYLIST);
        favoritesItem = item(Material.NOTE_BLOCK, Lang.CHANGE_PLAYLIST + Lang.FAVORITES);
    }

    public JukeBoxInventory(Player p) {
        this.menu = JukeBoxInventory.ItemsMenu.DEFAULT;
        this.id = p.getUniqueId();
        this.pdata = (PlayerData)PlayerData.players.get(p.getUniqueId());
        this.pdata.linked = this;
        inventories.put(this.id, this);
        this.inv = Bukkit.createInventory((InventoryHolder)null, 54, Lang.INV_NAME);
        this.inv.setItem(52, laterItem);
        this.inv.setItem(53, nextItem);
        this.setSongsPage();
        this.openInventory(p);
    }

    public void openInventory(Player p) {
        this.inv = p.openInventory(this.inv).getTopInventory();
        this.menu = JukeBoxInventory.ItemsMenu.DEFAULT;
        this.setItemsMenu();
    }

    public void setSongsPage() {
        for(int i = 0; i < 45; ++i) {
            this.inv.setItem(i, (ItemStack)null);
        }

        if (!JukeBox.songs.isEmpty()) {
            List<Song> playlist = !this.pdata.getPlaylistSongs().isEmpty() ? this.pdata.getPlaylistSongs() : null;

            for(int i = 0; i < 45; ++i) {
                Song s = (Song)JukeBox.songs.get(this.page * 45 + i);
                ItemStack is = this.getSongItem(s);
                if (playlist != null && playlist.contains(s)) {
                    loreAdd(is, playlistLore);
                }

                this.inv.setItem(i, is);
                if (JukeBox.songs.size() - 1 == this.page * 45 + i) {
                    break;
                }
            }

        }
    }

    public void setItemsMenu() {
        for(int i = 45; i < 52; ++i) {
            this.inv.setItem(i, (ItemStack)null);
        }

        if (this.menu != JukeBoxInventory.ItemsMenu.DEFAULT) {
            this.inv.setItem(45, menuItem);
        }

        switch(this.menu) {
            case DEFAULT:
                this.inv.setItem(45, stopItem);
                if (this.pdata.songPlayer != null) {
                    this.inv.setItem(46, toggleItem);
                }

                if (!JukeBox.songs.isEmpty()) {
                    this.inv.setItem(47, randomItem);
                }

                this.inv.setItem(49, playlistMenuItem);
                this.inv.setItem(50, optionsMenuItem);
                break;
            case OPTIONS:
                this.inv.setItem(47, item(Material.BEACON, "§cerror", Lang.RIGHT_CLICK, Lang.LEFT_CLICK));
                if (JukeBox.particles) {
                    this.inv.setItem(48, item(particles, "§cerror"));
                }

                this.inv.setItem(49, item(Material.SIGN, "§cerror"));
                this.inv.setItem(50, item(Material.BLAZE_POWDER, "§cerror"));
                this.volumeItem();
                this.shuffleItem();
                this.joinItem();
                this.particlesItem();
                break;
            case PLAYLIST:
                this.inv.setItem(47, nextSongItem);
                this.inv.setItem(48, clearItem);
                this.inv.setItem(50, this.pdata.getListeningPlaylist() == Playlists.PLAYLIST ? playlistItem : favoritesItem);
        }

    }

    public UUID getID() {
        return this.id;
    }

    public void checkClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (e.getClickedInventory() == this.inv) {
            if (e.getCurrentItem() != null) {
                if (p.getUniqueId().equals(this.id)) {
                    e.setCancelled(true);
                    int slot = e.getSlot();
                    if (!e.getCurrentItem().getType().name().contains("RECORD") && !e.getCurrentItem().getType().name().contains("DISC")) {
                        switch(slot) {
                            case 52:
                            case 53:
                                if (JukeBox.maxPage != 0) {
                                    if (slot == 53) {
                                        if (this.page == JukeBox.maxPage - 1) {
                                            return;
                                        }

                                        ++this.page;
                                    } else if (slot == 52) {
                                        if (this.page == 0) {
                                            return;
                                        }

                                        --this.page;
                                    }

                                    this.setSongsPage();
                                }
                                break;
                            default:
                                if (slot == 45) {
                                    if (this.menu == JukeBoxInventory.ItemsMenu.DEFAULT) {
                                        this.pdata.stopPlaying(true);
                                        this.inv.setItem(46, (ItemStack)null);
                                    } else {
                                        this.menu = JukeBoxInventory.ItemsMenu.DEFAULT;
                                        this.setItemsMenu();
                                    }

                                    return;
                                }

                                switch(this.menu) {
                                    case DEFAULT:
                                        switch(slot) {
                                            case 46:
                                                this.pdata.songPlayer.setPlaying(!this.pdata.songPlayer.isPlaying());
                                                return;
                                            case 47:
                                                this.pdata.playRandom();
                                                return;
                                            case 48:
                                            default:
                                                return;
                                            case 49:
                                                this.menu = JukeBoxInventory.ItemsMenu.PLAYLIST;
                                                this.setItemsMenu();
                                                return;
                                            case 50:
                                                this.menu = JukeBoxInventory.ItemsMenu.OPTIONS;
                                                this.setItemsMenu();
                                                return;
                                        }
                                    case OPTIONS:
                                        switch(slot) {
                                            case 47:
                                                if (e.getClick() == ClickType.RIGHT) {
                                                    this.pdata.setVolume((byte)(this.pdata.getVolume() - 10));
                                                }

                                                if (e.getClick() == ClickType.LEFT) {
                                                    this.pdata.setVolume((byte)(this.pdata.getVolume() + 10));
                                                }

                                                if (this.pdata.getVolume() < 0) {
                                                    this.pdata.setVolume(0);
                                                }

                                                if (this.pdata.getVolume() > 100) {
                                                    this.pdata.setVolume(100);
                                                }

                                                return;
                                            case 48:
                                                this.pdata.setParticles(!this.pdata.hasParticles());
                                                return;
                                            case 49:
                                                if (!JukeBox.autoJoin) {
                                                    this.pdata.setJoinMusic(!this.pdata.hasJoinMusic());
                                                }

                                                return;
                                            case 50:
                                                this.pdata.setShuffle(!this.pdata.isShuffle());
                                                return;
                                            default:
                                                return;
                                        }
                                    case PLAYLIST:
                                        switch(slot) {
                                            case 47:
                                                this.pdata.nextSong();
                                                break;
                                            case 48:
                                                this.pdata.getPlaylistSongs().clear();
                                                this.setSongsPage();
                                            case 49:
                                            default:
                                                break;
                                            case 50:
                                                this.inv.setItem(50, this.pdata.setListeningPlaylist(this.pdata.getListeningPlaylist() == Playlists.PLAYLIST ? Playlists.FAVORITES : Playlists.PLAYLIST) == Playlists.PLAYLIST ? playlistItem : favoritesItem);
                                                this.setSongsPage();
                                        }
                                }
                        }

                    } else {
                        Song s = (Song)JukeBox.songs.get(this.page * 45 + slot);
                        if (e.getClick() != ClickType.MIDDLE) {
                            this.pdata.playSong(s);
                        } else {
                            if (this.pdata.songPlayer != null && this.pdata.getPlaylistSongs().contains(s)) {
                                this.pdata.getPlaylistSongs().remove(s);
                                this.inv.setItem(slot, this.getSongItem(s));
                            } else if (this.pdata.addSong(s)) {
                                this.inv.setItem(slot, loreAdd(this.getSongItem(s), playlistLore));
                            }

                        }
                    }
                }
            }
        }
    }

    public ItemStack getSongItem(Song s) {
        ItemStack is = item(randomRecord(), JukeBox.getItemName(s, false));
        if (!StringUtils.isEmpty(s.getDescription())) {
            loreAdd(is, splitOnSpace(s.getDescription(), 30));
        }

        return is;
    }

    public void volumeItem() {
        if (this.menu == JukeBoxInventory.ItemsMenu.OPTIONS) {
            name(this.inv.getItem(47), Lang.VOLUME + this.pdata.getVolume() + "%");
        }
    }

    public void particlesItem() {
        if (this.menu == JukeBoxInventory.ItemsMenu.OPTIONS) {
            if (JukeBox.particles) {
                if (!JukeBox.particles) {
                    this.inv.setItem(48, (ItemStack)null);
                }

                name(this.inv.getItem(48), ChatColor.AQUA + (this.pdata.hasParticles() ? Lang.DISABLE : Lang.ENABLE) + " " + Lang.PARTICLES);
            }
        }
    }

    public void joinItem() {
        if (this.menu == JukeBoxInventory.ItemsMenu.OPTIONS) {
            name(this.inv.getItem(49), ChatColor.GREEN + (this.pdata.hasJoinMusic() ? Lang.DISABLE : Lang.ENABLE) + " " + Lang.CONNEXION_MUSIC);
        }
    }

    public void shuffleItem() {
        if (this.menu == JukeBoxInventory.ItemsMenu.OPTIONS) {
            name(this.inv.getItem(50), ChatColor.YELLOW + (this.pdata.isShuffle() ? Lang.DISABLE : Lang.ENABLE) + " " + Lang.SHUFFLE_MODE);
        }
    }

    public void playingStarted() {
        if (this.menu == JukeBoxInventory.ItemsMenu.DEFAULT) {
            this.inv.setItem(46, toggleItem);
        }

    }

    public void playingStopped() {
        if (this.menu == JukeBoxInventory.ItemsMenu.DEFAULT) {
            this.inv.setItem(46, (ItemStack)null);
        }

    }

    public static Material randomRecord() {
        Random ran = new Random();
        int i = ran.nextInt(12);
        return version > 12 ? Material.valueOf((String)discs13.get(i)) : Material.valueOf((String)discs8.get(i));
    }

    public static ItemStack item(Material type, String name, String... lore) {
        ItemStack is = new ItemStack(type);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        im.addItemFlags(ItemFlag.values());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack loreAdd(ItemStack is, List<String> lore) {
        ItemMeta im = is.getItemMeta();
        List<String> ls = im.getLore();
        if (ls == null) {
            ls = new ArrayList();
        }

        ((List)ls).addAll(lore);
        im.setLore((List)ls);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack name(ItemStack is, String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }

    public static List<String> splitOnSpace(String string, int minSize) {
        if (string != null && !string.isEmpty()) {
            List<String> ls = new ArrayList();
            if (string.length() <= minSize) {
                ls.add(string);
                return ls;
            } else {
                String[] var6;
                int var5 = (var6 = StringUtils.splitByWholeSeparator(string, "\\n")).length;

                for(int var4 = 0; var4 < var5; ++var4) {
                    String str = var6[var4];
                    int lastI = 0;
                    int ic = 0;

                    for(int i = 0; i < str.length(); ++i) {
                        String color = "";
                        if (!ls.isEmpty()) {
                            color = ChatColor.getLastColors((String)ls.get(ls.size() - 1));
                        }

                        if (ic >= minSize) {
                            if (str.charAt(i) == ' ') {
                                ls.add(color + str.substring(lastI, i));
                                ic = 0;
                                lastI = i + 1;
                            } else if (i + 1 == str.length()) {
                                ls.add(color + str.substring(lastI, i + 1));
                            }
                        } else if (str.length() - lastI <= minSize) {
                            ls.add(color + str.substring(lastI, str.length()));
                            break;
                        }

                        ++ic;
                    }
                }

                return ls;
            }
        } else {
            return null;
        }
    }

    static enum ItemsMenu {
        DEFAULT,
        OPTIONS,
        PLAYLIST;

        private ItemsMenu() {
        }
    }
}
