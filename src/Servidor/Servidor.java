/*
 * 
To change this template, choose Tools | Templates
 * and open the template in the editor.

Author = Ze
*/

package Servidor;

import Business.ComparatorPUP;
import Business.Desafio;
import Business.ExistingNameException;
import Business.InvalidLoginException;
import Business.NonexistingNameException;
import Business.NotEnoughElementsException;
import Business.PDU;
import Business.ParUsernamePontos;
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
import java.util.List;
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
    
    public static TreeSet<ParUsernamePontos> getTopScorers(){
        TreeSet<ParUsernamePontos> res=new TreeSet<>(new ComparatorPUP());
        for(Utilizador ut:Servidor.utilizadores.values())
            res.add(new ParUsernamePontos(ut.getNome(), ut.getScore()));
        return res;    
}
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
    
    public static String getPositionMusic(String nomeDes,int pos){
        return Servidor.desafios.get(nomeDes).getPergunta(pos).getMusica();
    }
    
    
    public static String getPositionImage(String nomeDes,int pos){
        return Servidor.desafios.get(nomeDes).getPergunta(pos).getImagem();
    }
    
    public static List<Desafio> listaDesafios(){
        List<Desafio> res=new ArrayList<>();
        for(Desafio d:Servidor.desafios.values())
            res.add(d);
        return res;
    }
    
    public static void registaUtilizador(String username,String nome,String auth) throws ExistingNameException{
        if(Servidor.utilizadores.containsKey(username)) {/*Envia erro de registo*/throw new ExistingNameException(username);}
        else Servidor.utilizadores.put(username, new Utilizador(username, nome,auth));
    }
    private static void finalizaDesafio(String nomeDesafio) throws NonexistingNameException {
        Integer pontos;

        Desafio d = Servidor.desafios.get(nomeDesafio);
        for (String st : d.getUtilizadores()) {
            pontos = d.getPontuacoes().get(st);
            Servidor.utilizadores.get(st).addScore(pontos);
        }
        Servidor.utilizadores.get(d.getTopPontuacao().first().getUsername()).addScore(10);
        Servidor.desafiosTerminados.put(nomeDesafio, d);
    }

    
    public static int respondePerguntaDesafio(String nomeUt,String nomeDes,int indPerg,int opcao)throws NonexistingNameException{
        if(!Servidor.desafios.containsKey(nomeDes))throw new NonexistingNameException(nomeDes);
        else if(!(Servidor.desafios.get(nomeDes).getUtilizadores().contains(nomeUt))) throw new NonexistingNameException(nomeUt);
        else{
            if(Servidor.desafios.get(nomeDes).getPergunta(indPerg).isCerta(opcao)) {Servidor.desafios.get(nomeDes).adicionaPont(nomeUt, 2);return 2;}
            else {Servidor.desafios.get(nomeDes).adicionaPont(nomeUt, -1);return -1;}
        }
    }
    
    public static void rageQuit(String nomeUt, String nomeDes)throws NonexistingNameException{
        if(!Servidor.desafios.containsKey(nomeDes))throw new NonexistingNameException(nomeDes);
        else if(!(Servidor.desafios.get(nomeDes).getUtilizadores().contains(nomeUt))) throw new NonexistingNameException(nomeUt);
        else{
            Servidor.desafios.get(nomeDes).eliminaUtilizador(nomeUt);
        }
    }
    
    public static void terminaDesafio(String nomeUt, String nomeDes) throws NonexistingNameException{
        if(!(Servidor.desafios.containsKey(nomeDes))) throw new  NonexistingNameException(nomeDes);
        else if(!(Servidor.desafios.get(nomeDes).getCriador().equals(nomeUt))) throw new NonexistingNameException(nomeUt);
        else{
            Servidor.finalizaDesafio(nomeDes);
        }
    }
    
    public static void adicionaUtilizadorDesafio(String nomeUt, String nomeDes) throws NonexistingNameException{
        if(!(Servidor.desafios.containsKey(nomeDes))) throw new NonexistingNameException(nomeDes);
        else if(!(Servidor.utilizadores.containsKey(nomeUt))) throw new NonexistingNameException(nomeUt);
        else{
            Servidor.desafios.get(nomeDes).adicionaUtilizador(nomeUt);
        }
    }
    
    public static void eliminaDesafio(String nomeDes) throws NonexistingNameException{
        if(!(Servidor.desafiosTerminados.containsKey(nomeDes))) throw new NonexistingNameException(nomeDes);
        else{
            Servidor.desafiosTerminados.remove(nomeDes);
        }
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
    
    public static Pergunta getPerguntaInd(String nomeDes,int index){
        return Servidor.desafios.get(nomeDes).getPergunta(index);
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
                
                switch(cabecalho[4])
                {
                    case 1: {
                        okPDU(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 2:
                        registerPDU(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    case 3:
                        loginPDU(cabecalho, receivePacket.getData(), receivePacket, serverSocket, dadosServidor);
                        break;
                    case 4: {
                        logoutPDU(cabecalho, receivePacket.getData(), receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 5:{
                        quitPDU(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor); 
                        break;
                    }
                    case 6:{
                        endPDU(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 7:{
                        listChallenges(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor); 
                        break;
                    }
                    case 8:{
                        makeChallenge(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 9:{
                        acceptChallenge(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 10:{
                        deleteChallenge(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 11:{
                        answer(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 13:{
                        listaRankings(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }    
                    case 15:{
                        getPerguntaDesafio(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 16:{
                        sendMusica(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                    case 17:{
                        sendImagem(cabecalho, receiveData, receivePacket, serverSocket, dadosServidor);
                        break;
                    }
                }
            }
        } catch (IOException ex)
        { Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
       
    private static void getPerguntaDesafio(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder desafio = new StringBuilder();
        int indPerg;
     
        byte[] sendData = {};
        int i=8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            desafio.append((char) pdu[i]);
        }
        i++;
        indPerg=(int)pdu[pdu.length-1];
  
            Pergunta p=Servidor.getPerguntaInd(desafio.toString(), indPerg);
            sendData = PDU.respostaRqPerguntaPDU(p, cabecalho);
          try {
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } catch (IOException ioe) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
            }
        }

    private static void listChallenges(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        byte[] sendData = PDU.ReplistChallPDU(Servidor.listaDesafios());
        try {
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } catch (IOException ioe) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
            }
        
    }
    
    private static void deleteChallenge(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder desafio = new StringBuilder();
        
        
        byte[] sendData = {};
        int i = 8;
        
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            desafio.append((char) pdu[i]);
        }
        i++;
        
        try {
            Servidor.eliminaDesafio(desafio.toString());
            sendData = PDU.respostaByte((byte) 0, cabecalho[3], (byte) 0);
        } catch (NonexistingNameException exc) {
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
    
    public static void listaRankings(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        byte[] sendData;
        sendData = PDU.respostaRanking(Servidor.getTopScorers());
        try {
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } catch (IOException ioe) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
            }
    }
    
    private static void answer(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder desafio = new StringBuilder();
        StringBuilder nomeut = new StringBuilder();
        int pontosAm;
        int indPerg;
        int opc;
        byte[] sendData = {};
        int i = 8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            nomeut.append((char) pdu[i]);
        }
        i++;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            desafio.append((char) pdu[i]);
        }
        i++;
        indPerg=(int)pdu[pdu.length-1];
        opc=(int) pdu[pdu.length-2];
        try {
            pontosAm=Servidor.respondePerguntaDesafio(nomeut.toString(), desafio.toString(),indPerg,opc);
            sendData = PDU.respostaByte((byte) pontosAm, cabecalho[3], (byte) 0);
        } catch (NonexistingNameException exc) {
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
            
    
    private static void acceptChallenge(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder desafio = new StringBuilder();
        StringBuilder nomeut = new StringBuilder();

        byte[] sendData = {};
        int i = 8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            nomeut.append((char) pdu[i]);
        }
        i++;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            desafio.append((char) pdu[i]);
        }
        i++;

        try {
            Servidor.adicionaUtilizadorDesafio(nomeut.toString(), desafio.toString());
            sendData = PDU.respostaByte((byte) 0, cabecalho[3], (byte) 0);
        } catch (NonexistingNameException exc) {
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
    
    private static void makeChallenge(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder desafio = new StringBuilder();
        StringBuilder nomeut = new StringBuilder();

        byte[] sendData = {};
        int i = 8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            nomeut.append((char) pdu[i]);
        }
        i++;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            desafio.append((char) pdu[i]);
        }
        i++;

        try {
            Servidor.generateDesafio(desafio.toString(), nomeut.toString());
            sendData = PDU.respostaByte((byte) 0, cabecalho[3], (byte) 0);
        } catch (ExistingNameException | NotEnoughElementsException exc) {
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
    
    private static void quitPDU(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder desafio = new StringBuilder();
        StringBuilder nomeut = new StringBuilder();

        byte[] sendData = {};
        int i = 8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            nomeut.append((char) pdu[i]);
        }
        i++;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            desafio.append((char) pdu[i]);
        }
        i++;

        try {
            Servidor.rageQuit(nomeut.toString(), desafio.toString());
            sendData = PDU.respostaByte((byte) 0, cabecalho[3], (byte) 0);
        } catch (NonexistingNameException exc) {
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
    
    private static void endPDU(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder desafio = new StringBuilder();
        StringBuilder nomeut = new StringBuilder();

        byte[] sendData = {};
        int i = 8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            nomeut.append((char) pdu[i]);
        }
        i++;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            desafio.append((char) pdu[i]);
        }
        i++;

        try {
            Servidor.terminaDesafio(nomeut.toString(), desafio.toString());
            sendData = PDU.respostaByte((byte) 0, cabecalho[3], (byte) 0);
        } catch (NonexistingNameException exc) {
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
    
    private static void logoutPDU(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor){
        StringBuilder utilizador = new StringBuilder();

        byte[] sendData = {};
        int i = 8;

        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            utilizador.append((char) pdu[i]);
        }
        i++;
        Servidor.logOut(utilizador.toString());
        sendData= PDU.respostaByte((byte)0, cabecalho[3], (byte) 0);
        try {
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } catch (IOException ioe) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }
    
    private static void okPDU(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor) {
        byte[] sendData = {};
        sendData = PDU.respostaByte((byte) 0, cabecalho[3], (byte) 0);
        try {
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } catch (IOException ioe) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
        }
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
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
    
     private static void registerPDU(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor)
    {
        StringBuilder utilizador = new StringBuilder();
        StringBuilder nome=new StringBuilder();
        String password;
        Utilizador ut;
        byte[] sendData = {};
        int i = 8;
        for(;i<cabecalho[5]+8&&((char)pdu[i]!='\0');i++){
            nome.append((char) pdu[i]);
        }
        i++;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            utilizador.append((char) pdu[i]);
        }
        i++;
        byte[] pass = new byte[cabecalho[5] - (i - 8)];
        System.arraycopy(pdu, i, pass, 0, cabecalho[5] - (i - 8));
        password = new String(pass);
        try {
            Servidor.registaUtilizador(utilizador.toString(), nome.toString(), password);
            sendData = PDU.respostaByte((byte)0, cabecalho[3],(byte) 0);
        } catch (ExistingNameException exc) {
            sendData = PDU.respostaString(exc.getMessage(), cabecalho[3], (byte) 255);
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
     
     private static void sendMusica(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor) {

        StringBuilder nome = new StringBuilder();

        byte[] sendData = {};
        byte[][] auxil = {};
        int i = 8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            nome.append((char) pdu[i]);
        }
        i++;
        int pos = (int) pdu[pdu.length - 1];

        String nomeMusica = Servidor.getPositionMusic(nome.toString(), pos);

        try {
            auxil = PDU.FileToPacoteArray(nomeMusica);
            for (byte[] pacote : auxil) {
                sendData = pacote;
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            //para nao perderes pacotes talvez aqui se meta um sleep;
            }
            sendData = PDU.respostaByte((byte) 0, (byte) 0);

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
            
        } catch (IOException ioe) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
        }

    }
     
    private static void sendImagem(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor) {

        StringBuilder nome = new StringBuilder();

        byte[] sendData = {};
        byte[][] auxil = {};
        int i = 8;
        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) {
            nome.append((char) pdu[i]);
        }
        i++;
        int pos = (int) pdu[pdu.length - 1];

        String nomeMusica = Servidor.getPositionImage(nome.toString(), pos);

        try {
            auxil = PDU.FileToPacoteArray(nomeMusica);
            for (byte[] pacote : auxil) {
                sendData = pacote;
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
                //Para nao Perderes pacotes talvez aqui metas um sleep;
            }
            sendData = PDU.respostaByte((byte) 0, (byte) 0);

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } catch (IOException ioe) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
        }

    }
   
        /*public static void makeChallPDU(short[] cabecalho, byte[] pdu, DatagramPacket receivePacket,
            DatagramSocket serverSocket, ComunicacaoServidor dadosServidor) {
        StringBuilder utilizador = new StringBuilder();
        String password;
        Desafio d;
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
            d = Servidor.generateDesafio(utilizador.toString(), password);
            
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
        }*/
    }

