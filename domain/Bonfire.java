package domain;

/**
 * Representa un bloque tipo caja dentro del mapa del juego. Maneja su tipo,
 * estado y posición, así como las operaciones básicas de creación y destrucción.
 */
public class Bonfire extends Boxy {

    /**
     * Constructor segun una posicion dada
     * @param position del hielo
     */
    public Bonfire(Position position, BoxState state) {
        super(BoxType.bonfire, position, state);
    }

    /**
     * Indica si se puede caminar sobre el bloque.
     * @return true si se puede caminar, false en caso contrario
     */
    public boolean canWalk() {
        return false;
    }

    /**
     * Crea el fuego del bloque.
     */
    @Override
    public void on() {
        if (state != BoxState.on) {
            super.on();
        }
    }

    /**
     * Destruye fuego del bloque.
     */
    @Override
    public void off(){
        if (state != BoxState.off) {
            super.off();
        }
    }
}