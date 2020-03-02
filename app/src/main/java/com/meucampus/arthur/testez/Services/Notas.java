package com.meucampus.arthur.testez.Services;

/**
 * Created by ARTHUR on 07/03/2018.
 */

public class Notas {
    private String codTurma;
    private int bimestre;
    private int nota;

    public Notas(String codTurma, int bimestre, int nota) {
        this.codTurma = codTurma;
        this.bimestre = bimestre;
        this.nota = nota;
    }

    public String getCodTurma() {
        return codTurma;
    }

    public void setCodTurma(String codTurma) {
        this.codTurma = codTurma;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
