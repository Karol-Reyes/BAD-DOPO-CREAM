package presentation;

import java.awt.*;
import javax.swing.*;

public class FloorGUI extends ResourcesGUI {
    private Timer gifTimer;
    private final GameControl gameControl;

    private PixelButton btnBonfire;
    private PixelButton btnFire;
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
            ready();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Muestra el texto principal en la GUI.
     */
    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel(
            "/Resources/textos/EscogerObjeto.png",
            320, 40, 175, 45
        );
        contentLabel.add(textLabel);
    }

    /**
     * Agrega el botón de la fogata a la GUI.
     */
    public void bonfire() {
        btnBonfire = new PixelButton("/Resources/box/bonfire.png", 140, 140);
        btnBonfire.setBounds(80, 170, 140, 140);
        btnBonfire.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBonfire.setDisabledIcon(btnBonfire.getIcon());
        btnBonfire.addActionListener(e -> selectObject("Bonfire", 4));
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
        btnFire.setBounds(450, 170, 140, 140);
        btnFire.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFire.setDisabledIcon(btnFire.getIcon());
        btnFire.addActionListener(e -> selectObject("Fire", 20));
        contentLabel.add(btnFire);
        contentLabel.setComponentZOrder(btnFire,0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de listo a la GUI.
     */
    public void ready() {
        btnReady = new PixelButton("/Resources/textos/Listo.png", 170, 40);
        btnReady.setBounds(260, 380, 170, 40);
        btnReady.setEnabled(false);
        btnReady.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReady.setDisabledIcon(btnReady.getIcon());
        btnReady.addActionListener(e -> goToGame());
        contentLabel.add(btnReady);
        contentLabel.setComponentZOrder(btnReady, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Maneja la selección de un objeto y navega a la pantalla de selección de cantidad.
     * @param object Nombre del objeto seleccionado
     * @param max Cantidad máxima seleccionable para el objeto
     */
    private void selectObject(String object, int max) {
        pendingObject = object;
        pendingMax = max;
        goToAmountSelection();
    }

    /**
     * Muestra la pantalla de selección de cantidad de objetos.
     */
    private void goToAmountSelection() {
        contentLabel.removeAll();

        showBackground();
        addIceCreamBackground();
        main_Text();
        background();
        textAmount();

        createNumberButtons(pendingMax, amount -> {
            gameControl.addObject(pendingObject, amount);
            returnFromAmountSelection();
        });
    }

    /**
     * Regresa de la selección de cantidad a la pantalla principal de selección de objetos.
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
        ready();
        updateButtonStates();

        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Actualiza el estado de los botones según los objetos seleccionados.
     */
    private void updateButtonStates() {
        btnBonfire.setEnabled(!gameControl.isObjectSelected("Bonfire"));
        btnFire.setEnabled(!gameControl.isObjectSelected("Fire"));
        btnReady.setEnabled(gameControl.hasObjects());
    }

    /**
     * Navega a la pantalla del juego.
     */
    private void goToGame() {
        Container parent = getParent();
        parent.remove(this);
        parent.add(new GameGUI(gameControl));
        parent.revalidate();
        parent.repaint();
    }
}