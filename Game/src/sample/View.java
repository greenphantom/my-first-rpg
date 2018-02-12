package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;

/*Decals needed for view:
Grass, Water, Mountains
Player, Enemy
Potion, Sword, ...
Healing, Damage, XP AreaEffect
MapTransition
 */

public class View {
    private GraphicsContext gc;
    private Canvas canvas;
    private Player player;
    private RunGame main;

    private int cameraX, cameraY;
    private String workingDir;
    Color[] items;


    private int mapWidth, mapHeight;
    private int tileSize;
    private int currentMapID;

    private MenuView menu;
    private MainMenu mainMenu;
    private HUDView hud;

    private Sprites sprites;

    public View(Canvas canvas, Player player, MainMenu mainMenu, RunGame main) {
        this.gc = canvas.getGraphicsContext2D();
        this.canvas = canvas;
        this.player = player;
        this.main = main;
        menu = new MenuView(player, canvas, main);
        hud = new HUDView(canvas, player);

        this.mainMenu = mainMenu;

        sprites = new Sprites();

        cameraX = 0; cameraY = 0;
        mapWidth = 100; mapHeight = 100;
        tileSize = 50; //width/height of tiles in pixels

        //Get working directory to load textures from
        workingDir = System.getProperty("user.dir") + "\\Game";
        currentMapID = 0;

    }

    //Load image arrays with sprite assets
    public void render(tile[][] map, Player p) {
        if(main.getMap().getMapID() != currentMapID) {
            centerOnPlayer();
        }
        currentMapID = main.getMap().getMapID();
        renderMap(map, p);
        renderGrid(map);
        if(menu.isOpen()) {
            menu.render();
        } else {//Only render these views if menu is closed
            hud.render();
        }
        gc = canvas.getGraphicsContext2D();
    }

    //Render Order: GroundType -> tileObeject -> player/enemy
    private void renderMap(tile[][] map, Player p) {
        //Get map dimensions
        mapWidth = map.length;
        mapHeight = map[0].length;

        //Draw background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Iterate through map, setting appropriate color for each tile, then draw rect
        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                gc.drawImage(sprites.getTerrainImage(map[i][j].decal), (i*tileSize)+cameraX, (j*tileSize)+cameraY, tileSize, tileSize);
                if(map[i][j].SN > 0) {
                    gc.drawImage(sprites.getTileObjectSprite(map[i][j].SN, map[i][j].spec), (i * tileSize) + cameraX, (j * tileSize) + cameraY, tileSize, tileSize);
                }
            }
        }

        // First parameter changed from playerImg
        gc.drawImage(sprites.getPlayerSprite(player.getPlayerSprite()), player.getPosition().x*tileSize+cameraX, player.getPosition().y*tileSize+cameraY, tileSize, tileSize);
    }



    private Image getImage(String fp) {
        File file = new File(fp);
        Image image = new Image(file.toURI().toString());
        return image;
    }

    private void renderGrid(tile[][] map) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        for(int i = 0; i < map.length+1; i++) {
            gc.strokeLine(0, i*tileSize+cameraY, canvas.getWidth(), i*tileSize+cameraY);
            gc.strokeLine(i*tileSize+cameraX, 0, i*tileSize+cameraX, canvas.getHeight());
        }
    }

    public void Up() {
        if(menu.isOpen()) {
            menu.Up();
        } else {
            moveCameraUp();
        }
    }

    public void Down() {
        if(menu.isOpen()) {
            menu.Down();
        } else {
            moveCameraDown();
        }
    }

    public void Right() {
        if(menu.isOpen()) {
            menu.Right();
        } else {
            moveCameraRight();
        }
    }

    public void Left() {
        if(menu.isOpen()) {
            menu.Left();
        } else {
            moveCameraLeft();
        }
    }

    public void centerOnPlayer() {
        cameraX = (int)(canvas.getWidth()/2) - (player.getPosition().x+1)*tileSize;
        cameraY =  (int)(canvas.getHeight()/2) - (player.getPosition().y+1)*tileSize;
    }

    public void Escape() {
        menu.Escape();
    }
    public void Enter() {
        menu.Enter();
    }
    public void P() {
        mainMenu.openGameMenu();
    }

    public void moveCameraUp() {
        if(cameraY >= 0) {//Top edge of board already in view
            return;
        }
        if((player.getPosition().y+1)*tileSize + cameraY > canvas.getHeight()/2) {//Keep player in center
            return;
        }
        cameraY+=tileSize;
    }

    public void moveCameraDown() {
        if(canvas.getHeight()-cameraY >= mapHeight*tileSize) {//Bottom edge of board already in view
            return;
        }
        if((player.getPosition().y+1)*tileSize + cameraY < canvas.getHeight()/2) {//Keep player in center
            return;
        }
        cameraY-=tileSize;
    }

    public void moveCameraLeft() {//Loft edge of board already in view
        if(cameraX >= 0) {
            return;
        }
        if((player.getPosition().x+1)*tileSize + cameraX > canvas.getWidth()/2) {//Keep player in center
            return;
        }
        cameraX+=tileSize;
    }

    public void moveCameraRight() {
        if(canvas.getWidth()-cameraX >= mapWidth*tileSize) {//Right edge of board already in view
            return;
        }
        if((player.getPosition().x+1)*tileSize + cameraX < canvas.getWidth()/2) {//Keep player in center
            return;
        }
        cameraX-=tileSize;
    }

    public boolean getMenuOpen() {
        return menu.isOpen();
    }


}
