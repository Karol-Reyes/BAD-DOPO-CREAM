package presentation;

import java.awt.*;
import javax.swing.*;

public class FloorGUI extends ResourcesGUI {
    private Timer gifTimer;
    private final GameControl gameControl;

    public FloorGUI(GameControl gameControl) {
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
            main_Text();
            bonfire();
            fire();
            nothing();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/EscogerObjeto.png",
           320, 40, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void bonfire() {
        PixelButton btnBonfire = new PixelButton("/Resources/box/bonfire.png", 140, 140);
        btnBonfire.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBonfire.setBounds(80, 170, 140, 140);
        btnBonfire.setDisabledIcon(btnBonfire.getIcon());
        btnBonfire.addActionListener(e -> {
            gameControl.setSelectedObjet("Bonfire");
            gameplay();
        });

        contentLabel.add(btnBonfire);
        contentLabel.setComponentZOrder(btnBonfire, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void fire() {
        PixelButton btnFire = new PixelButton("/Resources/box/fire.png", 140, 140);
        btnFire.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFire.setBounds(450, 170, 140, 140);
        btnFire.setDisabledIcon(btnFire.getIcon());
        btnFire.addActionListener(e -> {
            gameControl.setSelectedObjet("Fire");
            gameplay();
        });

        contentLabel.add(btnFire);
        contentLabel.setComponentZOrder(btnFire, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void nothing() {
        PixelButton btnNothing = new PixelButton("/Resources/textos/Ninguno.png", 120, 40);
        btnNothing.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNothing.setBounds(290, 330, 120, 40);
        btnNothing.setDisabledIcon(btnNothing.getIcon());
        btnNothing.addActionListener(e -> {
            gameControl.setSelectedObjet("Nothing");
            gameplay();
        });

        contentLabel.add(btnNothing);
        contentLabel.setComponentZOrder(btnNothing, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void gameplay() {
        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);
            gameControl.printSelections();
            GameGUI game = new GameGUI(gameControl);
            gameControl.printSelections();
            parent.add(game);
            parent.revalidate();
            parent.repaint();
        }
    }
}