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
    private final int BLOCKED_CELL = -1;
    private final int INTERSECTION = 2;
    private final int OPEN_CELL = 2;
    private final int SQ_SIZE = 32;
    private int closestDiamondXCell;
    private int closestDiamondYCell;
    private BufferedImage map;
    private int mapHeight;
    private int mapWidth;
    private int pickedDiamonds;
    
    public Nodo (int[][] controlMatrix,int posX, int posY, int prevDirection, int height, int direction, int level, BufferedImage map, boolean found, int pickedDiamonds){
        
        pathFound = found;
        matrix = controlMatrix;
        positionX = posX;
        positionY = posY;
        lastDirection = prevDirection; 
        g = height;
        parentIndex = g - 1; /// problem
        validMove(positionX, positionY);
        currentDirection = direction;
        setLevel(level);
        setMap(map);
        setPickedDiamonds(pickedDiamonds);
        
        System.out.println();
        System.out.println("X: "+positionX+"         Y:"+positionY);
        System.out.println("Parent index: "+parentIndex);
        System.out.println("CurrentDirection: "+currentDirection);
        
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
    
    public void setPickedDiamonds(int pickedDiamonds){
        
        this.pickedDiamonds = pickedDiamonds;
    }
    
    public void setMap(BufferedImage map){
        
        this.map = map;
    }
    
    public int computeManhDist(int y1, int x1, int y2, int x2){
        
        return Math.abs(y1 - y2) + Math.abs(x1 - x2);
    }
    
    public void setParentIndex(int index){
        parentIndex = index;
    }
    
    public void getClosestDiamondCells(){
        
        closestDiamondXCell = -1;
        closestDiamondYCell = -1;
        int minManhDist = 1000;
        System.out.println("getClosestDiamond");
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
        System.out.println("FIN getClosestDiamond");
    }
    
    public void setLevel(int level){
        
        this.level = level;
    }
    
    public void setValue(){
        System.out.println("setValue");
        if(level <= 3){ //Weak evaluation function for level 1
            
            value = computeManhDist(positionY, positionX, closestDiamondYCell, closestDiamondXCell);
            value += g;
            System.out.println("value = " + value);
        }
        
        else{ //Stronger evaluation function for level 2
            
            
        }
        System.out.println("Fin setValue");
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
    
    public int getLastDirection(){
        
        return lastDirection;
    }
    
    public void validMove(int posX, int posY){
            if(matrix[positionY - 1][positionX] != BLOCKED_CELL && lastDirection != 1)
                up = true;
            else
                up = false;
            
            if(matrix[positionY + 1][positionX ] != BLOCKED_CELL && lastDirection != 3)
                down = true;
            else
                down = false;
            
            if(matrix[positionY][positionX  + 1] != BLOCKED_CELL && lastDirection != 2)
                right = true;
            else
                right = false;
            
            if(matrix[positionY][positionX - 1] != BLOCKED_CELL && lastDirection != 4)
                left = true;
            else
                left = false;
    }
    
    public boolean isDoorOrDiamond(int posX, int posY){
        
        if(level <= 3){ //First level
            
            if(pickedDiamonds == 4) //If he's already picked all the level's diamonds, check for the door
                return matrix[posY][posX] == DOOR;
            
            return matrix[posY][posX] == DIAMOND; //Check for the diamonds, otherwise
        }
        
        //Second level
        
        if(pickedDiamonds == 8) //If he's already picked all the level's diamonds, check for the door
            return matrix[posY][posX] == DOOR;
        
        return matrix[posY][posX] == DIAMOND; //Check for the diamonds, otherwise
    }
    
    public boolean coordsAreValid(int x, int y){
        
        return (x >= 1) && (x < mapWidth - 1) && (y >= 1) && (y < mapHeight - 1);
    }
    
    public ArrayList<Nodo> computeChildren(int posX, int posY){
        System.out.println("ComputeCholdren!!!!");
        System.out.println("Up " + up + "      down" + down + "      left" + left + "      right" + right);
        ArrayList<Nodo> myChildren = new ArrayList<Nodo>();
        
        if(right == true){
            
            int nextCell = OPEN_CELL;
            
            int posX1 = posX;
            
            
                
                posX1++;
                nextCell = matrix[posY][posX1];
            
            
            if(isDoorOrDiamond(posX1, posY))
                myChildren.add(new Nodo(matrix, posX1, posY, 4, (g + 1), 2, level, map, true, pickedDiamonds));
            else
                myChildren.add(new Nodo(matrix, posX1, posY, 4, (g + 1), 2, level, map, false, pickedDiamonds));
            
        }
        
        if(left == true){
            
            int nextCell = OPEN_CELL;
            
            int posX2 = posX;
            
                
                posX2--;
                nextCell = matrix[posY][posX2];
            
            
            if(isDoorOrDiamond(posX2, posY))
                myChildren.add(new Nodo(matrix, posX2, posY, 2, (g + 1), 4, level, map, true, pickedDiamonds));
            else
                myChildren.add(new Nodo(matrix, posX2, posY, 2, (g + 1), 4, level, map, false, pickedDiamonds));
            
        }
        
        if(up == true){
            
            int nextCell = OPEN_CELL;
            
            int posY1 = posY;
            

                posY1--;
                nextCell = matrix[posY1][posX];
            
            if(isDoorOrDiamond(posX, posY1))
                myChildren.add(new Nodo(matrix, posX, posY1, 3, (g + 1), 1, level, map, true, pickedDiamonds));
            else
                myChildren.add(new Nodo(matrix, posX, posY1, 3, (g + 1), 1, level, map, false, pickedDiamonds));
            
            
        }
        
        if(down == true){
            
            int nextCell = OPEN_CELL;
            
            int posY2 = posY;

                
                posY2++;
                nextCell = matrix[posY2][posX];
            
            if(isDoorOrDiamond(posX, posY2))
                myChildren.add(new Nodo(matrix, posX, posY2, 1, (g + 1), 3, level, map, true, pickedDiamonds));
            else
                myChildren.add(new Nodo(matrix, posX, posY2, 1, (g + 1), 3, level, map, false, pickedDiamonds));
            
            
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
    
    public boolean equals(Nodo other){
        
        return (this.getPositionX() == other.getPositionX() && 
                this.getPositionY() == other.getPositionY() &&
                this.getLastDirection() == other.getLastDirection());
    }
    
    public String toString(){
        
        return "positionX = " + positionX + "; positionY = " + positionY+"; parentIndex = "+parentIndex;
    }
}
