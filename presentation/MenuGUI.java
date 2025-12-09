package presentation;

import java.awt.*;
import javax.swing.*;

public class MenuGUI extends ResourcesGUI {

    private Timer gifTimer;
    private final GameControl gameControl;

    public MenuGUI() {
        this.gameControl = new GameControl();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));    
        startSequence();
    }

    @Override
    protected void startSequence() {
        startChocolateGif();
        
        int chocolateDuration = 1700;

        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addLogo();
            addButton();
        });

        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    public void addLogo() {
        JLabel logo = ImageUtils.createScaledImageLabel("/Resources/inicio/logo.png", 640, 640, 25, -60);
        contentLabel.add(logo);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void addButton() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/boton_star.png", 170, 90);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(260, 490, 170, 90);

        btnStart.addActionListener(e -> {
            String[] options = {"Play", "Help", "Settings"};
            
            int selection = JOptionPane.showOptionDialog(
                this,
                null,
                null,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (selection == 0) {
                switchToCharacterSelection();
            }
            if (selection == 1) {
                howToPlay();
            }
        });

        contentLabel.add(btnStart);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void switchToCharacterSelection() {

        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);
            IceCreamGUI iceCreamPanel = new IceCreamGUI(gameControl);
            parent.add(iceCreamPanel);

            parent.revalidate();
            parent.repaint();
        }
    }

    private void howToPlay() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);
            HowToPlayGUI howToPlayPanel = new HowToPlayGUI();
            parent.add(howToPlayPanel);

            parent.revalidate();
            parent.repaint();
        }
    }
}
