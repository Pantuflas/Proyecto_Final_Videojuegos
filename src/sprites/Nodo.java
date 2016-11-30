/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

/**
 *
 * @author Usuario1
 */

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Nodo implements Comparable<Nodo>{
    
    boolean right = false;
    boolean left = false;
    boolean up = false;
    boolean down = false;
    boolean pathFound = false;
    private int currentDirection;
    private long value;
    int lastDirection = 0; //2 right, 4 left, 1 up, 3 down
    int positionX = 0;
    int positionY = 0;
    int matrix[][]; //Control matrix from the main class
    int g = 0;
    int parentIndex = 0;
    private int level;
    private final int DIAMOND = 3;
    private final int DOOR = 4;
    private final int INTERSECTION = 2;
    private final int SQ_SIZE = 32;
    private int closestDiamondXCell;
    private int closestDiamondYCell;
    private BufferedImage map;
    private int mapHeight;
    private int mapWidth;
    
    public Nodo (int[][] controlMatrix,int posX, int posY, int prevDirection, int height, int direction, int level, BufferedImage map){
        matrix = controlMatrix;
        positionX = posX;
        positionY = posY;
        lastDirection = prevDirection; 
        g = height;
        parentIndex = g - 1;
        validMove(positionX, positionY);
        currentDirection = direction;
        setLevel(level);
        setMap(map);
        
        if(level <= 3){
            
            mapHeight = 15;
            mapWidth = 15;
        }
        
        else{
            
            mapHeight = 23;
            mapWidth = 19;
        }
        
        getClosestDiamondCells();
        setValue();
    }
    
    public void setMap(BufferedImage map){
        
        this.map = map;
    }
    
    public int computeManhDist(int y1, int x1, int y2, int x2){
        
        return Math.abs(y1 - y2) + Math.abs(x1 - x2);
    }
    
    public void getClosestDiamondCells(){
        
        closestDiamondXCell = -1;
        closestDiamondYCell = -1;
        int minManhDist = 1000;
        
        System.out.println("mapHeight = " + mapHeight + "; mapWidth = " + mapWidth);
        System.out.println("positionY = " + positionY + "; positionX = " + positionX);
        
        for(int i = 0; i < mapHeight; i++){
            
            for(int j = 0; j < mapWidth; j++){
                
                if(matrix[i][j] == DIAMOND){
                    
                   int dist = computeManhDist(i, j, positionY, positionX);
                   
                   if(dist < minManhDist){
                       
                       minManhDist = dist;
                       closestDiamondXCell = j;
                       closestDiamondYCell = i;
                   }
                }
            }
        }
        
        System.out.println("closestDiamondYCell = " + closestDiamondYCell + "; closestDiamondXCell = " + closestDiamondXCell);
    }
    
    public void setLevel(int level){
        
        this.level = level;
    }
    
    public void setValue(){
        
        if(level <= 3){ //Weak evaluation function for level 1
            
            value = computeManhDist(positionY, positionX, closestDiamondYCell, closestDiamondXCell);
            value += g*g;
            System.out.println("value = " + value);
        }
        
        else{ //Stronger evaluation function for level 2
            
            
        }
    }
    
    public long getValue(){
        
        return value;
    }
    
    public int getCurrentDirection(){
        
        return currentDirection;
    }
    
    public boolean getPathFound(){
        
        return pathFound;
    }
    
    public int getG(){
        
        return g;
    }
    
    public void validMove(int posX, int posY){
        //If right is posible
            if(matrix[posX + 1][posY] != -1 && lastDirection != 2){
                right = true;
            }
        //If left is posible
            if(matrix[posX - 1][posY] != -1 && lastDirection != 4){
                left = true;
            }
        //If up is posible
            if(matrix[posX][posY - 1] != -1 && lastDirection != 1){
                up = true;
            }
        //If down is posible
            if(matrix[posX][posY + 1] != -1 && lastDirection != 3){
                down = true;
            }
    }
    
    public boolean isDoorOrDiamond(int posX, int posY){
        
        return matrix[posY][posX] == DIAMOND || matrix[posY][posX] == DOOR;
    }
    
    public boolean coordsAreValid(int x, int y){
        
        return (x >= 1) && (x < mapWidth - 1) && (y >= 1) && (y < mapHeight - 1);
    }
    
    public ArrayList<Nodo> computeChildren(int posX, int posY){
        
        ArrayList<Nodo> myChildren = new ArrayList<Nodo>();
        
        if(right == true){
            
            int nextCell = 1;
            
            int posX1 = posX;
            
            while(coordsAreValid(posX1, posY) && nextCell != INTERSECTION){
                
                posX1++;
                System.out.println("posX1 = " + posX1 + "; posY = " + posY);
                nextCell = matrix[posY][posX1];
            }
            
            if(!isDoorOrDiamond(posX, posY))
                myChildren.add(new Nodo(matrix, posX1, posY, 4, (g + 1), 2, level, map));
            
            else{
                
                pathFound = true;
                //return myChildren;
            }
        }
        
        if(left == true){
            
            int nextCell = 1;
            
            int posX2 = posX;
            
            while(coordsAreValid(posX2, posY) && nextCell == 1){
                
                posX2--;
                nextCell = matrix[posY][posX];
            }
            
            if(!isDoorOrDiamond(posX, posY))
                myChildren.add(new Nodo(matrix, posX2, posY, 2, (g + 1), 4, level, map));
            
            else{
                pathFound = true;
                //return myChildren;
            }
        }
        
        if(up == true){
            
            int nextCell = 1;
            
            int posY1 = posY;
            
            while(coordsAreValid(posX, posY1) && nextCell == 1){
                
                posY1--;
                nextCell = matrix[posY1][posX];
            }
            
            if(!isDoorOrDiamond(posX, posY))
                myChildren.add(new Nodo(matrix, posX, posY1, 3, (g + 1), 1, level, map));
            
            else{
                pathFound = true;
                //return myChildren;
            }
        }
        
        if(down == true){
            
            int nextCell = 1;
            
            int posY2 = posY;
            
            while(coordsAreValid(posX, posY2) && nextCell == 1){
                
                posY2++;
                nextCell = matrix[posY2][posX];
            }
            
            if(!isDoorOrDiamond(posX, posY))
                myChildren.add(new Nodo(matrix, posX, posY2, 1, (g + 1), 3, level, map));
            
            else{
                pathFound = true;
                //return myChildren;
            }
        }
            
        return myChildren;
    }
    public int getParentIndex(){
        
        return parentIndex;
    }
    public int getPositionX(){
        
        return positionX;
    }
    public int getPositionY(){
        
        return positionY;
    }
    public boolean foundPath(){
        return pathFound;
    }
    
    public int compareTo(Nodo other){
        
        return Integer.compare((int) this.getValue(), (int) other.getValue());
    }
    
    public String toString(){
        
        return "positionX = " + positionX + "; positionY = " + positionY;
    }
}
