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
    private String password;
    private int score;

    public Utilizador(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
    }

    public Utilizador() {
        this.username = new String();
        this.password = new String();
        this.score = 0;
    }

    public Utilizador(String username, String password, int score) {
        this.username = username;
        this.password = password;
        this.score = score;
    }

    public Utilizador(Utilizador ut) {
        this.username = ut.getUsername();
        this.password = ut.getPassword();
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
        return (this.username.equals(ut.getUsername()) && this.password.equals(ut.getPassword()) && this.score == ut.getScore());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 23 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 23 * hash + this.score;
        return hash;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
