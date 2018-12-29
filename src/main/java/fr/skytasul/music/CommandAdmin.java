//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music;

import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import fr.skytasul.music.utils.Lang;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandAdmin implements CommandExecutor {
    public CommandAdmin() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Lang.INCORRECT_SYNTAX);
            return false;
        } else {
            String var5;
            PlayerData pdata;
            Iterator var11;
            Player p;
            Iterator var29;
            switch((var5 = args[0]).hashCode()) {
                case -1800314195:
                    if (var5.equals("particles")) {
                        if (args.length < 2) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        if (args[1].equals("@a")) {
                            var11 = Bukkit.getOnlinePlayers().iterator();

                            while(var11.hasNext()) {
                                p = (Player)var11.next();
                                sender.sendMessage(p.getName() + " : " + this.particles(p));
                            }

                            return false;
                        } else {
                            p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                sender.sendMessage("§cUnknown player.");
                                return false;
                            }

                            sender.sendMessage(this.particles(p));
                            return false;
                        }
                    }
                    break;
                case -985752863:
                    if (var5.equals("player")) {
                        if (args.length < 2) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        OfflinePlayer pp = Bukkit.getOfflinePlayer(args[1]);
                        if (pp == null) {
                            sender.sendMessage("§cUnknown player.");
                            return false;
                        }

                        pdata = (PlayerData)PlayerData.players.get(pp.getUniqueId());
                        String s = Lang.MUSIC_PLAYING + " ";
                        if (pdata == null) {
                            s = s + "§cx";
                        } else if (pdata.songPlayer == null) {
                            s = s + "§cx";
                        } else {
                            Song song = pdata.songPlayer.getSong();
                            s = JukeBox.getSongName(song);
                        }

                        sender.sendMessage(s);
                        return false;
                    }
                    break;
                case -938285885:
                    if (var5.equals("random")) {
                        if (args.length < 2) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        if (args[1].equals("@a")) {
                            var11 = Bukkit.getOnlinePlayers().iterator();

                            while(var11.hasNext()) {
                                p = (Player)var11.next();
                                sender.sendMessage(p.getName() + " : " + this.random(p));
                            }

                            return false;
                        } else {
                            p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                sender.sendMessage("§cUnknown player.");
                                return false;
                            }

                            sender.sendMessage(this.random(p));
                            return false;
                        }
                    }
                    break;
                case -934641255:
                    if (var5.equals("reload")) {
                        sender.sendMessage(Lang.RELOAD_LAUNCH);

                        try {
                            JukeBox.getInstance().disableAll();
                            JukeBox.getInstance().initAll();
                        } catch (Exception var22) {
                            sender.sendMessage("§cError while reloading. Please check the console and send the stacktrace to SkytAsul on SpigotMC.");
                            var22.printStackTrace();
                        }

                        sender.sendMessage(Lang.RELOAD_FINISH);
                        return false;
                    }
                    break;
                case -810883302:
                    if (var5.equals("volume")) {
                        if (args.length < 3) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        p = Bukkit.getPlayer(args[1]);
                        if (p == null) {
                            sender.sendMessage("§cUnknown player.");
                            return false;
                        }

                        pdata = (PlayerData)PlayerData.players.get(p.getUniqueId());

                        try {
                            pdata.setVolume(Byte.parseByte(args[2]));
                            sender.sendMessage("§aVolume : " + pdata.getVolume());
                        } catch (NumberFormatException var20) {
                            sender.sendMessage(Lang.INVALID_NUMBER);
                        }

                        return false;
                    }
                    break;
                case 3267882:
                    if (var5.equals("join")) {
                        if (args.length < 2) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        if (args[1].equals("@a")) {
                            var11 = Bukkit.getOnlinePlayers().iterator();

                            while(var11.hasNext()) {
                                p = (Player)var11.next();
                                sender.sendMessage(p.getName() + " : " + this.join(p));
                            }

                            return false;
                        } else {
                            p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                sender.sendMessage("§cUnknown player.");
                                return false;
                            }

                            sender.sendMessage(this.join(p));
                            return false;
                        }
                    }
                    break;
                case 3443508:
                    if (var5.equals("play")) {
                        if (args.length < 3) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        if (args[1].equals("@a")) {
                            var29 = Bukkit.getOnlinePlayers().iterator();

                            while(var29.hasNext()) {
                                p = (Player)var29.next();
                                args[1] = p.getName();
                                String msg = this.play(args);
                                if (!msg.isEmpty()) {
                                    sender.sendMessage(p.getName() + " : " + msg);
                                }
                            }

                            return false;
                        } else {
                            String msg = this.play(args);
                            if (!msg.isEmpty()) {
                                sender.sendMessage(msg);
                            }

                            return false;
                        }
                    }
                    break;
                case 3540994:
                    if (var5.equals("stop")) {
                        if (args.length < 2) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        if (args[1].equals("@a")) {
                            var29 = Bukkit.getOnlinePlayers().iterator();

                            while(var29.hasNext()) {
                                p = (Player)var29.next();
                                sender.sendMessage(this.stop(p));
                            }

                            return false;
                        } else {
                            p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                sender.sendMessage("§cUnknown player.");
                                return false;
                            }

                            sender.sendMessage(this.stop(p));
                            return false;
                        }
                    }
                    break;
                case 1427818632:
                    if (var5.equals("download")) {
                        if (args.length < 3) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        try {
                            File file = new File(JukeBox.songsFolder, args[2] + ".nbs");
                            FileUtils.copyURLToFile(new URL(args[1]), file);
                            boolean valid = true;
                            FileInputStream stream = new FileInputStream(file);

                            try {
                                Song song = NBSDecoder.parse(stream);
                                if (song == null) {
                                    valid = false;
                                }
                            } catch (Throwable var21) {
                                valid = false;
                            } finally {
                                stream.close();
                                if (!valid) {
                                    sender.sendMessage("§cDownloaded file is not a nbs song file.");
                                }

                            }

                            if (valid) {
                                sender.sendMessage("§aSong downloaded. To add it to the list, you must reload the plugin. (§o/amusic reload§r§a)");
                            } else {
                                file.delete();
                            }

                            return false;
                        } catch (Throwable var24) {
                            sender.sendMessage("§cError when downloading file.");
                            var24.printStackTrace();
                            return false;
                        }
                    }
                    break;
                case 1985623669:
                    if (var5.equals("setitem")) {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("§cYou have to be a player to do that.");
                            return false;
                        }

                        ItemStack is = ((Player)sender).getInventory().getItemInHand();
                        if (is != null && is.getType() != Material.AIR) {
                            JukeBox.getInstance().jukeboxItem = is;
                        } else {
                            JukeBox.getInstance().jukeboxItem = null;
                        }

                        sender.sendMessage("§aItem edited. Now : §2" + (JukeBox.getInstance().jukeboxItem == null ? "null" : JukeBox.getInstance().jukeboxItem.toString()));
                        return false;
                    }
                    break;
                case 2072332025:
                    if (var5.equals("shuffle")) {
                        if (args.length < 2) {
                            sender.sendMessage(Lang.INCORRECT_SYNTAX);
                            return false;
                        }

                        if (args[1].equals("@a")) {
                            var11 = Bukkit.getOnlinePlayers().iterator();

                            while(var11.hasNext()) {
                                p = (Player)var11.next();
                                sender.sendMessage(p.getName() + " : " + this.shuffle(p));
                            }

                            return false;
                        } else {
                            p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                sender.sendMessage("§cUnknown player.");
                                return false;
                            }

                            sender.sendMessage(this.shuffle(p));
                            return false;
                        }
                    }
            }

            sender.sendMessage(Lang.AVAILABLE_COMMANDS + " <reload|player|play|stop|setitem|download|shuffle|particles|join|random|volume> ...");
            return false;
        }
    }

    private String play(String[] args) {
        Player cp = Bukkit.getPlayer(args[1]);
        if (cp == null) {
            return "§cUnknown player.";
        } else if (JukeBox.worlds && JukeBox.worldsEnabled.contains(cp.getWorld())) {
            return "";
        } else {
            try {
                int i = Integer.parseInt(args[2]);

                Song song;
                try {
                    song = (Song)JukeBox.songs.get(i);
                } catch (IndexOutOfBoundsException var6) {
                    return "§cError on §l" + i + " §r§c(inexistant)";
                }

                PlayerData pdata = (PlayerData)PlayerData.players.get(cp.getUniqueId());
                pdata.playSong(song);
                pdata.songPlayer.adminPlayed = true;
                return "";
            } catch (NumberFormatException var7) {
                return Lang.INVALID_NUMBER;
            }
        }
    }

    private String stop(Player cp) {
        PlayerData pdata = (PlayerData)PlayerData.players.get(cp.getUniqueId());
        pdata.stopPlaying(true);
        return Lang.PLAYER_MUSIC_STOPPED + cp.getName();
    }

    private String shuffle(Player cp) {
        PlayerData pdata = (PlayerData)PlayerData.players.get(cp.getUniqueId());
        return "§aShuffle : " + pdata.setShuffle(!pdata.isShuffle());
    }

    private String join(Player cp) {
        PlayerData pdata = (PlayerData)PlayerData.players.get(cp.getUniqueId());
        return "§aJoin : " + pdata.setJoinMusic(!pdata.hasJoinMusic());
    }

    private String particles(Player cp) {
        PlayerData pdata = (PlayerData)PlayerData.players.get(cp.getUniqueId());
        return "§aParticles : " + pdata.setParticles(!pdata.hasParticles());
    }

    private String random(Player cp) {
        PlayerData pdata = (PlayerData)PlayerData.players.get(cp.getUniqueId());
        Song song = pdata.playRandom();
        return song == null ? "§aShuffle : §cnothing to play" : "§aShuffle : " + song.getTitle();
    }
}
