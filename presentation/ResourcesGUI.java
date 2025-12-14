package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase base para las interfaces gráficas de usuario (GUI) que manejan recursos visuales comunes.
 * Proporciona funcionalidades para mostrar animaciones iniciales, fondos y botones estándar.
 */
public abstract class ResourcesGUI extends JPanel {

    protected JLabel contentLabel;
    private Timer gifTimer;
    protected static final int SCREEN_WIDTH = 690;
    protected static final int SCREEN_HEIGHT = 690;

    /**
     * Crea el panel base de recursos gráficos e inicia la secuencia visual inicial.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ResourcesGUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        startSequence();
    }

    /**
     * Inicia la secuencia de animación inicial mostrando primero el gif
     * y luego el fondo principal tras un tiempo determinado.
     */
    protected void startSequence() {
        startChocolateGif();
        
        int chocolateDuration = 1700;
        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    /**
     * Muestra el gif inicial animado ocupando toda la pantalla.
     */
    public void startChocolateGif() {
        java.net.URL gifUrl = getClass().getResource("/Resources/inicio/chocolate.gif");
        ImageIcon chocolateIcon;
        
        if (gifUrl != null) {
            chocolateIcon = new ImageIcon(gifUrl);
        } else {
            chocolateIcon = new ImageIcon("src/Resources/inicio/chocolate.gif");
        }
        
        contentLabel = new JLabel();
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentLabel.setVerticalAlignment(JLabel.CENTER);
        
        SwingUtilities.invokeLater(() -> {
            Image scaledImage = chocolateIcon.getImage().getScaledInstance(
                SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_DEFAULT
            );
            contentLabel.setIcon(new ImageIcon(scaledImage));
        });
        
        add(contentLabel, BorderLayout.CENTER);
    }

    /**
     * Muestra la imagen de fondo principal una vez finalizada la animación inicial.
     */
    public void showBackground() {
        removeAll();
        setLayout(new BorderLayout());
        
        java.net.URL backgroundUrl = getClass().getResource("/Resources/inicio/Multimedia.jpg");
        ImageIcon backgroundIcon;
        backgroundIcon = new ImageIcon(backgroundUrl);
        
        Image scaledBackground = backgroundIcon.getImage().getScaledInstance(
            SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_SMOOTH
        );
        
        contentLabel = new JLabel(new ImageIcon(scaledBackground));
        contentLabel.setLayout(null);
        add(contentLabel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    /**
     * Añade el fondo visual correspondiente a la selección de helado.
     */
    public void addIceCreamBackground() {
        JLabel backgroundLabel = ImageUtils.createScaledImageLabel(
                "/Resources/inicio/fondo_eleccion.png", 640, 420, 20, 35);
        contentLabel.add(backgroundLabel);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Añade el fondo visual del botón de regreso.
     */
    public void addBackBackground() {
        JLabel backgroundLabel = ImageUtils.createScaledImageLabel(
                "/Resources/inicio/fondo_back.png", 540, 150, 75, 465);
        contentLabel.add(backgroundLabel);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea y añade el botón para regresar al menú principal.
     */
    public void backButton() {
        PixelButton btnBack = new PixelButton("/Resources/inicio/boton_back.png", 180, 80);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setBounds(245, 500, 180, 80);
        btnBack.addActionListener(e -> {
            returnToMenu();
        });

        contentLabel.add(btnBack);
        contentLabel.setComponentZOrder(btnBack, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Retorna al menú principal y detiene cualquier animación activa.
     */
    private void returnToMenu() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
        Container parent = getParent();
        
        if (parent != null) {
            parent.remove(this);
            
            MenuGUI menuPanel = new MenuGUI();
            parent.add(menuPanel);         
            parent.revalidate();
            parent.repaint();
        }
    }

    /**
     * Muestra el texto principal del menú de selección.
     */
    public void mainText() {
        JLabel textLabel = ImageUtils.createScaledImageLabel(
                "/Resources/textos/seleccionar_elmodo.png", 320, 30, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Añade la imagen del cono de helado en la posición y tamaño indicados.
     * @param widthCone ancho del cono
     * @param heightCone alto del cono
     * @param xCone posición horizontal del cono
     * @param yCone posición vertical del cono
     */
    public void coneImage(int widthCone, int heightCone, int xCone, int yCone) {
        JLabel coneLabel = ImageUtils.createScaledImageLabel(
                "/Resources/seleccion_jugador/cono.png",
                widthCone, heightCone, xCone, yCone);
        contentLabel.add(coneLabel);
        contentLabel.setComponentZOrder(coneLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
}