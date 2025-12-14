package presentation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class SpriteManager {

    private static final int TILE = 32;

    private final Map<String, Image> sprites = new HashMap<>();

    public SpriteManager() {
        loadAll();
    }

    private void loadAll() {
        // BLOQUES 

        sprites.put("floor_inactive", load("/Resources/inicio/suelo.jpg"));
        sprites.put("floor_created", load("/Resources/inicio/suelo.jpg"));

        sprites.put("ice_inactive", load("/Resources/box/ice.png"));
        sprites.put("ice_created", load("/Resources/box/ice.png"));

        sprites.put("iron_inactive", load("/Resources/box/block.png"));
        sprites.put("iron_created", load("/Resources/box/block.png"));

        sprites.put("fire_inactive", load("/Resources/box/fire.png"));
        sprites.put("fire_created", load("/Resources/box/fire.png"));

        sprites.put("bonfire_inactive", load("/Resources/box/bonfire.png"));
        sprites.put("bonfire_created", load("/Resources/box/bonfire.png"));

        // FRUTAS 
        sprites.put("banana", load("/Resources/fruit/Banana.jpg"));
        sprites.put("grape", load("/Resources/fruit/Grape.jpg"));
        sprites.put("cherry", load("/Resources/fruit/Cherry.jpg"));
        sprites.put("pineapple", load("/Resources/fruit/Pineapple.jpg"));
        sprites.put("cactus", load("/Resources/fruit/cactus.png"));
        sprites.put("cactus_thorns", load("/Resources/fruit/cactusPuas.png"));

        // ENEMIGOS 
        sprites.put("flowerpot", load("/Resources/enemy/flowerpot/flowerpot.jpg"));
        sprites.put("flowerpot_on", load("/Resources/enemy/flowerpot/flowerpot_on.png"));
        sprites.put("enemy_narval", load("/Resources/enemy/narwhal.png"));
        sprites.put("enemy_default", load("/Resources/enemy/troll/static_down.png"));
        sprites.put("enemy_squid", load("/Resources/enemy/yellow_squid/static_down.png"));

        // JUGADOR DEFAULT 

        sprites.put("player_alive", load("/Resources/user/vainilla/static_down.png"));
        sprites.put("player_dead", load("/Resources/user/vainilla/dead.png"));

        // JUGADORES

        sprites.put("Chocolate_alive", load("/Resources/user/chocolate/static_down.png"));
        sprites.put("Chocolate_dead", load("/Resources/user/chocolate/dead.png"));

        sprites.put("Strawberry_alive", load("/Resources/user/strawberry/static_down.png"));
        sprites.put("Strawberry_dead", load("/Resources/user/strawberry/dead.png"));

        sprites.put("Vanilla_alive", load("/Resources/user/vainilla/static_down.png"));
        sprites.put("Vanilla_dead", load("/Resources/user/vainilla/dead.png"));

        sprites.put("Hungry_alive", load("/Resources/user/hungry/static_down.png"));
        sprites.put("Hungry_dead", load("/Resources/user/hungry/dead.png"));

        sprites.put("Fearful_alive", load("/Resources/user/fearful/static_down.png"));
        sprites.put("Fearful_dead", load("/Resources/user/fearful/dead.png"));

        sprites.put("Expert_alive", load("/Resources/user/expert/static_down.png"));
        sprites.put("Expert_dead", load("/Resources/user/expert/dead.png"));
    }

    // ==========================================================
    private Image load(String path) {
        URL url = getClass().getResource(path);

        if (url == null) {
            return placeholder();
        }

        return new ImageIcon(url).getImage();
    }

    private Image placeholder() {
        BufferedImage img = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setColor(new Color(200, 0, 0, 180));
        g.fillRect(0, 0, TILE, TILE);

        g.setColor(Color.WHITE);
        g.drawLine(0, 0, TILE, TILE);
        g.drawLine(TILE, 0, 0, TILE);

        g.dispose();
        return img;
    }

    public Image get(String key) {
        return sprites.get(key);
    }
}