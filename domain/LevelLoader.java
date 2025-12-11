package domain;


import java.io.*;

public class LevelLoader {
    
    /**
     * Carga un nivel completo desde archivo con configuración aplicada
     * @param nivel Número del nivel (1, 2, 3, etc.)
     * @param config Configuración del juego
     * @return BadIceCream configurado y listo para jugar
     */
    public static BadIceCream cargarNivelCompleto(int nivel, GameConfig config) {
        
        String nombreArchivo = "mapa" + nivel + ".txt";
        // 1. Leer el mapa base desde el archivo
        String mapaOriginal = leerMapaDesdeArchivo(nombreArchivo);
        
        if (mapaOriginal.isEmpty()) {
            System.err.println("No se pudo cargar el mapa " + nombreArchivo);
            return null;
        }
        
        
        // 2. Aplicar la configuración (reemplazar X, Y, F, G, T, N, K, L)
        String mapaConfigurado = aplicarConfiguracionAlMapa(mapaOriginal, config);

        // 3. Parsear el mapa y crear el juego
        BadIceCream game = MapParser.parseMap(mapaConfigurado, config);
        
        if (game != null) {
            System.out.println("Nivel " + nivel + " cargado exitosamente\n");
        }
        
        return game;
    }
    
    /**
     * Aplica la configuración del juego al mapa original
     */
    public static String aplicarConfiguracionAlMapa(String mapaOriginal, GameConfig config) {
        String mapa = mapaOriginal;
        
        // ========== JUGADOR 1 ==========
        char letraP1 = getLetraPersonaje(config.getCharacter1());
        mapa = mapa.replace('X', letraP1);
        
        // ========== JUGADOR 2 ==========
        if (config.getCharacter2() == null || config.getCharacter2().equals("null")) {
            mapa = mapa.replace('Y', '0');
        } else {
            char letraP2 = getLetraPersonaje(config.getCharacter2());
            mapa = mapa.replace('Y', letraP2);
        }
        
        // ========== FRUTA 1 ==========
        if (config.getFruit1() == null || config.getFruit1().equals("Only1")) {
            mapa = mapa.replace('F', '0');
        } else {
            char letraF1 = getLetraFruta(config.getFruit1());
            mapa = mapa.replace('F', letraF1);
        }
        
        // ========== FRUTA 2 ==========
        if (config.getFruit2() == null || config.getFruit2().equals("Only1")) {
            mapa = mapa.replace('G', '0');
        } else {
            char letraF2 = getLetraFruta(config.getFruit2());
            mapa = mapa.replace('G', letraF2);
        }
        
        // ========== ENEMIGO 1 ==========
        if (config.getEnemy1() == null || config.getEnemy1().equals("Only1")) {
            mapa = mapa.replace('T', '0');
        } else {
            char letraE1 = getLetraEnemigo(config.getEnemy1());
            mapa = mapa.replace('T', letraE1);
        }
        
        // ========== ENEMIGO 2 ==========
        if (config.getEnemy2() == null || config.getEnemy2().equals("Only1")) {
            mapa = mapa.replace('N', '0');
        } else {
            char letraE2 = getLetraEnemigo(config.getEnemy2());
            mapa = mapa.replace('N', letraE2);
        }
        
        // ========== OBSTÁCULOS ==========
        if (config.getObject() == null || config.getObject().equals("Nothing")) {
            // Eliminar ambos obstáculos
            mapa = mapa.replace('K', '0');
            mapa = mapa.replace('L', '0');
        } else if (config.getObject().equals("Fire")) {
            // Solo mantener K (Fire), eliminar L (Bonfire)
            mapa = mapa.replace('L', '0');
        } else if (config.getObject().equals("Bonfire")) {
            // Solo mantener L (Bonfire), eliminar K (Fire)
            mapa = mapa.replace('K', '0');
        }
        
        return mapa;
    }
    
    /**
     * Convierte el nombre del personaje a su letra correspondiente
     */
    private static char getLetraPersonaje(String nombre) {
        if (nombre == null) return '0';
        switch(nombre) {
            case "Chocolate": return 'C';
            case "Strawberry": return 'S';
            case "Vanilla": return 'V';
            case "Hungry": return 'R';
            case "Fearful": return 'J';
            case "Expert": return 'E';
            default: return '0';
        }
    }
    
    /**
     * Convierte el nombre de la fruta a su letra correspondiente
     */
    private static char getLetraFruta(String nombre) {
        if (nombre == null) return '0';
        switch(nombre) {
            case "Banana": return 'B';
            case "Grape": return 'A';
            case "Cherry": return 'D';
            case "Pineapple": return 'Z';
            case "Cactus": return 'Q';
            default: return '0';
        }
    }
    
    /**
     * Convierte el nombre del enemigo a su letra correspondiente
     */
    private static char getLetraEnemigo(String nombre) {
        if (nombre == null) return '0';
        switch(nombre) {
            case "Troll": return 'O';
            case "FlowerPot": return 'W';
            case "Narwhal": return 'P';
            case "YellowSquid": return 'U';
            default: return '0';
        }
    }
    
    /**
     * Lee un mapa desde un archivo en Resources/
     */
    public static String leerMapaDesdeArchivo(String nombreArchivo) {
        StringBuilder contenido = new StringBuilder();
        try {
            // Leer desde Resources/
            InputStream is = LevelLoader.class.getResourceAsStream("/Resources/maps/" + nombreArchivo);
            
            if (is == null) {
                System.err.println("No se encontró el archivo: /Resources/maps/" + nombreArchivo);
                return "";
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            reader.close();
            
            
        } catch (Exception e) {
            System.err.println("Error al leer nivel: " + e.getMessage());
            e.printStackTrace();
        }
        return contenido.toString();
    }
}