/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.Comparator;

/**
 *
 * @author Pedro Cunha
 */
public class PDUComparator implements Comparator<byte[]>{
    public int compare(byte[] pacoteFicheiro1,byte[] pacoteFicheiro2){
        return (int)pacoteFicheiro1[8]-pacoteFicheiro2[8];
    }
}
