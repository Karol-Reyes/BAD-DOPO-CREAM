package domain;

import java.util.*;

/**
 * Distribuye elementos (frutas, enemigos, obstáculos) simétricamente en el mapa
 */
public class MapDistributor {
    
    private final Random random;
    
    public MapDistributor() {
        this.random = new Random();
    }
    
    /**
     * Encuentra todas las posiciones vacías (0) en el mapa
     */
    public List<Position> encontrarPosicionesVacias(String[] lineas) {
        List<Position> vacias = new ArrayList<>();
        
        for (int r = 0; r < lineas.length; r++) {
            String linea = lineas[r];
            for (int c = 0; c < linea.length(); c++) {
                if (linea.charAt(c) == '0') {
                    vacias.add(new Position(r, c));
                }
            }
        }
        
        return vacias;
    }
    
    /**
     * Divide el mapa en zonas para distribución equilibrada
     */
    public Map<String, List<Position>> dividirEnZonas(List<Position> posiciones, int rows, int cols) {
        Map<String, List<Position>> zonas = new HashMap<>();
        
        zonas.put("ARRIBA", new ArrayList<>());
        zonas.put("CENTRO", new ArrayList<>());
        zonas.put("ABAJO", new ArrayList<>());
        zonas.put("IZQUIERDA", new ArrayList<>());
        zonas.put("DERECHA", new ArrayList<>());
        
        int tercioSuperior = rows / 3;
        int tercioInferior = (rows * 2) / 3;
        int tercioIzquierdo = cols / 3;
        int tercioDerecho = (cols * 2) / 3;
        
        for (Position pos : posiciones) {
            int r = pos.getRow();
            int c = pos.getCol();
            
            // Clasificar por zona vertical
            if (r < tercioSuperior) {
                zonas.get("ARRIBA").add(pos);
            } else if (r > tercioInferior) {
                zonas.get("ABAJO").add(pos);
            } else {
                zonas.get("CENTRO").add(pos);
            }
            
            // Clasificar por zona horizontal
            if (c < tercioIzquierdo) {
                zonas.get("IZQUIERDA").add(pos);
            } else if (c > tercioDerecho) {
                zonas.get("DERECHA").add(pos);
            }
        }
        
        return zonas;
    }
    
    /**
     * Distribuye frutas simétricamente
     */
    public Map<Position, String> distribuirFrutas(GameConfig config, List<Position> posicionesVacias, 
                                                    int rows, int cols, List<Position> posicionesJugadores) {
        Map<Position, String> distribucion = new HashMap<>();
        Map<String, List<Position>> zonas = dividirEnZonas(posicionesVacias, rows, cols);
        
        Map<String, Integer> frutas = config.getFruits();
        int totalFrutas = config.getTotalFruits();
        
        // Crear lista de frutas a distribuir
        List<String> listaFrutas = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frutas.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                listaFrutas.add(entry.getKey());
            }
        }
        
        // Mezclar para distribución aleatoria
        Collections.shuffle(listaFrutas, random);
        
        // ⭐ Distribución simétrica
        List<Position> posicionesSeleccionadas;
        
        // Si hay pocas frutas (1-4), distribuir en esquinas/bordes
        if (totalFrutas <= 4) {
            posicionesSeleccionadas = distribuirEnEsquinas(zonas, totalFrutas);
        }
        // Si hay frutas medias (5-12), distribuir en zonas balanceadas
        else if (totalFrutas <= 12) {
            posicionesSeleccionadas = distribuirBalanceado(zonas, totalFrutas);
        }
        // Si hay muchas frutas (13+), distribuir por todo el mapa
        else {
            posicionesSeleccionadas = distribuirPorTodoElMapa(posicionesVacias, totalFrutas, posicionesJugadores);
        }
        
        // Asignar frutas a posiciones
        for (int i = 0; i < Math.min(listaFrutas.size(), posicionesSeleccionadas.size()); i++) {
            distribucion.put(posicionesSeleccionadas.get(i), listaFrutas.get(i));
        }
        
        return distribucion;
    }
    
    /**
     * Distribuye enemigos alejados de los jugadores
     */
    public Map<Position, String> distribuirEnemigos(GameConfig config, List<Position> posicionesVacias,
                                                     int rows, int cols, List<Position> posicionesJugadores,
                                                     Map<Position, String> frutasYaColocadas) {
        Map<Position, String> distribucion = new HashMap<>();
        
        // Remover posiciones ocupadas por frutas
        List<Position> disponibles = new ArrayList<>(posicionesVacias);
        disponibles.removeAll(frutasYaColocadas.keySet());
        
        Map<String, Integer> enemigos = config.getEnemies();
        int totalEnemigos = config.getTotalEnemies();
        
        // Crear lista de enemigos
        List<String> listaEnemigos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : enemigos.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                listaEnemigos.add(entry.getKey());
            }
        }
        
        // ⭐ Filtrar posiciones ALEJADAS de los jugadores
        List<Position> posicionesAlejadas = filtrarPosicionesAlejadas(disponibles, posicionesJugadores, 5);
        
        // Distribuir simétricamente
        Map<String, List<Position>> zonas = dividirEnZonas(posicionesAlejadas, rows, cols);
        List<Position> posicionesSeleccionadas = distribuirBalanceado(zonas, totalEnemigos);
        
        // Asignar enemigos
        for (int i = 0; i < Math.min(listaEnemigos.size(), posicionesSeleccionadas.size()); i++) {
            distribucion.put(posicionesSeleccionadas.get(i), listaEnemigos.get(i));
        }
        
        return distribucion;
    }
    
    /**
     * Distribuye obstáculos por el mapa
     */
    public Map<Position, String> distribuirObstaculos(GameConfig config, List<Position> posicionesVacias,
                                                       Map<Position, String> frutasYaColocadas,
                                                       Map<Position, String> enemigosYaColocados) {
        Map<Position, String> distribucion = new HashMap<>();
        
        // Remover posiciones ocupadas
        List<Position> disponibles = new ArrayList<>(posicionesVacias);
        disponibles.removeAll(frutasYaColocadas.keySet());
        disponibles.removeAll(enemigosYaColocados.keySet());
        
        Map<String, Integer> obstaculos = config.getObstacles();
        
        // Crear lista de obstáculos
        List<String> listaObstaculos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : obstaculos.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                listaObstaculos.add(entry.getKey());
            }
        }
        
        // Distribuir aleatoriamente pero evitando agrupar
        Collections.shuffle(disponibles, random);
        
        for (int i = 0; i < Math.min(listaObstaculos.size(), disponibles.size()); i++) {
            distribucion.put(disponibles.get(i), listaObstaculos.get(i));
        }
        
        return distribucion;
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private List<Position> distribuirEnEsquinas(Map<String, List<Position>> zonas, int cantidad) {
        List<Position> seleccionadas = new ArrayList<>();
        
        // Combinar arriba y abajo para simetría
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
    
    private List<Position> distribuirBalanceado(Map<String, List<Position>> zonas, int cantidad) {
        List<Position> seleccionadas = new ArrayList<>();
        
        List<Position> arriba = new ArrayList<>(zonas.get("ARRIBA"));
        List<Position> centro = new ArrayList<>(zonas.get("CENTRO"));
        List<Position> abajo = new ArrayList<>(zonas.get("ABAJO"));
        
        Collections.shuffle(arriba, random);
        Collections.shuffle(centro, random);
        Collections.shuffle(abajo, random);
        
        // Distribuir equitativamente
        int porZona = cantidad / 3;
        int resto = cantidad % 3;
        
        // Agregar de cada zona
        seleccionadas.addAll(arriba.subList(0, Math.min(porZona + (resto > 0 ? 1 : 0), arriba.size())));
        seleccionadas.addAll(centro.subList(0, Math.min(porZona + (resto > 1 ? 1 : 0), centro.size())));
        seleccionadas.addAll(abajo.subList(0, Math.min(porZona, abajo.size())));
        
        return seleccionadas;
    }
    
    private List<Position> distribuirPorTodoElMapa(List<Position> posiciones, int cantidad,
                                                    @SuppressWarnings("unused") List<Position> jugadores) {
        List<Position> disponibles = new ArrayList<>(posiciones);
        Collections.shuffle(disponibles, random);
        
        return disponibles.subList(0, Math.min(cantidad, disponibles.size()));
    }
    
    private List<Position> filtrarPosicionesAlejadas(List<Position> posiciones, 
                                                      List<Position> jugadores, 
                                                      int distanciaMinima) {
        List<Position> alejadas = new ArrayList<>();
        
        for (Position pos : posiciones) {
            boolean estaAlejada = true;
            
            for (Position jugador : jugadores) {
                int distancia = Math.abs(pos.getRow() - jugador.getRow()) + 
                               Math.abs(pos.getCol() - jugador.getCol());
                
                if (distancia < distanciaMinima) {
                    estaAlejada = false;
                    break;
                }
            }
            
            if (estaAlejada) {
                alejadas.add(pos);
            }
        }
        
        return alejadas;
    }
}