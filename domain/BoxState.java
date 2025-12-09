package domain;

/**
 * Estados posibles de una caja en el juego.
 */
public enum BoxState {
    //ice
    created,
    destroyed,
    inactive, // estado inicial, no creada aún
    indestructible,
    //fogata
    on,
    off

}
