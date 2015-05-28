/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Business.Menu;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
        ComunicacaoCliente comC;
        Utilizador ut;
        String input1, input2;
        try {
            comC = new ComunicacaoCliente();
            Cliente.CarregaMenus();
            Cliente.menuLogin.executa();
            switch (Cliente.menuLogin.getOpcao()) 
            {
                case 1:
                    System.out.println("-------------REGISTO-----------");
                    System.out.println("Username: ");
                    input1 = Input.lerString();
                    System.out.println("Password: ");
                    input2 = Input.lerString();
                    //registo aqui crl
                    break;
                case 2:
                    System.out.println("------------LOGIN-------------");
                    System.out.println("Username: ");
                    input1 = Input.lerString();
                    System.out.println("Password: ");
                    input2 = Input.lerString();
                    comC.logInServer(input1, input2);
                    break;
                default:
                    break;

            }
            if(Cliente.menuLogin.getOpcao()!=0) {
                //executar menu de jogo e o caralho tb
                
            }
        } catch ( IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
         System.out.println("CLIENTE ON");
         try
         {
         comC = new ComunicacaoCliente();
         comC.logInServer("user1", "zezinho");
         String user = comC.getRespostaString();
         System.out.println("Login completo com: " + user);
         } catch (IOException ex)
         { Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex); }*/
        /*
         
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
