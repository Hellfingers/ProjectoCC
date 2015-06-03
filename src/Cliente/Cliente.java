/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Business.Menu;
import java.net.MalformedURLException;
import java.net.URL;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Business.Input;
import Business.Menu;
import Business.PDU;
import Business.Picture;
import Business.Utilizador;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.TreeSet;

/**
 *
 * @author core
 */
public class Cliente {

    private static Menu menuLogin, menuJogo, menuDesafio;
    
    public static void carregaFicheiro(String dest, TreeSet<byte[]> pacotes) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest + ".mp3");
        byte res[] = new byte[pacotes.size() * PDU.MAX_PACOTE_SIZE];
        int contador = 0;
        for (byte[] pacote : pacotes) {
            System.arraycopy(pacote, 9, res, contador * PDU.MAX_PACOTE_SIZE, PDU.MAX_PACOTE_SIZE);
        }
        fos.write(res);

    }

    public static void CarregaMenus() {
        String opcoesLogin[] = {"Registar Utilizador", "Fazer Login"};
        String opcoesJogo[] = {"Criar Desafio", "Ver Classificações", "Ver Desafios", "Realizar Desafio", "Terminar Desafio", "Eliminar Desafio"};
        String opcoesDesafio[] = {"Responder a Pergunta", "Terminar Desafio", "Desistir de Desafio"};

        Cliente.menuLogin = new Menu(opcoesLogin);
        Cliente.menuJogo = new Menu(opcoesJogo);
        Cliente.menuDesafio = new Menu(opcoesDesafio);
    }

    public static void playSong(String filename) {
        String songName = filename;
        String pathToMp3 = System.getProperty("user.dir") + "/media/music/" + songName;
        BasicPlayer player = new BasicPlayer();
        try {
            player.open(new URL("file:///" + pathToMp3));
            player.play();
        } catch (BasicPlayerException | MalformedURLException e) {
        }
    }
    
    public static void showImage(String filename){
        String imgName = filename;
        String pathToImg = System.getProperty("user.dir") + "\\media\\images\\" + imgName;
        Picture p=new Picture(pathToImg);
        p.show();
    }

    public static void execMenuPrincipal() {
        do {
            Cliente.menuJogo.executa();
            switch (Cliente.menuJogo.getOpcao()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 0:
            }
        } while (Cliente.menuJogo.getOpcao() != 0);
    }

    public static void main(String[] args) {
        ComunicacaoCliente comC;
        Utilizador ut;
        String input1, input2;
        Cliente.playSong("01. World On Fire.mp3");
        Cliente.showImage("cover.jpg");
        try {
            comC = new ComunicacaoCliente();
            Cliente.CarregaMenus();
            Cliente.menuLogin.executa();
            switch (Cliente.menuLogin.getOpcao()) {
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
            if (Cliente.menuLogin.getOpcao() != 0) {
                Cliente.execMenuPrincipal();

            }
            else System.exit(0);
        } catch (IOException ex) {
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
