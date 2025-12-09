package presentation;

import java.awt.*;
import javax.swing.*;

public class IntroGUI {

    private final JLabel gifLabel;
    private final Runnable menuCallback;
    private static final int SCREEN_WIDTH = 690;
    private static final int SCREEN_HEIGHT = 690;

    public IntroGUI(JLabel gifLabel, Runnable menuCallback) {
        this.gifLabel = gifLabel;
        this.menuCallback = menuCallback;
    }

    public void startIntro() {
        java.net.URL gifUrl = getClass().getResource("/Resources/inicio/complete_intro.gif");

        ImageIcon introNormalIcon;
        if (gifUrl != null) {
            introNormalIcon = new ImageIcon(gifUrl);
        } else {
            introNormalIcon = new ImageIcon("src/Resources/inicio/complete_intro.gif");
        }

        Image scaledImage = introNormalIcon.getImage().getScaledInstance(
            SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_DEFAULT);

        gifLabel.setIcon(new ImageIcon(scaledImage));
        
        gifLabel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        int normalIntroDuration = 13000;

        Timer timer2 = new Timer(normalIntroDuration, e -> menuCallback.run());
        timer2.setRepeats(false);
        timer2.start();
    }
}
