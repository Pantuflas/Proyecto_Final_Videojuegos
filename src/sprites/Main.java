/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

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
import java.util.Collections;
import java.util.Random;

public class Main extends Game {
    
    Background fondo;
    
    Agente agente;
    Agente agMapa;
    Agente sideB;
    Agente puerta;
    
    SpriteGroup grupoAgente, grupoBala, grupoPuerta;
    SpriteGroup grupoMapa;
    
    colisionAgentes colisionadorBM; //Bala-mapa
    colisionAgentes[] colisionadorAE; //Agente-enemigo
    colisionAgentes[] colisionadorBE; //Bala-enemigo
    colisionAgentes colisionadorAP; //Agente-puerta
    
    Timer velocidad;
    
    private int currBullets;
    
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
    
    private int lives = 3;
    
    private int[] totBobX;
    private int[] totBobY;
    private int[][] cornersCells;
    private final int PIXEL_ERR = SQ_SIZE/8;
    private final int INNER_POINT_DIFF = SQ_SIZE/4;
    
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
    
    private int[] directionCounter;
    
    private int counter = 0;
    private int pickedCoins1 = 0;
    private int pickedCoins2 = 0;
    private int currLevel = 0;
    private long startTime;
    private long currTime;
    private long prevCurrTime = -1;
    private final long TIME_FACTOR = 999999999;
    private final long DOOR_TIME_FACTOR = 10;
    
    private final int totalMaps = 2;
    
    private final String characterName = "bob";
    private final String enemyName = "enemy";
    private final String coinName_red = "diamante_red.png";
    private final String coinName_green = "diamante_green.png";
    private final String[] mapNames = {"Mapa1/Mapa_1_Bordes","Mapa2/Mapa_2_Bordes"};
    private final String doorName = "red_door";
    private int currMap = (currLevel < 3) ? 0 : 1;
    
    private final int CLIP_WIDTH = 960;
    private final int CLIP_HEIGHT = 480;
    
    private final int characterStrip = 8;
    private final int enemyStrip = 5;

    private final int CHARACTER_START_X = SQ_SIZE;
    private final int CHARACTER_START_Y = SQ_SIZE; /*32*//*SQ_SIZE + SQ_SIZE/2*/;
    private final int ENEMY_STARTPOS_X = 11*SQ_SIZE /*448*/; //480
    private final int ENEMY_STARTPOS_Y  = 5*SQ_SIZE /*96*/; //32
    
    private int bobX = -1;
    private int bobY = -1;
    
    //////////////////////////////////////////////////////////////////////
    /*
        
        new code
    
    */
    
    private int pmX = 1;
    private int pmY = 0;
    private double auxmY = -1;
    private double auxmX = -1;
    
    private int mX3 = -1;
    private int mY3 = 0;
    
    private int enemyCoordX = ENEMY_STARTPOS_X;
    private int enemyCoordY = ENEMY_STARTPOS_Y;
    private int direc3 = 4;
    private int prevEnemyDirection = -1;
    
    AnimatedSprite sprite3; //R2, the enemy
    
    private boolean[] levelStarted = {false, false, false, false, false, false, false};
    
    /*
        c = center
        r = right
        u = up
        l = left
        d = down
    */

    public void CargarImagenes(){
        
        bsLoader.storeImages("0_0", getImages("images/" + characterName + "_up.png", characterStrip, 1));
        bsLoader.storeImages("0_1", getImages("images/" + characterName + "_down.png", characterStrip, 1));
        bsLoader.storeImages("0_2", getImages("images/" + characterName + "_left.png", characterStrip, 1));
        bsLoader.storeImages("0_3", getImages("images/" + characterName + "_right.png", characterStrip, 1));
        
        bsLoader.storeImages("2_0", getImages("images/" + doorName + ".png", 1, 1));
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
        
        for(int i = 1; i < 10; i++){
            
            String n = "" + i;            
            numbers[i] = new Sprite(getImage("images/" + n + ".png"), 850, 180);
        }
        
        numbersLives = new Sprite[4];
        
        for(int i = 1; i < 4; i++){
            
            String n = "" + i;           
            numbersLives[i] = new Sprite(getImage("images/" + n + ".png"), 850, 300);
        }               
    }
    
    public void initResources() {
     //inicializacion de las variables del juego
        
        CargarImagenes();
        resetLevel();
    }
    
    public void resetLevel(){
        
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
        System.out.println("mapHeight/SQ_SIZE = " + mapHeight/SQ_SIZE);
        mapWidth = map.getWidth();
        System.out.println("mapWidth/SQ_SIZE = " + mapWidth/SQ_SIZE);
        
        controlMatrix = new int[mapHeight/SQ_SIZE][mapWidth/SQ_SIZE];
        fillControlMatrix();
        
        currBullets = 100;

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
        puerta.setImages(bsLoader.getStoredImages("2_0"));
        puerta.setDirection(0);
        puerta.setBackground(fondo);
        puerta.obtenerBsLoader(bsLoader);
        
        resetDoorCoords();
        
        ////////////////////////////////////////////////////////////
        
        mapHeight = map.getHeight();
        System.out.println("mapHeight/SQ_SIZE = " + mapHeight/SQ_SIZE);
        mapWidth = map.getWidth();
        System.out.println("mapWidth/SQ_SIZE = " + mapWidth/SQ_SIZE);
        controlMatrix = new int[mapHeight/SQ_SIZE][mapWidth/SQ_SIZE];
        fillControlMatrix();
        
        totCoins1 = new Sprite[TOT_COINS];
        totCoins2 = new Sprite[TOT_COINS];
        totCoinsCells1 = new int[TOT_COINS][2];
        totCoinsCells2 = new int[TOT_COINS][2];
        coinMatrix1 = new int[mapHeight/SQ_SIZE][mapWidth/SQ_SIZE - EXTRA_CELLS];
        coinMatrix2 = new int[mapHeight/SQ_SIZE][mapWidth/SQ_SIZE - EXTRA_CELLS];
        setTotCoinsPositions();
        
        //if(currLevel >= 3)
            displayControlMatrix();
        
        fixControlMatrixBorders();
        addIntersectionsToControlMatrix();
        addDiamondsToControlMatrix();
        
        /*System.out.println();
        
        displayControlMatrix();
        */
   
        ////////////////////////////////////////////////////////
        
        currPos = 'c';
        prevPos = 'c';
        
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
        
        colisionadorAP = new colisionAgentes("colisionador AP");
        colisionadorAP.setCollisionGroup(grupoPuerta, grupoAgente);
        
        grupoBala = new SpriteGroup("Grupo bala");
        
        colisionadorBM = new colisionAgentes("BM");
        colisionadorBM.setCollisionGroup(grupoMapa, grupoBala);
        
        velocidad = new Timer(1);
    }
    
    public void resetDoorCoords(){
        
        /*if(currTime%DOOR_TIME_FACTOR != 0)
            return;*/
        
        if(prevCurrTime == currTime)
            return;
        
        prevCurrTime = currTime;
        
        System.out.println("HERE!! currTime = " + currTime);
        ArrayList<Integer> myLX = new ArrayList<Integer>();
        ArrayList<Integer> myLY = new ArrayList<Integer>();
        
        for(int i = 0; i < mapHeight/SQ_SIZE; i++)    
            myLY.add(i);
               
        for(int j = 0; j < mapWidth/SQ_SIZE; j++)
            myLX.add(j);
            
        Collections.shuffle(myLX);
        Collections.shuffle(myLY);
        
        long seed = System.nanoTime();
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(Math.min(mapHeight/SQ_SIZE - 1, mapWidth/SQ_SIZE - 1));
        
        for(int i : myLY)
            for(int j : myLX){
                
                if(controlMatrix[i][j] >= 1){
                    
                    if(randomInt == 0){
                        
                        doorX = j;
                        doorY = i;
                        
                        puerta.setX(SQ_SIZE*doorX);
                        puerta.setY(SQ_SIZE*doorY);
                        
                        System.out.println("doorX = " + doorX + "; doorY = " + doorY);
                        
                        return;
                    }
                    
                    randomInt--;
                }
            }
    }
    
    public void setCoinMatrix(){
        
        for(int i = 0; i < mapHeight/SQ_SIZE; i++)
            for(int j = 0; j < mapWidth/SQ_SIZE - EXTRA_CELLS; j++){
                
                coinMatrix1[i][j] = 0;
                coinMatrix2[i][j] = 0;
            }
    }
    
    public void displayCoinMatrix(){
        
        for(int i = 0; i < mapHeight/SQ_SIZE; i++){
        
            for(int j = 0; j < mapWidth/SQ_SIZE - EXTRA_CELLS; j++){
                
                System.out.print(coinMatrix1[i][j] + " ");
                System.out.print(coinMatrix2[i][j] + " ");
            }
            
            System.out.println();
        }
    }
    
    public boolean thereIsNoCoin1(int y, int x){
        
        return coinMatrix1[y][x] == 0;
    }
    
    public boolean thereIsNoCoin2(int y, int x){
        
        return coinMatrix2[y][x] == 0;
    }
    
    public boolean areGoodCoinCells(int y, int x){
        
        return (y >= 1 && y < mapHeight/SQ_SIZE) && (x >= 1 && x < mapWidth/SQ_SIZE - EXTRA_CELLS) && (controlMatrix[y][x] >= 1) && (y != CHARACTER_START_Y/SQ_SIZE || x != CHARACTER_START_Y/SQ_SIZE);
    }
    
    public void setTotCoinsPositions(){
        
        
        if(currMap == 0){ //Coin positions for map 1
            
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
        }
        
        else{ //Coin positions for map 2
            
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
    
    void displayTotCoinsCells(){
        
        for(int i = 0; i < TOT_COINS; i++){
             
            System.out.println("totCoinsCells1[" + i + "][x] = " + totCoinsCells1[i][0]);
            System.out.println("totCoinsCells1[" + i + "][y] = " + totCoinsCells1[i][1]);
            
            System.out.println("totCoinsCells2[" + i + "][x] = " + totCoinsCells2[i][0]);
            System.out.println("totCoinsCells2[" + i + "][y] = " + totCoinsCells2[i][1]);
        }
    }
    
    public void createCoins(){
        
        totCoins1 = new Sprite[TOT_COINS];
        totCoins2 = new Sprite[TOT_COINS];
        
        for(int i = 0; i < TOT_COINS; i++){   
            
            totCoins1[i] = new Sprite(getImage("images/" + coinName_red), SQ_SIZE*totCoinsCells1[i][0], SQ_SIZE*totCoinsCells1[i][1]);
            totCoins1[i].setBackground(fondo); //Don't let the coins move!!!
            
            totCoins2[i] = new Sprite(getImage("images/" + coinName_green), SQ_SIZE*totCoinsCells2[i][0], SQ_SIZE*totCoinsCells2[i][1]);
            totCoins2[i].setBackground(fondo); //Don't let the coins move!!!
        }
    }
    
    public void deleteCoins(){
        
        for(int i = 0; i < TOT_COINS; i++){   
            
            totCoins1[i] = null;
            totCoins2[i] = null;
        }
    }
    
    public void computeCornersCoords(){
        
        totBobX[0] = bobX + INNER_POINT_DIFF + PIXEL_ERR;
        totBobY[0] = bobY + INNER_POINT_DIFF + PIXEL_ERR;
        
        totBobX[1] = bobX - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;
        totBobY[1] = bobY + INNER_POINT_DIFF + PIXEL_ERR;
        
        totBobX[2] = bobX + INNER_POINT_DIFF + PIXEL_ERR;
        totBobY[2] = bobY - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;
        
        totBobX[3] = bobX - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;
        totBobY[3] = bobY - INNER_POINT_DIFF + SQ_SIZE - PIXEL_ERR;
    } 
    
    public void computeCornersCells(){
        
        for(int i = 0; i < 4; i++){
            
            cornersCells[i][0] = totBobX[i]/SQ_SIZE;
            cornersCells[i][1] = totBobY[i]/SQ_SIZE;
        }
    }
    
    public void displayTotCoords(){
        
        for(int i = 0; i < 4; i++)
            System.out.println("totBobX[" + i  + "] = " + totBobX[i] + "; totBobY[" + i + "] = " + totBobY[i]);
    }
    
    public void displayCornersCells(){
        
        for(int i = 0; i < 4; i++)
            System.out.println("cornersCells[" + i + "][0] = " + cornersCells[i][0] + "; cornersCells[" + i + "][1] = " + cornersCells[i][1]);
    }
    
    public void displayControlMatrix(){
        
        for(int i = 0; i < mapHeight/SQ_SIZE; i++){
            
            for(int j = 0; j < mapWidth/SQ_SIZE; j++){
                
                if(controlMatrix[i][j] >= 0)
                    System.out.print(" ");
                    
                System.out.print(controlMatrix[i][j] + " ");
            }
            
            System.out.println();
        }
    }
    
    public void fillControlMatrix(){
        
        getPixelsRGB();
    }
    
    public boolean isCruise(int x, int y){
        
        int height = mapHeight/SQ_SIZE;
        int width = mapWidth/SQ_SIZE;
        
        int xCoord[] = {0, 1, 0, -1};
        int yCoord[] = {-1, 0, 1, 0};
        
        int totCrosses = 0;
        
        for(int i = 0; i < 4; i++){
            
            if(x + xCoord[i] >= 0 && x + xCoord[i] < width && y + yCoord[i] >= 0 && y + yCoord[i] < height){
                if(x + xCoord[(i + 1)%4] >= 0 && x + xCoord[(i + 1)%4] < width && y + yCoord[(i + 1)%4] >= 0 && y + yCoord[(i + 1)%4] < height){
                    if(controlMatrix[y + yCoord[i]][x + xCoord[i]] > 0 && controlMatrix[y + yCoord[(i + 1)%4]][x + xCoord[(i + 1)%4]] > 0)
                        totCrosses++;
                }
            }
        }

        return totCrosses > 0;
    }
    
    public void addIntersectionsToControlMatrix(){
        
        int height = mapHeight/SQ_SIZE;
        int width = mapWidth/SQ_SIZE;
        
        for(int i = 0; i < height; i++){
            
            for(int j = 0; j < width; j++){
                
                if(controlMatrix[i][j] > 0)
                    if(isCruise(j, i))
                        controlMatrix[i][j] = 2;
            }
        }
    }
    
    public void addDiamondsToControlMatrix(){
        
        int height = mapHeight/SQ_SIZE;
        int width = mapWidth/SQ_SIZE;
        
        for(int i = 0; i < height; i++){
            
            for(int j = 0; j < width; j++){
                
                for(int k = 0; k < 4; k++){
                    
                    if(j == totCoinsCells2[k][0] && i == totCoinsCells2[k][1])
                        controlMatrix[i][j] = DIAMOND;
                }
            }
        }
    }
    
    public void fixControlMatrixBorders(){
        
        int height = mapHeight/SQ_SIZE;
        int width = mapWidth/SQ_SIZE;
        
        for(int i = 0; i < height; i++){
            
            for(int j = 0; j < width; j++){
                
                if(i == 0 || j == 0 || i == height - 1 || j == width - 1)
                    controlMatrix[i][j] = -1;
            }
        }
    }
    
    public void getPixelsRGB(){
        
        int w = map.getWidth();
        int h = map.getHeight();
        //System.out.println("width, height: " + w + ", " + h);

        for (int i = SQ_SIZE/2; i < h; i += SQ_SIZE){
            
            for (int j = SQ_SIZE/2; j < w; j += SQ_SIZE){
            
                //System.out.println("x,y: " + j + ", " + i);
                int pixel = map.getRGB(j, i);
                printPixelARGB(pixel, i, j);
                //System.out.println("");
            }
        }
    }
    
    public boolean isBlackCell(int red, int green, int blue){
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
                ||(red == 43 && green == 36 && blue == 29) || (red == 41 && green == 34 && blue == 27)
                ||(red == 39 && green == 29 && blue == 23) || (red == 49 && green == 42 && blue == 36);
    }
    
    public boolean isGreenCell(int red, int green, int blue){
        
        return blue <= 20;
    }
    
    public void printPixelARGB(int pixel, int i, int j) {
        
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        //System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
        
        if(j/SQ_SIZE < map.getWidth()){
            
            //System.out.println("i = " + i + "; j = " + j);
            
            if(isGreenCell(red, green, blue))
                controlMatrix[i/SQ_SIZE][j/SQ_SIZE] = OPEN_CELL;
        
            else
                controlMatrix[i/SQ_SIZE][j/SQ_SIZE] = BLOCKED_CELL;
        }
        
        else
            controlMatrix[i/SQ_SIZE][j/SQ_SIZE] = OUT_OF_BOUNDS;
    }
    
    public void checkTime(){ 
        
        if(startTime - System.nanoTime()/TIME_FACTOR < 0)
            currTime = System.nanoTime()/TIME_FACTOR - startTime;
        
        //System.out.println("currTime = " + currTime);
        
        //if(currTime >= 100)
            //currLevel = 6;
    }

    public void update(long elapsedTime) {
    //actualizacion de las variables del juego
    
    if(lives == 0)
        currLevel = 6;
    
    //System.out.println("currLevel = " + currLevel); 
     
    switch(currLevel){

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

            levelStarted[2] = true;

            if(pickedCoins1 == TOT_COINS + 1)
                currLevel++;
            
            checkTime();

            updateGameWithObjects(elapsedTime);

            break;

        case 3:
 
            if(levelStarted[3] == false){

                //startTime = (long) System.nanoTime()/TIME_FACTOR;
                //TOT_COINS *= 2;
                //resetLevel();
                //setTotCoinsPositions();
                //createCoins();
                deleteCoins();
                levelStarted[3] = true;
            }

            //checkTime();

            if(keyDown(KeyEvent.VK_SPACE))
                currLevel++; 

            //updateGameWithObjects(elapsedTime);

           break;

        case 4:

            if(levelStarted[4] == false){

                startTime = (long) System.nanoTime()/TIME_FACTOR;
                TOT_COINS *= 2;
                resetLevel();
                levelStarted[4] = true;
            }

            checkTime();

            if(pickedCoins1 == TOT_COINS + 1) ////////////////////////// ????/
                currLevel++;  

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
    
    public void updateGame(long elapsedTime){
        
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
        
        switch(lives){
            
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
         *  Update operations for the main character
         */
        if (keyDown(KeyEvent.VK_RIGHT)) {
            
            if(controlMatrix[cornersCells[1][1]][cornersCells[1][0] /*+ 1*/] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0] /*+ 1*/] != -1)
                currPos = 'r';
                
            else
                currPos = 'c';
           //
           //System.out.println("right arrow pressed");
        }
        
        else if (keyDown(KeyEvent.VK_LEFT)) {
            
            if(controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1)
               currPos = 'l'; 
            
            else
                currPos = 'c';
        }
        
        else if (keyDown(KeyEvent.VK_UP)) {
           
            if(controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1 && controlMatrix[cornersCells[1][1]][cornersCells[1][0]] != -1)
                currPos = 'u';
                
            else
                currPos = 'c';
        }
        
        else if (keyDown(KeyEvent.VK_DOWN)) {
           
            if(controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0]] != -1)
                currPos = 'd';
            
            else
                currPos = 'c';
        }

        else
            currPos = 'c';
        
        
        switch(currPos){
            
            case 'u':
                
                if(prevPos != 'u'){
                
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
                
                if(prevPos != 'd'){
                    
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
                
                if(prevPos != 'l'){
                
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
                
                if(prevPos != 'r'){
                    
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
                
                if(prevPos != 'c'){
                    
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
    }
    
    public void shoot(double x, double y){
        
        if(currBullets > 0){
            
            Sprite bal = new Sprite(getImage("images/bala.png"), x, y);       
        
            switch(prevPos){

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
    
    public void checkBullets(){
        
        Sprite[] myBullets = grupoBala.getSprites();
        
        for (Sprite myBullet : myBullets) {
            if (myBullet != null) {
                if (controlMatrix[(int) myBullet.getY() / SQ_SIZE][(int) myBullet.getX() / SQ_SIZE] == -1) {
                    myBullet.setActive(false);
                }
            }
        }
    }
    
    public void updateGameWithObjects(long elapsedTime){
        
        //sideBar.update(elapsedTime);
        computeCornersCoords();
        //displayTotCoords();
        computeCornersCells();
        //displayCornersCells();
        
        //System.out.println("bobX = " + bobX + "; bobY = " + bobY);
        //System.out.println("bobX/SQ_SIZE = " + bobX/SQ_SIZE + "; bobY/SQ_SIZE = " + bobY/SQ_SIZE);
        //System.out.println("controlMatrix[" + bobY/SQ_SIZE + "][" + bobX/SQ_SIZE + "] = " + controlMatrix[bobY/SQ_SIZE][bobX/SQ_SIZE]);        
        //System.out.println();
        
        if(velocidad.action(elapsedTime)){
            
            moveCharacter(elapsedTime);
            moveR2(elapsedTime);
        }
        
        fondo.setToCenter(agente);
        fondo.update(elapsedTime);
        grupoAgente.update(elapsedTime);
        grupoPuerta.update(elapsedTime);
        
        grupoBala.update(elapsedTime);
        checkBullets();
        updateCoins(elapsedTime);
        
        //colisionadorBM.checkCollision(); //Create map through object
        
        if(pickedCoins1 == TOT_COINS)
            colisionadorAP.checkCollision();
        
        //System.out.println("pickedCoins = " + pickedCoins);
        
        if(colisionadorAP.getCollision()){ //The player got to the door, level up!
            
            currLevel++;
            currMap++;
            map = getImage(mapNames[currMap] + ".png");
        }
        
        if(keyPressed(KeyEvent.VK_SPACE))           
            shoot(agente.getX() + SQ_SIZE/2, agente.getY() + SQ_SIZE/2);  
        
        puerta.update(elapsedTime);
        
        currTime++;
        
        if(currTime%DOOR_TIME_FACTOR == 0){
            
            resetDoorCoords();  
        }
    }
    
    public void moveCharacter(long elapsedTime){   
        
        //sideBar.update(elapsedTime);
        computeCornersCoords();
        //displayTotCoords();
        computeCornersCells();
        //displayCornersCells();
        
        if(keyDown(KeyEvent.VK_UP)){

            if(controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1 && controlMatrix[cornersCells[1][1]][cornersCells[1][0]] != -1){
                
                agente.setDirection(0);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, 0, -1);
                currPos = 'u'; 
                prevPos = 'u';
                
                bobY -= 1;
            }
                
            else{
                
                agente.setStatus(0);
                currPos = 'c';
            }
        }
        
        else if (keyDown(KeyEvent.VK_DOWN)){
           
            if(controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0]] != -1){
                
                agente.setDirection(1);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, 0, 1);
                currPos = 'd'; 
                prevPos = 'd';
                
                bobY += 1;
            }
            
            else{
                
                agente.setStatus(0);
                currPos = 'c';
            }
        }  
        
        else if (keyDown(KeyEvent.VK_LEFT)){
            
            if(controlMatrix[cornersCells[2][1]][cornersCells[2][0]] != -1 && controlMatrix[cornersCells[0][1]][cornersCells[0][0]] != -1){
                
                agente.setDirection(2);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, -1, 0);
                currPos = 'l'; 
                prevPos = 'l';
                
                bobX -= 1;
            }
            
            else{
                
                agente.setStatus(0);
                currPos = 'c';
            }
        }
        
        else if(keyDown(KeyEvent.VK_RIGHT)){
            
            if(controlMatrix[cornersCells[1][1]][cornersCells[1][0] /*+ 1*/] != -1 && controlMatrix[cornersCells[3][1]][cornersCells[3][0] /*+ 1*/] != -1){
                
                agente.setDirection(3);
                agente.setStatus(1);
                agente.avanzar(elapsedTime, 1, 0);
                currPos = 'r';
                prevPos = 'r'; 
                bobX += 1;
            }
                
            else{
                
                agente.setStatus(0);
                currPos = 'c';
            }
        }
        
        else{
                        
            agente.setStatus(0);
            currPos = 'c';
        }
    }
    
    public void resetCharacter(){
        
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
        
    public int PosSpriteX(double xCoord){
        
        return (int) (xCoord)/SQ_SIZE;
    }
    
    public void setEnemyCoords(){
        
        enemyCoordX = (int) sprite3.getX();
        enemyCoordY = (int) sprite3.getY();
    }
    
    public void changeAnimation(int character, int direction){
        
        if(prevEnemyDirection == direction)
            return;
        
        //System.out.println("prevEnemyDirection = " + prevEnemyDirection);
        //System.out.println("direction = " + direction);
        prevEnemyDirection = direction;
        
        setEnemyCoords();
        
        switch(direction){
            
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
    
    public void moveR2(long elapsedTime){
        
        setEnemyCoords(); 
        //System.out.println("coordX = " + enemyCoordX + "; coordY = " + enemyCoordY);
        pmX = (enemyCoordX + SQ_SIZE/2)/SQ_SIZE; /*PosSpriteX((totSpiders[0][].getX()+13)); //X Cell*/
        pmY = (enemyCoordY + SQ_SIZE/2)/SQ_SIZE; /*PosSpriteX((sprite3.getY()+13)); //Y Cell*/
        
        auxmX = enemyCoordX - SQ_SIZE*pmX;/*sprite3.getX()-(65.5*pmX); //X position in cell*/
        auxmY = enemyCoordY - SQ_SIZE*pmY + 16;/*sprite3.getY()-(65.5*pmY); //Y Position in cell*/
        
        if(auxmX < 0)
            auxmX *= -1;
        
        if(auxmY < 0)
            auxmY *= -1;
        
        //System.out.println("pmX = " + pmX + "; pmY = " + pmY);
        //System.out.println("auxmX = " + auxmX + "; auxmY = " + auxmY);
        //System.out.println("direc3 = " + direc3);
        //System.out.println();
        
        int err = SQ_SIZE/8; //3-pixel error
        
        boolean decisionTaken = false;
         
        if(auxmX == SQ_SIZE/2 && auxmY == SQ_SIZE/2){
            
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
        changeAnimation(1, direc3);
    } // movimiento de sprite 3 (el enemigo)
    
    public boolean areGoodCoords(int i, int j){
        
        System.out.println("i = " + i + "; j = " + j);
        return (i > 0 && i < mapHeight/SQ_SIZE - 1) && (j > 0 && j < mapWidth/SQ_SIZE - 1);
    }
    
    public double getEuclidianDistance(int x1, int y1, int x2, int y2){
        
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    
    public boolean theyCrashed(int x1, int y1, int x2, int y2){
        
        return getEuclidianDistance(x1, y1, x2, y2) <= SQ_SIZE/CLOSENESS_FACTOR;
    }
    
    public void updateCoins(long elapsedTime){
        
        for(int i = 0; i < TOT_COINS; i++){
            for(int j = 0; j < 4; j++){
                
                if(totCoins1[i] != null){ //Character's coins
                    
                    if(theyCrashed(SQ_SIZE*cornersCells[j][0], SQ_SIZE*cornersCells[j][1], SQ_SIZE*totCoinsCells1[i][0], SQ_SIZE*totCoinsCells1[i][1])){
                        
                        playSound("sounds/coin.wav");
                        totCoins1[i] = null;
                        pickedCoins1++; //Character's counter 
                    }

                    else
                        totCoins1[i].update(elapsedTime);
                } 
                
                if(totCoins2[i] != null){ //Enemy's coins
                    
                    if(theyCrashed(SQ_SIZE*pmX, SQ_SIZE*pmY, SQ_SIZE*totCoinsCells2[i][0], SQ_SIZE*totCoinsCells2[i][1])){
                        
                        playSound("sounds/coin.wav");
                        totCoins2[i] = null;
                        pickedCoins2++; //Enemy's counter
                    }

                    else
                        totCoins2[i].update(elapsedTime);
                } 
            }
        }
    }   
    
    public void render(Graphics2D g){               
        
        switch(currLevel){
            
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
    
    public void renderGameWithObjects(Graphics2D g){
        
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
        sprite3.render(g);
        
        grupoBala.render(g);
        
        for(int j = 0; j < TOT_COINS; j++){
            
            if(totCoins1[j] != null)
                totCoins1[j].render(g);
        }
        
        for(int j = 0; j < TOT_COINS; j++){
            
            if(totCoins2[j] != null)
                totCoins2[j].render(g);
        }
        
        //g.drawString("Bullets:" + currBullets, 780, 250);
    }
    
    public void renderGame(Graphics2D g) {
     //rendereo de la pantalla        
     
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.drawImage(map, 0, 0, null);
        
        //sideBar.render(g);
        
        switch(lives){
            
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
        
        if(currLevel == 4){
            
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
        }
       
        switch(currPos){
            
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
  
        for(int j = 0; j < TOT_COINS; j++){
            
            if(totCoins1[j] != null)
                totCoins1[j].render(g);
        }
        
        for(int j = 0; j < TOT_COINS; j++){
            
            if(totCoins2[j] != null)
                totCoins2[j].render(g);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        GameLoader game = new GameLoader();
        game.setup(new Main(), new Dimension(960, 480), false);
        game.start();
    }
}
