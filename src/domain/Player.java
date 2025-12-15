package domain;

/**
 * Controller vacío para jugadores controlados por humanos
 * El input viene desde la GUI (teclado)
 */
public class Player implements ControllerCream {
    @SuppressWarnings("unused")
    private IceCream player;
    
    /**
     * Asigna el helado controlado por este jugador
     * @param player Helado controlado por el jugador
     */
    @Override
    public void setPlayer(IceCream player) {
        this.player = player;
    }
    
    /**
     * Método vacío, el jugador humano no necesita actualización automática
     */
    @Override
    public void update() {
    }
}