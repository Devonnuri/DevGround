package com.devground.world;

import com.devground.collision.CollisionBox;
import com.devground.entity.Entity;
import com.devground.entity.Player;
import com.devground.exception.GameException;
import com.devground.Window;
import com.devground.render.Camera;
import com.devground.render.Shader;
import com.devground.utils.NullCheck;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class World {
    private String name;
    public int width, height;
    private int scale;
    private Matrix4f world;

    public int[] tiles;
    public boolean[] tilesSolid;

    private CollisionBox[] collisionBoxes;
    private ArrayList<Entity> entities;

    private final int viewRange = 60;

    public World(String name) {
        this.name = name;

        File file = new File("./maps/"+name+".xml");
        Document doc;

        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        } catch (Exception e) {
            throw new GameException("맵 \"%s\"를 로딩하는 중 오류가 발생하였습니다. (%s)", name, e.getClass().getName());
        }

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        if(root.getNodeName().equalsIgnoreCase("world")) {
            this.name = (String) NullCheck.setDefault(root.getAttribute("name"), this.name);
            this.width = Integer.parseInt((String) NullCheck.setDefault(root.getAttribute("width"), this.width));
            this.height = Integer.parseInt((String) NullCheck.setDefault(root.getAttribute("height"), this.height));
            this.scale = Integer.parseInt((String) NullCheck.setDefault(root.getAttribute("scale"), this.scale));

            this.tiles = new int[width*height];
            this.tilesSolid = new boolean[width*height];
            this.collisionBoxes = new CollisionBox[width * height];
            this.world = new Matrix4f()
                    .setTranslation(new Vector3f(0))
                    .scale(scale);
            this.entities = new ArrayList<>();

            if(!NullCheck.isNull(root.getAttribute("fill")))
                fillTile(Tile.getTile(root.getAttribute("fill")));
            else
                fillTile(Tile.GRASS);

            NodeList nodeList = root.getElementsByTagName("tile");
            for(int i=0; i<nodeList.getLength(); i++) {
                if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element tile = (Element) nodeList.item(i);

                    String x = tile.getAttribute("x");
                    String y = tile.getAttribute("y");
                    String material = tile.getAttribute("material");
                    String solid = tile.getAttribute("solid");

                    if(NullCheck.isNull(x) || NullCheck.isNull(y)) continue;

                    material = (String) NullCheck.setDefault(material, "grass");
                    solid = (String) NullCheck.setDefault(solid, "false");

                    setTile(Integer.parseInt(x),
                            Integer.parseInt(y),
                            Tile.getTile(material),
                            Boolean.parseBoolean(solid));
                }
            }
        } else {
            throw new GameException("맵의 Root인 world 태그가 없습니다.");
        }

        this.entities.add(new Player(this));
    }

    public void render(TileRenderer renderer, Window window, Shader shader, Camera camera) {
        int posX = ((int) camera.getPosition().x + (window.getWidth()/2)) / (scale * 2);
        int posY = ((int) camera.getPosition().y - (window.getHeight()/2)) / (scale * 2);

        for(int x = 0; x < viewRange; x++) {
            for(int y = 0; y < viewRange; y++) {
                if(y+posY >= height || x-posX >= width) continue;

                Tile tile = getTile(x-posX, y+posY);

                if(tile != null)
                    renderer.render(tile, x-posX, -y-posY, shader, world, camera);
            }
        }

        for(Entity entity : entities) {
            entity.render(this, shader, camera);
        }
    }

    public void update(double fps, Window window, Camera camera) {
        for(Entity entity : entities) {
            entity.update(fps, window, camera);
        }
    }

    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int winW = window.getWidth();
        int winH = window.getHeight();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        if(pos.x > -(winW/2) + scale)      // 왼쪽면 넘어가는 것 방지
            pos.x = -(winW/2) + scale;
        if(pos.x < w + (winW/2) + scale)  // 오른쪽면 넘어가는 것 방지
            pos.x = w + (winW/2) + scale;

        if(pos.y < (winH/2) - scale)        // 윗면 넘어가는 것 방지
            pos.y = (winH/2) - scale;
        if(pos.y > h - (winH/2) - scale)    // 아랫면 넘어가는 것 방지
            pos.y = h - (winH/2) - scale;
    }

    public void setTile(int index, Tile material) {
        setTile(index, material, false);
    }

    public void setTile(int x, int y, Tile material) {
        setTile(x, y, material, false);
    }

    public void setTile(int x, int y, Tile material, boolean solid) {
        setTile(y*width + x, material, solid);
    }

    public void setTile(int index, Tile material, boolean solid) {
        tiles[index] = material.getId();

        if(solid) {
            int x = index % width;
            int y = index / width;
            collisionBoxes[index] = new CollisionBox(new Vector2f(x*2, -y*2), new Vector2f(1, 1));
        } else {
            collisionBoxes[index] = null;
        }
    }

    public void fillTile(Tile material) {
        fillTile(material, false);
    }

    public void fillTile(Tile material, boolean solid) {
        for(int i=0; i<width*height; i++) {
            setTile(i, material, solid);
        }
    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.getTile(tiles[y*width + x]);
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new GameException("getTile(%d, %d) 함수가 tiles 배열의 밖을 참조하였습니다.", x, y);
        }
    }

    public CollisionBox getCollisionBox(int x, int y) {
        try {
            return collisionBoxes[y*width + x];
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new GameException("getCollisionBox(%d, %d) 함수가 collisionBoxes 배열의 밖을 참조하였습니다.", x, y);
        }
    }


    public String getName() {
        return name;
    }

    public int getScale() {
        return scale;
    }

    public Matrix4f getWorldMatrix() {
        return world;
    }
}
