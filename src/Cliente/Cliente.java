/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Business.Desafio;
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
import Business.ParUsernamePontos;
import Business.Pergunta;
import Business.Picture;
import Business.Utilizador;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author core
 */
public class Cliente {

    private static Menu menuLogin, menuJogo, menuDesafio, menuInGame;
    private static BasicPlayer player;
    private static Picture imagem;
    private static ComunicacaoCliente comC;
    
    public static void carregaFicheiroMusica(String dest, TreeSet<byte[]> pacotes) throws IOException {
        FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/media/music/"+dest + ".mp3");
        byte res[] = new byte[pacotes.size() * PDU.MAX_PACOTE_SIZE];
        int contador = 0;
        for (byte[] pacote : pacotes) {
            System.arraycopy(pacote, 9, res, contador * PDU.MAX_PACOTE_SIZE, PDU.MAX_PACOTE_SIZE);
        }
        fos.write(res);

    }
    
    public static void carregaFicheiroImagem(String dest, TreeSet<byte[]> pacotes) throws IOException{
        FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/media/images/"+dest + ".jpg");
        byte res[] = new byte[pacotes.size() * PDU.MAX_PACOTE_SIZE];
        int contador = 0;
        for (byte[] pacote : pacotes) {
            System.arraycopy(pacote, 9, res, contador * PDU.MAX_PACOTE_SIZE, PDU.MAX_PACOTE_SIZE);
        }
        fos.write(res);
    }
    public static void CarregaMenus() {
        String opcoesLogin[] = {"Registar Utilizador", "Fazer Login"};
        String opcoesJogo[] = {"Criar Desafio", "Ver Classificações", "Listar Desafios","Realizar Desafio"};
        String opcoesDesafio[] = {"Modo Jogo",  "Terminar Desafio", "Eliminar Desafio"};

        Cliente.menuLogin = new Menu(opcoesLogin);
        Cliente.menuJogo = new Menu(opcoesJogo);
        Cliente.menuDesafio = new Menu(opcoesDesafio);
    }

    public static void playSong(String filename) {
        String songName = filename;
        String pathToMp3 = System.getProperty("user.dir") + "/media/music/" + songName;
        Cliente.player = new BasicPlayer();
        try {
            Cliente.player.open(new URL("file:///" + pathToMp3));
            Cliente.player.play();
        } catch (BasicPlayerException | MalformedURLException e) {
        }
    }
    public static String pathToImgFile(String imgName){
        
        return System.getProperty("user.dir") + "\\media\\images\\" + imgName;
    }
    
    public static String pathToMP3File(String songName){
        return System.getProperty("user.dir") + "/media/music/" + songName;
    }
    public static void showImage(String filename){
        String imgName = filename;
        String pathToImg = System.getProperty("user.dir") + "\\media\\images\\" + imgName;
        Cliente.imagem=new Picture(pathToImg);
        Cliente.imagem.show();
    }

    public static void stopSong()throws BasicPlayerException{
        Cliente.player.stop();
    }
    
    public static void execMenuDesafios(Utilizador ut, String d){
        do{
            Cliente.menuDesafio.executa();
            switch(Cliente.menuDesafio.getOpcao()){
                case 1:{Cliente.execMenuIngame(ut,d);break;}
                case 2:{break;}
                case 3:{break;}
            }
        }
        while(Cliente.menuDesafio.getOpcao()!=0);
    }
    
    public static void execMenuIngame(Utilizador ut, String d) {
        int currentQ;
        int opcao;
        Pergunta p;
        try {
            for (currentQ = 1; currentQ <= 10; currentQ++) {
                Cliente.comC.getPerguntaDesafioRequestPDU(d, currentQ);
                p=Cliente.comC.getPergunta();
                Cliente.comC.getMusica(d,currentQ);
                Cliente.carregaFicheiroMusica("0000"+currentQ, Cliente.comC.getPacotesMusica());
                Cliente.comC.getImagem(d,currentQ);
                Cliente.carregaFicheiroImagem("0000"+currentQ, Cliente.comC.getPacotesImagem());
                System.out.println(p.getEnunciado());
                Cliente.menuInGame=new Menu(new String[]{p.getOpcao1(),p.getOpcao2(),p.getOpcao3()});
                Cliente.playSong("0000"+currentQ+".mp3");
                Cliente.showImage("0000"+currentQ+".jpg");
                switch(Cliente.menuInGame.getOpcao()){
                    case 0:{
                        Cliente.comC.desistirDesafio(ut.getUsername(), d);
                        break;
                    }
                    default:{
                        Cliente.comC.respondePerguntaPDU(ut.getUsername(),d,currentQ,Cliente.menuInGame.getOpcao());
                        break;
                    }
                }
                Cliente.stopSong();
                File f=new File(Cliente.pathToMP3File("0000"+currentQ+".mp3"));
                File f1=new File(Cliente.pathToImgFile("0000"+currentQ+".jpg"));
                f.delete();
                f1.delete();
                if(Cliente.menuInGame.getOpcao()==0) break;
            }
        }
        catch(IOException|BasicPlayerException ioe){System.err.println(ioe.getMessage());}
    }
    
    public static void execMenuPrincipal(Utilizador ut) {
        String inputT;
        ArrayList<String> listaDesafios;
        TreeSet<ParUsernamePontos> rankings;
        try{
        do {
            Cliente.menuJogo.executa();
            switch (Cliente.menuJogo.getOpcao()) {
                case 1: {
                    System.out.println("Insira o nome do desafio: ");
                    inputT = Input.lerString();
                    Cliente.comC.createDesafio(ut.getNome(), inputT);
                    while(!Cliente.comC.getOK()){
                        System.out.println("Insira o nome do desafio: ");
                        inputT = Input.lerString();
                        Cliente.comC.createDesafio(ut.getNome(), inputT);
                    }
                    Cliente.execMenuDesafios(ut,inputT);
                    break;
                }
                case 2: {
                    Cliente.comC.listRankings();
                    rankings=Cliente.comC.getRankings();
                    for(ParUsernamePontos pup:rankings)
                        System.out.println(pup.toString());
                    break;
                }
                case 3: {
                    Cliente.comC.listChallenges();
                    listaDesafios=Cliente.comC.getChallenges();
                    for(String st:listaDesafios)
                        System.out.println(st);
                    break;
                }
                case 4: {
                    System.out.println("Insira o nome do desafio a realizar: ");
                    inputT = Input.lerString(); 
                    Cliente.comC.acceptChallenge(ut.getUsername(), inputT);
                    while(!(Cliente.comC.getOK())){
                        System.out.println("Insira o nome do desafio a realizar: ");
                        inputT = Input.lerString();
                        Cliente.comC.acceptChallenge(ut.getUsername(), inputT);
                    }
                    Cliente.execMenuDesafios(ut,inputT);
                    break;
                }

                case 0:{System.out.println("A Saír...");Cliente.comC.sendExit(ut.getUsername());Cliente.comC.getOK();break;}
            }
        } while (Cliente.menuJogo.getOpcao() != 0);
    }
        catch(IOException ioe){}
}

    public static void main(String[] args) {
        
        Utilizador ut=null;
        ParUsernamePontos pup;
        String input1, input2,inputN;
        
        try {
            Cliente.comC = new ComunicacaoCliente();
            Cliente.comC.sendHello();
            while(!Cliente.comC.getOK())
                Cliente.comC.sendHello();
            Cliente.CarregaMenus();
            Cliente.menuLogin.executa();
            switch (Cliente.menuLogin.getOpcao()) {
                case 1:
                    System.out.println("-------------REGISTO-----------");
                    System.out.println("Nome: ");
                    inputN= Input.lerString();
                    System.out.println("Username: ");
                    input1 = Input.lerString();
                    System.out.println("Password: ");
                    input2 = Input.lerString();
                    comC.registerServer(input1, inputN, input2);
                    while (!comC.getOK()) {
                        System.err.println("Username Existente");
                        System.out.println("-------------REGISTO-----------");
                        System.out.println("Nome: ");
                        inputN = Input.lerString();
                        System.out.println("Username: ");
                        input1 = Input.lerString();
                        System.out.println("Password: ");
                        input2 = Input.lerString();
                        comC.registerServer(input1, inputN, input2);
                    }
                    ut=new Utilizador(input1, inputN, input2);
                    break;
                case 2:
                    System.out.println("------------LOGIN-------------");
                    System.out.println("Username: ");
                    input1 = Input.lerString();
                    System.out.println("Password: ");
                    input2 = Input.lerString();
                    comC.logInServer(input1, input2);
                    pup = comC.getRespostaLogin();
                    while (pup == null) {
                        System.err.println("Login não validado");
                        System.out.println("------------LOGIN-------------");
                        System.out.println("Username: ");
                        input1 = Input.lerString();
                        System.out.println("Password: ");
                        input2 = Input.lerString();
                        comC.logInServer(input1, input2);
                        pup = comC.getRespostaLogin();
                    }
                    
                    ut=new Utilizador(input1, pup.getUsername(), input2, pup.getPontos());
                    System.out.println(pup.getUsername()+" "+pup.getPontos()+" pontos");
                                       
                    break;
                default:
                    break;

            }
            if (Cliente.menuLogin.getOpcao() != 0&& ut!=null) {
                Cliente.execMenuPrincipal(ut);

            }
            else System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
         System.out.println("CLIENTE ON");
         try
         {
         
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
