package domain;

import java.util.*;

/**
 * Distribuye frutas simétricamente en el mapa
 */
public class MapDistributor {
    
    private Random random;
    
    public MapDistributor() {
        this.random = new Random();
    }
    
    /**
     * Distribuye frutas simétricamente en el mapa
     * @param config configuración con las frutas a distribuir
     * @param posicionesVacias todas las posiciones vacías (0, F, G) del mapa
     * @param rows número de filas del mapa
     * @param cols número de columnas del mapa
     * @param posicionesJugadores posiciones donde están los jugadores
     * @return Map con Position → tipo de fruta ("Banana", "Cherry", etc.)
     */
    public Map<Position, String> distribuirFrutas(GameConfig config, 
                                                   List<Position> posicionesVacias, 
                                                   int rows, int cols, 
                                                   List<Position> posicionesJugadores) {
        Map<Position, String> distribucion = new HashMap<>();
        
        Map<String, Integer> frutas = config.getFruits();
        int totalFrutas = config.getTotalFruits();
        
        if (totalFrutas == 0) {
            System.out.println("⚠️ No hay frutas para distribuir");
            return distribucion;
        }
        
        System.out.println("🍎 Distribuyendo " + totalFrutas + " frutas...");
        
        // Crear lista de frutas a distribuir
        List<String> listaFrutas = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frutas.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                listaFrutas.add(entry.getKey());
            }
        }
        
        // Mezclar para distribución aleatoria
        Collections.shuffle(listaFrutas, random);
        
        // Dividir el mapa en zonas
        Map<String, List<Position>> zonas = dividirEnZonas(posicionesVacias, rows, cols);
        
        // Seleccionar posiciones según cantidad de frutas
        List<Position> posicionesSeleccionadas;
        
        if (totalFrutas <= 4) {
            // Pocas frutas: en esquinas/bordes
            posicionesSeleccionadas = distribuirEnEsquinas(zonas, totalFrutas);
            System.out.println("  📍 Distribución en esquinas");
        } else if (totalFrutas <= 12) {
            // Frutas medias: balanceado arriba-centro-abajo
            posicionesSeleccionadas = distribuirBalanceado(zonas, totalFrutas);
            System.out.println("  📍 Distribución balanceada");
        } else {
            // Muchas frutas: por todo el mapa
            posicionesSeleccionadas = distribuirPorTodoElMapa(posicionesVacias, totalFrutas);
            System.out.println("  📍 Distribución por todo el mapa");
        }
        
        // Asignar frutas a posiciones
        for (int i = 0; i < Math.min(listaFrutas.size(), posicionesSeleccionadas.size()); i++) {
            distribucion.put(posicionesSeleccionadas.get(i), listaFrutas.get(i));
        }
        
        System.out.println("  ✅ " + distribucion.size() + " frutas posicionadas");
        
        return distribucion;
    }
    
    /**
     * Divide el mapa en 3 zonas verticales: ARRIBA, CENTRO, ABAJO
     */
    private Map<String, List<Position>> dividirEnZonas(List<Position> posiciones, int rows, int cols) {
        Map<String, List<Position>> zonas = new HashMap<>();
        
        zonas.put("ARRIBA", new ArrayList<>());
        zonas.put("CENTRO", new ArrayList<>());
        zonas.put("ABAJO", new ArrayList<>());
        
        int tercioSuperior = rows / 3;
        int tercioInferior = (rows * 2) / 3;
        
        for (Position pos : posiciones) {
            int r = pos.getRow();
            
            if (r < tercioSuperior) {
                zonas.get("ARRIBA").add(pos);
            } else if (r > tercioInferior) {
                zonas.get("ABAJO").add(pos);
            } else {
                zonas.get("CENTRO").add(pos);
            }
        }
        
        System.out.println("  🗺️ Zonas: ARRIBA=" + zonas.get("ARRIBA").size() + 
                          ", CENTRO=" + zonas.get("CENTRO").size() + 
                          ", ABAJO=" + zonas.get("ABAJO").size());
        
        return zonas;
    }
    
    /**
     * Distribuye en esquinas para pocas frutas (1-4)
     * Alterna entre arriba y abajo para simetría
     */
    private List<Position> distribuirEnEsquinas(Map<String, List<Position>> zonas, int cantidad) {
        List<Position> seleccionadas = new ArrayList<>();
        
        List<Position> arriba = new ArrayList<>(zonas.get("ARRIBA"));
        List<Position> abajo = new ArrayList<>(zonas.get("ABAJO"));
        
        Collections.shuffle(arriba, random);
        Collections.shuffle(abajo, random);
        
        // Alternar arriba y abajo para simetría
        for (int i = 0; i < cantidad; i++) {
            if (i % 2 == 0 && !arriba.isEmpty()) {
                seleccionadas.add(arriba.remove(0));
            } else if (!abajo.isEmpty()) {
                seleccionadas.add(abajo.remove(0));
            } else if (!arriba.isEmpty()) {
                seleccionadas.add(arriba.remove(0));
            }
        }
        
        return seleccionadas;
    }
    
    /**
     * Distribuye balanceadamente entre las 3 zonas (5-12 frutas)
     */
    private List<Position> distribuirBalanceado(Map<String, List<Position>> zonas, int cantidad) {
        List<Position> seleccionadas = new ArrayList<>();
        
        List<Position> arriba = new ArrayList<>(zonas.get("ARRIBA"));
        List<Position> centro = new ArrayList<>(zonas.get("CENTRO"));
        List<Position> abajo = new ArrayList<>(zonas.get("ABAJO"));
        
        Collections.shuffle(arriba, random);
        Collections.shuffle(centro, random);
        Collections.shuffle(abajo, random);
        
        // Calcular cuántas frutas por zona
        int porZona = cantidad / 3;
        int resto = cantidad % 3;
        
        // Distribuir equitativamente
        int arribaCount = Math.min(porZona + (resto > 0 ? 1 : 0), arriba.size());
        int centroCount = Math.min(porZona + (resto > 1 ? 1 : 0), centro.size());
        int abajoCount = Math.min(porZona, abajo.size());
        
        seleccionadas.addAll(arriba.subList(0, arribaCount));
        seleccionadas.addAll(centro.subList(0, centroCount));
        seleccionadas.addAll(abajo.subList(0, abajoCount));
        
        System.out.println("    Arriba: " + arribaCount + ", Centro: " + centroCount + ", Abajo: " + abajoCount);
        
        return seleccionadas;
    }
    
    /**
     * Distribuye por todo el mapa para muchas frutas (13+)
     */
    private List<Position> distribuirPorTodoElMapa(List<Position> posiciones, int cantidad) {
        List<Position> disponibles = new ArrayList<>(posiciones);
        Collections.shuffle(disponibles, random);
        
        int maxFrutas = Math.min(cantidad, disponibles.size());
        return new ArrayList<>(disponibles.subList(0, maxFrutas));
    }
}