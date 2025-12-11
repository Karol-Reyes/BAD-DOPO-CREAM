package presentation;

import java.awt.*;
import javax.swing.*;

public class FruitsGUI extends ResourcesGUI {
    private Timer gifTimer;
    private String selectedFruit1 = null;
    private String selectedFruit2 = null;

    private PixelButton btnBanana;
    private PixelButton btnGrape;
    private PixelButton btnCheery;
    private PixelButton btnPineapple;
    private PixelButton btnCactus;
    private PixelButton btnOnly1;

    private final GameControl gameControl;

    public FruitsGUI(GameControl gameControl) {
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
            banana();
            grape();
            cherry();
            pineapple();
            cactus();
            only1();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/EscogerFruta.png",
           320, 40, 180, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void banana() {
        btnBanana = new PixelButton("/Resources/fruit/Banana.jpg", 80, 80);
        btnBanana.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBanana.setBounds(80, 170, 80, 80);
        btnBanana.setDisabledIcon(btnBanana.getIcon());
        btnBanana.addActionListener(e -> {
            selectFruit("Banana");
        });

        contentLabel.add(btnBanana);
        contentLabel.setComponentZOrder(btnBanana, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void grape() {
        btnGrape = new PixelButton("/Resources/fruit/Grape.jpg", 80, 80);
        btnGrape.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGrape.setBounds(220, 170, 80, 80);
        btnGrape.setDisabledIcon(btnGrape.getIcon());
        btnGrape.addActionListener(e -> {
            selectFruit("Grape");
        });

        contentLabel.add(btnGrape);
        contentLabel.setComponentZOrder(btnGrape, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void cherry() {
        btnCheery = new PixelButton("/Resources/fruit/Cherry.jpg", 80, 80);
        btnCheery.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCheery.setBounds(360, 170, 80, 80);
        btnCheery.setDisabledIcon(btnCheery.getIcon());
        btnCheery.addActionListener(e -> {
            selectFruit("Cherry");
        });

        contentLabel.add(btnCheery);
        contentLabel.setComponentZOrder(btnCheery, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void pineapple() {
        btnPineapple = new PixelButton("/Resources/fruit/Pineapple.jpg", 80, 80);
        btnPineapple.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPineapple.setBounds(500, 170, 80, 80);
        btnPineapple.setDisabledIcon(btnPineapple.getIcon());
        btnPineapple.addActionListener(e -> {
            selectFruit("Pineapple");
        });

        contentLabel.add(btnPineapple);
        contentLabel.setComponentZOrder(btnPineapple, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void cactus() {
        btnCactus = new PixelButton("/Resources/fruit/cactus.png", 80, 80);
        btnCactus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCactus.setBounds(305, 250, 80, 100);
        btnCactus.setDisabledIcon(btnCactus.getIcon());
        btnCactus.addActionListener(e -> {
            selectFruit("Cactus");
        });

        contentLabel.add(btnCactus);
        contentLabel.setComponentZOrder(btnCactus, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void only1() {
        btnOnly1 = new PixelButton("/Resources/textos/Solo1.png", 170, 40);
        btnOnly1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOnly1.setBounds(250, 370, 170, 40);
        btnOnly1.setDisabledIcon(btnOnly1.getIcon());
        btnOnly1.addActionListener(e -> {
            selectFruit("Only1");
        });

        contentLabel.add(btnOnly1);
        contentLabel.setComponentZOrder(btnOnly1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void selectFruit(String fruit) {
        if (selectedFruit1 == null) {
            selectedFruit1 = fruit;
            gameControl.setSelectedFruit1(fruit);
            disableButtons(fruit);
        } else if (selectedFruit2 == null) {
            selectedFruit2 = fruit;
            gameControl.setSelectedFruit2(fruit);
            disableButtons(fruit);
        }
        if (selectedFruit1 != null && selectedFruit2 != null) {
            selectEnemy();
        }
    }

    private void disableButtons(String fruit) {
        switch (fruit) {
            case "Banana" -> {
                btnBanana.setEnabled(false);
            }
            case "Grape" -> {
                btnGrape.setEnabled(false);
            }
            case "Cheery" -> {
                btnCheery.setEnabled(false);
            }
            case "Pineapple" -> {
                btnPineapple.setEnabled(false);
            }
            case "Cactus" -> {
                btnCactus.setEnabled(false);
            }
            case "Only1" -> {
                btnOnly1.setEnabled(false);
            }
        }
    }

    private void selectEnemy() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);

            EnemyGUI selectEnemyPanel = new EnemyGUI(gameControl);
            parent.add(selectEnemyPanel);

            parent.revalidate();
            parent.repaint();
        }
    }
}