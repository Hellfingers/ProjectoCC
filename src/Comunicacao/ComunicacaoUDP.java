/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Comunicacao;

/**
 *
 * @author core
 */
public interface ComunicacaoUDP 
{
    public int logInServer(String user, String password);
    
    public void criarDesafio();
    
    public void aceitarDesafio();
    
    public void jogarDesafio();
    
    public void obterPontuacao();
}
