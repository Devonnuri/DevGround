package com.devground.render.font;

import java.util.List;

public class FontData {
    public char ch;
    public int x;
    public int y;
    public int width;
    public int height;
    public int xOffset;
    public int yOffset;
    public int xAdvance;
    public int page;

    public FontData(char ch, int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance, int page) {
        this.ch = ch;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
        this.page = page;
    }

    public FontData(List<Integer> data) {
        this((char) data.get(0).intValue(), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5), data.get(6), data.get(7), data.get(8));
    }

    @Override
    public String toString() {
        return String.format("%c(pos=[%d, %d], size=[%d, %d], offset=[%d, %d], xAdvance=%d, page=%d)", ch, x, y, width, height, xOffset, yOffset, xAdvance, page);
    }
}
