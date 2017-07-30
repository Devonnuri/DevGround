package com.devground.render.font;

public class FontSheet {
    FontDataFile dataFile;

    public FontSheet(String filename, int pageCount) {
        dataFile = new FontDataFile(filename);

    }
}
