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
public class NonexistingNameException extends Exception{
    public NonexistingNameException(){
        super();
    }
    public NonexistingNameException(String err){
        super(err);
    }
}
