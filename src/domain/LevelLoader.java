package domain;
import java.io.*;

/**
 * Clase responsable de cargar niveles desde archivos y aplicar configuraciones.
 */
public class LevelLoader {

    /**
     * Carga un nivel desde archivo y aplica la configuración indicada.
     * @param level número del nivel a cargar
     * @param cfg configuración del juego
     * @return instancia del juego lista para jugar o null si falla
     */
    public static BadIceCream loadLevel(int level, GameConfig cfg) {
        String file = "mapa" + level + ".txt";
        String baseMap = readMap(file);

        if (baseMap.isEmpty()) {
            return null;
        }

        String finalMap = applyConfig(baseMap, cfg);
        return MapParser.parseMap(finalMap, cfg);
    }

    /**
     * Aplica la configuración del juego sobre el mapa base.
     * @param map mapa original
     * @param cfg configuración del juego
     * @return mapa modificado según la configuración
     */
    public static String applyConfig(String map, GameConfig cfg) {
        String result = map;

        result = result.replace('X', charPlayer(cfg.getCharacter1()));

        result = result.replace(
                'Y',
                cfg.getCharacter2() == null || cfg.getCharacter2().equals("null")
                        ? '0'
                        : charPlayer(cfg.getCharacter2())
        );
        return result;
    }

    /**
     * Obtiene el carácter asociado a un jugador.
     * @param name nombre del personaje
     * @return letra representativa del personaje
     */
    private static char charPlayer(String name) {
        if (name == null) return '0';
        return switch (name) {
            case "Chocolate" -> 'C';
            case "Strawberry" -> 'S';
            case "Vanilla" -> 'V';
            case "Hungry" -> 'R';
            case "Fearful" -> 'J';
            case "Expert" -> 'E';
            default -> '0';
        };
    }

    /**
     * Lee un mapa desde el directorio de recursos.
     * @param file nombre del archivo del mapa
     * @return contenido del mapa o cadena vacía si falla
     */
    public static String readMap(String file) {
        StringBuilder text = new StringBuilder();

        try (InputStream is = LevelLoader.class.getResourceAsStream("/Resources/maps/" + file)) {
            if (is == null) {
                return "";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (Exception e) {
            return "";
        }
        return text.toString();
    }
}