package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase que representa un botón con una imagen personalizada.
 * Extiende JButton y configura la apariencia del botón para que sea transparente
 */
public class PixelButton extends JButton {
    
    private final ImageIcon originalIcon;

    /**
     * Constructor de la clase PixelButton.
     * @param imagePath Ruta de la imagen a utilizar en el botón.
     * @param width Ancho deseado del botón.
     * @param height Alto deseado del botón.
     */
    public PixelButton(String imagePath, int width, int height) {
        originalIcon = new ImageIcon(getClass().getResource(imagePath));

        Image scaledImg = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        configureButton(scaledIcon);
    }

    /**
     * Configura las propiedades visuales del botón.
     * @param icon Icono a establecer en el botón.
     */
    private void configureButton(ImageIcon icon) {
        setIcon(icon);

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }
}