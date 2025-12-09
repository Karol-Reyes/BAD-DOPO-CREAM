package domain;

/**
 * Interfaz para obtener información de sprites de cualquier entidad del juego.
 * Permite que la capa de presentación obtenga sprites sin conocer tipos concretos.
 */
public interface SpriteProvider {
    
    /**
     * Obtiene la clave del sprite actual según el estado del objeto.
     * Formato: "tipo_estado" (ej: "player_up", "troll_down", "banana", "ice_created")
     */
    String getSpriteKey();
    
    /**
     * Indica si el sprite actual debe ser animado (GIF) o estático (PNG).
     * @return true para GIF animado, false para PNG estático
     */
    boolean isAnimated();
}