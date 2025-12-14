package presentation;
 
import java.awt.*;
import javax.swing.*;
 
/**
 * Crea la interfaz gráfica para la selección de nivel del juego.
 * Extiende ResourcesGUI e incluye botones para seleccionar entre tres niveles.
 */
public class LevelSelectionGUI extends ResourcesGUI {
    private Timer gifTimer;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 100;
    private static final int BUTTON_Y = 210;
 
    private final GameControl gameControl;
 
    /**
     * Crea la interfaz gráfica para la selección de nivel del juego.
     * @param gameControl controlador principal del juego
     */
    public LevelSelectionGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }
 
    /**
     * Inicia la secuencia visual inicial mostrando una animación
     * y posteriormente los elementos de selección de nivel.
     */
    @Override
    protected void startSequence() {
        startChocolateGif();
 
        int chocolateDuration = 1700;
        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addLevelBackground();
            addBackBackground();
            backButton();
            levelText();
            levelOneButton();
            levelTwoButton();
            levelThreeButton();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }
 
    /**
     * Añade el fondo gráfico que contiene las opciones de niveles.
     */
    public void addLevelBackground() {
        JLabel backgroundLabel = ImageUtils.createScaledImageLabel(
                "/Resources/inicio/fondo_opciones.png", 640, 280, 20, 125);
        contentLabel.add(backgroundLabel);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    /**
     * Muestra el texto indicativo para la selección de nivel.
     */
    private void levelText() {
        JLabel textLabel = ImageUtils.createScaledImageLabel(
                "/Resources/textos/seleccionNivel.png", 320, 30, 175, 135);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    /**
     * Crea y añade el botón para seleccionar el nivel uno.
     */
    public void levelOneButton() {
        PixelButton btnStart = new PixelButton(
                "/Resources/inicio/botonUno.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(95, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.addActionListener(e -> {
            gameControl.setSelectedLevel(1);
            thefruits();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    /**
     * Crea y añade el botón para seleccionar el nivel dos.
     */
    private void levelTwoButton() {
        PixelButton btnStart = new PixelButton(
                "/Resources/inicio/botonDos.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(290, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.addActionListener(e -> {
            gameControl.setSelectedLevel(2);
            thefruits();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    /**
     * Crea y añade el botón para seleccionar el nivel tres.
     */
    private void levelThreeButton() {
        PixelButton btnStart = new PixelButton(
                "/Resources/inicio/botonTres.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(485, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnStart.addActionListener(e -> {
            gameControl.setSelectedLevel(3);
            thefruits();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    /**
     * Cambia a la pantalla de selección de frutas una vez
     * que el nivel ha sido seleccionado.
     */
    private void thefruits(){
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
 
        Container parent = getParent();
 
        if (parent != null) {
            parent.remove(this);
 
            FruitsGUI chooseFruits = new FruitsGUI(gameControl);
            parent.add(chooseFruits);
 
            parent.revalidate();
            parent.repaint();
        }
    }
}