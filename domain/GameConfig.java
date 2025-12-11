package domain;

public class GameConfig {
    private String gameMode;
    private String character1;
    private String character2;
    private int level;
    private String fruit1;
    private String fruit2;
    private String enemy1;
    private String enemy2;
    private String object;
    
    // Constructor vacío
    public GameConfig() {}
    
    // Constructor completo
    public GameConfig(String gameMode, String character1, String character2, 
                      int level, String fruit1, String fruit2, 
                      String enemy1, String enemy2, String object) {
        this.gameMode = gameMode;
        this.character1 = character1;
        this.character2 = character2;
        this.level = level;
        this.fruit1 = fruit1;
        this.fruit2 = fruit2;
        this.enemy1 = enemy1;
        this.enemy2 = enemy2;
        this.object = object;
    }
    
    // Getters
    public String getGameMode() { return gameMode; }
    public String getCharacter1() { return character1; }
    public String getCharacter2() { return character2; }
    public int getLevel() { return level; }
    public String getFruit1() { return fruit1; }
    public String getFruit2() { return fruit2; }
    public String getEnemy1() { return enemy1; }
    public String getEnemy2() { return enemy2; }
    public String getObject() { return object; }
}