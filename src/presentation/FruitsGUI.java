package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase que representa la interfaz gráfica de usuario (GUI) para la selección de frutas.
 * Extiende ResourcesGUI e incluye botones para seleccionar frutas y avanzar al siguiente paso.
 */
public class FruitsGUI extends ResourcesGUI {
    private Timer gifTimer;
    //private final String selectedFruit1 = null;
    //private final String selectedFruit2 = null;

    private PixelButton btnBanana;
    private PixelButton btnGrape;
    private PixelButton btnCherry;
    private PixelButton btnPineapple;
    private PixelButton btnCactus;
    private PixelButton btnReady;

    private String pendingFruit;

    private final GameControl gameControl;

    /**
     * Crea la pantalla de selección de frutas y recibe el controlador del juego.
     * @param gameControl controlador principal del juego
     */
    public FruitsGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * Inicia la secuencia visual inicial mostrando una animación
     * antes de cargar los botones y textos de selección de frutas.
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
            banana();
            grape();
            cherry();
            pineapple();
            cactus();
            ready();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Muestra el texto principal que indica al jugador que debe escoger una fruta.
     */
    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/EscogerFruta.png",
           320, 40, 180, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y muestra el botón de selección de la fruta Banana.
     */
    public void banana() {
        btnBanana = new PixelButton("/Resources/fruit/Banana.jpg", 80, 80);
        btnBanana.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBanana.setBounds(80, 170, 80, 80);
        btnBanana.setDisabledIcon(btnBanana.getIcon());
        btnBanana.addActionListener(e -> {
            selectFruit("Banana");
        });

        contentLabel.add(btnBanana);
        contentLabel.setComponentZOrder(btnBanana, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y muestra el botón de selección de la fruta Grape.
     */
    public void grape() {
        btnGrape = new PixelButton("/Resources/fruit/Grape.jpg", 80, 80);
        btnGrape.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGrape.setBounds(220, 170, 80, 80);
        btnGrape.setDisabledIcon(btnGrape.getIcon());
        btnGrape.addActionListener(e -> {
            selectFruit("Grape");
        });

        contentLabel.add(btnGrape);
        contentLabel.setComponentZOrder(btnGrape, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y muestra el botón de selección de la fruta Cherry.
     */
    public void cherry() {
        btnCherry = new PixelButton("/Resources/fruit/Cherry.jpg", 80, 80);
        btnCherry.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCherry.setBounds(360, 170, 80, 80);
        btnCherry.setDisabledIcon(btnCherry.getIcon());
        btnCherry.addActionListener(e -> {
            selectFruit("Cherry");
        });

        contentLabel.add(btnCherry);
        contentLabel.setComponentZOrder(btnCherry, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y muestra el botón de selección de la fruta Pineapple.
     */
    public void pineapple() {
        btnPineapple = new PixelButton("/Resources/fruit/Pineapple.jpg", 80, 80);
        btnPineapple.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPineapple.setBounds(500, 170, 80, 80);
        btnPineapple.setDisabledIcon(btnPineapple.getIcon());
        btnPineapple.addActionListener(e -> {
            selectFruit("Pineapple");
        });

        contentLabel.add(btnPineapple);
        contentLabel.setComponentZOrder(btnPineapple, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y muestra el botón de selección de la fruta Cactus.
     */
    public void cactus() {
        btnCactus = new PixelButton("/Resources/fruit/cactus.png", 80, 80);
        btnCactus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCactus.setBounds(305, 250, 80, 100);
        btnCactus.setDisabledIcon(btnCactus.getIcon());
        btnCactus.addActionListener(e -> {
            selectFruit("Cactus");
        });

        contentLabel.add(btnCactus);
        contentLabel.setComponentZOrder(btnCactus, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y muestra el botón para avanzar a la selección de enemigos.
     */
    public void ready() {
        btnReady = new PixelButton("/Resources/textos/Listo.png", 170, 40);
        btnReady.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReady.setBounds(250, 370, 170, 40);
        btnReady.setDisabledIcon(btnReady.getIcon());
        btnReady.setEnabled(false);
        btnReady.addActionListener(e -> {
            goToEnemySelection();
        });

        contentLabel.add(btnReady);
        contentLabel.setComponentZOrder(btnReady, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Gestiona la selección de frutas por parte de los jugadores.
     * Asigna la fruta al jugador correspondiente y deshabilita el botón.
     * @param fruit nombre de la fruta seleccionada
     */
    private void selectFruit(String fruit) {
        pendingFruit = fruit;
        goToAmountSelection();
    }

    /**
     * Muestra la pantalla para seleccionar la cantidad de la fruta elegida.
     */
    private void goToAmountSelection() {
        contentLabel.removeAll();
        showBackground();   
        addIceCreamBackground();
        main_Text();        
        background();
        createNumberButtons(8, amount -> {
            gameControl.addFruit(pendingFruit, amount);
            returnFromAmountSelection();
        });
    }

    /**
     * Regresa a la pantalla de selección de frutas
     * después de que se ha seleccionado la cantidad.
     */
    private void returnFromAmountSelection() {
        showBackground();
        addIceCreamBackground();
        addBackBackground();
        backButton();
        main_Text();

        banana();
        grape();
        cherry();
        pineapple();
        cactus();
        ready();

        updateButtonStates();
    }

    /**
     * Actualiza el estado de los botones según las frutas seleccionadas.
     */
    private void updateButtonStates() {
        btnBanana.setEnabled(!gameControl.isFruitSelected("Banana"));
        btnGrape.setEnabled(!gameControl.isFruitSelected("Grape"));
        btnCherry.setEnabled(!gameControl.isFruitSelected("Cherry"));
        btnPineapple.setEnabled(!gameControl.isFruitSelected("Pineapple"));
        btnCactus.setEnabled(!gameControl.isFruitSelected("Cactus"));

        btnReady.setEnabled(gameControl.hasFruits());
    }

    /**
     * Navega a la pantalla de selección de enemigos.
     */
    private void goToEnemySelection() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            parent.add(new EnemyGUI(gameControl));
            parent.revalidate();
            parent.repaint();
        }
    }
}