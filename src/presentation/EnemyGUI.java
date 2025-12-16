package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase de la interfaz gráfica de usuario (GUI) para la selección de enemigos.
 * Extiende ResourcesGUI e incluye botones para seleccionar enemigos y avanzar al siguiente paso.
 */
public class EnemyGUI extends ResourcesGUI {
    private Timer gifTimer;
    //private String selectedEnemy1 = null;
    //private String selectedEnemy2 = null;

    private PixelButton btnTroll;
    private PixelButton btnFlowerpot;
    private PixelButton btnYellowSquid;
    private PixelButton btnNarwhal;
    private String pendingEnemy;
    private PixelButton btnReady;

    private final GameControl gameControl;

    /**
     * Constructor de la clase EnemyGUI.
     * @param gameControl Controlador del juego para gestionar la lógica de selección de enemigos.
     */
    public EnemyGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * Inicia la secuencia de selección de enemigos.
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
            troll();
            flowerpot();
            yellowSquid();
            narwhal();
            ready();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Muestra el texto principal en la GUI.
     */
    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/EscogerEnemigos.png",
           320, 40, 180, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón del Troll a la GUI.
     */
    public void troll() {
        btnTroll = new PixelButton("/Resources/enemy/troll/static_down.png",
           150, 120);
        btnTroll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTroll.setBounds(80, 110, 150, 120);
        btnTroll.setDisabledIcon(btnTroll.getIcon());
        btnTroll.addActionListener(e -> {
            selectEnemy("Troll", 5);
        });

        contentLabel.add(btnTroll);
        contentLabel.setComponentZOrder(btnTroll, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de la Maceta a la GUI.
     */
    public void flowerpot() {
        btnFlowerpot = new PixelButton("/Resources/enemy/flowerpot/flowerpot.jpg",
           140, 140);
        btnFlowerpot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlowerpot.setBounds(90, 270, 150, 150);
        btnFlowerpot.setDisabledIcon(btnFlowerpot.getIcon());
        btnFlowerpot.addActionListener(e -> {
            selectEnemy("Flowerpot", 4);
        });

        contentLabel.add(btnFlowerpot);
        contentLabel.setComponentZOrder(btnFlowerpot, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón del Calamar Amarillo a la GUI.
     */
    public void yellowSquid() {
        btnYellowSquid = new PixelButton("/Resources/enemy/yellow_squid/static_down.png",
           80, 180);
        btnYellowSquid.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnYellowSquid.setBounds(500, 80, 70, 180);
        btnYellowSquid.setDisabledIcon(btnYellowSquid.getIcon());
        btnYellowSquid.addActionListener(e -> {
            selectEnemy("YellowSquid", 3);
        });

        contentLabel.add(btnYellowSquid);
        contentLabel.setComponentZOrder(btnYellowSquid, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón del Narval a la GUI.
     */
    public void narwhal() {
        btnNarwhal = new PixelButton("/Resources/enemy/Narwhal.png", 150, 150);
        btnNarwhal.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNarwhal.setBounds(480, 270, 150, 150);
        btnNarwhal.setDisabledIcon(btnNarwhal.getIcon());
        btnNarwhal.addActionListener(e -> {
            selectEnemy("Narwhal", 4);
        });

        contentLabel.add(btnNarwhal);
        contentLabel.setComponentZOrder(btnNarwhal, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de "Listo" a la GUI.
     * Permite avanzar a la selección de piso una vez que se han seleccionado dos enemigos.
     */
    public void ready() {
        btnReady = new PixelButton("/Resources/textos/Listo.png", 170, 40);
        btnReady.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReady.setBounds(250, 370, 170, 40);
        btnReady.setDisabledIcon(btnReady.getIcon());
        btnReady.setEnabled(false);
        btnReady.addActionListener(e -> {
            goToFloor();
        });

        contentLabel.add(btnReady);
        contentLabel.setComponentZOrder(btnReady, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Maneja la selección de enemigos.
     * @param enemy Nombre del enemigo seleccionado.
     */
    private void selectEnemy(String enemy, int maxAmount) {
        pendingEnemy = enemy;
        goToAmountSelection(maxAmount);
    }
private void goToAmountSelection(int maxAmount) {
        contentLabel.removeAll();

        showBackground();
        addIceCreamBackground();
        main_Text();
        background();
        textAmount();

        createNumberButtons(maxAmount, amount -> {
            gameControl.addEnemy(pendingEnemy, amount);
            returnFromAmountSelection();   
        });

        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void returnFromAmountSelection() {
        contentLabel.removeAll();

        showBackground();
        addIceCreamBackground();
        addBackBackground();
        backButton();
        main_Text();

        troll();
        flowerpot();
        yellowSquid();
        narwhal();
        ready();

        updateButtonStates();

        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void updateButtonStates() {
        btnTroll.setEnabled(!gameControl.isEnemySelected("Troll"));
        btnFlowerpot.setEnabled(!gameControl.isEnemySelected("Flowerpot"));
        btnYellowSquid.setEnabled(!gameControl.isEnemySelected("YellowSquid"));
        btnNarwhal.setEnabled(!gameControl.isEnemySelected("Narwhal"));

        btnReady.setEnabled(gameControl.hasEnemies());
    }

    /**
     * Muestra la pantalla de selección de piso.
     */
    private void goToFloor() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();
        parent.remove(this);
        parent.add(new FloorGUI(gameControl));
        parent.revalidate();
        parent.repaint();
    }
}