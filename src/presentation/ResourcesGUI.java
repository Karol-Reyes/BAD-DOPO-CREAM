package presentation;

import java.awt.*;
import java.util.function.IntConsumer;

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

    //Botonones de juego y cantidad
    public PixelButton btn1;
    public PixelButton btn2;
    public PixelButton btn3;
    public PixelButton btn4;
    public PixelButton btn5;
    public PixelButton btn6;
    public PixelButton btn7;
    public PixelButton btn8;
    public PixelButton btn9;
    public PixelButton btn10;
    public PixelButton btn11;
    public PixelButton btn12;
    public PixelButton btn13;
    public PixelButton btn14;
    public PixelButton btn15;
    public PixelButton btn16;
    public PixelButton btn17;
    public PixelButton btn18;
    public PixelButton btn19;
    public PixelButton btn20;

    public PixelButton btnPlay;
    public PixelButton btnHelp;
    public PixelButton btnImport;

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

    /*
    * crea el fondo 
    */
    public void background() {
        JLabel backgroundLabel = ImageUtils.createScaledImageLabel("/Resources/inicio/fondo_opciones.png"
        , 540, 300, 75, 220);
        contentLabel.add(backgroundLabel);
        contentLabel.setComponentZOrder(backgroundLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Crea el botón de jugar y lo añade al panel.
     */
    public void buttonPlay() {
        btnPlay = new PixelButton("/Resources/textos/Jugar.png", 120, 70);
        btnPlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPlay.setBounds(290, 250, 120, 70);
        btnPlay.addActionListener(e -> {
            playGame();
        });

        contentLabel.add(btnPlay);
        contentLabel.setComponentZOrder(btnPlay, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    /**
     * Crea el botón de ayuda y lo añade al panel.
     */
    public void buttonHelp() {
        btnHelp = new PixelButton("/Resources/textos/Ayuda.png", 120, 70);
        btnHelp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHelp.setBounds(290, 330, 120, 70);
        btnHelp.addActionListener(e -> {
            help();
        });

        contentLabel.add(btnHelp);
        contentLabel.setComponentZOrder(btnHelp, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    /**
     * Crea el botón de importar y lo añade al panel.
     */
    public void buttonImport() {
        btnImport = new PixelButton("/Resources/textos/Importar.png", 160, 70);
        btnImport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImport.setBounds(270, 410, 160, 70);
        btnImport.addActionListener(e -> {
           importGame();
        });

        contentLabel.add(btnImport);
        contentLabel.setComponentZOrder(btnImport, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    /*
    * crea el boton de jugar    
    */
    public void playGame() {
    
    } 

    /*
    * muestra la ayuda
    */
    public void help() {
        
    }

    /*
    * muestra la opcion de importar
    */
    public void importGame() {

    }
    
    /**
     * Crea botones numerados del 1 al máximo especificado y los añade al panel.
     * @param max Número máximo para los botones (inclusive).
     * @param onSelect Función que se ejecuta al seleccionar un botón, recibiendo el número del botón como argumento.
     */
    protected void createNumberButtons(
        int max,
        IntConsumer onSelect
    ) {
        int startX = 200;   
        int startY = 290;  

        int buttonSize = 35; 
        int gapX = 30;       
        int gapY = 20;       

        int columns = 5;

        for (int i = 0; i < max; i++) {
            int value = i + 1;

            int col = i % columns;
            int row = i / columns;

            int x = startX + col * (buttonSize + gapX);
            int y = startY + row * (buttonSize + gapY);

            PixelButton btn = new PixelButton(
                "/Resources/numeros/" + value + ".png",
                buttonSize,
                buttonSize
            );

            btn.setBounds(x, y, buttonSize, buttonSize);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
            btn.addActionListener(e -> onSelect.accept(value));

            contentLabel.add(btn);
            contentLabel.setComponentZOrder(btn, 0);
        }

        contentLabel.revalidate();
        contentLabel.repaint();
    }

    /**
     * Muestra el texto para seleccionar la cantidad de frutas.
     */
    public void textAmount() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/EscogerCantidad.png",
         180, 50, 250, 235);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
}