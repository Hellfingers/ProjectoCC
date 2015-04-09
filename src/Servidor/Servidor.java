/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

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
        byte[] sendData = new byte[1024];
     
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876);
            
            while (true) 
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
        } catch (IOException ex)
        { Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);   }
    }
}
