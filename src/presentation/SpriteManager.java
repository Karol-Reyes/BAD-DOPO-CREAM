package presentation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Clase responsable de gestionar y cargar los sprites utilizados en el juego.
 */
public class SpriteManager {

    private static final int TILE = 32;
    private final Map<String, Image> sprites = new HashMap<>();

    /**
     * Constructor de la clase SpriteManager.
     * Carga todos los sprites al inicializar la instancia.
     */
    public SpriteManager() {
        loadAll();
    }

    /**
     * Carga todos los sprites necesarios para el juego.
     */
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

        sprites.put("bonfire_inactive", load("/Resources/box/bonfire_off.png"));
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

        sprites.put("Vanilla_dead", load("/Resources/user/vainilla/dead.png"));
        sprites.put("Vanilla_broke", load("/Resources/user/vainilla/broke.png"));
        sprites.put("Vanilla_static_down", load("/Resources/user/vainilla/static_down.png"));
        sprites.put("Vanilla_static_up", load("/Resources/user/vainilla/static_up.png"));
        sprites.put("Vanilla_static_left", load("/Resources/user/vainilla/static_left.png"));
        sprites.put("Vanilla_static_right", load("/Resources/user/vainilla/static_right.png"));
        sprites.put("Vanilla_froze_down", load("/Resources/user/vainilla/froze_down.png"));
        sprites.put("Vanilla_froze_up", load("/Resources/user/vainilla/froze_up.png"));
        sprites.put("Vanilla_froze_left", load("/Resources/user/vainilla/froze_left.png"));
        sprites.put("Vanilla_froze_right", load("/Resources/user/vainilla/froze_right.png"));

        // JUGADORES

        sprites.put("Chocolate_dead", load("/Resources/user/chocolate/dead.png"));
        sprites.put("Chocolate_broke", load("/Resources/user/chocolate/broke.png"));
        sprites.put("Chocolate_static_down", load("/Resources/user/chocolate/static_down.png"));
        sprites.put("Chocolate_static_up", load("/Resources/user/chocolate/static_up.png"));
        sprites.put("Chocolate_static_left", load("/Resources/user/chocolate/static_left.png"));
        sprites.put("Chocolate_static_right", load("/Resources/user/chocolate/static_right.png"));
        sprites.put("Chocolate_froze_down", load("/Resources/user/chocolate/froze_down.png"));
        sprites.put("Chocolate_froze_up", load("/Resources/user/chocolate/froze_up.png"));
        sprites.put("Chocolate_froze_left", load("/Resources/user/chocolate/froze_left.png"));
        sprites.put("Chocolate_froze_right", load("/Resources/user/chocolate/froze_right.png"));

        sprites.put("Strawberry_dead", load("/Resources/user/strawberry/dead.png"));
        sprites.put("Strawberry_broke", load("/Resources/user/strawberry/broke.png"));
        sprites.put("Strawberry_static_down", load("/Resources/user/strawberry/static_down.png"));
        sprites.put("Strawberry_static_up", load("/Resources/user/strawberry/static_up.png"));
        sprites.put("Strawberry_static_left", load("/Resources/user/strawberry/static_left.png"));
        sprites.put("Strawberry_static_right", load("/Resources/user/strawberry/static_right.png"));
        sprites.put("Strawberry_froze_down", load("/Resources/user/strawberry/froze_down.png"));
        sprites.put("Strawberry_froze_up", load("/Resources/user/strawberry/froze_up.png"));
        sprites.put("Strawberry_froze_left", load("/Resources/user/strawberry/froze_left.png"));
        sprites.put("Strawberry_froze_right", load("/Resources/user/strawberry/froze_right.png"));

        sprites.put("Vanilla_dead", load("/Resources/user/vainilla/dead.png"));
        sprites.put("Vanilla_broke", load("/Resources/user/vainilla/broke.png"));
        sprites.put("Vanilla_static_down", load("/Resources/user/vainilla/static_down.png"));
        sprites.put("Vanilla_static_up", load("/Resources/user/vainilla/static_up.png"));
        sprites.put("Vanilla_static_left", load("/Resources/user/vainilla/static_left.png"));
        sprites.put("Vanilla_static_right", load("/Resources/user/vainilla/static_right.png"));
        sprites.put("Vanilla_froze_down", load("/Resources/user/vainilla/froze_down.png"));
        sprites.put("Vanilla_froze_up", load("/Resources/user/vainilla/froze_up.png"));
        sprites.put("Vanilla_froze_left", load("/Resources/user/vainilla/froze_left.png"));
        sprites.put("Vanilla_froze_right", load("/Resources/user/vainilla/froze_right.png"));

        sprites.put("Hungry_dead", load("/Resources/user/hungry/dead.png"));
        sprites.put("Hungry_broke", load("/Resources/user/hungry/broke.png"));
        sprites.put("Hungry_static_down", load("/Resources/user/hungry/static_down.png"));
        sprites.put("Hungry_static_up", load("/Resources/user/hungry/static_up.png"));
        sprites.put("Hungry_static_left", load("/Resources/user/hungry/static_left.png"));
        sprites.put("Hungry_static_right", load("/Resources/user/hungry/static_right.png"));
        sprites.put("Hungry_froze_down", load("/Resources/user/hungry/froze_down.png"));
        sprites.put("Hungry_froze_up", load("/Resources/user/hungry/froze_up.png"));
        sprites.put("Hungry_froze_left", load("/Resources/user/hungry/froze_left.png"));
        sprites.put("Hungry_froze_right", load("/Resources/user/hungry/froze_right.png"));
        
        sprites.put("Fearful_dead", load("/Resources/user/fearful/dead.png"));
        sprites.put("Fearful_broke", load("/Resources/user/fearful/broke.png"));
        sprites.put("Fearful_static_down", load("/Resources/user/fearful/static_down.png"));
        sprites.put("Fearful_static_up", load("/Resources/user/fearful/static_up.png"));
        sprites.put("Fearful_static_left", load("/Resources/user/fearful/static_left.png"));
        sprites.put("Fearful_static_right", load("/Resources/user/fearful/static_right.png"));
        sprites.put("Fearful_froze_down", load("/Resources/user/fearful/froze_down.png"));
        sprites.put("Fearful_froze_up", load("/Resources/user/fearful/froze_up.png"));
        sprites.put("Fearful_froze_left", load("/Resources/user/fearful/froze_left.png"));
        sprites.put("Fearful_froze_right", load("/Resources/user/fearful/froze_right.png"));

        sprites.put("Expert_dead", load("/Resources/user/expert/dead.png"));
        sprites.put("Expert_broke", load("/Resources/user/expert/broke.png"));
        sprites.put("Expert_static_down", load("/Resources/user/expert/static_down.png"));
        sprites.put("Expert_static_up", load("/Resources/user/expert/static_up.png"));
        sprites.put("Expert_static_left", load("/Resources/user/expert/static_left.png"));
        sprites.put("Expert_static_right", load("/Resources/user/expert/static_right.png"));
        sprites.put("Expert_froze_down", load("/Resources/user/expert/froze_down.png"));
        sprites.put("Expert_froze_up", load("/Resources/user/expert/froze_up.png"));
        sprites.put("Expert_froze_left", load("/Resources/user/expert/froze_left.png"));
        sprites.put("Expert_froze_right", load("/Resources/user/expert/froze_right.png"));

        // Cuadro Azul
        sprites.put("fruit_indicator", load("/Resources/game/CuadroEleccion.png"));
    }

    // ==========================================================
    /**
     * Carga una imagen desde la ruta especificada.
     */
    private Image load(String path) {
        URL url = getClass().getResource(path);

        if (url == null) {
            return placeholder();
        }

        return new ImageIcon(url).getImage();
    }

    /**
     * Genera una imagen de marcador de posición en caso de que no se pueda cargar la imagen real.
     */
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

    /**
     * Obtiene la imagen del sprite correspondiente a la clave dada.
     * @param key Clave del sprite.
     * @return Imagen del sprite.
     */
    public Image get(String key) {
        return sprites.get(key);
    }
}