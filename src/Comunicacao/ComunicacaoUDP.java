/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Comunicacao;

import java.io.IOException;

/**
 *
 * @author core
 */
public interface ComunicacaoUDP 
{
    public Boolean logInServer(String user, String password) throws IOException;
    
    public String criarDesafio(String desafio) throws IOException;
    
    public void aceitarDesafio() throws IOException;
    
    public void jogarDesafio() throws IOException;
    
    public int obterPontuacao(String desaf) throws IOException;
}
