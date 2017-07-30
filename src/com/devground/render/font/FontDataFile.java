package com.devground.render.font;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FontDataFile {
    String filename;
    HashMap<Character, FontData> fontData = new HashMap<>();

    public FontDataFile(String filename) {
        this.filename = filename;

        InputStream stream = getClass().getResourceAsStream("/resource/fonts/"+filename+"/"+filename+".fnt");
        String[] text = new BufferedReader(new InputStreamReader(stream)).lines().toArray(String[]::new);
        for(String str : text) {
            String[] splited = Arrays.stream(str.split(" ")).filter(line -> !line.equals("")).toArray(String[]::new);
            if(splited.length < 2) continue;

            //char id=32      x=0    y=0    width=0    height=0    xoffset=-3   yoffset=31   xadvance=14   page=0    chnl=0
            if(splited[0].equalsIgnoreCase("char")) {
                int[] temp = new int[10];
                List<Integer> elements = Arrays.stream(splited).filter(line -> line.split("=").length > 1).map(s -> Integer.parseInt(s.split("=")[1])).collect(Collectors.toList());
                fontData.put((char) elements.get(0).intValue(), new FontData(elements));
            }
        }
    }

    public FontData get(char ch) {
        return fontData.get(ch);
    }
}