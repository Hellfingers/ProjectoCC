/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Business.Desafio;
import Business.ExistingNameException;
import Business.InvalidLoginException;
import Business.NonexistingNameException;
import Business.NotEnoughElementsException;
import Business.PDU;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.TreeSet;
import java.util.Random;
import java.util.HashSet;
import java.util.logging.Level;
import Business.Pergunta;
import Business.Utilizador;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * 
 * @author core
 */
public class Servidor {
    private static TreeSet<String> sourceImg;
    private static TreeSet<String> sourceMp3;
    private static HashSet<Pergunta> perguntas;
    private static HashMap<String,Desafio> desafios=new HashMap<>();
    private static HashMap<String,Utilizador> utilizadores=new HashMap<>();
    private static HashMap<String,Desafio> desafiosTerminados=new HashMap<>();
    
    public static void carregaDB() throws FileNotFoundException{
        //Leitura do ficheiro de Imagens
        Pergunta p;
        Servidor.perguntas=new HashSet<>();
        Servidor.sourceImg=new TreeSet<>();
        Servidor.sourceMp3= new TreeSet<>();
        Scanner sc=new Scanner(new File(Servidor.createPathToFile("Imagens.txt")));
        sc.useDelimiter(System.getProperty("line.separator"));
        while(sc.hasNext())
            Servidor.sourceImg.add(sc.next());
        sc.close();
        //Leitura do ficheiro de Músicas
        sc=new Scanner(new File(Servidor.createPathToFile("Musicas.txt")));
        sc.useDelimiter(System.getProperty("line.separator"));
        while(sc.hasNext())
            Servidor.sourceMp3.add(sc.next());
        sc.close();
        //Leitura do ficheiro de Perguntas
        sc=new Scanner(new File(Servidor.createPathToFile("Questoes.txt")));
        sc.useDelimiter("%%--");
        while(sc.hasNext()){
            p=Servidor.parseBlock(sc.next());
            if(Servidor.sourceImg.contains(p.getImagem())&&Servidor.sourceMp3.contains(p.getMusica()));
            {
                Servidor.perguntas.add(p);
            }
            
        }
        sc.close();
    }
    public static String createPathToFile(String filename){
        return System.getProperty("user.dir")+"\\srv\\"+filename;
    }
    
    public static void registaUtilizador(String username,String nome,String auth) throws ExistingNameException{
        if(Servidor.utilizadores.containsKey(username)) {/*Envia erro de registo*/throw new ExistingNameException(username);}
        else Servidor.utilizadores.put(username, new Utilizador(username, nome,auth));
    }
    
    public static Utilizador logIn(String username, String auth)throws NonexistingNameException,InvalidLoginException{
        if(!(Servidor.utilizadores.containsKey(username))) {/*Envia erro de login*/throw new NonexistingNameException(username);}
        else if(!(Servidor.utilizadores.get(username).getPassword().equals(auth))) {/*Envia erro de login*/throw new InvalidLoginException(username);}
        else{
            Utilizador ut=Servidor.utilizadores.get(username);
            ut.setSessao(true);
            return ut;
        }
    }
    
    public static void logOut(String username){
        Servidor.utilizadores.get(username).setSessao(false);
    }
    public static Desafio getDesafio(String nome) throws NonexistingNameException {
        if (!(Servidor.desafios.containsKey(nome))) {
            /*Envia erro de desafio*/
            throw new NonexistingNameException(nome);
        }
        else return Servidor.desafios.get(nome);
    }
    public static Desafio generateDesafio(String nome,String username)throws ExistingNameException,NotEnoughElementsException{
        if(Servidor.desafios.containsKey(nome)) {/*Envia erro de desafio*/throw new ExistingNameException(nome);}
        else if(Servidor.desafios.keySet().size()<10) {/*Envia erro de desafio*/throw new NotEnoughElementsException(Servidor.desafios.keySet().size());}
        else{
        Random rng=new Random();
        Pergunta p;
        int pos;
        Desafio des=new Desafio(nome, username);
        ArrayList<Pergunta> aux=new ArrayList<>();
        ArrayList<Pergunta> lista=new ArrayList<>(Servidor.perguntas);
        for(int i=0;i<10;i++){
            pos=rng.nextInt(lista.size());
            p=lista.get(pos);
            while(aux.contains(p)){
                pos=rng.nextInt(lista.size());
                p=lista.get(pos);
            }
            aux.add(p);
        }
        for(Pergunta perg:aux)
            des.insertPergunta(perg);
        Servidor.desafios.put(nome, des);
        return des;
    }
}
    
    public static Pergunta parseBlock(String line){
        StringTokenizer strtok=new StringTokenizer(line, "\n");
        Pergunta p=new Pergunta();
        p.setMusica(strtok.nextToken());
        p.setImagem(strtok.nextToken());
        p.setEnunciado(strtok.nextToken());
        p.setOpcao1(strtok.nextToken());
        p.setOpcao2(strtok.nextToken());
        p.setOpcao3(strtok.nextToken());
        p.setCorrecta(Integer.parseInt(strtok.nextToken()));
        return p;
        
    }
    public static void main(String[] args) 
    {        
        try{
        Servidor.carregaDB();
            System.out.println("Ficheiros Carregados");}
        catch(FileNotFoundException fnf){
            System.err.println("Ficheiro não encontrado "+fnf.getMessage());
        }
        byte[] receiveData = new byte[1024];
        short[] cabecalho;
        ComunicacaoServidor dadosServidor = new ComunicacaoServidor();
        
        System.out.println("SERVIDOR ON");
        
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876);
            
            while (true) 
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
                serverSocket.receive(receivePacket);
                
                cabecalho = PDU.UndoPDU(receivePacket.getData());
                System.out.println("RECEIVED: " + cabecalho[4]);
                
                switch(cabecalho[3])
                {
                    case 3: loginPDU(cabecalho, receivePacket.getData(), receivePacket, serverSocket, dadosServidor); break;
                }
            }
        } catch (IOException ex)
        { Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    private static void loginPDU(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor)
    {
        StringBuilder utilizador = new StringBuilder();
        String password;
        Utilizador ut;
        byte[] sendData = {};
        int i = 8;

        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            utilizador.append((char) pdu[i]);
        }
        i++;
        byte[] pass = new byte[cabecalho[5] - (i - 8)];
        System.arraycopy(pdu, i, pass, 0, cabecalho[5] - (i - 8));
        password = new String(pass);
        try {
            ut = Servidor.logIn(utilizador.toString(), password);
            sendData = PDU.respostaLogin(ut.getNome(),ut.getScore());
        } catch (InvalidLoginException | NonexistingNameException exc) {
            sendData = PDU.respostaString("Username ou password erradas", cabecalho[3], (byte) 255);
        } finally {
            try {
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } catch (IOException ioe) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
            }
        }

    }
    
    }

