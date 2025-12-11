package presentation;

import java.awt.*;
import javax.swing.*;

public class ImageUtils {
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
