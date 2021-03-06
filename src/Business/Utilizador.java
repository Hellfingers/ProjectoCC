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
public class Utilizador {

    private String username;
    private String nome;
    private String password;
    private boolean sessao;
    private int score;

    public Utilizador(String username,String nome ,String password) {
        this.username = username;
        this.nome=nome;
        this.password = password;
        this.sessao = false;
        this.score = 0;
    }

    public Utilizador() {
        this.username = new String();
        this.password = new String();
        this.nome=new String();
        this.sessao = false;
        this.score = 0;
    }

    public Utilizador(String username,String nome,String password, int score) {
        this.username = username;
        this.nome=nome;
        this.password = password;
        this.sessao = false;
        this.score = score;
    }

    public Utilizador(Utilizador ut) {
        this.username = ut.getUsername();
        this.nome=ut.getNome();
        this.password = ut.getPassword();
        this.sessao = ut.getSessao();
        this.score = ut.getScore();

    }

    @Override
    protected Utilizador clone() {
        return new Utilizador(this);
    }

    public String getUsername() {
        return this.username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNome() {
        return nome;
    }
    
    public void addScore(int score){
        this.score+=score;
    }

    public boolean isSessao() {
        return sessao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public boolean getSessao() {
        return this.sessao;
    }

    public void setSessao(boolean sessao) {
        this.sessao = sessao;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof Utilizador)) {
            return false;
        }
        Utilizador ut = (Utilizador) obj;
        return (this.username.equals(ut.getUsername())&&this.nome.equals(ut.getNome()) && this.password.equals(ut.getPassword()) && this.score == ut.getScore());
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
