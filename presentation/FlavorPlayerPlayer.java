package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase que representa la pantalla de selección de sabores para el modo jugador contra jugador.
 * Extiende ResourcesGUI e incluye botones para que ambos jugadores seleccionen sus sabores.
 */
public class FlavorPlayerPlayer extends ResourcesGUI {
    private Timer gifTimer;
    private String player1 = null;
    private String player2 = null;

    private PixelButton btnV1;
    private PixelButton btnF1;
    private PixelButton btnC1;
    private PixelButton btnV2;
    private PixelButton btnF2;
    private PixelButton btnC2;

    private final GameControl gameControl;

    /**
     * Crea la pantalla de selección de sabores para el modo jugador contra jugador.
     * @param gameControl controlador principal del juego
     */
    public FlavorPlayerPlayer(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * Inicia la secuencia visual inicial mostrando una animación
     * antes de cargar la selección de sabores para ambos jugadores.
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
            main_Text();
            coneImage1();
            coneImage2();
            vanillaButton1();
            strawberryButton1();
            chocolateButton1();
            vanillaButton2();
            strawberryButton2();
            chocolateButton2();
            textPlayer1();
            textPlayer2();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Muestra el texto principal que indica la selección de sabor.
     */
    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/seleccionarSabor.png",
         320, 30, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el cono del jugador 1.
     */
    private void coneImage1() {
        JLabel labelFlavors = ImageUtils.createScaledImageLabel("/Resources/seleccion_jugador/cono.png",
         265, 200, 50, 190);
        contentLabel.add(labelFlavors);
        contentLabel.setComponentZOrder(labelFlavors, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón de selección de sabor vainilla para el jugador 1.
     */
    private void vanillaButton1() {
        btnV1 = new PixelButton("/Resources/seleccion_jugador/vainilla.png", 100, 120);
        btnV1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnV1.setBounds(135, 110, 100, 120);
        btnV1.setDisabledIcon(btnV1.getIcon());
        btnV1.addActionListener(e -> {
            gameControl.setCharacter1("Vanilla");
            selectCharacter("Vanilla1");
        });

        contentLabel.add(btnV1);
        contentLabel.setComponentZOrder(btnV1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón de selección de sabor fresa para el jugador 1.
     */
    private void strawberryButton1() {
        btnF1 = new PixelButton("/Resources/seleccion_jugador/fresa.png", 100, 120);
        btnF1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnF1.setBounds(215, 95, 100, 120);
        btnF1.setDisabledIcon(btnF1.getIcon());
        btnF1.addActionListener(e -> {
            gameControl.setCharacter1("Strawberry");
            selectCharacter("Strawberry1");
        });

        contentLabel.add(btnF1);
        contentLabel.setComponentZOrder(btnF1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón de selección de sabor chocolate para el jugador 1.
     */
    private void chocolateButton1() {
        btnC1 = new PixelButton("/Resources/seleccion_jugador/chocolate.png", 100, 120);
        btnC1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnC1.setBounds(50, 95, 100, 120);
        btnC1.setDisabledIcon(btnC1.getIcon());
        btnC1.addActionListener(e -> {
            gameControl.setCharacter1("Chocolate");
            selectCharacter("Chocolate1");
        });

        contentLabel.add(btnC1);
        contentLabel.setComponentZOrder(btnC1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el cono del jugador 2.
     */
    private void coneImage2() {
        JLabel labelFlavors = ImageUtils.createScaledImageLabel("/Resources/seleccion_jugador/cono.png",
         265, 200, 370, 190);
        contentLabel.add(labelFlavors);
        contentLabel.setComponentZOrder(labelFlavors, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón de selección de sabor vainilla para el jugador 2.
     */
    private void vanillaButton2() {
        btnV2 = new PixelButton("/Resources/seleccion_jugador/vainilla.png", 100, 120);
        btnV2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnV2.setBounds(455, 110, 100, 120);
        btnV2.setDisabledIcon(btnV2.getIcon());
        btnV2.addActionListener(e -> {
            gameControl.setCharacter2("Vanilla");
            selectCharacter("Vanilla2");
        });

        contentLabel.add(btnV2);
        contentLabel.setComponentZOrder(btnV2, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón de selección de sabor fresa para el jugador 2.
     */
    private void strawberryButton2() {
        btnF2 = new PixelButton("/Resources/seleccion_jugador/fresa.png", 100, 120);
        btnF2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnF2.setBounds(535, 95, 100, 120);
        btnF2.setDisabledIcon(btnF2.getIcon());
        btnF2.addActionListener(e -> {
            gameControl.setCharacter2("Strawberry");
            selectCharacter("Strawberry2");
        });

        contentLabel.add(btnF2);
        contentLabel.setComponentZOrder(btnF2, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón de selección de sabor chocolate para el jugador 2.
     */
    private void chocolateButton2() {
        btnC2 = new PixelButton("/Resources/seleccion_jugador/chocolate.png", 100, 120);
        btnC2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnC2.setBounds(370, 95, 100, 120);
        btnC2.setDisabledIcon(btnC2.getIcon());
        btnC2.addActionListener(e -> {
            gameControl.setCharacter2("Chocolate");
            selectCharacter("Chocolate2");
        });

        contentLabel.add(btnC2);
        contentLabel.setComponentZOrder(btnC2, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto identificador del jugador 1.
     */
    private void textPlayer1() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/Jugador1.png",
         120, 30, 120, 390);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto identificador del jugador 2.
     */
    private void textPlayer2() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/Jugador2.png",
         120, 30, 450, 390);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Registra la selección de sabor de cada jugador y
     * avanza a la selección de nivel cuando ambos han elegido.
     * @param character identificador del sabor seleccionado
     */
    private void selectCharacter(String character) {
        if (player1 == null) {
            player1 = character;
            disableButtons(character);
        } else if (player2 == null) {
            player2 = character;
            disableButtons(character);
        }
        if (player1 != null && player2 != null) {
            levelSelection();
        }
    }

    /**
     * Deshabilita los botones correspondientes para evitar
     * selecciones duplicadas entre jugadores.
     * @param character identificador del sabor seleccionado
     */
    private void disableButtons(String character) {
        switch (character) {
            case "Vanilla1" -> {
                btnV1.setEnabled(false);
                btnF1.setEnabled(false);
                btnC1.setEnabled(false);
                btnV2.setEnabled(false);
            }
            case "Strawberry1" -> {
                btnV1.setEnabled(false);
                btnF1.setEnabled(false);
                btnC1.setEnabled(false);
                btnF2.setEnabled(false);
            }
            case "Chocolate1" -> {
                btnV1.setEnabled(false);
                btnF1.setEnabled(false);
                btnC1.setEnabled(false);
                btnC2.setEnabled(false);
            }
            case "Vanilla2" -> {
                btnV2.setEnabled(false);
                btnF2.setEnabled(false);
                btnC2.setEnabled(false);
                btnV1.setEnabled(false);
            }
            case "Strawberry2" -> {
                btnV2.setEnabled(false);
                btnF2.setEnabled(false);
                btnC2.setEnabled(false);
                btnF1.setEnabled(false);
            }
            case "Chocolate2" -> {
                btnV2.setEnabled(false);
                btnF2.setEnabled(false);
                btnC2.setEnabled(false);
                btnC1.setEnabled(false);
            }
        }
    }

    /**
     * Cambia la pantalla actual a la selección de nivel
     * una vez que ambos jugadores han elegido su sabor.
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