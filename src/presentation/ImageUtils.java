package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Clase con utilidades para manejar imágenes en la interfaz gráfica.
 */
public class ImageUtils {

    /**
     * Crea un JLabel con una imagen escalada.
     * @param path Ruta de la imagen.
     * @param width Ancho deseado.
     * @param height Alto deseado.
     * @param x Posición X del JLabel.
     * @param y Posición Y del JLabel.
     * @return JLabel con la imagen escalada.
     */
    public static JLabel createScaledImageLabel(String path, int width, int height, int x, int y) {
        ImageIcon original = new ImageIcon(ImageUtils.class.getResource(path));
        Image scaledImage = original.getImage().getScaledInstance(
            width,
            height,
            Image.SCALE_SMOOTH
        );

        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setBounds(x, y, width, height);

        return label;
    }

    /**
     * Crea un JLabel con un GIF animado escalado.
     * @param path Ruta del GIF.
     * @param width Ancho deseado.
     * @param height Alto deseado.
     * @param x Posición X del JLabel.
     * @param y Posición Y del JLabel.
     * @return JLabel con el GIF animado escalado.
     */
    public static JLabel createAnimatedGifLabel(String path, int width, int height, int x, int y) {
        ImageIcon gif = new ImageIcon(ImageUtils.class.getResource(path));

        JLabel label = new JLabel(gif) {
            @Override
            protected void paintComponent(Graphics g) {
                // Escala dinámicamente sin romper animación
                g.drawImage(gif.getImage(), 0, 0, width, height, this);
            }
        };

        label.setBounds(x, y, width, height);
        return label;
    }
}
