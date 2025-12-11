package presentation;

import domain.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameGUI1 extends JPanel {

    private BadIceCream game;
    private GamePanel panel;
    private Timer timer;

    public GameGUI1() {
        setLayout(new BorderLayout());
        loadGame();

        panel = new GamePanel(game);
        add(panel, BorderLayout.CENTER);

        setupKeyBindings();

        timer = new Timer(150, e -> {
            if (!game.isGameWon() && !game.isGameLost()) {
                game.updateGame();
            }
            panel.repaint();
        });
        timer.start();
    }

    // =============== CARGAR NIVEL =================
    private void loadGame() {
        game = LevelOne.createLevel();

        for (IceCream p : game.getPlayers()) {
            p.setGameMap(game.getMap());
        }
    }

    // =============== TECLAS ==========================
    private void setupKeyBindings() {
        InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        bind(im, am, "UP", KeyEvent.VK_UP, () -> game.movePlayer(0, Direction.UP));
        bind(im, am, "DOWN", KeyEvent.VK_DOWN, () -> game.movePlayer(0, Direction.DOWN));
        bind(im, am, "LEFT", KeyEvent.VK_LEFT, () -> game.movePlayer(0, Direction.LEFT));
        bind(im, am, "RIGHT", KeyEvent.VK_RIGHT, () -> game.movePlayer(0, Direction.RIGHT));

        bind(im, am, "FREEZE", KeyEvent.VK_SPACE, () -> game.playerCreateIce(0));
        bind(im, am, "BREAK", KeyEvent.VK_X, () -> game.playerDestroyIce(0));
        bind(im, am, "RESET", KeyEvent.VK_R, this::loadGame);
    }

    private void bind(InputMap im, ActionMap am, String name, int key, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { action.run(); }
        });
    }

    // ======================== PANEL INTERNO ==============================
    private static class GamePanel extends JPanel {

        private BadIceCream game;
        private static final int TILE = 32;

        public GamePanel(BadIceCream game) {
            this.game = game;
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            GameMap map = game.getMap();

            // ---------------- MAPA ----------------
            for (int r = 0; r < map.getRows(); r++) {
                for (int c = 0; c < map.getCols(); c++) {

                    Boxy b = map.getBlock(new Position(r, c));

                    if (b != null && b.isCreated()) {
                        g.setColor(Color.CYAN);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                    }

                    g.fillRect(c * TILE, r * TILE, TILE, TILE);

                    g.setColor(Color.BLACK);
                    g.drawRect(c * TILE, r * TILE, TILE, TILE);
                }
            }

            // ---------------- FRUTAS ----------------
            for (Fruit f : game.getFruits()) {
                if (!f.isEaten()) {

                    switch (f.getType()) {
                        case banana: g.setColor(Color.YELLOW); break;
                        case grape: g.setColor(new Color(150, 0, 200)); break;
                        case cherry:
                            if (f.getSpriteKey().equals("cherry_teleporting"))
                                g.setColor(new Color(255, 140, 0));
                            else
                                g.setColor(Color.RED);
                            break;
                        case pineapple: g.setColor(new Color(255, 128, 0)); break;
                        case cactus:
                            if (f.getSpriteKey().equals("cactus_thorns"))
                                g.setColor(Color.WHITE);
                            else
                                g.setColor(Color.GRAY);
                            break;
                        default: g.setColor(Color.LIGHT_GRAY); break;
                    }

                    Position p = f.getPosition();
                    g.fillOval(p.getCol() * TILE + 8, p.getRow() * TILE + 8, 16, 16);
                }
            }

            // ---------------- ENEMIGOS ----------------
            for (Enemy e : game.getEnemies()) {
                Position p = e.getPosition();
                int x = p.getCol() * TILE;
                int y = p.getRow() * TILE;

                if (e.getType() == EnemyType.flowerpot) {
                    Flowerpot fp = (Flowerpot) e;
                    g.setColor(fp.isCharging() ? Color.PINK : Color.MAGENTA);
                    g.fillRect(x + 8, y + 8, 18, 18);
                } 
                else if (e.getType() == EnemyType.narval) {
                    g.setColor(new Color(0, 200, 200));
                    int[] xs = { x + 16, x + 6, x + 26 };
                    int[] ys = { y + 4,  y + 28, y + 28 };
                    g.fillPolygon(xs, ys, 3);
                } 
                else {
                    g.setColor(Color.GREEN);
                    g.fillOval(x + 6, y + 6, 20, 20);
                }
            }

            // ---------------- JUGADOR ----------------
            for (IceCream p : game.getPlayers()) {
                g.setColor(p.isAlive() ? Color.WHITE : Color.GRAY);
                Position pos = p.getPosition();
                g.fillOval(pos.getCol() * TILE + 4, pos.getRow() * TILE + 4, 24, 24);
            }

            drawMessages(g);
        }

        private void drawMessages(Graphics g) {
            if (game.isGameWon()) {
                drawOverlay(g, "¡GANASTE!", new Color(0, 200, 0, 180));
            }
            if (game.isGameLost()) {
                drawOverlay(g, "PERDISTE", new Color(200, 0, 0, 180));
            }
        }

        private void drawOverlay(Graphics g, String msg, Color bg) {
            g.setColor(bg);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.setColor(Color.WHITE);

            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;

            g.drawString(msg, x, y);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String tip = "Presiona R para reiniciar";
            x = (getWidth() - g.getFontMetrics().stringWidth(tip)) / 2;
            g.drawString(tip, x, y + 40);
        }
    }
}

