/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class PDU {

    private byte version;
    private byte security;
    private short label;
    //private  tipo;
    private int nCampos;
    private short size;
    private ArrayList<Campo> campos;

    short getValue(byte[] data) {
        short value = data[1];
        value = (short) ((value << 8) | data[0]);

        return value;
    }

    public byte[] formataPDU(){
        return new byte[255];
    }
    

}

class Campo {

    private int numero;
    private byte size;
    private String valor;

}
