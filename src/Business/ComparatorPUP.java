/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Pedro Cunha
 */
public class ComparatorPUP implements Comparator<ParUsernamePontos>,Serializable{
    public int compare(ParUsernamePontos p1,ParUsernamePontos p2){
        if(p1.getPontos()!=p2.getPontos()) return p2.getPontos()-p1.getPontos();
        else return p1.getUsername().compareTo(p2.getUsername());
    }
}
