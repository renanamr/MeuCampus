package com.meucampus.arthur.testez;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 14/12/2016.
 */
public class Grade {
    private String horarioManha[][];
    private String horarioTarde[][];

    public String[][] getHorarioManha() {
        return horarioManha;
    }

    public void setHorarioManha(String[][] horarioManha) {
        this.horarioManha = horarioManha;
    }

    public String[][] getHorarioTarde() {
        return horarioTarde;
    }

    public void setHorarioTarde(String[][] horarioTarde) {
        this.horarioTarde = horarioTarde;
    }
    private List<Disciplina> disciplinas;
    public void addDisciplina(Disciplina d){
        if (this.disciplinas == null)
            this.disciplinas = new ArrayList<>();
        this.disciplinas.add(d);
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

}
