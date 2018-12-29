//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.skytasul.music.utils;

import java.util.Random;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Particles {
    private static Random ran = new Random();

    public Particles() {
    }

    public static void sendParticles(Player p) {
        p.spawnParticle(Particle.NOTE, p.getEyeLocation().add(p.getLocation().getDirection().multiply(2)), 1, (double)(ran.nextInt(24) / 24), 0.5D, 0.1D, 1.0D, (Object)null);
    }
}
