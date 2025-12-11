package domain;

/**
 * Estados posibles de una fruta en el juego.
 */
public enum FruitState {
    active,
    eaten,
    teleporting, // estado temporal durante teletransporte (Cherry)
    dangerous //para el cactus
}