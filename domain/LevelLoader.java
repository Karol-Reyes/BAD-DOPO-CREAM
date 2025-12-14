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

        result = result.replace(
                'F',
                cfg.getFruit1() == null || cfg.getFruit1().equals("Only1")
                        ? '0'
                        : charFruit(cfg.getFruit1())
        );

        result = result.replace(
                'G',
                cfg.getFruit2() == null || cfg.getFruit2().equals("Only1")
                        ? '0'
                        : charFruit(cfg.getFruit2())
        );

        result = result.replace(
                'T',
                cfg.getEnemy1() == null || cfg.getEnemy1().equals("Only1")
                        ? '0'
                        : charEnemy(cfg.getEnemy1())
        );

        result = result.replace(
                'N',
                cfg.getEnemy2() == null || cfg.getEnemy2().equals("Only1")
                        ? '0'
                        : charEnemy(cfg.getEnemy2())
        );

        if (null == cfg.getObject()) {
            result = result.replace('K', '0').replace('L', '0');
        } else switch (cfg.getObject()) {
            case "Nothing" -> result = result.replace('K', '0').replace('L', '0');
            case "Fire" -> result = result.replace('L', '0');
            case "Bonfire" -> result = result.replace('K', '0');
            default -> {
            }
        }

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
     * Obtiene el carácter asociado a una fruta.
     * @param name nombre de la fruta
     * @return letra representativa de la fruta
     */
    private static char charFruit(String name) {
        if (name == null) return '0';
        return switch (name) {
            case "Banana" -> 'B';
            case "Grape" -> 'A';
            case "Cherry" -> 'D';
            case "Pineapple" -> 'Z';
            case "Cactus" -> 'Q';
            default -> '0';
        };
    }

    /**
     * Obtiene el carácter asociado a un enemigo.
     * @param name nombre del enemigo
     * @return letra representativa del enemigo
     */
    private static char charEnemy(String name) {
        if (name == null) return '0';
        return switch (name) {
            case "Troll" -> 'O';
            case "FlowerPot" -> 'W';
            case "Narwhal" -> 'P';
            case "YellowSquid" -> 'U';
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