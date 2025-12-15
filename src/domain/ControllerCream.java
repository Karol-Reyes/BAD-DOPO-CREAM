package domain;

/**
 * Interfaz para controlar el comportamiento de un IceCream
 */
public interface ControllerCream {
    /**
     * Actualiza el estado y toma decisiones de movimiento
     */
    void update();
    
    /**
     * Establece el jugador que será controlado
     */
    void setPlayer(IceCream player);
}