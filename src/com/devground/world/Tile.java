package com.devground.world;

public enum Tile {
    GRASS("grass"),
    STONE("stone");

    private String name;

    Tile(String name) {
        this.name = name;
    }

    public int getId() {
        return ordinal();
    }

    public String getName() {
        return name;
    }

    public static Tile getTile(int id) {
        return Tile.values()[id];
    }

    public static Tile getTile(String name) {
        return Tile.valueOf(name.toUpperCase());
    }
}
