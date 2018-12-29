//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music;

import fr.skytasul.music.utils.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMusic implements CommandExecutor {
    public CommandMusic() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.PLAYER);
            return false;
        } else {
            Player p = (Player)sender;
            open(p);
            return false;
        }
    }

    public static void open(Player p) {
        if (!JukeBox.worlds || JukeBox.worldsEnabled.contains(p.getWorld())) {
            if (JukeBoxInventory.inventories.containsKey(p.getUniqueId())) {
                JukeBoxInventory inv = (JukeBoxInventory)JukeBoxInventory.inventories.get(p.getUniqueId());
                inv.setSongsPage();
                inv.openInventory(p);
            } else {
                new JukeBoxInventory(p);
            }

        }
    }
}
