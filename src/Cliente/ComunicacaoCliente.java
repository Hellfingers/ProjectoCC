/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author core
 */
public class ComunicacaoCliente implements Comunicacao.ComunicacaoUDP {

    @Override
    public int logInServer(String user, String password) 
    {
        byte[] sendData = new byte[1024];
        String loginInfo = "Login+" + user + "+" + password;
        
        try {            
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            
            sendData = loginInfo.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);            
        } 
        catch (IOException ex) 
        { 
            Logger.getLogger(ComunicacaoCliente.class.getName()).log(Level.SEVERE, null, ex); 
            return 0; 
        }
        
        return 1;
    }

    @Override
    public void criarDesafio() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void obterPontuacao() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
