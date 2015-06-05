/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

/**
 *
 * @author User
 */
public class Pergunta {

    private String enunciado;
    private String opcao1, opcao2, opcao3;
    private int correcta;
    private String imagem;
    private String musica;

    public Pergunta() {
        this.enunciado = new String();
        this.opcao1 = new String();
        this.opcao2 = new String();
        this.opcao3 = new String();
        this.correcta = 0;
        this.imagem = new String();
        this.musica = new String();
    }

    public Pergunta(String enunciado, String opcao1, String opcao2, String opcao3, int correcta, String pathImagem, String pathMusica) {
        this.enunciado = enunciado;
        this.opcao1 = opcao1;
        this.opcao2 = opcao2;
        this.opcao3 = opcao3;
        this.correcta = correcta;
        this.imagem = pathImagem;
        this.musica = pathMusica;
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

    public String getImagem() {
        return this.imagem;
    }

    public String getMusica() {
        return this.musica;
    }

    public boolean isCerta(int opcao){
        return opcao==this.correcta;
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
        this.imagem = path;
    }

    public void setMusica(String path) {
        this.musica = path;
    }

    public Pergunta clone() {
        return new Pergunta(this.getEnunciado(), this.getOpcao1(), this.getOpcao2(), this.getOpcao3(), this.getCorrecta(), this.getImagem(), this.getMusica());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(this.getClass().getSimpleName().equals(o.getClass().getSimpleName())) || o == null) {
            return false;
        } else {
            Pergunta p = (Pergunta) o;
            return ((this.correcta == p.getCorrecta()) && (this.enunciado.equals(p.getEnunciado())) && (this.opcao1.equals(p.getOpcao1())) && (this.opcao2.equals(p.getOpcao2())) && (this.opcao3.equals(p.getOpcao3())) && (this.musica.equals(p.getMusica())) && (this.imagem.equals(p.getImagem())));

        }
    }
    
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append(this.musica+"\n");
        sb.append(this.imagem+"\n");
        sb.append(this.enunciado+"\n");
        sb.append(this.opcao1+"\n");
        sb.append(this.opcao2+"\n");
        sb.append(this.opcao3+"\n");
        return sb.toString();
    }
}
