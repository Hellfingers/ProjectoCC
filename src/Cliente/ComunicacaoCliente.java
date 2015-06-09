/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Business.ComparatorPUP;
import Business.PDU;
import Business.PDUComparator;
import Business.ParUsernamePontos;
import Business.Pergunta;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author core
 */
public class ComunicacaoCliente implements Comunicacao.ComunicacaoUDP 
{
    private InetAddress IPAddress;
    DatagramSocket clientSocket;

    public ComunicacaoCliente() throws UnknownHostException, SocketException
    {
        this.IPAddress = InetAddress.getByName("localhost");
        this.clientSocket = new DatagramSocket();        
    }
    
    @Override
    public Boolean logInServer(String user, String password) throws IOException
    {
        byte[] SecInfo = password.getBytes();
        byte[] sendData = PDU.loginPDU(user, SecInfo);
            
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }

    public boolean createDesafio(String username,String nomeDes) throws IOException{
        byte[] sendData=PDU.makeChallPDU(username, nomeDes);
        DatagramPacket sendPacket=new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public boolean listChallenges()throws IOException{
        byte[] sendData=PDU.listChallengesPDU();
        DatagramPacket sendPacket=new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public boolean acceptChallenge(String user, String nomeDes)throws IOException{
        byte[] sendData=PDU.acceptChallengePDU(user, nomeDes);
        DatagramPacket sendPacket=new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public boolean listRankings() throws IOException{
        byte[] sendData=PDU.listRankingPDU();
        DatagramPacket sendPacket=new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
        
    
    public Boolean registerServer(String user,String nome,String pass)throws IOException{
        byte[] SecInfo = pass.getBytes();
        byte[] sendData = PDU.registerPDU(nome, user, SecInfo);
            
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public ParUsernamePontos getRespostaLogin() throws IOException
    {
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        short lenghtCampos = 0;
        int i;
        StringBuilder str = new StringBuilder();

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);        
        
        
        for (i = 8; i < lenghtCampos + 8 && ((char) pdu[i] != '\0'); i++) 
        {
            str.append((char) pdu[i]);
        }
        if((char)pdu[pdu.length-1]=='\0') return null;
        else return new ParUsernamePontos(str.toString(), pdu[pdu.length-1]);
    }
    
    public Boolean getPerguntaDesafioRequestPDU(String nomeDes,int nQuestao) throws IOException{
        byte[] sendData = PDU.requestPergunta(nomeDes, nQuestao);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }

    public Boolean respondePerguntaPDU(String userName,String nomeDes, int nQuestao, int opcao) throws IOException{
        byte[] sendData = PDU.answerPDU(nomeDes, (byte)opcao, userName, (byte)nQuestao);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public boolean sendExit(String username)throws IOException{
        byte[] sendData = PDU.logoutPDU(username);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public ArrayList<String> getChallenges()throws IOException{
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        short lenghtCampos = 0;
        int i=8;
        StringBuilder str = new StringBuilder();
        ArrayList<String> res=new ArrayList<>();

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);        
        
        for(;i<lenghtCampos+8;i++){
        str=new StringBuilder();
        while((char) pdu[i] != '\0') 
        {
            str.append((char) pdu[i]);
            i++;
        }
        res.add(str.toString());
        }
        
        return res;
    }
    
    public boolean getMusica(String d, int index)throws IOException{
        byte[] sendData = PDU.getMusicaPDU(d,(byte)index);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public boolean getImagem(String d,int index)throws IOException{
        byte[] sendData = PDU.getImagemPDU(d,(byte)index);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public boolean desistirDesafio(String username, String desafio) throws IOException{
        byte[] sendData = PDU.quitChallPDU(username, desafio);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, 9876);
        this.clientSocket.send(sendPacket);
        
        return true;
    }
    
    public String getRespostaString() throws IOException{
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        short lenghtCampos = 0;
        StringBuilder str = new StringBuilder();

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);        
        
        
        for (int i = 8; i < lenghtCampos + 8 && ((char) pdu[i] != '\0'); i++) 
        {
            str.append((char) pdu[i]);
        }
        
        return str.toString();
    }
    
    public Boolean getOK() throws IOException{
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        short lenghtCampos = 0;
        StringBuilder str = new StringBuilder();

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);        
        
        return pdu[pdu.length-1]==0&&pdu[5]==0;
    }
    
    public boolean sendHello()throws IOException{
        byte[] sendData;
        sendData=PDU.helloPDU();
        DatagramPacket sendPacket=new DatagramPacket(sendData, sendData.length);
        this.clientSocket.send(sendPacket);
        return true;
    }
    
    public TreeSet<ParUsernamePontos> getRankings()throws IOException{
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        short lenghtCampos = 0;
        StringBuilder pontos=new StringBuilder();
        int i=8;
        StringBuilder str = new StringBuilder();
        TreeSet<ParUsernamePontos> res=new TreeSet<>(new ComparatorPUP());

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);        
        
        for(;i<lenghtCampos+8;i++){
        str=new StringBuilder();
        while((char) pdu[i] != '\0') 
        {
            str.append((char) pdu[i]);
            i++;
        }
        while((char) pdu[i]!='\0'){
            pontos.append((char)pdu[i]);
            i++;
        }
            
        res.add(new ParUsernamePontos(str.toString(), Integer.parseInt(pontos.toString())));
        }
        
        return res;
    }
    
    
    public TreeSet<byte[]> getPacotesMusica()throws IOException{
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        short lenghtCampos = 0;
        TreeSet<byte[]> res= new TreeSet<>(new PDUComparator());
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);      
        if(!(Arrays.equals(pdu, PDU.respostaByte((byte)0, (byte)0))))
            res.add(pdu);
        while(!(Arrays.equals(pdu, PDU.respostaByte((byte)0,(byte) 0)))){
            this.clientSocket.receive(receivePacket);
            pdu = receivePacket.getData();
            tamanhoCampos = new byte[2];
            tamanhoCampos[0] = pdu[6];
            tamanhoCampos[1] = pdu[7];
            lenghtCampos = PDU.getShortValue(tamanhoCampos);
            if(!(Arrays.equals(pdu, PDU.respostaByte((byte)0, (byte)0))))
                res.add(pdu);
        }
        
        return res;
    }
    
    public TreeSet<byte[]> getPacotesImagem()throws IOException{
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        short lenghtCampos = 0;
        TreeSet<byte[]> res= new TreeSet<>(new PDUComparator());
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);      
        if(!(Arrays.equals(pdu, PDU.respostaByte((byte)0, (byte)0))))
            res.add(pdu);
        while(!(Arrays.equals(pdu, PDU.respostaByte((byte)0,(byte) 0)))){
            this.clientSocket.receive(receivePacket);
            pdu = receivePacket.getData();
            tamanhoCampos = new byte[2];
            tamanhoCampos[0] = pdu[6];
            tamanhoCampos[1] = pdu[7];
            lenghtCampos = PDU.getShortValue(tamanhoCampos);
            if(!(Arrays.equals(pdu, PDU.respostaByte((byte)0, (byte)0))))
                res.add(pdu);
        }
        
        return res;
    }
    
    @Override
    public String criarDesafio(String desaf) throws IOException
    {
        byte[] sendData = new byte[1024];
        String loginInfo = "MAKE_CHALLENGE+" + desaf;
            
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        sendData = loginInfo.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        
        return "";        
    }

    @Override
    public void aceitarDesafio() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void jogarDesafio() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int obterPontuacao(String desaf) throws IOException 
    {
        int pontuacao = 0;
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
            
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        sendData = desaf.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String score = new String(receivePacket.getData());
        clientSocket.close();
        
        return pontuacao;
    }
    
    
    public Pergunta getPergunta()throws IOException{
        byte[] receiveData = new byte[1024];
        byte[] pdu;
        int i = 8;
        short lenghtCampos = 0;
        StringBuilder str = new StringBuilder();
        StringBuilder op1= new StringBuilder();
        StringBuilder op2=new StringBuilder();
        StringBuilder op3=new StringBuilder();
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                
        this.clientSocket.receive(receivePacket);
        pdu = receivePacket.getData();
        byte[] tamanhoCampos = new byte[2];
        tamanhoCampos[0] = pdu[6]; tamanhoCampos[1] = pdu[7];
        lenghtCampos = PDU.getShortValue(tamanhoCampos);        
        
        
        for (; i < lenghtCampos + 8 && ((char) pdu[i] != '\0'); i++) 
        {
            str.append((char) pdu[i]);
        }
        i++;
        for (; i < lenghtCampos + 8 && ((char) pdu[i] != '\0'); i++) {
            op1.append((char) pdu[i]);
        }
        i++;
        for (; i < lenghtCampos + 8 && ((char) pdu[i] != '\0'); i++) {
            op2.append((char) pdu[i]);
        }
        i++;
        for (; i < lenghtCampos + 8 && ((char) pdu[i] != '\0'); i++) {
            op3.append((char) pdu[i]);
        }
        i++;
    
        return new Pergunta(str.toString(), op1.toString(), op2.toString(), op3.toString());
    }
    
   
}
