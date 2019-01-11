package fr.skytasul.music;

import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class Events implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        JukeBoxInventory inventory = JukeBoxInventory.inventories.get(e.getWhoClicked().getUniqueId());
        if (inventory != null) {
            inventory.checkClick(e);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        PlayerData playerData = PlayerData.players.get(e.getPlayer().getUniqueId());
        if (playerData != null) {
            playerData.checkLeave(e);
        }
    }

    @EventHandler
    public void onSongDestroy(SongDestroyingEvent e) {
        if (e.getSongPlayer() != null) {
            for (UUID uuid : e.getSongPlayer().getPlayerUUIDs()) {
                try {
                    PlayerData playerData = PlayerData.players.get(uuid);
                    if (playerData != null) {
                        playerData.checkSongDestroy(e);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
