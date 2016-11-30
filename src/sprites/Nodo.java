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

import java.util.ArrayList;

public class Nodo implements Comparable<Nodo>{
    
    boolean right = false;
    boolean left = false;
    boolean up = false;
    boolean down = false;
    boolean pathFound = false;
    private int currentDirection;
    int lastDirection = 0; //1 right, 2 left, 3 up, 4 down
    int positionX = 0;
    int positionY = 0;
    int matrix[][]; //Control matrix from the main class
    int g = 0;
    int father = 0;
    
    public Nodo (int[][] controlMatrix,int posX, int posY, int last, int lastPos){
        
        matrix = controlMatrix;
        positionX = posX;
        positionY = posY;
        lastDirection = last; 
        g = lastPos;
        father = g - 1;
        validMove(positionX, positionY);
    }
    
    
    
    public boolean getPathFound(){
        
        return pathFound;
    }
    
    public int getG(){
        
        return g;
    }
    
    public void validMove(int posX, int posY){
        //If right is posible
            if(matrix[posX + 1][posY] != -1 && lastDirection != 1){
                right = true;
            }
        //If left is posible
            if(matrix[posX - 1][posY] != -1 && lastDirection != 2){
                left = true;
            }
        //If up is posible
            if(matrix[posX][posY - 1] != -1 && lastDirection != 3){
                up = true;
            }
        //If down is posible
            if(matrix[posX][posY + 1] != -1 && lastDirection != 4){
                down = true;
            }
    }
    public ArrayList<Nodo> computeChildren(int posX, int posY){
        
        ArrayList<Nodo> myChildren = new ArrayList<Nodo>();
        
        if(right == true){
            int nextCell = 1;
            while(nextCell == 1){
                posX++;
                nextCell = matrix[posX][posY];
            }
            if(matrix[posX][posY] == 2)
                myChildren.add(new Nodo(matrix, posX, posY, 2, (g + 1)));
            else{
                pathFound = true;
            }
        }
        if(left == true){
            int nextCell = 1;
            while(nextCell == 1){
                posX--;
                nextCell = matrix[posX][posY];
            }
            if(matrix[posX][posY] == 2)
                myChildren.add(new Nodo(matrix, posX, posY, 1, (g + 1)));
            else{
                pathFound = true;
            }
        }
        if(up == true){
            int nextCell = 1;
            while(nextCell == 1){
                posY--;
                nextCell = matrix[posX][posY];
            }
            if(matrix[posX][posY] == 2)
                myChildren.add(new Nodo(matrix, posX, posY, 4, (g + 1)));
            else{
                pathFound = true;
            }
        }
        if(down == true){
            int nextCell = 1;
            while(nextCell == 1){
                posY++;
                nextCell = matrix[posX][posY];
            }
            if(matrix[posX][posY] == 2)
                myChildren.add(new Nodo(matrix, posX, posY, 3, (g + 1)));
            else{
                pathFound = true;
            }
        }
            
        return myChildren;
    }
    public int getFather(){
        return father;
    }
    public int getPosX(){
        return positionX;
    }
    public int getPosY(){
        return positionY;
    }
    public boolean foundPath(){
        return pathFound;
    }
    
    public int compareTo(Nodo other){
        
        return Integer.compare(this.getG(), other.getG());
    }
}
