package com.uzm.common.libraries.holograms.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

public class Hologram {

    private boolean spawned;
    private Location location;
    private Map<Integer, HologramLine> lines = new HashMap<>();

    private List<String> defaultLines = Lists.newArrayList();

    public Hologram(Location location, String... lines) {
        this.location = location;

        int current = 0;
        for (String line : lines) {
            this.lines.put(++current,
                    new HologramLine(this, location.clone().add(0, 0.33 * current, 0), line));
        }
    }

    public Hologram spawn() {
        if (spawned) {
            return this;
        }

        lines.values().forEach(HologramLine::spawn);
        this.spawned = true;
        return this;
    }

    public Hologram despawn() {
        if (!spawned) {
            return this;
        }

        lines.values().forEach(HologramLine::despawn);
        this.spawned = false;
        return this;
    }

    public Hologram withLine(String line) {
        int l = 1;
        while (lines.containsKey(l)) {
            l++;
        }

        this.lines.put(l, new HologramLine(this, location.clone().add(0, 0.33 * l, 0), line));
        if (spawned) {
            lines.get(l).spawn();
        }

        return this;
    }

    public Hologram updateLine(int id, String line) {
        if (!lines.containsKey(id)) {
            return this;
        }

        HologramLine hl = lines.get(id);
        hl.setLine(line);
        return this;
    }


    public void setDefaultLines(List<String> l) {
        defaultLines = l;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public Location getLocation() {
        return location;
    }

    public List<HologramLine> getLines() {
        return ImmutableList.copyOf(lines.values());
    }

    public List<String> getDefaultLines() {
        return defaultLines;
    }
}