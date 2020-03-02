package com.meucampus.arthur.testez;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 16/11/2016.
 */
public class Usuario {

    private String nome;
    private String matricula;

    private String permissoes = "0";
    private Horario horario;
    private String[] turma;
    private String lastM;

    public String getLastM() {
        return lastM;
    }

    public void setLastM(String lastM) {
        this.lastM = lastM;
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Professor professor;
    public Professor getProfessor() {
        return professor;
    }
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
    public String[] getTurma() {
        return turma;
    }
    public void setTurma(String[] turma) {
        this.turma = turma;
    }
    public Usuario (String matricula,  String tipo, String[] turma){


        this.matricula = matricula;
        this.tipo = tipo;
        this.turma = turma;



    }

    public void setMensagens(List<Mensagem> m){
        this.mensagens = m;
    }

    public Usuario(){

    }
    private List<Mensagem> mensagens;

    private int id;
    public  List<Mensagem> getMensagens() {
        return mensagens;
    }
    public void addMensagem(Mensagem m){
        if (mensagens == null){
            mensagens = new ArrayList<>();
        }
        mensagens.add(m);
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPermissoes() {
        return permissoes;
    }
    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }
    public Horario getHorario() {
        return horario;
    }
    public void setHorario(Horario horario) {
        this.horario = horario;
    }
    private String tipo;
    private String curso;

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
