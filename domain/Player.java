package domain;

/**
 * Controller vacío para jugadores controlados por humanos
 * El input viene desde la GUI (teclado)
 */
public class Player implements ControllerCream {
    private IceCream player;
    
    @Override
    public void setPlayer(IceCream player) {
        this.player = player;
    }
    
    @Override
    public void update() {
        // No hace nada - el jugador se controla desde GUI
    }
}