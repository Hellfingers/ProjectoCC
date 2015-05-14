/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Business.PDU;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author core
 */
public class Servidor {

    public static void main(String[] args) 
    {        
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
        int i = 8;

        for (; i < cabecalho[5] + 8 && ((char) pdu[i] != '\0'); i++) 
        {
            utilizador.append((char) pdu[i]);
        }

        i++;
        byte[] pass = new byte[cabecalho[5] - (i - 8)];
        System.arraycopy(pdu, i, pass, 0, cabecalho[5] - (i - 8));
        password = new String(pass);

        System.out.println("UTILI: " + utilizador.toString() + " PASSWORD: " + password);
     
        try 
        { 
            byte[] sendData;
            if(dadosServidor.logInServer(utilizador.toString(), password))
            {
                sendData = PDU.respostaNome(utilizador.toString(), cabecalho[3]);
            }
            else sendData = PDU.respostaErro("Username ou password erradas.", cabecalho[3]);
            
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } 
        catch (IOException ex){ Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex); }
        
    }
}
