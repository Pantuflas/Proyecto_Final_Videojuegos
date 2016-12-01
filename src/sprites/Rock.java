/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

/**
 *
 * @author Salvador
 */
public class Rock {
    
    private long initialTime;
    private int xCell;
    private int yCell;
    
    public Rock(long initialTime, int xCell, int yCell){
        
        setInitialTime(initialTime);
        setXCell(xCell);
        setYCell(yCell);
    }
    
    public void setInitialTime(long initialTime){
        
        this.initialTime = initialTime;
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
