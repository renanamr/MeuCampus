package com.meucampus.arthur.testez;

/**
 * Created by ARTHUR on 17/12/2016.
 */
public class Professor {
    private String[] turmas;
    public String[] getTurmas() {
        return turmas;
    }
    public void setTurmas(String[] turmas) {
        this.turmas = turmas;
    }
    public String[] getMaterias() {
        return materias;
    }
    public void setMaterias(String[] materias) {
        this.materias = materias;
    }
    private String[] materias;
    private String matricula;
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

}
