/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author User
 */
public class Desafio {

    private String nome;
    private HashMap<Integer, Pergunta> perguntas;
    private String criador;
    private GregorianCalendar datahora;
    private TreeSet<String> utilizadores;
    private HashMap<String, Integer> pontuacoes;

    public Desafio() {
        this.nome = new String();
        this.criador = new String();
        this.perguntas = new HashMap<>();
        this.utilizadores = new TreeSet<>();
        this.pontuacoes = new HashMap<>();

    }

    public Desafio(String nome, String criador) {
        this.nome = nome;
        this.criador = criador;
        this.perguntas = new HashMap<>();
        this.utilizadores = new TreeSet<>();
        this.pontuacoes = new HashMap<>();
        this.datahora=new GregorianCalendar();
        this.datahora.add(Calendar.MINUTE, 5);
    }

    public Desafio(String nome, String criador, int dia, int mes, int ano, int horas, int minutos, int segundos) {
        this.nome = nome;
        this.criador = criador;
        this.perguntas = new HashMap<>();
        this.utilizadores = new TreeSet<>();
        this.pontuacoes = new HashMap<>();
        this.datahora=new GregorianCalendar(ano, mes-1, dia, horas, minutos, segundos);
    }

    public String getCriador() {
        return this.criador;
    }
    public String getNome(){
        return this.nome;
    }
    public HashMap<Integer, Pergunta> getPerguntas() {
        HashMap<Integer, Pergunta> res = new HashMap<>();
        for (Pergunta p : this.perguntas.values()) {
            res.put(res.size() + 1, p.clone());
        }
        return res;
    }

    public HashMap<String, Integer> getPontuacoes() {
        return pontuacoes;
    }

    public TreeSet<String> getUtilizadores() {
        return new TreeSet<>(utilizadores);
    }
 
    public GregorianCalendar getDatahora() {
        return datahora;
    }
    
    public void setCriador(String criador) {
        this.criador = criador;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void adicionaUtilizador(String ut){
        this.utilizadores.add(ut);
        this.pontuacoes.put(ut,0);
    }
    
    public void adicionaPont(String ut,int pont){
        Integer pontosAt=this.pontuacoes.get(ut);
        pontosAt+=pont;
    }

    public void setPerguntas(HashMap<Integer, Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public void insertPergunta(Pergunta p) {
        this.perguntas.put(perguntas.size() + 1, p);
    }

    public Pergunta getPergunta(Integer i) {
        return this.perguntas.get(i).clone();
    }
    public TreeSet<ParUsernamePontos> getTopPontuacao(){
        TreeSet<ParUsernamePontos> res=new TreeSet<>(new ComparatorPUP());
        for(String st: this.utilizadores){
            res.add(new ParUsernamePontos(st, this.pontuacoes.get(st)));
        }
        return res;
    }
    
    public void eliminaUtilizador(String ut){
        this.utilizadores.remove(ut);
        this.pontuacoes.remove(ut);
    }

            
}
