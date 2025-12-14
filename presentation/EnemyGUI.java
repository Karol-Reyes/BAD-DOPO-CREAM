package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase de la interfaz gráfica de usuario (GUI) para la selección de enemigos.
 * Extiende ResourcesGUI e incluye botones para seleccionar enemigos y avanzar al siguiente paso.
 */
public class EnemyGUI extends ResourcesGUI {
    private Timer gifTimer;
    private String selectedEnemy1 = null;
    private String selectedEnemy2 = null;

    private PixelButton btnTroll;
    private PixelButton btnFlowerPot;
    private PixelButton btnYellowSquid;
    private PixelButton btnNarwhal;
    private PixelButton btnOnly1;

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
            flowerPot();
            yellowSquid();
            narwhal();
            only1();
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
            selectEnemy("Troll");
        });

        contentLabel.add(btnTroll);
        contentLabel.setComponentZOrder(btnTroll, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de la Maceta a la GUI.
     */
    public void flowerPot() {
        btnFlowerPot = new PixelButton("/Resources/enemy/flowerpot/flowerpot.jpg",
           140, 140);
        btnFlowerPot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlowerPot.setBounds(90, 270, 150, 150);
        btnFlowerPot.setDisabledIcon(btnFlowerPot.getIcon());
        btnFlowerPot.addActionListener(e -> {
            selectEnemy("FlowerPot");
        });

        contentLabel.add(btnFlowerPot);
        contentLabel.setComponentZOrder(btnFlowerPot, 0);
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
            selectEnemy("YellowSquid");
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
            selectEnemy("Narwhal");
        });

        contentLabel.add(btnNarwhal);
        contentLabel.setComponentZOrder(btnNarwhal, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón del Solo1 a la GUI.
     */
    public void only1() {
        btnOnly1 = new PixelButton("/Resources/textos/Solo1.png", 170, 40);
        btnOnly1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOnly1.setBounds(260, 250, 170, 40);
        btnOnly1.setDisabledIcon(btnOnly1.getIcon());
        btnOnly1.addActionListener(e -> {
            selectEnemy("Only1");
        });

        contentLabel.add(btnOnly1);
        contentLabel.setComponentZOrder(btnOnly1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Maneja la selección de enemigos.
     * @param enemy Nombre del enemigo seleccionado.
     */
    private void selectEnemy(String enemy) {
        if (selectedEnemy1 == null) {
            selectedEnemy1 = enemy;
            gameControl.setSelectedEnemy1(enemy);
            disableButtons(enemy);
        } else if (selectedEnemy2 == null) {
            selectedEnemy2 = enemy;
            gameControl.setSelectedEnemy2(enemy);
            disableButtons(enemy);
        }
        if (selectedEnemy1 != null && selectedEnemy2 != null) {
            floor();
        }
    }

    /**
     * Deshabilita los botones de los enemigos ya seleccionados.
     * @param enemy Nombre del enemigo a deshabilitar.
     */
    private void disableButtons(String enemy) {
        switch (enemy) {
            case "Troll" -> {
                btnTroll.setEnabled(false);
            }
            case "FlowerPot" -> {
                btnFlowerPot.setEnabled(false);
            }
            case "YellowSquid" -> {
                btnYellowSquid.setEnabled(false);
            }
            case "Narwhal" -> {
                btnNarwhal.setEnabled(false);
            }
        }
    }

    /**
     * Muestra la pantalla de selección de piso.
     */
    private void floor() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);

            FloorGUI selectFloorPanel = new FloorGUI(gameControl);
            parent.add(selectFloorPanel);

            parent.revalidate();
            parent.repaint();
        }
    }
}