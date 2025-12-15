package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase de la interfaz gráfica de usuario (GUI) para la selección del sabor del jugador.
 * Extiende ResourcesGUI e incluye botones para seleccionar sabores y avanzar al siguiente paso.
 */
public class FlavorPlayerGUI extends ResourcesGUI {

    private Timer gifTimer;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 120;

    private final GameControl gameControl;
    
    /**
     * Constructor de la clase FlavorPlayerGUI.
     */
    public FlavorPlayerGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * Inicia la secuencia de selección de sabor.
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
            coneImage(265,200,205,189);
            main_Text();
            vanillaButton();
            strawberryButton();
            chocolateButton();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Muestra el texto principal en la GUI.
     */
    public void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/seleccionarSabor.png", 320, 30, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de Vainilla a la GUI.
     */
    public void vanillaButton() {
        PixelButton btnStart = new PixelButton("/Resources/seleccion_jugador/vainilla.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(290, 110, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.addActionListener(e -> {
            gameControl.setCharacter1("Vanilla");
            levelSelection();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de Fresa a la GUI.
     */
    public void strawberryButton() {
        PixelButton btnStart = new PixelButton("/Resources/seleccion_jugador/fresa.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(372, 97, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.addActionListener(e -> {
            gameControl.setCharacter1("Strawberry");
            levelSelection();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de Chocolate a la GUI.
     */
    public void chocolateButton() {
        PixelButton btnStart = new PixelButton("/Resources/seleccion_jugador/chocolate.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(208, 97, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.addActionListener(e -> {
            gameControl.setCharacter1("Chocolate");
            levelSelection();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra la pantalla de selección de nivel.
     */
    private void levelSelection() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);

            LevelSelectionGUI levelSelectionPanel = new LevelSelectionGUI(gameControl);
            parent.add(levelSelectionPanel);

            parent.revalidate();
            parent.repaint();
        }
    }

}