//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music.utils;

import com.xxmicloxx.NoteBlockAPI.Layer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class CustomSongPlayer extends RadioSongPlayer {
    public boolean particlesEnabled;
    public boolean adminPlayed;

    public CustomSongPlayer(Song song) {
        super(song, SoundCategory.RECORDS);
    }

    public void playTick(Player player, int tick) {
        super.playTick(player, tick);
        if (this.particlesEnabled) {
            boolean empty = true;
            Iterator var5 = this.song.getLayerHashMap().values().iterator();

            while(var5.hasNext()) {
                Layer layer = (Layer)var5.next();
                if (layer.getNote(tick) != null) {
                    empty = false;
                    break;
                }
            }

            if (!empty) {
                Particles.sendParticles(player);
            }

        }
    }
}
