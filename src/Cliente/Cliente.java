/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author core
 */
public class Cliente {

    public static void main(String[] args) 
    {        
        ComunicacaoCliente comC;
        System.out.println("CLIENTE ON");
        try 
        {
            comC = new ComunicacaoCliente();
            comC.logInServer("user1", "zezinho");  
            String user = comC.getRespostaString();            
            System.out.println("Login completo com: " + user);
        } catch (IOException ex) 
        { Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex); }
    }
}
