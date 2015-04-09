/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.io.File;

/**
 *
 * @author User
 */
public class Pergunta {

    private String enunciado;
    private String opcao1, opcao2, opcao3;
    private int correcta;
    private File imagem;
    private File musica;

    public Pergunta() {
        this.enunciado = new String();
        this.opcao1 = new String();
        this.opcao2 = new String();
        this.opcao3 = new String();
        this.correcta = 0;
        this.imagem = new File("");
        this.musica = new File("");
    }

    public Pergunta(String enunciado, String opcao1, String opcao2, String opcao3, int correcta, String pathImagem, String pathMusica) {
        this.enunciado = enunciado;
        this.opcao1 = opcao1;
        this.opcao2 = opcao2;
        this.opcao3 = opcao3;
        this.correcta = correcta;
        this.imagem = new File(pathImagem);
        this.musica = new File(pathMusica);
    }

    public String getEnunciado() {
        return this.enunciado;
    }

    public String getOpcao1() {
        return this.opcao1;
    }

    public String getOpcao2() {
        return this.opcao2;
    }

    public String getOpcao3() {
        return this.opcao3;
    }

    public int getCorrecta() {
        return this.correcta;
    }

    public File getImagem() {
        return this.imagem;
    }

    public File getMusica() {
        return this.musica;
    }

    public void setCorrecta(int correcta) {
        this.correcta = correcta;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public void setOpcao1(String opcao1) {
        this.opcao1 = opcao1;
    }

    public void setOpcao2(String opcao2) {
        this.opcao2 = opcao2;
    }

    public void setOpcao3(String opcao3) {
        this.opcao3 = opcao3;
    }

    public void setImagem(String path) {
        this.imagem = new File(path);
    }

    public void setMusica(String path) {
        this.musica = new File(path);
    }

    public Pergunta clone(){
        return new Pergunta(this.getEnunciado(), this.getOpcao1(), this.getOpcao2(), this.getOpcao3(), this.getCorrecta(), this.getImagem().getName(), this.getMusica().getName());
    }
    
}
