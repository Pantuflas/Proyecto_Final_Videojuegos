/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sprites;

import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.sprite.AdvanceSprite;
import com.golden.gamedev.object.collision.BasicCollisionGroup;

/**
 *
 * @author iolmos\
 *  Un colisionador para limites de pantalla
 *  Un colisionador para fantasma
 *  Un colisionador para 
 */
public class colisionAgentes extends BasicCollisionGroup  {
    
    String id;
    boolean collision;
    
    public colisionAgentes(String id){
        
        this.id = id;
        collision = false;
    }
    
    {
        pixelPerfectCollision = true;
    }

    public void collided(Sprite agente1, Sprite agente2) {
    //en esta funcion se declara que se va a hacer si se detecta una colision
    //en tres los agente1 y agente2, que ya entran como sprites
       //agente1.setX(agente1.getOldX()-3);
       
       if(agente1 == null || agente2 == null){
            
            collision = false;
            return;
       }
       
       agente1.setActive(true);
       agente2.setActive(false);
       
       if(id.equals("BE"))
           agente1.setActive(false);
       
       collision = true;
    } 
    
    public boolean getCollision(){
        
        return collision;
    }
}
