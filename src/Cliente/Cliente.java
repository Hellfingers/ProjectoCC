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
public class Cliente {

    public static void main(String[] args) 
    {        
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
       
        try {
            
            
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
            clientSocket.close();
        } catch (IOException ex) 
        { Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex); }
    }
}
