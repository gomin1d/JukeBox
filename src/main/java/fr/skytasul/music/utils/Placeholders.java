//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music.utils;

import fr.skytasul.music.PlayerData;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;

public class Placeholders {
    public Placeholders() {
    }

    public static void registerPlaceholders() {
        PlaceholderAPI.registerPlaceholderHook("JukeBox", new PlaceholderHook() {
            public String onPlaceholderRequest(Player p, String params) {
                PlayerData pdata = (PlayerData)PlayerData.players.get(p.getUniqueId());
                if (pdata == null) {
                    return "none";
                } else if (params.startsWith("playeroptions_")) {
                    String var4;
                    switch((var4 = params.split("_")[1]).hashCode()) {
                        case -1800314195:
                            if (var4.equals("particles")) {
                                return pdata.hasParticles() ? "yes" : "no";
                            }
                            break;
                        case -810883302:
                            if (var4.equals("volume")) {
                                return pdata.getVolume() + "%";
                            }
                            break;
                        case 3267882:
                            if (var4.equals("join")) {
                                return pdata.hasJoinMusic() ? "yes" : "no";
                            }
                            break;
                        case 2072332025:
                            if (var4.equals("shuffle")) {
                                return pdata.isShuffle() ? "yes" : "no";
                            }
                    }

                    return "§c§lunknown option";
                } else if (params.equals("active")) {
                    if (pdata.songPlayer == null) {
                        return "§cnone";
                    } else {
                        String name = pdata.songPlayer.getSong().getTitle();
                        return name.isEmpty() ? pdata.songPlayer.getSong().getPath().getName() : name;
                    }
                } else {
                    return null;
                }
            }
        });
    }
}
