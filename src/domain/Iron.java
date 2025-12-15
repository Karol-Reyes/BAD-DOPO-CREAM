package domain;

/**
 * Representa un bloque de hierro en el mapa del juego.
 */
public class Iron extends Boxy {

    /**
     * Crea un bloque de hierro en una posición inicial.
     * @param pos posición del bloque
     * @param state estado inicial del bloque
     */
    public Iron(Position pos, BoxState state) {
        super(BoxType.iron, pos, state);
    }

    /**
     * Indica si el bloque permite ser atravesado.
     * @return false siempre
     */
    @Override
    public boolean canWalk() {
        return false;
    }

    /**
     * Indica si el bloque puede destruirse.
     * @return false siempre
     */
    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    /**
     * Indica si el bloque puede crearse.
     * @return true siempre
     */
    @Override
    public boolean canBeCreated() {
        return true;
    }

    /**
     * Marca el bloque como indestructible.
     */
    @Override
    public void create() {
        if (state != BoxState.indestructible) {
            super.indestructible();
        }
    }

    /**
     * Responde al evento de congelación.
     */
    @Override
    public void onFreeze() {
    }

    /**
     * Actualiza el estado del bloque.
     */
    @Override
    public void update() {
    }
}