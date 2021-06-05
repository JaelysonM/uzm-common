package com.uzm.common.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SchematicController {
    private short[] blocks;
    private byte[] data;
    private short width;
    private short lenght;
    private short height;


    public SchematicController(short[] blocks, byte[] data, short width, short lenght, short height) {
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.lenght = lenght;
        this.height = height;
    }

    public short[] getBlocks() {
        return this.blocks;
    }

    public byte[] getData() {
        return this.data;
    }

    public short getWidth() {
        return this.width;
    }

    public short getLenght() {
        return this.lenght;
    }

    public short getHeight() {
        return this.height;
    }

    public List<Block> build(Location location) {
        List<Block> buildBlocks = new LinkedList<>();
        short[] blocks = getBlocks();
        byte[] blockData = getData();

        short length = getLenght();
        short width = getWidth();
        short height = getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int i = y * width * length + z * width + x;
                    Block block = location.clone().add(x, y, z).getBlock();
                    block.setTypeIdAndData(getBlocks()[i], blockData[i], true);
                    buildBlocks.add(block);
                }
            }
        }
        return buildBlocks;
    }

    public static SchematicController loadSchematic(Plugin instance, String name, String dir) {
        try {
            FileInputStream stream = new FileInputStream(new File(instance.getDataFolder() + "\\" + dir, name + ".schematic"));
            NBTInputStream nbtInputStream = new NBTInputStream(stream);

            CompoundTag schematicTag = (CompoundTag) nbtInputStream.readTag();

            if (!schematicTag.getName().equalsIgnoreCase("schematic")) {
                Bukkit.getServer().getLogger().log(Level.SEVERE, "Tag \"Schematic\" does not exist or is not first", new IllegalArgumentException());
                return null;
            }

            Map<String, Tag> schematic = schematicTag.getValue();

            if (!schematic.containsKey("Blocks")) {
                Bukkit.getServer().getLogger().log(Level.SEVERE, "Schematic file is missing a \"Blocks\" tag", new IllegalArgumentException());
                return null;
            }

            short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
            short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
            short height = getChildTag(schematic, "Height", ShortTag.class).getValue();

            byte[] blockId = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
            byte[] blockData = (getChildTag(schematic, "Data", ByteArrayTag.class)).getValue();

            byte[] addId = schematic.containsKey("AddBlocks") ? getChildTag(schematic, "AddBlocks", ByteArrayTag.class).getValue() : new byte[0];

            short[] blocks = new short[blockId.length];

            for (int index = 0; index < blockId.length; index++) {
                if (index >> 1 >= addId.length) {
                    blocks[index] = ((short) (blockId[index] & 0xFF));
                } else if ((index & 0x1) == 0) {
                    blocks[index] = ((short) (((addId[(index >> 1)] & 0xF) << 8) + (blockId[index] & 0xFF)));
                } else {
                    blocks[index] = ((short) (((addId[(index >> 1)] & 0xF0) << 4) + (blockId[index] & 0xFF)));
                }
            }
            return new SchematicController(blocks, blockData, width, length, height);
        } catch (IOException err) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "Um erro inesperado ao tentar carregar a schematic \"" + name + "\": ", err);
            return null;
        }

    }

    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected)
            throws IllegalArgumentException {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }

}
