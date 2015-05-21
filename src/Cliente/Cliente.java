/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Business.Menu;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Business.Input;
import Business.Menu;
import Business.Utilizador;

/**
 *
 * @author core
 */
public class Cliente {

    private static Menu menuLogin, menuJogo, menuDesafio;

    public static void CarregaMenus() 
    {
        String opcoesLogin[] = {"Registar Utilizador", "Fazer Login"};
        String opcoesJogo[] = {"Criar Desafio", "Ver Classificações", "Ver Desafios", "Realizar Desafio", "Terminar Desafio", "Eliminar Desafio", "Terminar Sessão"};
        String opcoesDesafio[] = {"Responder a Pergunta", "Terminar Desafio", "Desistir de Desafio"};
        Cliente.menuLogin = new Menu(opcoesLogin);
        Cliente.menuJogo = new Menu(opcoesJogo);
        Cliente.menuDesafio = new Menu(opcoesDesafio);
    }

    public static void main(String[] args) 
    {
        Utilizador ut;
        Cliente.CarregaMenus();
        Cliente.menuLogin.executa();
        if (Cliente.menuLogin.getOpcao() == 1) {
            System.out.println("Futuramente Registo");
            //Registo
        } else if (Cliente.menuLogin.getOpcao() == 2) {
            System.out.println("Futuramente Login");
            //User Pass
        }
        do
        {
        Cliente.menuJogo.executa();
        }
        while(Cliente.menuJogo.getOpcao()!=0);
        
        
        /*
         ComunicacaoCliente comC;
         System.out.println("CLIENTE ON");
         try 
         {
         comC = new ComunicacaoCliente();
         comC.logInServer("user1", "zezinho");  
         String user = comC.getRespostaString();            
         System.out.println("Login completo com: " + user);
         } catch (IOException ex) 
         { Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex); }*/
    }
}
