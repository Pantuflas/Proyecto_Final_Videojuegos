/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sprites;


import com.golden.gamedev.Game;
import com.golden.gamedev.engine.BaseIO;
import com.golden.gamedev.engine.BaseLoader;
import com.golden.gamedev.object.AnimatedSprite;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.sprite.AdvanceSprite;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;


/**
 *
 * @author iolmos
 */
public class Agente extends AdvanceSprite {
    
    String id;
    BaseLoader miBSLoader;
    int estado; //variable que almacena los estados que puede tener el agente
    int direccion; //direccion a la cual debe de ver el monito: 1 derecha, 0 izquierda
    boolean aux1 = false, aux2 = false;

    public Agente(String id){
        
        super();
        estado = 0; //equivalente a no hacer nada y estar solo parado
        direccion = 1;
        this.id = id;
    }

    /*
     * arg0: oldstatus
     * arg1: olddir
     * arg2: status
     * arg3: dir
     */
    protected void animationChanged(int oldStatus, int oldDir, int currStatus, int currDir) {
        
        super.animationChanged(oldStatus, oldDir, currStatus, currDir);
        
        /*
         * Aquí se deberia de colocar lo que va a hacer el agente 
         */
        //si se desea cambiar la velocidad de animacion con un delay
        
        switch(currStatus){
            
            case 0: //agente esta parado sin hacer nada
                
                this.getAnimationTimer().setDelay(150);                
                this.setImages(miBSLoader.getStoredImages(id + "_" + currDir));                              
                this.setAnimate(false);
                this.setLoopAnim(false);

                break;
                 
            case 1: //el agente esta caminando
                
                this.getAnimationTimer().setDelay(150);                
                this.setImages(miBSLoader.getStoredImages(id + "_" + currDir));  
                this.setAnimate(true);
                this.setLoopAnim(true);
                 
                break;
                
            case 3:
                
                this.getAnimationTimer().setDelay(150);
                this.setAnimate(true);
                //this.setLoopAnim(true);
                 
                break;
        }
    }

    /*
     * Con esta funcion, se carga el bsloader en el agente, para que lo tenga referenciado
     */
    public void obtenerBsLoader(BaseLoader bsLoader) {
        
        this.miBSLoader = bsLoader;
    }

    public void CambiarStatus(int valor){
        
        setStatus(valor);
    }

    public void Cambiardireccion(int d){
        
        setDirection(d);
    }

     public boolean avanzar(long elapsedTime, double dx, double dy) {//avanzar hacia la derecha
        
        update(elapsedTime);
        move(dx, dy);
        return true;
    }

    public boolean parar(long elapsedTime, double lapso) {
        
        update(elapsedTime);
        setSpeed(0, 0);
        return true;
    }
}
