package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase que representa la interfaz gráfica para la selección del modo de juego.
 * Extiende ResourcesGUI e incluye botones y textos para elegir entre diferentes modos de juego.
 */
public class IceCreamGUI extends ResourcesGUI {

    private Timer gifTimer;
    private final GameControl gameControl;
    
    /**
     * Crea la interfaz gráfica principal para la selección del modo de juego.
     * @param gameControl controlador principal del juego
     */
    public IceCreamGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * Inicia la secuencia visual inicial mostrando una animación y,
     * posteriormente, las opciones de selección de modo de juego.
     */
    @Override
    protected void startSequence() {
        startChocolateGif();
        
        int chocolateDuration = 1700;
        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addIceCreamBackground();
            addBackBackground();
            buttonSolo();
            buttonTwoPlayers();
            buttonBot();
            backButton();
            mainText();
            textPlayer();
            text1v1();
            textMachine();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }
    
    /**
     * Crea y añade el botón para iniciar el modo de juego en solitario.
     */
    public void buttonSolo() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/One.png", 120, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(50, 90, 120, 300);
        btnStart.addActionListener(e -> {
            gameControl.setGameMode("Solo");
            chooseFlavors();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y añade el botón para iniciar el modo de juego uno contra uno.
     */
    public void buttonTwoPlayers() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/Two.png", 200, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(245, 90, 200, 300);
        btnStart.addActionListener(e -> {
            select1v1();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y añade el botón para iniciar el modo de juego contra la máquina.
     */
    public void buttonBot() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/Trolly.png", 110, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(520, 90, 110, 300);

        btnStart.addActionListener(e -> {
            gameControl.setGameMode("Machine");
            machineMode();
        });
        
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto descriptivo del modo de juego para un solo jugador.
     */
    public void textPlayer() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/jugador.png", 100, 30, 60, 400);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto descriptivo del modo de juego uno contra uno.
     */
    public void text1v1() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/1v1.png", 60, 30, 315, 400);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto descriptivo del modo de juego contra la máquina.
     */
    public void textMachine() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/maquina.png", 90, 30, 525, 395);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    /**
     * Cambia a la pantalla de selección de sabores para el jugador.
     */
    private void chooseFlavors() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
        
        Container parent = getParent();
        
        if (parent != null) {
            parent.remove(this);
            
            FlavorPlayerGUI flavorPanel = new FlavorPlayerGUI(gameControl);
            parent.add(flavorPanel);
            
            parent.revalidate();
            parent.repaint();
        }
    }
    
    /**
     * Cambia a la pantalla de selección del modo uno contra uno.
     */
    private void select1v1() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);

            Mode1v1 mode1v1 = new Mode1v1(gameControl);
            parent.add(mode1v1);

            parent.revalidate();
            parent.repaint();
        }
    }

    /**
     * Cambia a la pantalla de configuración del modo contra la máquina.
     */
    private void machineMode() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);

            MachineModeGUI machineMode = new MachineModeGUI(gameControl);
            parent.add(machineMode);

            parent.revalidate();
            parent.repaint();
        }
    }
}