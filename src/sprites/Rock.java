/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import com.golden.gamedev.object.Sprite;
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

/**
 *
 * @author Salvador
 */
public class Rock {
    
    private long initialTime;
    private int xCell;
    private int yCell;
    private Sprite rockSprite;
    private final int MIN_TIME = 4;
    private final int MAX_TIME = 8;
    private int totalTime = -1;
    
    public Rock(long initialTime, int xCell, int yCell, Sprite rockSprite){
        
        setInitialTime(initialTime);
        setXCell(xCell);
        setYCell(yCell);
        setRockSprite(rockSprite);
        setTotalTime();
    }
    
    public void setTotalTime(){
        
        totalTime = MIN_TIME + (int)(Math.random() * ((MAX_TIME - MIN_TIME) + 1));
    }
    
    public int getTotalTime(){
        
        return totalTime;
    }
    
    public void update(long elapsedTime){
        
        rockSprite.update(elapsedTime);
    }
    
    public void render(Graphics2D g){
        
        if(rockSprite == null)
            return;
        
        rockSprite.render(g);
    }
    
    public void setInitialTime(long initialTime){
        
        this.initialTime = initialTime;
    }
    
    public void setRockSprite(Sprite rockSprite){
        
        this.rockSprite = rockSprite;
    }
    
    public long getInitialTime(){
        
        return initialTime;
    }
    
    public void setXCell(int xCell){
        
        this.xCell = xCell;
    }
    
    public int getXCell(){
        
        return xCell;
    }
    
    public void setYCell(int yCell){
        
        this.yCell = yCell;
    }
    
    public int getYCell(){
        
        return yCell;
    }
}
