package presentation;

import java.awt.*;
import javax.swing.*;

public class PixelButton extends JButton {
    
    private final ImageIcon originalIcon;

    public PixelButton(String imagePath, int width, int height) {
        originalIcon = new ImageIcon(getClass().getResource(imagePath));

        Image scaledImg = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        configureButton(scaledIcon);
    }

    private void configureButton(ImageIcon icon) {
        setIcon(icon);

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }
}