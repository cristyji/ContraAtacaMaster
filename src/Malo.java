
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
public class Malo extends Base {
    private char cTipo;
    
    public Malo(int iX, int iY, Image imaImagen, char cTipo) {
        super(iX, iY, imaImagen);
        this.cTipo = cTipo;
    }
    
    public void setTipo(char cTipo){
        this.cTipo = cTipo;
    }
    
    public char getTipo(){
        return cTipo;
    }
    
}
