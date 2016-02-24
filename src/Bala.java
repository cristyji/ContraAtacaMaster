
import java.awt.Image;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rubén
 */
public class Bala extends Base{
    public char cTipo;
    public int iVel;
    
    public Bala(int iX, int iY, Image imaImagen, char cTipo, int iVel) {
        super(iX, iY, imaImagen);
        this.cTipo = cTipo;
        this.iVel = iVel;
    }
    
    public void avanza(){
        switch(cTipo){
            // LA BALA TIENE UN ANGULO DE 135 GRADOS
            case 'i':
                this.setX(this.getX() - iVel);
                this.setY(this.getY() + iVel);
                break;
            // LA BALA TIENE UN ANGULO DE 45 GRADOS
            case 'd':
                this.setX(this.getX() + iVel);
                this.setY(this.getY() + iVel);
                break;
            // LA BALA TIENE UN ANGULO DE 90 GRADOS
            case 'l':
                this.setY(this.getY() + iVel);
                break;
        }
    }
    
    public void setTipo(char cTipo){
        this.cTipo = cTipo;
    }
    
    public char getTipo(){
        return cTipo;
    }
    
    public void setVel(int iVel){
        this.iVel = iVel;
    }
    
    public int getVel(){
        return iVel;
    }
}
