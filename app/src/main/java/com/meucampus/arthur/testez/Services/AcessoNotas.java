package com.meucampus.arthur.testez.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meucampus.arthur.testez.BDCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 05/03/2018.
 */

public class AcessoNotas {
    private SQLiteDatabase bd;
    public AcessoNotas(Context context){
        BDCore auxbd = new BDCore(context);
        bd = auxbd.getWritableDatabase();
    }

    public void inserirNotas(Notas notas)
    {
        ContentValues valores = new ContentValues();
        valores.put("cod_turma", notas.getCodTurma());
        valores.put("bimestre", notas.getBimestre());
        valores.put("nota", notas.getNota());
        bd.insert("notas", null, valores);
    }

    public List<Notas> listar(){
        List<Notas> notas = new ArrayList<>();
        String[] colunas = new String[]{"cod_turma", "bimestre", "notas", "id"};

        Cursor cursor =bd.query("notas", colunas, null, null,null, null, "");
        Log.e("Cursor:::" , cursor.getCount()+";");
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                Notas turmas1 = new Notas(cursor.getString(0), cursor.getInt(1), cursor.getInt(2));
                notas.add(turmas1);
            }while(cursor.moveToNext());
        }
        return notas;
    }
}
