package com.meucampus.arthur.testez.Services;

/**
 * Created by ARTHUR on 05/03/2018.
 */

public class Turmas {
    private String codigo;
    private String descricao;
    private String matriculaProfessor;
    private String nomeProfessor;
    private int nota1;
    private int nota2;
    private int nota3;
    private int nota4;

    public Turmas(String codigo, String descricao, String matriculaProfessor, String nomeProfessor, int nota1, int nota2, int nota3, int nota4) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.matriculaProfessor = matriculaProfessor;
        this.nomeProfessor = nomeProfessor;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
        this.nota4 = nota4;
    }

    public int getNota1() {
        return nota1;
    }

    public void setNota1(int nota1) {
        this.nota1 = nota1;
    }

    public int getNota2() {
        return nota2;
    }

    public void setNota2(int nota2) {
        this.nota2 = nota2;
    }

    public int getNota3() {
        return nota3;
    }

    public void setNota3(int nota3) {
        this.nota3 = nota3;
    }

    public int getNota4() {
        return nota4;
    }

    public void setNota4(int nota4) {
        this.nota4 = nota4;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    @Override
    public String toString() {
        return "{codigo: "+ this.codigo+", descricao: "+ this.descricao+", matriculaProfessor: "+ this.matriculaProfessor+ ", nomeProfessor: " + this.nomeProfessor+"}";
    }


    public Turmas(){}

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMatriculaProfessor() {
        return matriculaProfessor;
    }

    public void setMatriculaProfessor(String matriculaProfessor) {
        this.matriculaProfessor = matriculaProfessor;
    }
}
