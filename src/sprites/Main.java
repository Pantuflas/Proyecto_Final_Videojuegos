/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

//Prueba GIT
import com.golden.gamedev.*;
import com.golden.gamedev.object.*;
import com.golden.gamedev.object.background.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
//import java.awt.*;
import java.awt.image.*;

import java.awt.image.BufferedImage;
import com.golden.gamedev.object.font.BitmapFont;
import com.golden.gamedev.object.CollisionManager;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Random;

/*  
    x[0]  x[1]
    y[0]  y[1]
     _ _ _
    |     |
    |     |
    |_ _ _|

    x[2]  x[3]
    y[2]  y[3]

 */
public class Main extends Game {

    Background fondo;

    Agente agente;
    Agente agMapa;
    Agente sideB;
    Agente puerta;
    Agente bucket;

    SpriteGroup grupoAgente, grupoBala, grupoPuerta, grupoBucket;
    SpriteGroup grupoMapa;

    colisionAgentes colisionadorBM; //Bala-mapa
    colisionAgentes[] colisionadorAE; //Agente-enemigo
    colisionAgentes[] colisionadorBE; //Bala-enemigo
    colisionAgentes colisionadorAP; //Agente-puerta
    colisionAgentes colisionadorAB; //Agente-cubeta

    Timer velocidad;

    private int currBullets;
    private int currRocks;

    //////////////////////////////////////////////////
    private Sprite startScreen;
    private Sprite gameOverScreen;
    private Sprite gameWonScreen;
    private Sprite level1Screen;
    private Sprite level2Screen;
    //private Sprite sideBar;
    private Sprite[] numbers;
    private Sprite[] numbersLives;

    private Sprite sprite1;
    private Sprite bobStatic;
    private AnimatedSprite bobRight;
    private AnimatedSprite bobLeft;
    private AnimatedSprite bobUp;
    private AnimatedSprite bobDown;
    private boolean gameOver = false;

    //Sprite spiderStatic1;
    //Sprite spiderStatic2;
    private AnimatedSprite sprite2;
    private BufferedImage image;
    private BufferedImage map;
    private double posx, posy;
    private int[][] controlMatrix;
    private final int SQ_SIZE = 32; //32 pixels is the side of each square of the game
    private int mapHeight;
    private int mapWidth;
    private final int BLOCKED_CELL = -1;
    private final int OPEN_CELL = 1;
    private final int OUT_OF_BOUNDS = 0;
    private final int DIAMOND = 3;
    private char currPos;
    private char prevPos;

    private int doorX;
    private int doorY;
    private int bucketX;
    private int bucketY;

    private int lives = 3;

    private int[] totBobX;
    private int[] totBobY;
    private int[][] cornersCells;
    private final int PIXEL_ERR = SQ_SIZE / 8;
    private final int INNER_POINT_DIFF = SQ_SIZE / 4;

    private final int AM_SPIDERS = 1;

    private Sprite[] totCoins1; //character's coins
    private Sprite[] totCoins2; //Enemy's coins
    private int[][] totCoinsCells1; //Characters coins
    private int[][] totCoinsCells2; //Enemies coins
    private int TOT_COINS = 4;
    private int[][] coinMatrix1;
    private int[][] coinMatrix2;

    private final int STEP_SIZE = 1;
    private final int WAIT = 1;
    private final int CLOSENESS_FACTOR = SQ_SIZE;

    private final int EXTRA_CELLS = 6;
    private final int DOOR = 4;
    private final int INTERSECTION = 2;
    private final int INITIAL_ROCKS = 3;

    private int[] directionCounter;

    private int counter = 0;
    private int pickedCoins1 = 0;
    private int pickedCoins2 = 0; //Diamonds picked by the enemy
    private int prevPickedCoins2 = -1; //previous amount of diamonds picked by the enemy
    private int currLevel = 0;
    private long startTime;
    private long currTime;
    private long prevCurrTime = -1;
    private final long TIME_FACTOR = 999999999;
    private final long DOOR_TIME_FACTOR = 10;
    private final int ROCK_TIME = 5;

    private final int totalMaps = 2;

    private final String characterName = "bob";
    private final String enemyName = "enemy";
    private final String coinName_red = "diamante_red.png";
    private final String coinName_green = "diamante_green.png";
    private final String[] mapNames = {"Mapa1/Mapa_1_Bordes", "Mapa2/Mapa_2_Bordes"};
    private final String doorName = "red_door";
    private final String bucketName = "poket";
    private final String rockName = "rock";
    private int currMap = (currLevel < 3) ? 0 : 1;

    private static final int CLIP_WIDTH = 960;
    private static final int CLIP_HEIGHT = 480;

    private final int characterStrip = 8;
    private final int enemyStrip = 5;

    private final int CHARACTER_START_X = SQ_SIZE;
    private final int CHARACTER_START_Y = SQ_SIZE;
    /*32*//*SQ_SIZE + SQ_SIZE/2*/
    ;
    private final int ENEMY_STARTPOS_X = 15 * SQ_SIZE; //11*/ //480
    private final int ENEMY_STARTPOS_Y = 1 * SQ_SIZE;
    
    private final int INITIAL_DOOR_X_CELL = 8;
    private final int INITIAL_DOOR_Y_CELL = 12;
    
    /*96*/ //32
    private final int INITIAL_BUCKET_X_CELL = 15;
    private final int INITIAL_BUCKET_Y_CELL = 11;
    private int bobX = -1;
    private int bobY = -1;
    private int bobXCell = CHARACTER_START_X / SQ_SIZE;
    private int bobYCell = CHARACTER_START_Y / SQ_SIZE;
    private int bobXAux = -1;
    private int bobYAux = -1;
    private int bobDirection = 0;
    private int bobMoveX = 0;
    private int bobMoveY = 0;

    /*
        
        new code
    
     */
    private int pmX = ENEMY_STARTPOS_X / SQ_SIZE;
    private int pmY = ENEMY_STARTPOS_Y / SQ_SIZE;
    private double auxmY = -1;
    private double auxmX = -1;

    private int mX3 = 0;
    private int mY3 = 0;
    
    private boolean finish = false;
    private boolean rockPlaced = false;
    
    private int enemyCoordX = ENEMY_STARTPOS_X;
    private int enemyCoordY = ENEMY_STARTPOS_Y;
    private int direc3 = 0;
    private int prevEnemyDirection = -1;

    AnimatedSprite sprite3; //R2

    PriorityQueue<Nodo> open; //For the A* algorithm
    ArrayList<Nodo> closed; //For the A* algorithm
    ArrayList<Integer> path; //Stores the tree path of nodes to get to a position
    ArrayList<Integer> moves; //Stores the required directions to get to a position
    ArrayList<Nodo> solutionNodes;
    
    ArrayList<Rock> placedRocks;
    ArrayList<Sprite> rocksSprites;

    private boolean[] levelStarted = {false, false, false, false, false, false, false};
    private boolean allIsReady = false;
    
    private int MOVER2_COUNTER = 0;

    /*
        c = center
        r = right
        u = up
        l = left
        d = down
     */
    public void CargarImagenes() {

        bsLoader.storeImages("0_0", getImages("images/" + characterName + "_up.png", characterStrip, 1));
        bsLoader.storeImages("0_1", getImages("images/" + characterName + "_down.png", characterStrip, 1));
        bsLoader.storeImages("0_2", getImages("images/" + characterName + "_left.png", characterStrip, 1));
        bsLoader.storeImages("0_3", getImages("images/" + characterName + "_right.png", characterStrip, 1));

        bsLoader.storeImages("1_0", getImages("images/" + enemyName + "_up.png", enemyStrip, 1));
        bsLoader.storeImages("1_1", getImages("images/" + enemyName + "_down.png", enemyStrip, 1));
        bsLoader.storeImages("1_2", getImages("images/" + enemyName + "_left.png", enemyStrip, 1));
        bsLoader.storeImages("1_3", getImages("images/" + enemyName + "_right.png", enemyStrip, 1));

        bsLoader.storeImages("2_0", getImages("images/" + doorName + ".png", 1, 1));
        bsLoader.storeImages("3_0", getImages("images/" + bucketName + ".png", 1, 1));
        bsLoader.storeImages("4_0", getImages("images/" + rockName + ".png", 1, 1));
        ///////////////////////////////////////////////////////////////////////////////////

        map = getImage(mapNames[currMap] + ".png");

        bobStatic = new Sprite(getImage("images/bob_static.png"), CHARACTER_START_X, CHARACTER_START_Y);
        startScreen = new Sprite(getImage("images/startScreen.png"), 0, 0);
        gameOverScreen = new Sprite(getImage("images/gameOverScreen.png"), 0, 0);
        gameWonScreen = new Sprite(getImage("images/gameWonScreen.png"), 0, 0);
        level1Screen = new Sprite(getImage("images/level1Screen.png"), 0, 0);
        level2Screen = new Sprite(getImage("images/level2Screen.png"), 0, 0);
        //sideBar = new Sprite(getImage("images/sideBar.png"), 768, 0);        

        numbers = new Sprite[10];

        for (int i = 1; i < 10; i++) {

            String n = "" + i;
            numbers[i] = new Sprite(getImage("images/" + n + ".png"), 850, 180);
        }

        numbersLives = new Sprite[4];

        for (int i = 1; i < 4; i++) {

            String n = "" + i;
            numbersLives[i] = new Sprite(getImage("images/" + n + ".png"), 850, 300);
        }
    }

    public void initResources() {
        //inicializacion de las variables del juego

        CargarImagenes();
        resetLevel();
    }

    public void resetLevel() {
        
        path = new ArrayList<Integer>();
        moves = new ArrayList<Integer>();
        open = new PriorityQueue<Nodo>();
        closed = new ArrayList<Nodo>();
        solutionNodes = new ArrayList<Nodo>();
        placedRocks = new ArrayList<Rock>();
        rocksSprites = new ArrayList<Sprite>(); //Where the rocks' images are stored!

        fondo = new ImageBackground(map);
        fondo.setClip(0, 0, CLIP_WIDTH, CLIP_HEIGHT);     // CLIP_WIDTH = 960, CLIP_HEIGHT = 480                 
        colisionadorAE = new colisionAgentes[AM_SPIDERS]; //Agente-enemigo
        colisionadorBE = new colisionAgentes[AM_SPIDERS];

        sprite3 = new AnimatedSprite(getImages("images/" + enemyName + "_down.png", enemyStrip, 1), enemyCoordX, enemyCoordY);
        sprite3.move(mX3, mY3);
        sprite3.setAnimate(true);
        sprite3.setLoopAnim(true);
        sprite3.setBackground(fondo);

        mapHeight = map.getHeight();
        System.out.println("mapHeight/SQ_SIZE = " + mapHeight / SQ_SIZE);
        mapWidth = map.getWidth();
        System.out.println("mapWidth/SQ_SIZE = " + mapWidth / SQ_SIZE);

        controlMatrix = new int[mapHeight / SQ_SIZE][mapWidth / SQ_SIZE];
        fillControlMatrix();

        currBullets = 100/*AM_SPIDERS*4*/;
        currRocks = 0; //It starts without rocks!

        ///////////////////////////////////////////////////////////
        pickedCoins1 = 0;
        pickedCoins2 = 0;
        cornersCells = new int[4][2];
        totBobX = new int[4];
        totBobY = new int[4];

        bobX = CHARACTER_START_X;
        bobY = CHARACTER_START_Y;
        //CargarImagenes();

        ///////////////////////////////////////////////////////////
        agente = new Agente("0");
        agente.setImages(bsLoader.getStoredImages("0_0"));
        agente.setX(bobX);
        agente.setY(bobY);
        agente.setDirection(1);
        agente.setBackground(fondo);
        agente.obtenerBsLoader(bsLoader);

        //sideBar.setBackground(fondo);
        
        puerta = new Agente("2");
        doorX = INITIAL_DOOR_X_CELL;
        doorY = INITIAL_DOOR_Y_CELL;
        puerta.setX(SQ_SIZE * doorX);
        puerta.setY(SQ_SIZE * doorY);
        puerta.setImages(bsLoader.getStoredImages("2_0"));
        puerta.setDirection(0);
        puerta.setBackground(fondo);
        puerta.obtenerBsLoader(bsLoader);
        resetDoorCoords();
        
        bucket = new Agente("3");
        bucketX = INITIAL_BUCKET_X_CELL;
        bucketY = INITIAL_BUCKET_Y_CELL;
        bucket.setX(SQ_SIZE*bucketX);
        bucket.setY(SQ_SIZE*bucketY);
        bucket.setImages(bsLoader.getStoredImages("3_0"));
        bucket.setDirection(0);
        bucket.setBackground(fondo);
        bucket.obtenerBsLoader(bsLoader);
        resetBucketCoords();

        ////////////////////////////////////////////////////////////
        mapHeight = map.getHeight();
        System.out.println("mapHeight/SQ_SIZE = " + mapHeight / SQ_SIZE);
        mapWidth = map.getWidth();
        System.out.println("mapWidth/SQ_SIZE = " + mapWidth / SQ_SIZE);
        controlMatrix = new int[mapHeight / SQ_SIZE][mapWidth / SQ_SIZE];
        fillControlMatrix();

        totCoins1 = new Sprite[TOT_COINS];
        totCoins2 = new Sprite[TOT_COINS];
        totCoinsCells1 = new int[TOT_COINS][2];
        totCoinsCells2 = new int[TOT_COINS][2];
        coinMatrix1 = new int[mapHeight / SQ_SIZE][mapWidth / SQ_SIZE - EXTRA_CELLS];
        coinMatrix2 = new int[mapHeight / SQ_SIZE][mapWidth / SQ_SIZE - EXTRA_CELLS];
        setTotCoinsPositions();

        //if(currLevel >= 3)
        displayControlMatrix();

        fixControlMatrixBorders();
        addIntersectionsToControlMatrix();
        addDiamondsToControlMatrix();
        addDoorToControlMatrix();

        System.out.println();

        displayControlMatrix();

        ////////////////////////////////////////////////////////
        currPos = 'c';
        prevPos = 'c';

        directionCounter = new int[AM_SPIDERS];

        for (int i = 0; i < AM_SPIDERS; i++) {
            directionCounter[i] = SQ_SIZE / 2;
        }

        setCoinMatrix();
        createCoins();
        displayTotCoinsCells();
        displayCoinMatrix();

        /////////////////////////////////////////////////////////
        grupoAgente = new SpriteGroup("Grupo agente");
        grupoAgente.add(agente);
        grupoAgente.setBackground(fondo);

        grupoPuerta = new SpriteGroup("Grupo puerta");
        grupoPuerta.add(puerta);
        grupoPuerta.setBackground(fondo);

        grupoBucket = new SpriteGroup("Grupo bucket");
        grupoBucket.add(bucket);
        grupoBucket.setBackground(fondo);

        colisionadorAP = new colisionAgentes("colisionador AP");
        colisionadorAP.setCollisionGroup(grupoPuerta, grupoAgente);

        colisionadorAB = new colisionAgentes("colisionador AB");
        colisionadorAB.setCollisionGroup(grupoAgente, grupoBucket);

        grupoBala = new SpriteGroup("Grupo bala");

        colisionadorBM = new colisionAgentes("BM");
        colisionadorBM.setCollisionGroup(grupoMapa, grupoBala);

        velocidad = new Timer(1);

        allIsReady = true;
        
        Nodo startingNode = new Nodo(controlMatrix, pmX, pmY, direc3, 0, 4, currLevel, map, false, pickedCoins2);
        getMovementsR2(startingNode);
    }

    public void resetDoorCoords() {

        /*if(currTime%DOOR_TIME_FACTOR != 0)
            return;*/
        
        int auxDoorX = doorX;
        int auxDoorY = doorY;
        
        if (prevCurrTime == currTime) {
            return;
        }

        prevCurrTime = currTime;

        //System.out.println("HERE!! currTime = " + currTime);
        ArrayList<Integer> myLX = new ArrayList<Integer>();
        ArrayList<Integer> myLY = new ArrayList<Integer>();

        for (int i = 0; i < mapHeight / SQ_SIZE; i++) {
            myLY.add(i);
        }

        for (int j = 0; j < mapWidth / SQ_SIZE; j++) {
            myLX.add(j);
        }

        Collections.shuffle(myLX);
        Collections.shuffle(myLY);

        long seed = System.nanoTime();
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(Math.min(mapHeight / SQ_SIZE - 1, mapWidth / SQ_SIZE - 1));

        for (int i : myLY) {
            for (int j : myLX) {

                if (controlMatrix[i][j] >= 1) {

                    if (randomInt == 0) {

                        doorX = j;
                        doorY = i;

                        puerta.setX(SQ_SIZE * doorX);
                        puerta.setY(SQ_SIZE * doorY);
                                                
                        //System.out.println("doorX = " + doorX + "; doorY = " + doorY);
                        
                        addDoorToControlMatrix();
                        
                        if(auxDoorY > 0 && auxDoorX > 0)
                            controlMatrix[auxDoorY][auxDoorX] = OPEN_CELL; //Delete the previous door coords
                        
                        return;
                    }

                    randomInt--;
                }
            }
        }
    }

    public void resetBucketCoords() {

        if (prevCurrTime == currTime) {
            return;
        }

        ArrayList<Integer> myLX = new ArrayList<Integer>();
        ArrayList<Integer> myLY = new ArrayList<Integer>();

        for (int i = 0; i < mapHeight / SQ_SIZE; i++) {
            myLY.add(i);
        }

        for (int j = 0; j < mapWidth / SQ_SIZE; j++) {
            myLX.add(j);
        }

        Collections.shuffle(myLX);
        Collections.shuffle(myLY);

        long seed = System.nanoTime();
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(Math.min(mapHeight / SQ_SIZE - 1, mapWidth / SQ_SIZE - 1));

        for (int i : myLY) {
            for (int j : myLX) {

                if (controlMatrix[i][j] >= 1) {

                    if (randomInt == 0) {

                        bucketX = j;
                        bucketY = i;

                        bucket.setX(SQ_SIZE * bucketX);
                        bucket.setY(SQ_SIZE * bucketY);

                        //System.out.println("bucketX = " + bucketX + "; bucketY = " + bucketY);

                        return;
                    }

                    randomInt--;
                }
            }
        }
    }

    public void setCoinMatrix() {

        for (int i = 0; i < mapHeight / SQ_SIZE; i++) {
            for (int j = 0; j < mapWidth / SQ_SIZE - EXTRA_CELLS; j++) {

                coinMatrix1[i][j] = 0;
                coinMatrix2[i][j] = 0;
            }
        }
    }

    public void displayCoinMatrix() {

        for (int i = 0; i < mapHeight / SQ_SIZE; i++) {

            for (int j = 0; j < mapWidth / SQ_SIZE - EXTRA_CELLS; j++) {

                System.out.print(coinMatrix1[i][j] + " ");
                System.out.print(coinMatrix2[i][j] + " ");
            }

            System.out.println();
        }
    }

    public boolean thereIsNoCoin1(int y, int x) {

        return coinMatrix1[y][x] == 0;
    }

    public boolean thereIsNoCoin2(int y, int x) {

        return coinMatrix2[y][x] == 0;
    }

    public boolean areGoodCoinCells(int y, int x) {

        return (y >= 1 && y < mapHeight / SQ_SIZE) && (x >= 1 && x < mapWidth / SQ_SIZE - EXTRA_CELLS) && (controlMatrix[y][x] >= 1) && (y != CHARACTER_START_Y / SQ_SIZE || x != CHARACTER_START_Y / SQ_SIZE);
    }

    public void setTotCoinsPositions() {

        if (currMap == 0) { //Coin positions for map 1

            //Monedas del enemigo
            totCoinsCells2[0][0] = 6;
            totCoinsCells2[0][1] = 10;

            totCoinsCells2[1][0] = 1;
            totCoinsCells2[1][1] = 15;

            totCoinsCells2[2][0] = 1;
            totCoinsCells2[2][1] = 9;

            totCoinsCells2[3][0] = 3;
            totCoinsCells2[3][1] = 15;

            //Monedas del personaje
            totCoinsCells1[0][0] = 10;
            totCoinsCells1[0][1] = 10;

            totCoinsCells1[1][0] = 15;
            totCoinsCells1[1][1] = 15;

            totCoinsCells1[2][0] = 15;
            totCoinsCells1[2][1] = 9;

            totCoinsCells1[3][0] = 13;
            totCoinsCells1[3][1] = 15;
        } else { //Coin positions for map 2

            totCoinsCells2 = new int[TOT_COINS][2]; //We now need a larger array!

            //Monedas del enemigo
            totCoinsCells2[0][0] = 6;
            totCoinsCells2[0][1] = 1;

            totCoinsCells2[1][0] = 3;
            totCoinsCells2[1][1] = 8;

            totCoinsCells2[2][0] = 5;
            totCoinsCells2[2][1] = 8;

            totCoinsCells2[3][0] = 1;
            totCoinsCells2[3][1] = 17;

            totCoinsCells2[4][0] = 8;
            totCoinsCells2[4][1] = 1;

            totCoinsCells2[5][0] = 10;
            totCoinsCells2[5][1] = 1;

            totCoinsCells2[6][0] = 10;
            totCoinsCells2[6][1] = 17;

            totCoinsCells2[7][0] = 8;
            totCoinsCells2[7][1] = 17;

            //Monedas del personaje
            totCoinsCells1 = new int[TOT_COINS][2]; //We now need a larger array!

            totCoinsCells1[0][0] = 17;
            totCoinsCells1[0][1] = 8;

            totCoinsCells1[1][0] = 12;
            totCoinsCells1[1][1] = 17;

            totCoinsCells1[2][0] = 12;
            totCoinsCells1[2][1] = 1;

            totCoinsCells1[3][0] = 16;
            totCoinsCells1[3][1] = 1;

            totCoinsCells1[4][0] = 14;
            totCoinsCells1[4][1] = 1;

            totCoinsCells1[5][0] = 14;
            totCoinsCells1[5][1] = 17;

            totCoinsCells1[6][0] = 19;
            totCoinsCells1[6][1] = 8;

            totCoinsCells1[7][0] = 21;
            totCoinsCells1[7][1] = 17;
        }
    }

    void displayTotCoinsCells() {

        for (int i = 0; i < TOT_COINS; i++) {

            System.out.println("totCoinsCells1[" + i + "][x] = " + totCoinsCells1[i][0]);
            System.out.println("totCoinsCells1[" + i + "][y] = " + totCoinsCells1[i][1]);

            System.out.println("totCoinsCells2[" + i + "][x] = " + totCoinsCells2[i][0]);
            System.out.println("totCoinsCells2[" + i + "][y] = " + totCoinsCells2[i][1]);
        }
    }

    public void createCoins() {

        totCoins1 = new Sprite[TOT_COINS];
        totCoins2 = new Sprite[TOT_COINS];

        for (int i = 0; i < TOT_COINS; i++) {

            totCoins1[i] = new Sprite(getImage("images/" + coinName_red), SQ_SIZE * totCoinsCells1[i][0], SQ_SIZE * totCoinsCells1[i][1]);
            totCoins1[i].setBackground(fondo); //Don't let the coins move!!!

            totCoins2[i] = new Sprite(getImage("images/" + coinName_green), SQ_SIZE * totCoinsCells2[i][0], SQ_SIZE * totCoinsCells2[i][1]);
            totCoins2[i].setBackground(fondo); //Don't let the coins move!!!
        }
    }

    public void deleteCoins() {

        for (int i = 0; i < TOT_COINS; i++) {

            totCoins1[i] = null;
            totCoins2[i] = null;
        }
    }

    public void computeCornersCoords() {

        totBobX[0] = bobX + INNER_POINT_DIFF + PIXEL_ERR;
        totBobY[0] = bobY + INNER_POINT_DIFF + PIXEL_ERR;

        totBobX[1] = bobX - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;
        totBobY[1] = bobY + INNER_POINT_DIFF + PIXEL_ERR;

        totBobX[2] = bobX + INNER_POINT_DIFF + PIXEL_ERR;
        totBobY[2] = bobY - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;

        totBobX[3] = bobX - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;
        totBobY[3] = bobY - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;
    }

    public void computeCornersCells() {

        for (int i = 0; i < 4; i++) {

            cornersCells[i][0] = totBobX[i] / SQ_SIZE;
            cornersCells[i][1] = totBobY[i] / SQ_SIZE;
        }
    }

    public void displayTotCoords() {

        for (int i = 0; i < 4; i++) {
            System.out.println("totBobX[" + i + "] = " + totBobX[i] + "; totBobY[" + i + "] = " + totBobY[i]);
        }
    }

    public void displayCornersCells() {

        for (int i = 0; i < 4; i++) {
            System.out.println("cornersCells[" + i + "][0] = " + cornersCells[i][0] + "; cornersCells[" + i + "][1] = " + cornersCells[i][1]);
        }
    }

    public void displayControlMatrix() {

        for (int i = 0; i < mapHeight / SQ_SIZE; i++) {

            for (int j = 0; j < mapWidth / SQ_SIZE; j++) {

                if (controlMatrix[i][j] >= 0) {
                    System.out.print(" ");
                }

                System.out.print(controlMatrix[i][j] + " ");
            }

            System.out.println();
        }
    }

    public void fillControlMatrix() {

        getPixelsRGB();
    }

    public boolean isCruise(int x, int y) {

        int height = mapHeight / SQ_SIZE;
        int width = mapWidth / SQ_SIZE;

        int xCoord[] = {0, 1, 0, -1};
        int yCoord[] = {-1, 0, 1, 0};

        int totCrosses = 0;

        for (int i = 0; i < 4; i++) {

            if (x + xCoord[i] >= 0 && x + xCoord[i] < width && y + yCoord[i] >= 0 && y + yCoord[i] < height) {
                if (x + xCoord[(i + 1) % 4] >= 0 && x + xCoord[(i + 1) % 4] < width && y + yCoord[(i + 1) % 4] >= 0 && y + yCoord[(i + 1) % 4] < height) {
                    if (controlMatrix[y + yCoord[i]][x + xCoord[i]] > 0 && controlMatrix[y + yCoord[(i + 1) % 4]][x + xCoord[(i + 1) % 4]] > 0) {
                        totCrosses++;
                    }
                }
            }
        }

        return totCrosses > 0;
    }

    public void addIntersectionsToControlMatrix() {

        int height = mapHeight / SQ_SIZE;
        int width = mapWidth / SQ_SIZE;

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                if (controlMatrix[i][j] > 0) {
                    if (isCruise(j, i)) {
                        controlMatrix[i][j] = INTERSECTION;
                    }
                }
            }
        }
    }

    public void addDiamondsToControlMatrix() {

        int height = mapHeight / SQ_SIZE;
        int width = mapWidth / SQ_SIZE;

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                for (int k = 0; k < TOT_COINS; k++) {

                    if (j == totCoinsCells2[k][0] && i == totCoinsCells2[k][1]) {
                        controlMatrix[i][j] = DIAMOND;
                    }
                }
            }
        }
    }

    public void addDoorToControlMatrix() {

        int height = mapHeight / SQ_SIZE;
        int width = mapWidth / SQ_SIZE;

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                if (j == doorX && i == doorY) {
                    controlMatrix[i][j] = DOOR;
                }
            }
        }
    }

    public void fixControlMatrixBorders() {

        int height = mapHeight / SQ_SIZE;
        int width = mapWidth / SQ_SIZE;

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                if (i == 0 || j == 0 || i == height - 1 || j == width - 1) {
                    controlMatrix[i][j] = -1;
                }
            }
        }
    }

    public void getPixelsRGB() {

        int w = map.getWidth();
        int h = map.getHeight();
        System.out.println("width, height: " + w + ", " + h);

        for (int i = SQ_SIZE / 2; i < h; i += SQ_SIZE) {

            for (int j = SQ_SIZE / 2; j < w; j += SQ_SIZE) {

                System.out.println("x,y: " + j + ", " + i);
                int pixel = map.getRGB(j, i);
                printPixelARGB(pixel, i, j);
                //System.out.println("");
            }
        }
    }

    public boolean isBlackCell(int red, int green, int blue) {
        /*
            black = 
                    42, 33, 25
                    31, 25, 19
                    43, 36, 29
                    41, 34, 27
                    39, 29, 23
                    49, 42, 36
         */
        return (red == 42 && green == 33 && blue == 25) || (red == 31 && green == 25 && blue == 19)
                || (red == 43 && green == 36 && blue == 29) || (red == 41 && green == 34 && blue == 27)
                || (red == 39 && green == 29 && blue == 23) || (red == 49 && green == 42 && blue == 36);
    }

    public boolean isGreenCell(int red, int green, int blue) {

        return blue <= 20;
    }

    public void printPixelARGB(int pixel, int i, int j) {

        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);

        if (j / SQ_SIZE < map.getWidth()) {

            System.out.println("i = " + i + "; j = " + j);

            if (isGreenCell(red, green, blue)) {
                controlMatrix[i / SQ_SIZE][j / SQ_SIZE] = OPEN_CELL;
            } else {
                controlMatrix[i / SQ_SIZE][j / SQ_SIZE] = BLOCKED_CELL;
            }
        } else {
            controlMatrix[i / SQ_SIZE][j / SQ_SIZE] = OUT_OF_BOUNDS;
        }
    }

    public void checkTime() {

        if (startTime - System.nanoTime() / TIME_FACTOR < 0) {
            currTime = System.nanoTime() / TIME_FACTOR - startTime;
        }

        System.out.println("currTime = " + currTime);

        //if(currTime >= 100)
        //currLevel = 6;
    }

    public void update(long elapsedTime) {
        //actualizacion de las variables del juego

        if (lives == 0) {
            currLevel = 6;
        }

        System.out.println("currLevel = " + currLevel);

        switch (currLevel) {

            case 0:

                /*if(levelStarted[0] == false){

                bsSound.play("sounds/fever.wav"); 
                levelStarted[0] = true;
            }

            startScreen.update(elapsedTime);

            if(keyDown(KeyEvent.VK_ENTER))*/
                currLevel++;

                break;

            case 1:

                /*levelStarted[1] = true;
            level1Screen.update(elapsedTime);

            if(keyDown(KeyEvent.VK_SPACE))*/
                currLevel++;

                break;

            case 2:

                levelStarted[2] = true; //Level 1

                if (pickedCoins1 == TOT_COINS + 1) {
                    currLevel++;
                }

                checkTime();

                updateGameWithObjects(elapsedTime);

                break;

            case 3:

                if (levelStarted[3] == false) {

                    //startTime = (long) System.nanoTime()/TIME_FACTOR;
                    //TOT_COINS *= 2;
                    //resetLevel();
                    //setTotCoinsPositions();
                    //createCoins();
                    deleteCoins();
                    levelStarted[3] = true;
                }

                //checkTime();
                if (keyDown(KeyEvent.VK_SPACE)) {
                    currLevel++;
                }

                //updateGameWithObjects(elapsedTime);
                break;

            case 4:

                if (levelStarted[4] == false) { //Level 2

                    startTime = (long) System.nanoTime() / TIME_FACTOR;
                    TOT_COINS *= 2;
                    resetLevel();
                    levelStarted[4] = true;
                }

                checkTime();

                if (pickedCoins1 == TOT_COINS + 1) ////////////////////////// ????/
                {
                    currLevel++;
                }

                updateGameWithObjects(elapsedTime);
                break;

            case 5:

                /*if(levelStarted[5] == false){
                
                bsSound.stopAll();   
                bsSound.play("sounds/loop.wav");  
                levelStarted[5] = true;
            }

            gameWonScreen.update(elapsedTime);

            if(keyDown(KeyEvent.VK_ENTER))*/
                System.exit(0);

                break;

            case 6:

                /*if(levelStarted[6] == false){
                
                bsSound.stopAll();
                playSound("sounds/gameOver.wav");
                levelStarted[6] = true;   
            }

            gameOverScreen.update(elapsedTime);

            if(keyDown(KeyEvent.VK_ENTER))*/
                System.exit(0);

                break;
        }
    }

    public void updateGame(long elapsedTime) {

        //if(pickedCoins == TOT_COINS)
        //System.exit(0);
        //sideBar.update(elapsedTime);
        computeCornersCoords();
        //displayTotCoords();
        computeCornersCells();
        //displayCornersCells();

        //System.out.println("bobX = " + bobX + "; bobY = " + bobY);
        //System.out.println("bobX/SQ_SIZE = " + bobX/SQ_SIZE + "; bobY/SQ_SIZE = " + bobY/SQ_SIZE);
        //System.out.println("controlMatrix[" + bobY/SQ_SIZE + "][" + bobX/SQ_SIZE + "] = " + controlMatrix[bobY/SQ_SIZE][bobX/SQ_SIZE]);
        
        switch (lives) {

            case 3:

                numbersLives[3].update(elapsedTime);
                break;

            case 2:

                numbersLives[2].update(elapsedTime);
                break;

            case 1:

                numbersLives[1].update(elapsedTime);
                break;
        }

        /**
         * Update operations for the main character
         */
        if (keyDown(KeyEvent.VK_RIGHT)) {

            if (controlMatrix[cornersCells[1][1]][cornersCells[1][0] /*+ 1*/] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0] /*+ 1*/] != -1) {
                currPos = 'r';
            } else {
                currPos = 'c';
            }
            //
            //System.out.println("right arrow pressed");
        } else if (keyDown(KeyEvent.VK_LEFT)) {

            if (controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1) {
                currPos = 'l';
            } else {
                currPos = 'c';
            }
        } else if (keyDown(KeyEvent.VK_UP)) {

            if (controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1 && controlMatrix[cornersCells[1][1]][cornersCells[1][0]] != -1) {
                currPos = 'u';
            } else {
                currPos = 'c';
            }
        } else if (keyDown(KeyEvent.VK_DOWN)) {

            if (controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0]] != -1) {
                currPos = 'd';
            } else {
                currPos = 'c';
            }
        } else {
            currPos = 'c';
        }

        switch (currPos) {

            case 'u':

                if (prevPos != 'u') {

                    bobUp = new AnimatedSprite(getImages("images/upVertMovement.png", 8, 1), bobX, bobY);
                    prevPos = 'u';
                }

                bobUp.move(0, -1);
                bobY -= 1;
                bobUp.update(elapsedTime);
                bobUp.setAnimate(true);
                bobUp.setLoopAnim(true);

                bobDown = null;
                bobRight = null;
                bobLeft = null;
                bobStatic = null;

                break;

            case 'd':

                if (prevPos != 'd') {

                    bobDown = new AnimatedSprite(getImages("images/downVertMovement.png", 8, 1), bobX, bobY);
                    prevPos = 'd';
                }

                bobDown.move(0, 1);
                bobY += 1;
                bobDown.update(elapsedTime);
                bobDown.setAnimate(true);
                bobDown.setLoopAnim(true);

                bobUp = null;
                bobRight = null;
                bobLeft = null;
                bobStatic = null;

                break;

            case 'l':

                if (prevPos != 'l') {

                    bobLeft = new AnimatedSprite(getImages("images/leftHorMovement.png", 8, 1), bobX, bobY);
                    prevPos = 'l';
                }

                bobLeft.move(-1, 0);
                bobX -= 1;
                bobLeft.update(elapsedTime);
                bobLeft.setAnimate(true);
                bobLeft.setLoopAnim(true);

                bobUp = null;
                bobDown = null;
                bobRight = null;
                bobStatic = null;

                break;

            case 'r':

                if (prevPos != 'r') {

                    bobRight = new AnimatedSprite(getImages("images/rightHorMovement.png", 8, 1), bobX, bobY);
                    prevPos = 'r';
                }

                bobRight.move(1, 0);
                bobX += 1;
                bobRight.update(elapsedTime);
                bobRight.setAnimate(true);
                bobRight.setLoopAnim(true);

                bobDown = null;
                bobLeft = null;
                bobUp = null;
                bobStatic = null;

                break;

            case 'c':

                if (prevPos != 'c') {

                    bobStatic = new Sprite(getImage("images/bobStatic.png"), bobX, bobY);
                    prevPos = 'c';
                }

                //Oddly, without the line below, it'll crash -> Null pointer exception
                bobStatic = new Sprite(getImage("images/bobStatic.png"), bobX, bobY);

                bobStatic.setSpeed(0, 0);
                bobStatic.update(elapsedTime);

                bobUp = null;
                bobDown = null;
                bobLeft = null;
                bobRight = null;

                break;
        }

        updateCoins(elapsedTime);
        counter++;

        /*if(currLevel == 4){

        switch((int) currTime/10){

            case 0:
                break;

            case 1:

                numbers[1].update(elapsedTime);
                break;

            case 2:
                numbers[2].update(elapsedTime);
                break;

            case 3:
                numbers[3].update(elapsedTime);
                break;

            case 4:
                numbers[4].update(elapsedTime);
                break;

            case 5:
                numbers[5].update(elapsedTime);
                break;

            case 6:
                numbers[6].update(elapsedTime);
                break;

            case 7:
                numbers[7].update(elapsedTime);
                break;

            case 8:
                numbers[8].update(elapsedTime);
                break;

            case 9:
                numbers[9].update(elapsedTime);
                break;
        }
    }*/
    }

    public void shoot(double x, double y) {

        if (currBullets > 0) {

            Sprite bal = new Sprite(getImage("images/bala.png"), x, y);

            switch (prevPos) {

                case 'u':

                    bal.setSpeed(0.0, -0.3);
                    break;

                case 'd':

                    bal.setSpeed(0.0, 0.3);
                    break;

                case 'l':

                    bal.setSpeed(-0.3, 0.0);
                    break;

                case 'r':

                    bal.setSpeed(0.3, 0.0);
                    break;
            }

            grupoBala.add(bal);
            currBullets--;
        }
    }

    public void checkBullets() {

        Sprite[] myBullets = grupoBala.getSprites();

        for (Sprite myBullet : myBullets) {
            if (myBullet != null) {
                if (controlMatrix[(int) myBullet.getY() / SQ_SIZE][(int) myBullet.getX() / SQ_SIZE] == -1) {
                    myBullet.setActive(false);
                }
            }
        }
    }

    public void updateGameWithObjects(long elapsedTime) {

        //sideBar.update(elapsedTime);
        computeCornersCoords();
        //displayTotCoords();
        computeCornersCells();
        //displayCornersCells();

        //System.out.println("bobX = " + bobX + "; bobY = " + bobY);
        //System.out.println("bobX/SQ_SIZE = " + bobX/SQ_SIZE + "; bobY/SQ_SIZE = " + bobY/SQ_SIZE);
        //System.out.println("controlMatrix[" + bobY/SQ_SIZE + "][" + bobX/SQ_SIZE + "] = " + controlMatrix[bobY/SQ_SIZE][bobX/SQ_SIZE]);        
        //System.out.println();
        if (velocidad.action(elapsedTime)) {

            moveCharacter(elapsedTime);
            //moveCharacterMemo(elapsedTime);
            

            sprite3.move(mX3, mY3);
            displayControlMatrix();
        }

        /*if (prevPickedCoins2 < pickedCoins2) { //If the enemy just caught another diamond

            prevPickedCoins2++;
            pmX = (enemyCoordX + SQ_SIZE/2)/SQ_SIZE; 
            pmY = (enemyCoordY + SQ_SIZE/2)/SQ_SIZE; 
            moveR2();
        }*/
        
        enemyCoordX = (int)sprite3.getX();
        enemyCoordY = (int)sprite3.getY();
        pmX = (enemyCoordX + SQ_SIZE/2)/SQ_SIZE; 
        pmY = (enemyCoordY + SQ_SIZE/2)/SQ_SIZE; 
        
        if(moves.size() != 0){
         
            moveR2();
        }
        
        fondo.setToCenter(agente);
        fondo.update(elapsedTime);
        grupoAgente.update(elapsedTime);
        grupoPuerta.update(elapsedTime);
        grupoBucket.update(elapsedTime);

        grupoBala.update(elapsedTime);
        checkBullets();
        updateCoins(elapsedTime);

        //colisionadorBM.checkCollision(); //Create map through object
        if (pickedCoins1 == TOT_COINS) {
            colisionadorAP.checkCollision();
        }

        //System.out.println("pickedCoins = " + pickedCoins);
        if (colisionadorAP.getCollision()) { //The player got to the door, level up!

            currLevel++;
            currMap++;
            map = getImage(mapNames[currMap] + ".png");
        }

        colisionadorAB.checkCollision();
       
        if(colisionadorAB.getCollision()){ //Agent - bucket collision
            
            currRocks = INITIAL_ROCKS;
        }
        
        System.out.println("currRocks = " + currRocks);
        
        updateRocks();

        if (keyPressed(KeyEvent.VK_SPACE)) {
            
            //shoot(agente.getX() + SQ_SIZE / 2, agente.getY() + SQ_SIZE / 2);
            
            if(currRocks > 0){
                
                System.out.println("Checking if rock can be placed!");
                
                System.out.println("currPos = " + currPos);
                System.out.println("bobX = " + bobX + "; bobY = " + bobY);
                
                if(rockCanBePlaced(currPos, bobX/SQ_SIZE, bobY/SQ_SIZE)){
                    
                    System.out.println("IT CAN BE PLACED!!!!");
                    placeRock(currPos, bobX, bobY, bobX/SQ_SIZE, bobY/SQ_SIZE);
                    rockPlaced = true;
                    currRocks--;
                }
            }
        }

        puerta.update(elapsedTime);

        currTime++;

        if (currTime % DOOR_TIME_FACTOR == 0) {

            resetBucketCoords();
            resetDoorCoords();
        }
    }
    
    public boolean rockCanBePlaced(char currPos, int bobXCell, int bobYCell){
        
        switch(currPos){
            
            case 'u':
                return controlMatrix[bobYCell - 1][bobXCell] == OPEN_CELL;
                
            case 'd':
                return controlMatrix[bobYCell + 1][bobXCell] == OPEN_CELL;
                
            case 'l':
                return controlMatrix[bobYCell][bobXCell - 1] == OPEN_CELL;
                
            case 'r':
                return controlMatrix[bobYCell][bobXCell + 1] == OPEN_CELL;
        }
        
        return false;
    }
    
    public void updateRocks(){
        
        for(Rock r : placedRocks){
            
            if(Math.abs(currTime - r.getInitialTime()) == ROCK_TIME){
                
                deleteRockImage(r.getXCell()*SQ_SIZE, r.getYCell()*SQ_SIZE);
                r = null;
            }   
        }
    }
    
    public void deleteRockImage(int xCoord, int yCoord){
        
        for(Sprite s : rocksSprites){
            
            System.out.println("s.getX() = " + s.getX() + "; s.getY() = " + s.getY());
            System.out.println("xCoord = " + xCoord + "; yCoord = " + yCoord);
            
            if(s.getX() == xCoord && s.getY() == yCoord){
                
                s = null;
            }
        }
    }
    
    public void placeRock(char currPos, int bobX, int bobY, int bobXCell, int bobYCell){
        
        switch(currPos){
            
            case 'u':
                
                controlMatrix[bobYCell - 1][bobXCell] = BLOCKED_CELL;
                placedRocks.add(new Rock(currTime, bobX, bobY - 1));
                Sprite newRock1 = new Sprite(getImage("images/" + rockName + ".png"), (bobX/SQ_SIZE)*(SQ_SIZE), ((bobY - SQ_SIZE)/SQ_SIZE)*SQ_SIZE);
                newRock1.setBackground(fondo);
                rocksSprites.add(newRock1);
                break;
                
            case 'd':
                controlMatrix[bobYCell + 1][bobXCell] = BLOCKED_CELL;
                placedRocks.add(new Rock(currTime, bobX, bobY + 1));
                Sprite newRock2 = new Sprite(getImage("images/" + rockName + ".png"), (bobX/SQ_SIZE)*(SQ_SIZE), ((bobY + SQ_SIZE)/SQ_SIZE)*SQ_SIZE);
                newRock2.setBackground(fondo);
                rocksSprites.add(newRock2);
                break;
                
            case 'l':
                controlMatrix[bobYCell][bobXCell - 1] = BLOCKED_CELL;
                placedRocks.add(new Rock(currTime, bobX - 1, bobY));
                Sprite newRock3 = new Sprite(getImage("images/" + rockName + ".png"), ((bobX - SQ_SIZE)/SQ_SIZE)*SQ_SIZE, (bobY/SQ_SIZE)*SQ_SIZE);
                newRock3.setBackground(fondo);
                rocksSprites.add(newRock3);
                break;
                
            case 'r':
                controlMatrix[bobYCell][bobXCell + 1] = BLOCKED_CELL;
                placedRocks.add(new Rock(currTime, bobX + 1, bobY));
                Sprite newRock4 = new Sprite(getImage("images/" + rockName + ".png"), ((bobX + SQ_SIZE)/SQ_SIZE)*SQ_SIZE, (bobY/SQ_SIZE)*SQ_SIZE);
                newRock4.setBackground(fondo);
                rocksSprites.add(newRock4);
                break;
        }
    }

    public void moveCharacter(long elapsedTime) {

        //sideBar.update(elapsedTime);
        computeCornersCoords();
        //displayTotCoords();
        computeCornersCells();
        //displayCornersCells();

        if (keyDown(KeyEvent.VK_UP)) {

            if (controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1 && controlMatrix[cornersCells[1][1]][cornersCells[1][0]] != -1) {

                agente.setDirection(0);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, 0, -1);
                currPos = 'u';
                //System.out.println("currPos = " + currPos);
                prevPos = 'u';

                bobY -= 1;
            } else {

                agente.setStatus(0);
                //currPos = 'c';
            }
        } else if (keyDown(KeyEvent.VK_DOWN)) {

            if (controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0]] != -1) {

                agente.setDirection(1);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, 0, 1);
                currPos = 'd';
                prevPos = 'd';

                bobY += 1;
            } else {

                agente.setStatus(0);
                //currPos = 'c';
            }
        } else if (keyDown(KeyEvent.VK_LEFT)) {

            if (controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1) {

                agente.setDirection(2);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, -1, 0);
                currPos = 'l';
                prevPos = 'l';

                bobX -= 1;
            } else {

                agente.setStatus(0);
                //currPos = 'c';
            }
        } else if (keyDown(KeyEvent.VK_RIGHT)) {

            if (controlMatrix[cornersCells[1][1]][cornersCells[1][0] /*+ 1*/] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0] /*+ 1*/] != -1) {

                agente.setDirection(3);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, 1, 0);
                currPos = 'r';
                prevPos = 'r';
                bobX += 1;
            } else {

                agente.setStatus(0);
                //currPos = 'c';
            }
        } else {

            agente.setStatus(0);
            //currPos = 'c';
        }
    }
    
    public void moveCharacterMemo(long elapsedTime){
        bobX = (int)agente.getX();
        bobY = (int)agente.getY();
        bobXCell = bobX/SQ_SIZE;
        bobYCell = bobY/SQ_SIZE;
        bobXAux = (int)agente.getX() - (SQ_SIZE*bobXCell) + (SQ_SIZE/2);
        bobYAux = (int)agente.getY() - (SQ_SIZE*bobYCell) + (SQ_SIZE/2);
        
        if(keyDown(KeyEvent.VK_D)){
            bobDirection = 2;
        }
        if(keyDown(KeyEvent.VK_A)){
            bobDirection = 4;
        }
        if(keyDown(KeyEvent.VK_W)){
            bobDirection = 1;
        }
        if(keyDown(KeyEvent.VK_S)){
            bobDirection = 3;
        }
        
        if(bobXAux == SQ_SIZE/2){
            switch(bobDirection){
                case 1:
                    if(controlMatrix[bobYCell - 1][bobXCell] != BLOCKED_CELL){
                        bobMoveY = -1;
                        bobMoveX = 0;
                    }else{
                        bobMoveY = 0;
                        bobMoveX = 0;
                        bobDirection = 0;
                    }
                        
                    break;
                    
                case 2:
                    if(controlMatrix[bobYCell][bobXCell + 1] != BLOCKED_CELL){
                        bobMoveX = 1;
                        bobMoveY = 0;
                    }else{
                        bobMoveY = 0;
                        bobMoveX = 0;
                        bobDirection = 0;
                    }
                    break;
                    
                case 3:
                    if(controlMatrix[bobYCell + 1][bobXCell] != BLOCKED_CELL){
                        bobMoveY = 1;
                        bobMoveX = 0;
                    }else{
                        bobMoveY = 0;
                        bobMoveX = 0;
                        bobDirection = 0;
                    }
                    break;
                    
                case 4:
                    if(controlMatrix[bobYCell][bobXCell - 1] != BLOCKED_CELL){
                        bobMoveX = -1;
                        bobMoveY = 0;
                    }else{
                        bobMoveY = 0;
                        bobMoveX = 0;
                        bobDirection = 0;
                    }
                    break;
            }
        }
        agente.move(bobMoveX, bobMoveY);
 
    }

    public void resetCharacter() {

        bobX = CHARACTER_START_X;
        bobY = CHARACTER_START_Y;
        agente = new Agente("0");
        agente.setImages(bsLoader.getStoredImages("0_0"));
        agente.setX(bobX);
        agente.setY(bobY);
        agente.setDirection(1);
        agente.setBackground(fondo);
        agente.obtenerBsLoader(bsLoader);
        grupoAgente.add(agente);
    }

    public int PosSpriteX(double xCoord) {

        return (int) (xCoord) / SQ_SIZE;
    }

    public void setEnemyCoords() {

        enemyCoordX = (int) sprite3.getX();
        enemyCoordY = (int) sprite3.getY();
    }

    public void changeAnimation(int character, int direction) {

        if(prevEnemyDirection == direction)
            return;

        //System.out.println("prevEnemyDirection = " + prevEnemyDirection);
        //System.out.println("direction = " + direction);
        prevEnemyDirection = direction;

        setEnemyCoords();

        switch (direction) {

            case 1:

                sprite3 = new AnimatedSprite(getImages("images/" + enemyName + "_up.png", enemyStrip, 1), enemyCoordX, enemyCoordY);
                sprite3.setAnimate(true);
                sprite3.setLoopAnim(true);
                sprite3.setBackground(fondo);
                break;

            case 2:

                sprite3 = new AnimatedSprite(getImages("images/" + enemyName + "_right.png", enemyStrip, 1), enemyCoordX, enemyCoordY);
                sprite3.setAnimate(true);
                sprite3.setLoopAnim(true);
                sprite3.setBackground(fondo);
                break;

            case 3:

                sprite3 = new AnimatedSprite(getImages("images/" + enemyName + "_down.png", enemyStrip, 1), enemyCoordX, enemyCoordY);
                sprite3.setAnimate(true);
                sprite3.setLoopAnim(true);
                sprite3.setBackground(fondo);
                break;

            case 4:

                sprite3 = new AnimatedSprite(getImages("images/" + enemyName + "_left.png", enemyStrip, 1), enemyCoordX, enemyCoordY);
                sprite3.setAnimate(true);
                sprite3.setLoopAnim(true);
                sprite3.setBackground(fondo);
                break;
        }
    }

    public void getMoves() {

        moves.clear();

        //System.out.println("path.size() = " + path.size());
        moves.add(closed.get(0).getCurrentDirection());
        
        for (int i = 1; i < path.size(); i++) {

            int closedIndex = path.get(i); //Get the index of the child in the closed array
            moves.add(closed.get(closedIndex).getCurrentDirection()); //Get the direction
        }
        
        /*for(int c : moves){
            System.out.println(c);
        }*/
    }

    public void getPath() {

        //Take the path from the "closed" array list
        path.clear();
        
        /*for(Nodo x : closed){
            System.out.println(x);
        }*/

        int index = closed.size() - 1;

        //System.out.println("!!!!!!! index = " + index);

        while (index >= 0) {

            path.add(0, closed.get(index).getParentIndex());
            solutionNodes.add(0, closed.get(index));
            index = closed.get(index).getParentIndex();
            
            //System.out.println("index = " + index);
        }
        
        /*for(Nodo d : solutionNodes)
            System.out.println(d);
        
        System.out.println("Getting moves!!");*/
        getMoves();
    }

    public boolean nodeIsInClosed(Nodo n) {

        for (Nodo nClosed : closed) { //Check whether or not the node n is already in closed!

            if (nClosed.equals(n)) {
                return true;
            }
        }

        return false;
    }

    public void addChildrenToOpen(ArrayList<Nodo> xChildren) {

        for (Nodo n : xChildren) {

            if (!nodeIsInClosed(n)) { //Only add the node to open if it is not in "closed"

                //System.out.println("HEEEEERE!!!!!!!!!!!!!!!!!!");
                open.add(n);
            }
        }
    }

    public void getMovementsR2(Nodo startingNode) {

        open.clear();
        closed.clear();

        open.add(startingNode);

        while (!open.isEmpty()) {

            //System.out.println("open.size() = " + open.size());

            Nodo x = open.poll();

            closed.add(x);

            //System.out.println("Node x =" + x);
            
            //System.out.println("x.getPathFound() = " + x.getPathFound());

            if (x.getPathFound() == true) { //It got either a diamond or the door
                
                getPath();
                return;
            }

            ArrayList<Nodo> xChildren = x.computeChildren(x.getPositionX(), x.getPositionY()); //We generate the children
            changeParent(xChildren, closed.size()-1);
            //System.out.println("xChildren.size() = " + xChildren.size());
            addChildrenToOpen(xChildren); //We add them to open
        }
    }
    
    public void changeParent(ArrayList<Nodo> xChildren, int index){
        for(Nodo n : xChildren)
            n.setParentIndex(index);
    }

    public void randomMoveR2(long elapsedTime) {

        /*setEnemyCoords(); 
        pmX = (enemyCoordX + SQ_SIZE/2)/SQ_SIZE; /*PosSpriteX((totSpiders[0][].getX()+13)); //X Cell
        pmY = (enemyCoordY + SQ_SIZE/2)/SQ_SIZE; /*PosSpriteX((sprite3.getY()+13)); //Y Cell
        
        auxmX = enemyCoordX - SQ_SIZE*pmX + SQ_SIZE/2;/*sprite3.getX()-(65.5*pmX); //X position in cell
        auxmY = enemyCoordY - SQ_SIZE*pmY + SQ_SIZE/2;/*sprite3.getY()-(65.5*pmY); //Y Position in cell
        
        if(auxmX < 0)
            auxmX *= -1;
        
        if(auxmY < 0)
            auxmY *= -1;
        
        System.out.println("pmX = " + pmX + "; pmY = " + pmY);
        System.out.println("auxmX = " + auxmX + "; auxmY = " + auxmY);
        System.out.println("direc3 = " + direc3);
        System.out.println();
        
        int err = SQ_SIZE/8; //3-pixel error
        
        boolean decisionTaken = false;
         
        if(auxmX == SQ_SIZE/2 && auxmY == SQ_SIZE/2){
            
            System.out.println("I'm in!!!");
            int arriba, abajo, derecha, izquierda;
            
            if(controlMatrix[pmY - 1][pmX] != BLOCKED_CELL)
                arriba = 0;
            else
                arriba = 1;
            
            if(controlMatrix[pmY + 1][pmX] != BLOCKED_CELL)
                abajo = 0;
            else
                abajo = 1;
            
            if(controlMatrix[pmY][pmX + 1] != BLOCKED_CELL)
                derecha = 0;
            else
                derecha = 1;
            
            if(controlMatrix[pmY][pmX - 1] != BLOCKED_CELL)
                izquierda = 0;
            else
                izquierda = 1;
           
            Random rn = new Random();
            int decision;
            
            //1 arriba, 2 derecha, 3 abajo, 4 izquierda
            
            switch(direc3){
                
                case 1:
                    if(arriba == 0 && derecha == 0 && izquierda == 0){
                        
                        decision = rn.nextInt(3) + 1;
                       
                        switch(decision){
                            
                            case 1:
                                
                                mX3 = 0;
                                mY3 = -1;
                                direc3 = 1;
                                break;
                            case 2:
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                            case 3:
                                mX3 = -1;
                                mY3 = 0;
                                direc3 = 4;
                                break;
                        }
                        
                        decisionTaken = true;
                    }
                    if(arriba == 0 && derecha == 0 && izquierda == 1){
                        decision = rn.nextInt(2)+1;
                        switch(decision){
                            case 1:
                                mX3 = 0;
                                mY3 = -1;
                                direc3 = 1;
                                break;
                            case 2:
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(derecha == 0 && izquierda == 0 && arriba == 1){
                        decision = rn.nextInt(2)+1;
                            switch(decision){
                               case 1:
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                               case 2:
                                mX3 = -1;
                                mY3 = 0;
                                direc3 = 4;
                                break;
                            }
                            decisionTaken = true;
                    }
                    if(arriba == 0 && izquierda == 0 && derecha == 1){
                        decision = rn.nextInt(2)+1;
                        switch(decision){
                            case 1:
                                mX3 = 0;
                                mY3 = -1;
                                direc3 = 1;
                                break;
                            case 2:
                                mX3 = -1;
                                mY3 = 0;
                                direc3 = 4;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(arriba == 1 && derecha == 0 && izquierda == 1){
                        mX3 = 1;
                        mY3 = 0;
                        direc3 = 2;
                        decisionTaken = true;
                    }
                    if(arriba == 1 && derecha == 1 && izquierda == 0){
                        mX3 = -1;
                        mY3 = 0;
                        direc3 = 4;
                        decisionTaken = true;
                    }
                    if(arriba == 1 && derecha == 1 && izquierda == 1){
                        mX3 = 0;
                        mY3 = 1;
                        direc3 = 3;
                        decisionTaken = true;
                    }
                    break;
                
                case 2:
                    if(arriba == 0 && derecha == 0 && abajo == 0){
                        decision = rn.nextInt(3) + 1;
                        switch(decision){
                            case 1:
                                mX3 = 0;
                                mY3 = -1;
                                direc3 = 1;
                                break;
                            case 2:
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                            case 3:
                                mX3 = 0;
                                mY3 = 1;
                                direc3 = 3;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(arriba == 0 && derecha == 0 && abajo == 1){
                        decision = rn.nextInt(2)+1;
                        switch(decision){
                            case 1:
                                mX3 = 0;
                                mY3 = -1;
                                direc3 = 1;
                                break;
                            case 2:
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(derecha == 0 && abajo == 0 && arriba == 1){
                        decision = rn.nextInt(2)+1;
                            switch(decision){
                               case 1:
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                               case 2:
                                mX3 = 0;
                                mY3 = 1;
                                direc3 = 3;
                                break;
                            }
                            decisionTaken = true;
                    }
                    if(arriba == 0 && abajo == 0 && derecha == 1){
                        decision = rn.nextInt(2)+1;
                        switch(decision){
                            case 1:
                                mX3 = 0;
                                mY3 = -1;
                                direc3 = 1;
                                break;
                            case 2:
                                mX3 = 0;
                                mY3 = 1;
                                direc3 = 3;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(arriba == 0 && derecha == 1 && abajo == 1){
                        mX3 = 0;
                        mY3 = -1;
                        direc3 = 1;
                        decisionTaken = true;
                    }
                    if(arriba == 1 && derecha == 1 && abajo == 0){
                        mX3 = 0;
                        mY3 = 1;
                        direc3 = 3;
                        decisionTaken = true;
                    }
                    if(arriba == 1 && derecha == 1 && abajo == 1){
                        mX3 = -1;
                        mY3 = 0;
                        direc3 = 4;
                        decisionTaken = true;
                    }
                    break;
                
                case 3:
                    if(abajo == 0 && derecha == 0 && izquierda == 0){
                        decision = rn.nextInt(3) + 1;
                        switch(decision){
                            case 1:
                                
                                mX3 = 0;
                                mY3 = 1;
                                direc3 = 3;
                                break;
                            case 2:
                                
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                            case 3:
                                
                                mX3 = -1;
                                mY3 = 0;
                                direc3 = 4;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(abajo == 0 && derecha == 0 && izquierda == 1){
                        decision = rn.nextInt(2)+1;
                        switch(decision){
                            case 1:
                                
                                mX3 = 0;
                                mY3 = 1;
                                direc3 = 3;
                                break;
                            case 2:
                                
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(derecha == 1 && abajo == 0 && izquierda == 0){
                        decision = rn.nextInt(2)+1;
                            switch(decision){
                               case 1:
                                mX3 = 0;
                                mY3 = 1;
                                direc3 = 3;
                                break;
                               case 2:
                                mX3 = -1;
                                mY3 =0;
                                direc3 = 4;
                                break;
                            }
                            decisionTaken = true;
                    }
                    if(derecha == 0 && izquierda == 0 && abajo == 1){
                        decision = rn.nextInt(2)+1;
                        switch(decision){
                            case 1:
                                mX3 = 1;
                                mY3 = 0;
                                direc3 = 2;
                                break;
                            case 2:
                                mX3 = -1;
                                mY3 = 0;
                                direc3 = 4;
                                break;
                        }
                        decisionTaken = true;
                    }
                    if(izquierda == 1 && derecha == 0 && abajo == 1){
                        mX3 = 1;
                        mY3 = 0;
                        direc3 = 2;
                        decisionTaken = true;
                    }
                    if(izquierda == 0 && derecha == 1 && abajo == 1){
                        mX3 = -1;
                        mY3 = 0;
                        direc3 = 4;
                        decisionTaken = true;
                    }
                    if(izquierda == 1 && derecha == 1 && abajo == 1){
                        mX3 = 0;
                        mY3 = -1;
                        direc3 = 1;
                        decisionTaken = true;
                    }
                    break;
                case 4:
                        if(arriba == 0 && izquierda == 0 && abajo == 0){
                            decision = rn.nextInt(3) + 1;
                            switch(decision){
                                case 1:
                                    mX3 = 0;
                                    mY3 = -1;
                                    direc3 = 1;
                                    break;
                                case 2:
                                    mX3 = -1;
                                    mY3 = 0;
                                    direc3 = 4;
                                    break;
                                case 3:
                                    mX3 = 0;
                                    mY3 = 1;
                                    direc3 = 3;
                                    break;
                            }
                            decisionTaken = true;
                        }
                        if(arriba == 0 && izquierda == 0 && abajo == 1){
                            decision = rn.nextInt(2)+1;
                            switch(decision){
                                case 1:
                                    mX3 = 0;
                                    mY3 = -1;
                                    direc3 = 1;
                                    break;
                                case 2:
                                    mX3 = -1;
                                    mY3 = 0;
                                    direc3 = 4;
                                    break;
                            }
                            decisionTaken = true;
                        }
                        if(izquierda == 0 && abajo == 0 && arriba == 1){
                            decision = rn.nextInt(2)+1;
                                switch(decision){
                                   case 1:
                                    mX3 = -1;
                                    mY3 = 0;
                                    direc3 = 4;
                                    break;
                                   case 2:
                                    mX3 = 0;
                                    mY3 = 1;
                                    direc3 = 3;
                                    break;
                                }
                                decisionTaken = true;
                        }
                        if(arriba == 0 && abajo == 0 && izquierda == 1){
                            decision = rn.nextInt(2)+1;
                            switch(decision){
                                case 1:
                                    mX3 = 0;
                                    mY3 = -1;
                                    direc3 = 1;
                                    break;
                                case 2:
                                    mX3 = 0;
                                    mY3 = 1;
                                    direc3 = 3;
                                    break;
                            }
                            decisionTaken = true;
                        }
                        if(arriba == 0 && izquierda == 1 && abajo == 1){
                            mX3 = 0;
                            mY3 = -1;
                            direc3 = 1;
                            decisionTaken = true;
                        }
                        if(arriba == 1 && izquierda == 1 && abajo == 0){
                            mX3 = 0;
                            mY3 = 1;
                            direc3 = 3;
                            decisionTaken = true;
                        }
                        if(arriba == 1 && izquierda == 1 && abajo == 1){
                            mX3 = 1;
                            mY3 = 0;
                            direc3 = 2;
                            decisionTaken = true;
                        }
                    break;
            }
               
        }
        
        if(prevEnemyDirection != direc3 && decisionTaken == false){
            direc3 += 2;
            direc3 %= 4;
            
            if(direc3 == 0)
                direc3 = 4;
            
            switch(direc3){
                
                case 1:
                    mY3 = -1;
                    mX3 = 0;
                    break;
                    
                case 2:
                    
                    mY3 = 0;
                    mX3 = 1;
                    break;
                    
                case 3:
                    
                    mY3 = 1;
                    mX3 = 0;
                    break;
                    
                case 4:
                    
                    mY3 = 0;
                    mX3 = -1;
                    break;
            }
        }
        
        System.out.println("mX3 = " + mX3 + "; mY3 = " + mY3);
        sprite3.move(mX3, mY3);
        sprite3.update(elapsedTime);
        changeAnimation(1, direc3);*/
    }

    public boolean coordIsIntersection(int x, int y) {

        return controlMatrix[y][x] == INTERSECTION;
    }

    public void setMoveDirection(int direction) {
        
        switch (direction) {
            
            case 1:
                mX3 = 0;
                mY3 = -1;
                direc3 = 1;
                break;
            
            case 2:
                mX3 = 1;
                mY3 = 0;
                direc3 = 2;
                break;
            
            case 3:
                mX3 = 0;
                mY3 = 1;
                direc3 = 3;
                break;
            
            case 4:
                mX3 = -1;
                mY3 = 0;
                direc3 = 4;
                break;
        }
        
        changeAnimation(1, direc3);
    }

    public void moveR2(/*long elapsedTime*/) {

        pmX = (enemyCoordX + SQ_SIZE / 2) / SQ_SIZE; //Position in x
        pmY = (enemyCoordY + SQ_SIZE / 2) / SQ_SIZE; //Position in y

        auxmX = enemyCoordX - SQ_SIZE * pmX + SQ_SIZE / 2; //X position in cell
        auxmY = enemyCoordY - SQ_SIZE * pmY + SQ_SIZE / 2; //Y Position in cell

        if(auxmX < 0)
            auxmX *= -1;

        if(auxmY < 0)
            auxmY *= -1;
        
        int winningIndex = -1;
        
        if(auxmX == SQ_SIZE / 2 && auxmY == SQ_SIZE / 2){ //If you're in the center of a cell
            
            for(int i = 0; i < solutionNodes.size(); i++){
                
                //System.out.println("pmX = " + pmX + "; pmY = " + pmY);
                
                if(solutionNodes.get(i).getPositionX() == (pmX+1) && solutionNodes.get(i).getPositionY() == pmY){
                    
                    winningIndex = i;
                    setMoveDirection(solutionNodes.get(i).getCurrentDirection());
                }
                if(solutionNodes.get(i).getPositionX() == (pmX-1) && solutionNodes.get(i).getPositionY() == pmY){
                    
                    winningIndex = i;
                    setMoveDirection(solutionNodes.get(i).getCurrentDirection());
                }
                if(solutionNodes.get(i).getPositionX() == pmX && solutionNodes.get(i).getPositionY() == (pmY-1)){
                    
                    winningIndex = i;
                    setMoveDirection(solutionNodes.get(i).getCurrentDirection());
                }
                if(solutionNodes.get(i).getPositionX() == pmX && solutionNodes.get(i).getPositionY() == (pmY+1)){
                    
                    winningIndex = i;
                    setMoveDirection(solutionNodes.get(i).getCurrentDirection());
                }
            }
            
            if(finish == true || rockPlaced == true){
                //System.out.println("PARA YA LLEGASTE");
                stopEnemy();
                open.clear();
                closed.clear();
                solutionNodes.clear();
                getMovementsR2(new Nodo(controlMatrix, pmX, pmY, 0, 0, 0, currLevel, map, false, pickedCoins2));
                finish = false;
                rockPlaced = false;
            }
            
            if(winningIndex == solutionNodes.size() - 1){
                finish = true;
                //System.out.println("winningIndex = "+winningIndex);
                //System.out.println("pmX = "+pmX+"; pmY = "+pmY);
                //System.out.println("solutionNodes.get(" + winningIndex + ").getPositionX() = " + solutionNodes.get(winningIndex).getPositionX() + "; solutionNodes.get(" + winningIndex + ").getPositionY() = " + solutionNodes.get(winningIndex).getPositionY());
                
            }
            
            
            
            if(winningIndex == solutionNodes.size() - 1 && pmX == solutionNodes.get(winningIndex).getPositionX() && pmY == solutionNodes.get(winningIndex).getPositionY()){ //If it is the node in which the diamond is
                //System.out.println("PARA YA LLEGASTE");
                stopEnemy();
                getMovementsR2(new Nodo(controlMatrix, pmX, pmY, 0, 0, 0, currLevel, map, false, pickedCoins2));
            }
        }
        
        

        /*int currentDecisionIndex = 0;
        for(int i = 0; i<moves.size(); i++){
            if (auxmX == SQ_SIZE / 2 && auxmY == SQ_SIZE / 2) {

                System.out.println("moves.size() = " + moves.get(i));
                setMoveDirection(moves.get(i));
                currentDecisionIndex++;
            }
        }*/
    }

    public boolean areGoodCoords(int i, int j) {

        //System.out.println("i = " + i + "; j = " + j);
        return (i > 0 && i < mapHeight / SQ_SIZE - 1) && (j > 0 && j < mapWidth / SQ_SIZE - 1);
    }

    public double getEuclidianDistance(int x1, int y1, int x2, int y2) {

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public boolean theyCrashed(int x1, int y1, int x2, int y2) {

        return getEuclidianDistance(x1, y1, x2, y2) <= SQ_SIZE / CLOSENESS_FACTOR;
    }

    public void updateCoins(long elapsedTime) {

        for (int i = 0; i < TOT_COINS; i++) {
            for (int j = 0; j < 4; j++) {

                if (totCoins1[i] != null) { //Character's coins

                    if (theyCrashed(SQ_SIZE * cornersCells[j][0], SQ_SIZE * cornersCells[j][1], SQ_SIZE * totCoinsCells1[i][0], SQ_SIZE * totCoinsCells1[i][1])) {

                        playSound("sounds/coin.wav");
                        totCoins1[i] = null;
                        pickedCoins1++; //Character's counter 
                    } else {
                        totCoins1[i].update(elapsedTime);
                    }
                }

                if (totCoins2[i] != null) { //Enemy's coins

                    if (theyCrashed(SQ_SIZE * pmX, SQ_SIZE * pmY, SQ_SIZE * totCoinsCells2[i][0], SQ_SIZE * totCoinsCells2[i][1])) {

                        playSound("sounds/coin.wav");
                        totCoins2[i] = null;
                        pickedCoins2++; //Enemy's counter
                        deleteDiamond(totCoinsCells2[i][0], totCoinsCells2[i][1]);
                    } 
                    
                    else {
                        totCoins2[i].update(elapsedTime);
                    }
                }
            }
        }
    }
    
    public void deleteDiamond(int x, int y){
        
        controlMatrix[y][x] = OPEN_CELL;
    }
    
    public void stopEnemy(){
        
        mX3 = 0;
        mY3 = 0;
    }

    public void render(Graphics2D g) {

        switch (currLevel) {

            case 0:

                startScreen.render(g);
                break;

            case 1:

                level1Screen.render(g);
                break;

            case 2:

                renderGameWithObjects(g);
                break;

            case 3:

                level2Screen.render(g);
                break;

            case 4:

                renderGameWithObjects(g);
                break;

            case 5:

                gameWonScreen.render(g);
                break;

            case 6:

                gameOverScreen.render(g);
                break;
        }
    }

    public void renderGameWithObjects(Graphics2D g) {

        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());

        //g.drawImage(map, 0, 0, null);
        fondo.render(g);
        //sideBar.render(g);

        /*switch(lives){
            
            case 3:
                
                numbersLives[3].render(g);
                break;
                
            case 2:
                
                numbersLives[2].render(g);
                break;
            
            case 1:
                
                numbersLives[1].render(g);
                break;
        }*/
 /*if(currLevel == 4){
            
            switch((int) currTime/10){
                
                case 0:
                    
                    break;
                
                case 1:
                    
                    numbers[9].render(g);
                    break;
                
                case 2:
                    numbers[8].render(g);
                    break;
                
                case 3:
                    numbers[7].render(g);
                    break;
                    
                case 4:
                    numbers[6].render(g);
                    break;
                    
                case 5:
                    numbers[5].render(g);
                    break;
                    
                case 6:
                    numbers[4].render(g);
                    break;
                    
                case 7:
                    numbers[3].render(g);
                    break;
                    
                case 8:
                    numbers[2].render(g);
                    break;
                    
                case 9:
                    numbers[1].render(g);
                    break;
                    
                case 10:
                    currLevel = 6;
                    break; 
            }
        }*/
        
        grupoPuerta.render(g);
        grupoAgente.render(g);
        grupoBucket.render(g);
        sprite3.render(g);
        
        renderRocks(g);

        grupoBala.render(g);

        for (int j = 0; j < TOT_COINS; j++) {

            if (totCoins1[j] != null) {
                totCoins1[j].render(g);
            }
        }

        for (int j = 0; j < TOT_COINS; j++) {

            if (totCoins2[j] != null) {
                totCoins2[j].render(g);
            }
        }

        //g.drawString("Bullets:" + currBullets, 780, 250);
    }
    
    public void renderRocks(Graphics2D g){
        
        for(Sprite s : rocksSprites)
            s.render(g);
    }

    public void renderGame(Graphics2D g) {
        //rendereo de la pantalla        

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(map, 0, 0, null);

        //sideBar.render(g);
        switch (lives) {

            case 3:

                numbersLives[3].render(g);
                break;

            case 2:

                numbersLives[2].render(g);
                break;

            case 1:

                numbersLives[1].render(g);
                break;
        }

        if (currLevel == 4) {

            switch ((int) currTime / 10) {

                case 0:

                    break;

                case 1:

                    numbers[9].render(g);
                    break;

                case 2:
                    numbers[8].render(g);
                    break;

                case 3:
                    numbers[7].render(g);
                    break;

                case 4:
                    numbers[6].render(g);
                    break;

                case 5:
                    numbers[5].render(g);
                    break;

                case 6:
                    numbers[4].render(g);
                    break;

                case 7:
                    numbers[3].render(g);
                    break;

                case 8:
                    numbers[2].render(g);
                    break;

                case 9:
                    numbers[1].render(g);
                    break;

                case 10:
                    currLevel = 6;
                    break;
            }
        }

        switch (currPos) {

            case 'r':
                bobRight.render(g);
                break;

            case 'u':

                bobUp.render(g);
                break;

            case 'l':

                bobLeft.render(g);
                break;

            case 'd':

                bobDown.render(g);
                break;

            case 'c':

                bobStatic.render(g);
                break;
        }

        for (int j = 0; j < TOT_COINS; j++) {

            if (totCoins1[j] != null) {
                totCoins1[j].render(g);
            }
        }

        for (int j = 0; j < TOT_COINS; j++) {

            if (totCoins2[j] != null) {
                totCoins2[j].render(g);
            }
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        GameLoader game = new GameLoader();
        game.setup(new Main(), new Dimension(CLIP_WIDTH, CLIP_HEIGHT), false);
        game.start();
    }
}
