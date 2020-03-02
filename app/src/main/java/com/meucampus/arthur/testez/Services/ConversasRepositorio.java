package com.meucampus.arthur.testez.Services;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 31/12/2017.
 */
public class ConversasRepositorio {
    private static int flag;
    private static List<String> matriculas = new ArrayList<>();

    public ConversasRepositorio(int flag, List<String> matriculas) {
        this.flag = flag;
        this.matriculas = matriculas;
    }
    public ConversasRepositorio(){}
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<String> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<String> matriculas) {
        this.matriculas = matriculas;
    }
}
