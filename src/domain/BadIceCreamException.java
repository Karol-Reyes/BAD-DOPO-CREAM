package domain;

/**
 * Excepción crítica del juego Bad Ice Cream.
 * Representa errores graves de estado, inicialización o lógica.
 */
public class BadIceCreamException extends RuntimeException {

    // ===== MENSAJES DE ERROR CRÍTICOS =====
    public static final String NULL_MAP = "El GameMap es nulo. El juego no puede inicializarse sin un mapa.";
    public static final String INVALID_PLAYER_INDEX = "Índice de jugador inválido. Posible corrupción del estado del juego.";
    public static final String INVALID_ENEMY_INDEX = "Índice de enemigo inválido.";
    public static final String NULL_ENTITY = "Se intentó registrar una entidad nula en el juego.";
    public static final String INVALID_WAVE_STATE = "Estado inválido de oleadas de frutas.";
    public static final String FRUIT_FAILURE = "Error al generar o actualizar frutas en el juego.";
    public static final String GAME_UPDATE_FAILURE = "Error crítico durante la actualización del juego.";
    public static final String MAP_INCONSISTENT_STATE = "El mapa se encuentra en un estado inconsistente.";

            /**
     * Constructor de la excepción BadIceCreamException.
     * @param message Mensaje de error descriptivo.
     */
    public BadIceCreamException(String message) {
        super(message);
    }

    /**
     * Constructor de la excepción BadIceCreamException con causa.
     * @param message Mensaje de error descriptivo.
     * @param cause Causa raíz de la excepción.
     */
    public BadIceCreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
