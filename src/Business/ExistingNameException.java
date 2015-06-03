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
public class ExistingNameException extends Exception{
    public ExistingNameException(){
        super();
    }
    public ExistingNameException(String err){
        super(err);
    }
}
