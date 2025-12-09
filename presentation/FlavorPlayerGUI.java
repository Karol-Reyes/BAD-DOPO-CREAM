package presentation;

import java.awt.*;
import javax.swing.*;

public class FlavorPlayerGUI extends ResourcesGUI {

    private Timer gifTimer;
    private static final int buttonWidth = 100;
    private static final int buttonHeight = 120;

    private final GameControl gameControl;
    
    public FlavorPlayerGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    @Override
    protected void startSequence() {
        startChocolateGif();

        int chocolateDuration = 1700;
        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addIceCreamBackground();
            addBackBackground();
            backButton();
            coneImage(265,200,205,189);
            main_Text();
            vanillaButton();
            strawberryButton();
            chocolateButton();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    
    
    public void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/seleccionarSabor.png", 320, 30, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void vanillaButton() {
        PixelButton btnStart = new PixelButton("/Resources/seleccion_jugador/vainilla.png", buttonWidth, buttonHeight);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(290, 110, buttonWidth, buttonHeight);
        btnStart.addActionListener(e -> {
            gameControl.setCharacter1("Vanilla");
            levelSelection();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void strawberryButton() {
        PixelButton btnStart = new PixelButton("/Resources/seleccion_jugador/fresa.png", buttonWidth, buttonHeight);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(372, 97, buttonWidth, buttonHeight);
        btnStart.addActionListener(e -> {
            gameControl.setCharacter1("Strawberry");
            levelSelection();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void chocolateButton() {
        PixelButton btnStart = new PixelButton("/Resources/seleccion_jugador/chocolate.png", buttonWidth, buttonHeight);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(208, 97, buttonWidth, buttonHeight);
        btnStart.addActionListener(e -> {
            gameControl.setCharacter1("Chocolate");
            levelSelection();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void levelSelection() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);

            LevelSelectionGUI levelSelectionPanel = new LevelSelectionGUI(gameControl);
            parent.add(levelSelectionPanel);

            parent.revalidate();
            parent.repaint();
        }
    }

}