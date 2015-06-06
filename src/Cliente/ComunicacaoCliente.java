/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Business.PDU;
import Business.ParUsernamePontos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
        
        return pdu[pdu.length-1]==0;
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
}
