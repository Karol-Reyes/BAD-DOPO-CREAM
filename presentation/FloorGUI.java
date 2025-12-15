package presentation;

import java.awt.*;
import javax.swing.*;

public class FloorGUI extends ResourcesGUI {
    private Timer gifTimer;
    private final GameControl gameControl;

    private PixelButton btnBonfire;
    private PixelButton btnFire;
    @SuppressWarnings("unused")
    private PixelButton btnNothing;
    private PixelButton btnReady;

    private String pendingObject;
    private int pendingMax;

    /**
     * Constructor de la clase FloorGUI.
     */
    public FloorGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * Inicia la secuencia del gif de chocolate.
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
            bonfire();
            fire();
            nothing();
            ready();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Muestra el texto principal en la GUI.
     */
    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/EscogerObjeto.png",
           320, 40, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de la fogata a la GUI.
     */
    public void bonfire() {
        btnBonfire = new PixelButton("/Resources/box/bonfire.png", 140, 140);
        btnBonfire.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBonfire.setBounds(80, 170, 140, 140);
        btnBonfire.setDisabledIcon(btnBonfire.getIcon());
        btnBonfire.addActionListener(e -> {
            selectObject("Bonfire", 4);
        });

        contentLabel.add(btnBonfire);
        contentLabel.setComponentZOrder(btnBonfire, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón del fuego a la GUI.
     */
    public void fire() {
        btnFire = new PixelButton("/Resources/box/fire.png", 140, 140);
        btnFire.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFire.setBounds(450, 170, 140, 140);
        btnFire.setDisabledIcon(btnFire.getIcon());
        btnFire.addActionListener(e -> {
            selectObject("Fire", 20);
        });

        contentLabel.add(btnFire);
        contentLabel.setComponentZOrder(btnFire, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de ninguno a la GUI.
     */
    public void nothing() {
        btnNothing = new PixelButton("/Resources/textos/Ninguno.png", 120, 40);
        btnNothing.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNothing.setBounds(290, 330, 120, 40);
        btnNothing.setDisabledIcon(btnNothing.getIcon());
        btnNothing.addActionListener(e -> {
            selectObject("Nothing", 0);
        });

        contentLabel.add(btnNothing);
        contentLabel.setComponentZOrder(btnNothing, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de listo a la GUI.
     */
    public void ready() {
        btnReady = new PixelButton("/Resources/textos/Listo.png", 170, 40);
        btnReady.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReady.setBounds(260, 380, 170, 40);
        btnReady.setDisabledIcon(btnReady.getIcon());

        btnReady.setEnabled(false); 

        btnReady.addActionListener(e -> gameplay());

        contentLabel.add(btnReady);
        contentLabel.setComponentZOrder(btnReady, 0);
    }

    /**
     * Maneja la selección de un objeto y su cantidad.
     * @param object nombre del objeto seleccionado
     * @param max cantidad máxima permitida para el objeto
     */
    private void selectObject(String object, int max) {
        pendingObject = object;
        pendingMax = max;
        goToAmountSelection();
    }

    /**
     * Cambia a la pantalla de selección de cantidad.
     */
    private void goToAmountSelection() {
        contentLabel.removeAll();
        showBackground();
        addIceCreamBackground();
        main_Text();
        background();
        createNumberButtons(pendingMax, amount -> {
            gameControl.setSelectedObject(pendingObject, amount);
            returnFromAmountSelection();
        });
    }

    /**
     * Regresa de la selección de cantidad a la selección de objeto.
     */
    private void returnFromAmountSelection() {
        contentLabel.removeAll();

        showBackground();
        addIceCreamBackground();
        addBackBackground();
        backButton();
        main_Text();

        bonfire();
        fire();
        nothing();
        updateButtonStates();
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Actualiza el estado de los botones según la selección actual.
     */
    private void updateButtonStates() {
        boolean selected = gameControl.hasObject();
        btnBonfire.setEnabled(!selected);
        btnFire.setEnabled(!selected);
    }

    /**
     * Cambia a la pantalla de juego.
     */
    private void gameplay() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);

            parent.add(new GameGUI(gameControl));
            parent.revalidate();
            parent.repaint();
        }
    }
}