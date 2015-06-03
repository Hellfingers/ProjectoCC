/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

/**
 *
 * @author Pedro Cunha
 */
public class NotEnoughElementsException extends Exception{
    public NotEnoughElementsException(){
        super();
    }
    public NotEnoughElementsException(int nElems){
        super(""+nElems);
    }
}
