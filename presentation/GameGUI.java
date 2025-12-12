package  presentation;

import domain.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameGUI extends JPanel {

    private BadIceCream game;
    private GamePanel panel;
    private Timer timer;
    private GameControl gameControl;
    private SpriteManager spriteManager;  
    
    private JLabel pauseOverlay;
    private boolean isPaused = false;



    /**
     * Constructor que recibe la configuración del juego
     */
    public GameGUI(GameControl gameControl) {
        this.gameControl = gameControl;
        setLayout(new BorderLayout());

        // Maneja sprites una sola vez
        spriteManager = new SpriteManager();

        // Imprimir la configuración seleccionada
        gameControl.printSelections();

        // Cargar el nivel según la configuración
        loadGame();

        // Crear el panel de visualización con SpriteManager
        panel = new GamePanel(game, spriteManager);
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


    // CARGAR NIVEL 

    private void loadGame() {

        GameConfig config = gameControl.toGameConfig();
        game = LevelLoader.cargarNivelCompleto(config.getLevel(), config);

        for (IceCream p : game.getPlayers()) {
            p.setGameMap(game.getMap());
        }

        if (panel != null) {
            panel.setGame(game);
        }
    }

    // =====================================================
    // TECLAS
    // =====================================================
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

        // controles del jugador 2
        bind(im, am, "P2_UP", KeyEvent.VK_W, () -> game.movePlayer(1, Direction.UP));
        bind(im, am, "P2_DOWN", KeyEvent.VK_S, () -> game.movePlayer(1, Direction.DOWN));
        bind(im, am, "P2_LEFT", KeyEvent.VK_A, () -> game.movePlayer(1, Direction.LEFT));
        bind(im, am, "P2_RIGHT", KeyEvent.VK_D, () -> game.movePlayer(1, Direction.RIGHT));

        bind(im, am, "P2_FREEZE", KeyEvent.VK_F, () -> game.playerCreateIce(1));
        bind(im, am, "P2_BREAK", KeyEvent.VK_G, () -> game.playerDestroyIce(1));
        
        // controles generales de la partida
        bind(im, am, "RESET", KeyEvent.VK_R, this::loadGame);
        bind(im, am, "PAUSE", KeyEvent.VK_P, this::togglePause);

    }

    private void bind(InputMap im, ActionMap am, String name, int key, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
                panel.repaint();
            }
        });
    }

    /* pausa o reanuda el juego */
    private void togglePause() {
        isPaused = !isPaused;

        game.setPaused(isPaused);

        if (isPaused) {
            timer.stop();              // detener repintado
            showPauseOverlay();
        } else {
            hidePauseOverlay();
            timer.start();
        }
    }

    private void showPauseOverlay() {
        pauseOverlay = new JLabel();
        pauseBackground();
        buttonContinue();
        buttonExit();
    }

    private void pauseBackground() {
        pauseOverlay.setBounds(0, 0, getWidth(), getHeight());
        pauseOverlay.setLayout(null);
        pauseOverlay.setOpaque(true);
        pauseOverlay.setBackground(new Color(0, 0, 0, 150));

        JLabel backgroundLabel = ImageUtils.createScaledImageLabel("/Resources/inicio/fondo_opciones.png",
        400, 250, 130, 180);
        pauseOverlay.add(backgroundLabel);
        pauseOverlay.revalidate();
        pauseOverlay.repaint();
    }
    
    private void buttonContinue() {
        PixelButton btnContinue = new PixelButton("/Resources/textos/Continuar.png",
         150, 70);
        btnContinue.setBounds(220, 230, 150, 70);
        btnContinue.addActionListener(e -> togglePause());
        pauseOverlay.add(btnContinue);
        pauseOverlay.setComponentZOrder(btnContinue, 0);
        pauseOverlay.revalidate();
        pauseOverlay.repaint();
    }
    
    private void buttonExit() {
        PixelButton btnExit = new PixelButton("/Resources/textos/Salir.png", 100, 50);
        btnExit.setBounds(290, 360, 100, 50);
        btnExit.addActionListener(e -> exitToLevelSelection());
        pauseOverlay.add(btnExit);
        pauseOverlay.setComponentZOrder(btnExit, 0);
        pauseOverlay.revalidate();
        pauseOverlay.repaint();

        this.add(pauseOverlay);
        this.setComponentZOrder(pauseOverlay, 0);
        this.repaint();
    }

    private void hidePauseOverlay() {
        if (pauseOverlay != null) {
            this.remove(pauseOverlay);
            pauseOverlay = null;
            this.revalidate();
            this.repaint();
        }
    }

    private void exitToLevelSelection() {
        Container parent = getParent();
        if (parent != null) {
            parent.remove(GameGUI.this);
            LevelSelectionGUI lvl = new LevelSelectionGUI(gameControl);
            parent.add(lvl);
            parent.revalidate();
            parent.repaint();
        }
    }

    // =====================================================
    // PANEL INTERNO (FINAL)
    // =====================================================
    public class GamePanel extends JPanel {

        private BadIceCream game;
        private final SpriteManager spriteManager;
        private static final int TILE = 32;

        public GamePanel(BadIceCream game, SpriteManager spriteManager) {
            this.game = game;
            this.spriteManager = spriteManager;
            setBackground(Color.BLACK);
        }

        public void setGame(BadIceCream newGame) {
            this.game = newGame;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            GameMap map = game.getMap();

            // =============== MAPA ===============
            for (int r = 0; r < map.getRows(); r++) {
                for (int c = 0; c < map.getCols(); c++) {

                    Position pos = new Position(r, c);
                    Boxy b = map.getBlock(pos);

                    if (b == null) continue;

                    Image img = spriteManager.get(b.getSpriteKey());
                    g.drawImage(img, c * TILE, r * TILE, TILE, TILE, null);
                }
            }

            // =============== FRUTAS ===============
            for (Fruit f : game.getFruits()) {
                if (!f.isEaten()) {
                    Image img = spriteManager.get(f.getSpriteKey());
                    Position p = f.getPosition();
                    g.drawImage(img, p.getCol() * TILE, p.getRow() * TILE, TILE, TILE, null);
                }
            }

            // =============== ENEMIGOS ===============
            for (Enemy e : game.getEnemies()) {

                String key;
                switch (e.getType()) {
                    case flowerpot -> {
                        Flowerpot fp = (Flowerpot) e;
                        key = fp.isCharging() ? "flowerpot_on" : "flowerpot";
                    }
                    case narval -> key = "enemy_narval";
                    case yellowSquid -> key = "enemy_squid";
                    default -> key = "enemy_default";
                }

                Image img = spriteManager.get(key);
                Position p = e.getPosition();
                g.drawImage(img, p.getCol() * TILE, p.getRow() * TILE, TILE, TILE, null);
            }

            // =============== JUGADORES (CON FLAVOR) ===============
            for (IceCream pl : game.getPlayers()) {

                String flavor = pl.getFlavor();               
                String state = pl.isAlive() ? "alive" : "dead";

                String key = flavor + "_" + state;           

                Image img = spriteManager.get(key);

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
