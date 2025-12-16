package  presentation;

import domain.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Clase principal de la interfaz gráfica de usuario (GUI) para el juego BadIceCream.
 * Esta clase extiende JPanel e incluye un panel de juego que muestra el mapa
 */
public class GameGUI extends JPanel {

    private BadIceCream game;
    private GamePanel panel;
    private Timer timer;
    private GameControl gameControl;
    private SpriteManager spriteManager;  

    @SuppressWarnings("unused")
    private boolean gameEnded = false;
    
    private JLabel pauseOverlay;
    private boolean isPaused = false;

    private JLabel fruitBar;
    private PixelButton btnPause;
    private JLabel winOverlay;
    private boolean winShown = false;
    private JLabel loseOverlay;
    private boolean loseShown = false;

    private int visualTime = 180; 
    private Timer visualTimer;


    /**
     * Constructor que recibe la configuración del juego
     */
    public GameGUI(GameControl gameControl) {
        this.gameControl = gameControl;
        setLayout(new BorderLayout());

        spriteManager = new SpriteManager();
        loadGame();

        panel = new GamePanel(game, spriteManager);
        add(panel, BorderLayout.CENTER);

        panel.setVisualTime(visualTime); 
        startVisualTimer();
        addFruitBar();
        PauseButton();
        setupKeyBindings();

        timer = new Timer(150, e -> {

        if (!game.isGameWon() && !game.isGameLost()) {
            game.updateGame();
        }

        if (game.isGameWon() && !winShown) {
            timer.stop();
            if (visualTimer != null) visualTimer.stop(); 
            showWinOverlay();
            winShown = true;
            return;
        }

        if (game.isGameLost() && !loseShown) {
            timer.stop();
            if (visualTimer != null) visualTimer.stop(); 
            showLoseOverlay();
            loseShown = true;
            return;
        }

        panel.repaint();
    });
        timer.start();
    }

    /**
     * Carga el juego según la configuración actual.
     */
    private void loadGame() {
        resetEndStates();
        visualTime = 180;

        if (visualTimer != null) {
            visualTimer.restart();
        }


        GameConfig config = gameControl.toGameConfig();
        game = LevelLoader.loadLevel(config.getLevel(), config);

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
    /**
     * Configura las asociaciones de teclas para controlar el juego.
     */
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

    /**
     * Asocia una tecla a una acción específica.
     * @param im InputMap donde se asigna la tecla.
     * @param am ActionMap donde se asigna la acción.
     * @param name nombre de la acción.
     * @param key código de la tecla.
     * @param action acción a ejecutar al presionar la tecla.
     */
    private void bind(InputMap im, ActionMap am, String name, int key, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (winShown || loseShown) return;
                action.run();
                panel.repaint();
            }
        });
    }

    /* 
     * pausa o reanuda el juego 
     */
    private void togglePause() {
        isPaused = !isPaused;

        game.setPaused(isPaused);

        if (isPaused) {
            timer.stop();
            showPauseOverlay();
        } else {
            hidePauseOverlay();
            timer.start();
        }
    }

    /**
     * Oculta la superposición de pausa.
     */
    private void hidePauseOverlay() {
        if (pauseOverlay != null) {
            this.remove(pauseOverlay);
            pauseOverlay = null;
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Muestra la superposición de pausa.
     */
    private void showPauseOverlay() {
        pauseOverlay = new JLabel();
        pauseBackground();
        textPause();
        buttonContinue();
        buttonExit();
    }

    /**
     * Configura el fondo de la superposición de pausa.
     */
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

    /**
     * Configura el texto de la superposición de pausa.
     */
    private void textPause() {
        JLabel text = ImageUtils.createScaledImageLabel("/Resources/textos/Pausa.png",
        130, 40, 270, 200);
        pauseOverlay.add(text);
        pauseOverlay.setComponentZOrder(text, 0);
    }
    
    /**
     * Configura el botón de continuar en la superposición de pausa.
     */
    private void buttonContinue() {
        PixelButton btnContinue = new PixelButton("/Resources/textos/Continuar.png",
         140, 60);
        btnContinue.setBounds(260, 270, 140, 60);
        btnContinue.addActionListener(e -> togglePause());
        pauseOverlay.add(btnContinue);
        pauseOverlay.setComponentZOrder(btnContinue, 0);
        pauseOverlay.revalidate();
        pauseOverlay.repaint();
    }
    
    /**
     * Configura el botón de salir en la superposición de pausa.
     */
    private void buttonExit() {
        PixelButton btnExit = new PixelButton("/Resources/textos/Salir.png", 100, 50);
        btnExit.setBounds(280, 340, 100, 50);
        btnExit.addActionListener(e -> exitToLevelSelection());
        pauseOverlay.add(btnExit);
        pauseOverlay.setComponentZOrder(btnExit, 0);
        pauseOverlay.revalidate();
        pauseOverlay.repaint();

        this.add(pauseOverlay);
        this.setComponentZOrder(pauseOverlay, 0);
        this.repaint();
    }

    /**
     * Sale a la pantalla de selección de nivel.
     */
    private void exitToLevelSelection() {

        gameControl.resetSelections();

        if (timer != null) {
            timer.stop();
        }

        Container parent = getParent();
        if (parent != null) {
            parent.remove(GameGUI.this);
            parent.add(new LevelSelectionGUI(gameControl));
            parent.revalidate();
            parent.repaint();
        }
    }


    /**
     * Agrega la barra de frutas al juego.
     */
    private void addFruitBar() {
        panel.setLayout(null);

        fruitBar = ImageUtils.createScaledImageLabel(
            "/Resources/game/fondoFrutas.png", 
            580, 70,                         
            0, 580                           
        );
        panel.add(fruitBar);
        panel.setComponentZOrder(fruitBar, 0);
        panel.repaint();
    }

    /**
     * Agrega el botón de pausa al juego.
     */
    private void PauseButton() {
        btnPause = new PixelButton("/Resources/game/BotonPausa.png", 100, 50);
        btnPause.setBounds(600, 50, 50, 50);
        btnPause.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPause.setDisabledIcon(btnPause.getIcon());
        btnPause.addActionListener(e -> togglePause());
        panel.add(btnPause);
        panel.setComponentZOrder(btnPause, 0);
        panel.repaint();
    }

    /**
     * Muestra la superposición de victoria.
     */
    private void showWinOverlay() {
        btnPause.setEnabled(false);
        game.setPaused(true);
        this.setFocusable(false);
        winOverlay = new JLabel();
        winOverlay.setBounds(0, 0, getWidth(), getHeight());
        winOverlay.setLayout(null);
        winOverlay.setOpaque(true);
        winOverlay.setBackground(new Color(0, 0, 0, 150));
        backgroundWin();
        textWin();
        ImageWin();
        buttonMenuWin();
        buttonRestartWin();
        this.add(winOverlay);
        this.setComponentZOrder(winOverlay, 0);
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Configura el fondo de la superposición de victoria.
     */
    private void backgroundWin() {
        JLabel bg = ImageUtils.createScaledImageLabel(
            "/Resources/inicio/fondo_eleccion.png",
            550, 350,
            60, 160
        );
        winOverlay.add(bg);
    }

    /**
     * Configura el texto de la superposición de victoria.
     */
    private void textWin() {
        JLabel text = ImageUtils.createScaledImageLabel(
            "/Resources/textos/NivelCompletado.png",
            260, 50,
            205, 160
        );
        winOverlay.add(text);
        winOverlay.setComponentZOrder(text, 0);

    }

    /**
     * Configura la imagen de la superposición de victoria.
     */
    private void ImageWin() {
        JLabel image = ImageUtils.createScaledImageLabel(
            "/Resources/game/Victoria.png",
            400, 220,
            140, 210
        );
        winOverlay.add(image);
        winOverlay.setComponentZOrder(image, 0);

    }

    /**
     * Configura el botón de menú en la superposición de victoria.
     */
    private void buttonMenuWin() {
        PixelButton btnExit = new PixelButton("/Resources/textos/VolverMenu.png",
        120, 50);
        btnExit.setBounds(120, 425, 120, 50);
        btnExit.addActionListener(e -> exitToLevelSelection());
        winOverlay.add(btnExit);
        winOverlay.setComponentZOrder(btnExit, 0);
        winOverlay.revalidate();
        winOverlay.repaint();
    }

    /**
     * Configura el botón de reiniciar en la superposición de victoria.
     */
    private void buttonRestartWin() {
        PixelButton btnRestart = new PixelButton("/Resources/textos/Reiniciar.png",
        120, 50);
        btnRestart.setBounds(390, 425, 150, 50);
        btnRestart.addActionListener(e -> resetGame());
        winOverlay.add(btnRestart);
        winOverlay.setComponentZOrder(btnRestart, 0);
        winOverlay.revalidate();
        winOverlay.repaint();
    }

    /**
     * Reinicia el juego desde el estado inicial.
     */
    private void resetGame() {
        btnPause.setEnabled(true);
        loadGame();
    }

    /**
     * Muestra la superposición de derrota.
     */
    private void showLoseOverlay() {
        btnPause.setEnabled(false);
        game.setPaused(true);
        this.setFocusable(false);
        loseOverlay = new JLabel();
        loseOverlay.setBounds(0, 0, getWidth(), getHeight());
        loseOverlay.setLayout(null);
        loseOverlay.setOpaque(true);
        loseOverlay.setBackground(new Color(0, 0, 0, 150));
        backgroundLose();
        textLose();
        ImageLose();
        buttonMenuLose();
        buttonRestartLose();
        this.add(loseOverlay);
        this.setComponentZOrder(loseOverlay, 0);
        this.revalidate();
        this.repaint();
    }

    /**
     * Configura el fondo de la superposición de derrota.
     */
    private void backgroundLose() {
        JLabel bg = ImageUtils.createScaledImageLabel(
            "/Resources/inicio/fondo_eleccion.png",
            550, 350,
            60, 160
        );
        loseOverlay.add(bg);
    }

    /**
     * Configura el texto de la superposición de derrota.
     */
    private void textLose() {
        JLabel text = ImageUtils.createScaledImageLabel(
            "/Resources/textos/nivelFallido.png",
            260, 50,
            205, 160
        );
        loseOverlay.add(text);
        loseOverlay.setComponentZOrder(text, 0);
    }

    /**
     * Configura la imagen de la superposición de derrota.
     */
    private void ImageLose() {
        JLabel image = ImageUtils.createScaledImageLabel(
            "/Resources/game/imagenDerrota.png",
            400, 110,
            140, 250
        );
        loseOverlay.add(image);
        loseOverlay.setComponentZOrder(image, 0);
    }

    /**
     * Configura el botón de menú en la superposición de derrota.
     */
    private void buttonMenuLose() {
        PixelButton btnExit = new PixelButton("/Resources/textos/VolverMenu.png",
        120, 50);
        btnExit.setBounds(120, 425, 120, 50);
        btnExit.addActionListener(e -> exitToLevelSelection());
        loseOverlay.add(btnExit);
        loseOverlay.setComponentZOrder(btnExit, 0);
        loseOverlay.revalidate();
        loseOverlay.repaint();
    }

    /**
     * Configura el botón de reiniciar en la superposición de derrota.
     */
    private void buttonRestartLose() {
        PixelButton btnRestart = new PixelButton("/Resources/textos/Reiniciar.png",
        120, 50);
        btnRestart.setBounds(390, 425, 150, 50);
        btnRestart.addActionListener(e -> resetGame());
        loseOverlay.add(btnRestart);
        loseOverlay.setComponentZOrder(btnRestart, 0);
        loseOverlay.revalidate();
        loseOverlay.repaint();
    }

    /**
     * Reinicia los estados de fin de juego.
     */
    private void resetEndStates() {
        gameEnded = false;
        winShown = false;
        loseShown = false;

        if (winOverlay != null) {
            this.remove(winOverlay);
            winOverlay = null;
        }

        if (loseOverlay != null) {
            this.remove(loseOverlay);
            loseOverlay = null;
        }

        if (timer != null && !timer.isRunning()) {
            timer.start();
        }

        isPaused = false;
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Inicia el timer de visualización del juego.
     */
    private void startVisualTimer() {
        visualTimer = new Timer(1000, e -> {
            if (isPaused || winShown || loseShown) return;

            if (visualTime > 0) {
                visualTime--;
                panel.setVisualTime(visualTime); 
                panel.repaint();
            }
        });
        visualTimer.start();
    }

    /**
     * Obtiene el tiempo de visualización del juego.
     * @return tiempo de visualización del juego.
     */
    public int getVisualTime() {
        return visualTime;
    }


    // =====================================================
    // PANEL INTERNO (FINAL)
    // =====================================================
    /**
     * Clase interna que representa el panel de juego.
     * Extiende JPanel y maneja la representación gráfica del juego.
     */
    public class GamePanel extends JPanel {

        private BadIceCream game;
        private final SpriteManager spriteManager;
        private static final int TILE = 32;
        private int visualTime;

        /**
         * Constructor del panel de juego.
         * @param game instancia del juego BadIceCream.
         * @param spriteManager gestor de sprites para cargar imágenes.
         */
        public GamePanel(BadIceCream game, SpriteManager spriteManager) {
            this.game = game;
            this.spriteManager = spriteManager;
            setBackground(Color.BLACK);
        }

        /**
         * Establece la instancia del juego.
         * @param newGame nueva instancia del juego BadIceCream.
         */
        public void setGame(BadIceCream newGame) {
            this.game = newGame;
            repaint();
        }

        /**
         * Método sobrescrito para pintar los componentes del juego.
         * @param g objeto Graphics utilizado para dibujar.
         */
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
                if (!f.isActive()) continue;
                if (f.isEaten()) continue;

                Image img = spriteManager.get(f.getSpriteKey());
                Position p = f.getPosition();
                
                g.drawImage(img, p.getCol() * TILE, p.getRow() * TILE, TILE, TILE, null);
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
            // =============== TIMER VISUAL ===============
            int min = visualTime / 60;
            int sec = visualTime % 60;

            String text = String.format("%02d:%02d", min, sec);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString(text, 590, 35);

        }

        /**
         * Establece el tiempo de visualización del juego.
         * @param visualTime nuevo tiempo de visualización.
         */
        public void setVisualTime(int visualTime) {
            this.visualTime = visualTime;
        }

    }
}