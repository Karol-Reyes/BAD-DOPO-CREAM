package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase de la interfaz gráfica de usuario (GUI) para el menú principal.
 * Extiende ResourcesGUI e incluye un botón para iniciar el juego o acceder a la ayuda.
 */
public class MenuGUI extends ResourcesGUI {

    private Timer gifTimer;
    private final GameControl gameControl;

    /**
     * Constructor de la clase MenuGUI.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MenuGUI() {
        this.gameControl = new GameControl();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));    
        startSequence();
    }

    /**
     * Inicia la secuencia de animación inicial mostrando primero el gif
     * y luego el fondo principal junto con el logo y el botón de inicio.
     */
    @Override
    protected void startSequence() {
        startChocolateGif();
        
        int chocolateDuration = 1700;

        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addLogo();
            addButton();
        });

        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Agrega el logo del juego a la GUI.
     */
    public void addLogo() {
        JLabel logo = ImageUtils.createScaledImageLabel("/Resources/inicio/logo.png", 640, 640, 25, -60);
        contentLabel.add(logo);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Agrega el botón de inicio a la GUI.
     */
    public void addButton() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/boton_star.png", 170, 90);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(260, 490, 170, 90);

        btnStart.addActionListener(e -> {
            String[] options = {"Play", "Help", "Settings"};
            
            int selection = JOptionPane.showOptionDialog(
                this,
                null,
                null,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (selection == 0) {
                switchToCharacterSelection();
            }
            if (selection == 1) {
                howToPlay();
            }
        });

        contentLabel.add(btnStart);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Cambia a la pantalla de selección de personaje.
     */
    private void switchToCharacterSelection() {

        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);
            IceCreamGUI iceCreamPanel = new IceCreamGUI(gameControl);
            parent.add(iceCreamPanel);

            parent.revalidate();
            parent.repaint();
        }
    }

    /**
     * Muestra la pantalla de "Cómo jugar".
     */
    private void howToPlay() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);
            HowToPlayGUI howToPlayPanel = new HowToPlayGUI();
            parent.add(howToPlayPanel);

            parent.revalidate();
            parent.repaint();
        }
    }
}
