//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import com.xxmicloxx.NoteBlockAPI.Song;
import fr.skytasul.music.utils.CustomSongPlayer;
import fr.skytasul.music.utils.Lang;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerData implements Listener {
    public static Map<UUID, PlayerData> players;
    private UUID id;
    private boolean join = false;
    private boolean shuffle = false;
    private int volume = 20;
    private boolean particles = true;
    private ArrayList<Song> playlist = new ArrayList();
    private ArrayList<Song> favorites = new ArrayList();
    private Playlists listening;
    private int playlistIndex;
    public CustomSongPlayer songPlayer;
    private Player p;
    JukeBoxInventory linked;

    private PlayerData(UUID id) {
        this.listening = Playlists.PLAYLIST;
        this.playlistIndex = 0;
        this.linked = null;
        this.id = id;
    }

    public Song playSong(Song song) {
        if (this.songPlayer != null) {
            this.stopPlaying(false);
        }

        this.songPlayer = new CustomSongPlayer(song);
        this.songPlayer.particlesEnabled = this.particles;
        this.songPlayer.setVolume((byte)this.volume);
        this.songPlayer.getFadeIn().setFadeDuration(0);
        this.songPlayer.setAutoDestroy(true);
        this.songPlayer.addPlayer(this.p);
        this.songPlayer.setPlaying(true);
        JukeBox.sendMessage(this.p, Lang.MUSIC_PLAYING + " " + JukeBox.getSongName(song));
        if (this.linked != null) {
            this.linked.playingStarted();
        }

        return song;
    }

    public boolean addSong(Song song) {
        if (this.songPlayer == null) {
            this.playSong(song);
            return false;
        } else {
            this.getPlaylistSongs().add(song);
            return true;
        }
    }

    public Song playRandom() {
        return JukeBox.songs.isEmpty() ? null : this.playSong(JukeBox.randomSong());
    }

    public void stopPlaying(boolean msg) {
        if (this.songPlayer != null) {
            CustomSongPlayer tmp = this.songPlayer;
            this.songPlayer = null;
            tmp.destroy();
            if (msg) {
                JukeBox.sendMessage(this.p, Lang.MUSIC_STOPPED);
            }

            if (this.linked != null) {
                this.linked.playingStopped();
            }

        }
    }

    public void nextSong() {
        if (this.songPlayer == null) {
            List<Song> playlist = this.getPlaylistSongs();
            if (playlist.isEmpty()) {
                return;
            }

            this.playSong((Song)playlist.get(0));
        }

        this.songPlayer.setTick((short)(this.songPlayer.getSong().getLength() + 1));
        if (!this.songPlayer.isPlaying()) {
            this.songPlayer.setPlaying(true);
        }

    }

    public void playerJoin(Player player, boolean replay) {
        this.p = player;
        if (replay) {
            if (this.songPlayer == null) {
                if (this.hasJoinMusic()) {
                    this.playRandom();
                }
            } else if (!this.songPlayer.adminPlayed) {
                this.songPlayer.setPlaying(true);
                JukeBox.sendMessage(this.p, Lang.RELOAD_MUSIC + " (" + JukeBox.getSongName(this.songPlayer.getSong()) + ")");
            }

        }
    }

    public void checkSongDestroy(SongDestroyingEvent e) {
        if (e.getSongPlayer() == this.songPlayer
                && Bukkit.getPlayer(id) != null /* is online */) {
            Song toListen = null;
            List<Song> playlist = this.getPlaylistSongs();
            if (!playlist.isEmpty()) {
                if (playlist.size() == this.playlistIndex) {
                    this.playlistIndex = 0;
                }

                toListen = (Song)playlist.get(this.playlistIndex);
                ++this.playlistIndex;
            } else if (this.shuffle) {
                toListen = JukeBox.randomSong();
            }

            if (toListen != null) {
                e.setCancelled(true);
                this.songPlayer.adminPlayed = false;
                this.songPlayer.getPlaylist().add(new Song[]{toListen});
                this.songPlayer.playSong(this.songPlayer.getPlaylist().getCount() - 1);
                this.songPlayer.setPlaying(true);
                JukeBox.sendMessage(this.p, Lang.MUSIC_PLAYING + " " + JukeBox.getSongName(toListen));
            }
        }

    }

    public void checkLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.getUniqueId().equals(this.id)) {
            if (this.songPlayer != null) {
                this.songPlayer.setPlaying(false);
            }

        }
    }

    public List<Song> getPlaylistSongs() {
        switch(this.listening) {
            case PLAYLIST:
                return this.playlist;
            case FAVORITES:
                return this.favorites;
            default:
                return null;
        }
    }

    public Playlists getListeningPlaylist() {
        return this.listening;
    }

    public Playlists setListeningPlaylist(Playlists playlist) {
        this.listening = playlist;
        this.playlistIndex = 0;
        return this.listening;
    }

    public UUID getID() {
        return this.id;
    }

    public boolean hasJoinMusic() {
        return this.join;
    }

    public boolean setJoinMusic(boolean join) {
        this.join = join;
        if (this.linked != null) {
            this.linked.joinItem();
        }

        return join;
    }

    public boolean isShuffle() {
        return this.shuffle;
    }

    public boolean setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
        if (this.linked != null) {
            this.linked.shuffleItem();
        }

        return shuffle;
    }

    public int getVolume() {
        return this.volume;
    }

    public int setVolume(int volume) {
        if (this.songPlayer != null) {
            this.songPlayer.setVolume((byte)volume);
        }

        this.volume = volume;
        if (this.linked != null) {
            this.linked.volumeItem();
        }

        return volume;
    }

    public boolean hasParticles() {
        return this.particles;
    }

    public boolean setParticles(boolean particles) {
        if (this.songPlayer != null) {
            this.songPlayer.particlesEnabled = particles;
        }

        this.particles = particles;
        if (this.linked != null) {
            this.linked.particlesItem();
        }

        return particles;
    }

    public boolean isDefault(PlayerData base) {
        if (base.hasJoinMusic() != this.hasJoinMusic() && !JukeBox.autoJoin) {
            return false;
        } else if (base.isShuffle() != this.isShuffle()) {
            return false;
        } else if (base.getVolume() != this.getVolume()) {
            return false;
        } else {
            return base.hasParticles() == this.hasParticles();
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap();
        map.put("id", this.id.toString());
        map.put("join", this.hasJoinMusic());
        map.put("shuffle", this.isShuffle());
        map.put("volume", this.getVolume());
        map.put("particles", this.hasParticles());
        if (!this.favorites.isEmpty()) {
            List<String> list = new ArrayList();
            Iterator var4 = this.favorites.iterator();

            while(var4.hasNext()) {
                Song song = (Song)var4.next();
                list.add(JukeBox.getInternal(song));
            }

            map.put("favorites", list);
        }

        return map;
    }

    static PlayerData create(UUID id) {
        PlayerData pdata = new PlayerData(id);
        PlayerData base = JukeBox.defaultPlayer;
        pdata.setJoinMusic(base.hasJoinMusic());
        pdata.setShuffle(base.isShuffle());
        pdata.setVolume(base.getVolume());
        pdata.setParticles(base.hasParticles());
        if (JukeBox.autoJoin) {
            pdata.setJoinMusic(true);
        }

        return pdata;
    }

    public static PlayerData deserialize(Map<String, Object> map, Map<String, Song> songsName) {
        PlayerData pdata = new PlayerData(map.containsKey("id") ? UUID.fromString((String)map.get("id")) : null);
        pdata.setJoinMusic((Boolean)map.get("join"));
        pdata.setShuffle((Boolean)map.get("shuffle"));
        if (map.containsKey("particles")) {
            pdata.setParticles((Boolean)map.get("particles"));
        }

        if (map.containsKey("favorites")) {
            Iterator var4 = ((List)map.get("favorites")).iterator();

            while(var4.hasNext()) {
                String s = (String)var4.next();
                Song song = (Song)songsName.get(s);
                if (song == null) {
                    JukeBox.getInstance().getLogger().warning("Song \"" + s + "\" for playlist of " + pdata.getID().toString());
                } else {
                    pdata.favorites.add(song);
                }
            }
        }

        return pdata;
    }
}
