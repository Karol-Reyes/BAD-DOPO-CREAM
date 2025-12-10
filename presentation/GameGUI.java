package  presentation;

import domain.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GameGUI extends JPanel {

    private BadIceCream game;
    private GamePanel panel;
    private Timer timer;
    private GameControl gameControl;

    /**
     * Constructor que recibe la configuración del juego
     */
    public GameGUI(GameControl gameControl) {
        this.gameControl = gameControl;
        setLayout(new BorderLayout());
        
        // Imprimir la configuración seleccionada
        gameControl.printSelections();
        
        // Cargar el nivel según la configuración
        loadGame();

        // Crear el panel de visualización
        panel = new GamePanel(game);
        add(panel, BorderLayout.CENTER);

        // Configurar controles
        setupKeyBindings();

        // Iniciar el timer del juego
        timer = new Timer(150, e -> {
            if (!game.isGameWon() && !game.isGameLost()) {
                game.updateGame();
            }
            panel.repaint();
        });
        timer.start();
    }

    // =============== CARGAR NIVEL =================
    // =============== CARGAR NIVEL =================
    private void loadGame() {
        System.out.println("\n🔄 Iniciando carga del juego...");
        
        // Convertir GameControl (presentation) a GameConfig (domain)
        GameConfig config = gameControl.toGameConfig();
        
        // Cargar el nivel completo usando LevelLoader
        game = LevelLoader.cargarNivelCompleto(config.getLevel(), config);
        
        // Verificar si se cargó correctamente
        if (game == null) {
            System.err.println("⚠️ Error al cargar el nivel configurado");
            System.err.println("🔄 Cargando nivel por defecto (LevelOne)...");
            game = LevelOne.createLevel();
        }
        
        // Configurar el mapa para cada jugador
        for (IceCream p : game.getPlayers()) {
            p.setGameMap(game.getMap());
        }
        
        // IMPORTANTE: Actualizar la referencia del juego en el panel
        if (panel != null) {
            panel.setGame(game);
        }
        
        System.out.println("✅ Juego reiniciado!\n");
    

        
        // Configurar el mapa para cada jugador
        for (IceCream p : game.getPlayers()) {
            p.setGameMap(game.getMap());
        }
        
        System.out.println("✅ Juego listo para empezar!\n");
    }

    // =============== TECLAS ==========================
    private void setupKeyBindings() {
        InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        // Controles del jugador 1
        bind(im, am, "UP", KeyEvent.VK_UP, () -> game.movePlayer(0, Direction.UP));
        bind(im, am, "DOWN", KeyEvent.VK_DOWN, () -> game.movePlayer(0, Direction.DOWN));
        bind(im, am, "LEFT", KeyEvent.VK_LEFT, () -> game.movePlayer(0, Direction.LEFT));
        bind(im, am, "RIGHT", KeyEvent.VK_RIGHT, () -> game.movePlayer(0, Direction.RIGHT));

        bind(im, am, "FREEZE", KeyEvent.VK_SPACE, () -> game.playerCreateIce(0));
        bind(im, am, "BREAK", KeyEvent.VK_X, () -> game.playerDestroyIce(0));
        
        // Reiniciar el juego
        bind(im, am, "RESET", KeyEvent.VK_R, this::loadGame);

    }

    private void bind(InputMap im, ActionMap am, String name, int key, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                action.run(); 
                panel.repaint(); // Actualizar la visualización
            }
        });
    }


    // ======================== PANEL INTERNO ==============================
private static class GamePanel extends JPanel {

    private BadIceCream game;
    private static final int TILE = 32;

    private final Map<String, Image> sprites = new HashMap<>();

    public GamePanel(BadIceCream game) {
        this.game = game;
        setBackground(Color.WHITE);

        loadSprites();
    }

    /**
     * Actualiza la referencia al juego (usado para reiniciar)
     */
    public void setGame(BadIceCream newGame) {
        this.game = newGame;
        repaint();
    }

    

    // ==========================================================
    // CARGAR TODOS LOS SPRITES
    // ==========================================================
    private void loadSprites() {

        // BLOQUES - Según lo que devuelve getSpriteKey()
        // Floor (suelo)
        sprites.put("floor_inactive", load("/Resources/inicio/suelo.jpg")); 
        sprites.put("floor_created", load("/Resources/inicio/suelo.jpg"));
        
        // Ice (hielo)
        sprites.put("ice_inactive", load("/Resources/box/ice.png"));
        sprites.put("ice_created", load("/Resources/box/ice.png"));
        
        // Iron (bloque indestructible)
        sprites.put("iron_inactive", load("/Resources/box/block.png"));
        sprites.put("iron_created", load("/Resources/box/block.png"));
        
        // Fire
        sprites.put("fire_inactive", load("/Resources/box/fire.png"));
        sprites.put("fire_created", load("/Resources/box/fire.png"));
        
        // Bonfire
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
        sprites.put("enemy_flowerpot", load("/Resources/enemy/flowerpot/flowerpot.jpg"));
        sprites.put("enemy_flowerpot_charge", load("/Resources/enemy/flowerpot/flowerpot_on.png"));
        sprites.put("enemy_narval", load("/Resources/enemy/narwhal.png"));
        sprites.put("enemy_default", load("/Resources/enemy/troll/static_down.png"));

        // JUGADORES
        sprites.put("player_alive", load("/Resources/user/vainilla/static_down.png"));
        sprites.put("player_dead", load("/Resources/user/vainilla/dead.gif"));
}

    // Sustituye tu método load actual por este (dentro de GamePanel)
    private Image load(String path) {
        // Usa getResource (ruta desde classpath). Ej: "/Resources/fruit/Banana.png"
        URL url = getClass().getResource(path);

        if (url == null) {
            // Mensaje claro para depuración (verás qué ruta falta)
            System.err.println("NO SE ENCONTRÓ: " + path);

            // Retorna una imagen placeholder (para que la app no explote y se vea algo)
            return createPlaceholderImage();
        }

        return new ImageIcon(url).getImage();
    }

    // Método helper que crea una imagen simple cuando falta el sprite.
    // Devuelve un BufferedImage pequeña (TILE x TILE) con un patrón visible.
    private Image createPlaceholderImage() {
        int size = TILE; // usa tu constante TILE = 32
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        // Fondo semitransparente
        g2.setColor(new Color(200, 0, 0, 180));
        g2.fillRect(0, 0, size, size);

        // Cruz para indicar "falta imagen"
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.WHITE);
        g2.drawLine(4, 4, size - 4, size - 4);
        g2.drawLine(size - 4, 4, 4, size - 4);

        g2.dispose();
        return img;
    }

    // ==========================================================
    // PINTAR TODO - VERSIÓN CORREGIDA
    // ==========================================================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        GameMap map = game.getMap();

        // ---------------- MAPA (BLOQUES) ----------------
        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Position pos = new Position(r, c);
                Boxy b = map.getBlock(pos);

                Image img = null;
                
                // Determinar qué sprite usar según el tipo de bloque
                if (b != null) {
                    // Usar un método getSpriteKey() similar a como lo hacen las frutas
                    String spriteKey = b.getSpriteKey(); // Debes agregar este método a Boxy
                    img = sprites.get(spriteKey);
                    
                    // Fallback por si el sprite no existe
                    if (img == null) {
                        img = sprites.get("block");
                    }
                } else {
                    // Si no hay bloque (null), es espacio vacío - no dibujar nada
                    continue; // Salta al siguiente, deja el fondo negro
                }

                // Dibujar el sprite correspondiente
                if (img != null) {
                    g.drawImage(img, 
                        c * TILE, r * TILE, 
                        TILE, TILE, 
                        null
                    );
                }
            }
        }

        // ---------------- FRUTAS ----------------
        for (Fruit f : game.getFruits()) {
            if (!f.isEaten()) {
                String key = f.getSpriteKey();
                Image img = sprites.get(key);
                Position p = f.getPosition();

                g.drawImage(img,
                    p.getCol() * TILE,
                    p.getRow() * TILE,
                    TILE, TILE,
                    null
                );
            }
        }

        // ---------------- ENEMIGOS ----------------
        for (Enemy e : game.getEnemies()) {
            Position p = e.getPosition();
            int x = p.getCol() * TILE;
            int y = p.getRow() * TILE;

            String key;

            switch (e.getType()) {
                case flowerpot -> {
                    Flowerpot fp = (Flowerpot) e;
                    key = fp.isCharging() ? "enemy_flowerpot_charge" : "enemy_flowerpot";
                }
                case narval -> key = "enemy_narval";
                default -> key = "enemy_default";
            }

            g.drawImage(sprites.get(key), x, y, TILE, TILE, null);
        }

        // ---------------- JUGADORES ----------------
        for (IceCream pl : game.getPlayers()) {
            String key = pl.isAlive() ? "player_alive" : "player_dead";
            Image img = sprites.get(key);
            Position pos = pl.getPosition();

            g.drawImage(img,
                pos.getCol() * TILE,
                pos.getRow() * TILE,
                TILE, TILE,
                null
            );
            }
        }
    }
}