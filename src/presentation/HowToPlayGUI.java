package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase que representa la interfaz gráfica de usuario (GUI) para las instrucciones de cómo jugar.
 * Extiende ResourcesGUI y maneja la secuencia de visualización de las instrucciones.
 */
public class HowToPlayGUI extends ResourcesGUI {

    private Timer gifTimer;

    private JLabel gifLabelMove;
    private JLabel textLabelMove;
    private PixelButton btnContinue1;

    private JLabel gifLabelIce;
    private JLabel textLabelIce;
    private PixelButton btnContinue2;

    private JLabel gifBreak;
    private JLabel textBreak;
    private PixelButton btnContinue3;

    private JLabel gifFruit;
    private JLabel textFruit;
    private PixelButton btnContinue4;

    private JLabel gifdie;
    private JLabel textdie;

    /**
     * Crea la pantalla de instrucciones del juego y configura
     * sus propiedades visuales iniciales.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public HowToPlayGUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));    
        startSequence();
    }

    /**
     * Inicia la secuencia visual del tutorial mostrando una animación inicial
     * antes de presentar la primera instrucción.
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
            gifMove();
            textMove();
            buttonContinue1();
        });

        gifTimer.setRepeats(false);
        gifTimer.start();
    }
    
    /**
     * Muestra el GIF que explica el movimiento del jugador.
     */
    public void gifMove() {
        gifLabelMove = ImageUtils.createAnimatedGifLabel(
            "/Resources/inicio/moveTutorial.gif",
            480, 250,
            100, 100
        );

        contentLabel.add(gifLabelMove);
        contentLabel.setComponentZOrder(gifLabelMove, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto explicativo sobre cómo moverse en el juego.
     */
    public void textMove() {
        textLabelMove = ImageUtils.createScaledImageLabel(
            "/Resources/textos/ComoMoverse.png",
            200, 40, 230, 50
        );

        contentLabel.add(textLabelMove);
        contentLabel.setComponentZOrder(textLabelMove, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón para continuar a la siguiente instrucción del tutorial.
     */
    public void buttonContinue1() {
        btnContinue1 = new PixelButton("/Resources/textos/Continuar.png", 210, 30);
        btnContinue1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinue1.setBounds(240, 380, 210, 30);

        btnContinue1.addActionListener(e -> onContinuePressed1());

        contentLabel.add(btnContinue1);
        contentLabel.setComponentZOrder(btnContinue1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Maneja la transición desde la instrucción de movimiento
     * a la instrucción de congelar.
     */
    private void onContinuePressed1() {
        contentLabel.remove(gifLabelMove);
        contentLabel.remove(textLabelMove);
        contentLabel.remove(btnContinue1);
        contentLabel.revalidate();
        contentLabel.repaint();
        gifIce();
        textIce();
        buttonContinue2();
    }

    /**
     * Muestra el GIF que explica cómo congelar bloques.
     */
    private void gifIce() {
        gifLabelIce = ImageUtils.createAnimatedGifLabel(
            "/Resources/inicio/iceTutorial.gif",
            480, 250,
            100, 100
        );

        contentLabel.add(gifLabelIce);
        contentLabel.setComponentZOrder(gifLabelIce, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto explicativo sobre cómo congelar.
     */
    private void textIce() {
        textLabelIce = ImageUtils.createScaledImageLabel(
            "/Resources/textos/ComoCongelar.png",
            200, 40, 230, 50
        );

        contentLabel.add(textLabelIce);
        contentLabel.setComponentZOrder(textLabelIce, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón para continuar a la instrucción de romper bloques.
     */
    private void buttonContinue2() {
        btnContinue2 = new PixelButton("/Resources/textos/Continuar.png", 210, 30);
        btnContinue2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinue2.setBounds(240, 380, 210, 30);

        btnContinue2.addActionListener(e -> onContinuePressed2());

        contentLabel.add(btnContinue2);
        contentLabel.setComponentZOrder(btnContinue2, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Maneja la transición desde la instrucción de congelar
     * a la instrucción de romper bloques.
     */
    private void onContinuePressed2() {
        contentLabel.remove(gifLabelIce);
        contentLabel.remove(textLabelIce);
        contentLabel.remove(btnContinue2);
        contentLabel.revalidate();
        contentLabel.repaint();
        breakIce();
        textBreak();
        buttonContinue3();
    }

    /**
     * Muestra el GIF que explica cómo romper bloques.
     */
    private void breakIce() {
        gifBreak = ImageUtils.createAnimatedGifLabel(
            "/Resources/inicio/BreakGif.gif",
            480, 250,
            100, 100
        );

        contentLabel.add(gifBreak);
        contentLabel.setComponentZOrder(gifBreak, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto explicativo sobre cómo romper bloques.
     */
    private void textBreak() {
        textBreak = ImageUtils.createScaledImageLabel(
            "/Resources/textos/ComoRomper.png",
            230, 40, 230, 50
        );

        contentLabel.add(textBreak);
        contentLabel.setComponentZOrder(textBreak, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón para continuar a la instrucción de comer frutas.
     */
    private void buttonContinue3() {
        btnContinue3 = new PixelButton("/Resources/textos/Continuar.png", 210, 30);
        btnContinue3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinue3.setBounds(240, 380, 210, 30);

        btnContinue3.addActionListener(e -> onContinuePressed3());

        contentLabel.add(btnContinue3);
        contentLabel.setComponentZOrder(btnContinue3, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Maneja la transición desde la instrucción de romper bloques
     * a la instrucción de comer frutas.
     */
    private void onContinuePressed3() {
        contentLabel.remove(gifBreak);
        contentLabel.remove(textBreak);
        contentLabel.remove(btnContinue3);
        contentLabel.revalidate();
        contentLabel.repaint();
        eatFruit();
        textFruit();
        buttonContinue4();
    }

    /**
     * Muestra el GIF que explica cómo comer frutas.
     */
    private void eatFruit() {
        gifFruit = ImageUtils.createAnimatedGifLabel(
            "/Resources/inicio/gifFruit.gif",
            480, 250,
            100, 100
        );

        contentLabel.add(gifFruit);
        contentLabel.setComponentZOrder(gifFruit, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto explicativo sobre cómo comer frutas.
     */
    private void textFruit() {
        textFruit = ImageUtils.createScaledImageLabel(
            "/Resources/textos/ComoComer.png",
            230, 40, 230, 50
        );

        contentLabel.add(textFruit);
        contentLabel.setComponentZOrder(textFruit, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón para continuar a la instrucción final del tutorial.
     */
    private void buttonContinue4() {
        btnContinue4 = new PixelButton("/Resources/textos/Continuar.png", 210, 30);
        btnContinue4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinue4.setBounds(240, 380, 210, 30);

        btnContinue4.addActionListener(e -> onContinuePressed4());

        contentLabel.add(btnContinue4);
        contentLabel.setComponentZOrder(btnContinue4, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Maneja la transición hacia la instrucción de muerte del jugador.
     */
    private void onContinuePressed4() {
        contentLabel.remove(gifFruit);
        contentLabel.remove(textFruit);
        contentLabel.remove(btnContinue4);
        contentLabel.revalidate();
        contentLabel.repaint();
        die();
        textdie();
    }

    /**
     * Muestra el GIF que explica cómo el jugador puede morir.
     */
    private void die() {
        gifdie = ImageUtils.createAnimatedGifLabel(
            "/Resources/inicio/DieGif.gif",
            480, 250,
            100, 100
        );

        contentLabel.add(gifdie);
        contentLabel.setComponentZOrder(gifdie, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto explicativo sobre cómo ocurre la muerte del jugador.
     */
    private void textdie() {
        textdie = ImageUtils.createScaledImageLabel(
            "/Resources/textos/ComoMorir.png",
            230, 40, 230, 50
        );

        contentLabel.add(textdie);
        contentLabel.setComponentZOrder(textdie, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
}