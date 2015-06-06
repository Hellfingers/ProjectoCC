/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

/**
 *
 * @author Pedro Cunha
 */
public class ParUsernamePontos {
    private String username;
    private int pontos;

    public ParUsernamePontos() {
    }

    public ParUsernamePontos(String username, int pontos) {
        this.username = username;
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }

    public String getUsername() {
        return username;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public boolean equals(Object o){
        if(this==o) return true;
        else if((!(this.getClass().getSimpleName().equals(o.getClass().getSimpleName())))||o==null) return false;
        else{
            ParUsernamePontos pup=(ParUsernamePontos)o;
            return this.pontos==pup.getPontos()&&this.username.equals(pup.getUsername());
        }
    }
    public String toString(){
        return this.username+" "+this.pontos+" pontos";
    }
}
