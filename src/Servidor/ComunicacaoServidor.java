/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Business.Desafio;
import Business.Utilizador;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author Jose
 */
public class ComunicacaoServidor implements Comunicacao.ComunicacaoUDP 
{
    HashMap<String, Utilizador> users ;
    HashMap<String, Desafio> desafios; //<nome do desafio,DESAFIOS>
    HashMap<String, Desafio> desafiosDecorrer; //<Nome que o jogador lhe deu,DESAFIOS>
    
    
    public ComunicacaoServidor()
    {
        users = new HashMap<String, Utilizador>();
        desafios = new HashMap<String, Desafio>();
        desafiosDecorrer = new HashMap<String, Desafio>();
    }

    public HashMap<String, Desafio> getDesafios() {
        return this.desafios;
    }
    public HashMap<String,Desafio> getDesafiosDecorrer(){
        return this.desafiosDecorrer;
    }
    public HashMap<String,Utilizador> getUsers(){
        return this.users;
    }
    
    
    @Override
    public Boolean logInServer(String user, String password) throws IOException 
    {
        if(this.users.containsKey(user) && this.users.get(user).getPassword().equals(password))
        {
            this.users.get(user).setSessao(true);
            return true;
        }
        else return false;
    }

    @Override
    public String criarDesafio(String desaf)
    {         
        //Datas...
        DateFormat formatoData = new SimpleDateFormat("yyMMdd");
        DateFormat formatoHora = new SimpleDateFormat("HHmmss");
        Calendar dataAtual = Calendar.getInstance();
        
        String resposta = "Reply+" + formatoData.format(dataAtual.getTime()) 
                + "+" + formatoHora.format(dataAtual.getTime());
        
        desafiosDecorrer.put(desaf, desafios.get(desaf));
        
        return resposta;
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
    public int obterPontuacao(String desaf)
    {
        int pontuacao = 0;
        //desafiosDecorrer.get(desaf).
        
        return pontuacao;
    }
}
