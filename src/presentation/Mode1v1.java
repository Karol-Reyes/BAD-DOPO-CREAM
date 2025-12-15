package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase de la interfaz gráfica de usuario (GUI) para la selección del modo 1 vs 1.
 * Extiende ResourcesGUI e incluye botones para seleccionar entre Jugador vs Jugador
 */
public class Mode1v1 extends ResourcesGUI {

    private Timer gifTimer;
    private final GameControl gameControl;

    /**
     * Crea la pantalla de selección de modo 1 vs 1.
     * @param gameControl controlador principal del juego
     */
    public Mode1v1(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * Inicia la secuencia visual de la pantalla mostrando la animación inicial
     * y luego los elementos interactivos del modo de juego.
     */
    @Override
    protected void startSequence() {
        startChocolateGif();
        
        int chocolateDuration = 1700;
        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addIceCreamBackground();
            addBackBackground();
            backButton();
            mainText();
            playerPlayer();
            playerMachine();
            textPlayerPlayer();
            textPlayerMachine();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Crea y añade el botón para el modo Jugador contra Jugador.
     */
    public void playerPlayer() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/Two.png", 200, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(100, 90, 200, 300);
        btnStart.addActionListener(e -> {
            gameControl.setGameMode("PlayerVSPlayer");
            flavorPlayerPlayer();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y añade el botón para el modo Jugador contra Máquina.
     */
    public void playerMachine() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/JugadorMaquina.png", 200, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(380, 90, 200, 300);
        btnStart.addActionListener(e -> {
            gameControl.setGameMode("PlayerVSMachine");
            flavorPlayerMachine();
        });
        
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto correspondiente al modo Jugador contra Jugador.
     */
    public void textPlayerPlayer() {
        JLabel textLabel = ImageUtils.createScaledImageLabel(
                "/Resources/textos/jugadorvsJugador.png", 170, 30, 110, 390);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto correspondiente al modo Jugador contra Máquina.
     */
    public void textPlayerMachine() {
        JLabel textLabel = ImageUtils.createScaledImageLabel(
                "/Resources/textos/jugadorvsMaquina.png", 170, 30, 400, 390);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Cambia a la pantalla de selección de sabores para el modo Jugador contra Jugador.
     */
    private void flavorPlayerPlayer() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            FlavorPlayerPlayer flavorPlayerPlayer = new FlavorPlayerPlayer(gameControl);
            parent.add(flavorPlayerPlayer);
            parent.revalidate();
            parent.repaint();
        }
    }

    /**
     * Cambia a la pantalla de selección de sabores para el modo Jugador contra Máquina.
     */
    private void flavorPlayerMachine() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            FlavorPlayerMachine flavorPlayerMachine = new FlavorPlayerMachine(gameControl);
            parent.add(flavorPlayerMachine);
            parent.revalidate();
            parent.repaint();
        }
    }
}