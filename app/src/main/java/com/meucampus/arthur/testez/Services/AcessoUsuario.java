package com.meucampus.arthur.testez.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.meucampus.arthur.testez.BDCore;

/**
 * Created by ARTHUR on 07/03/2018.
 */

public class AcessoUsuario {
    private SQLiteDatabase bd;
    public AcessoUsuario(Context context){
        BDCore bdCore = new BDCore(context);
        bd = bdCore.getReadableDatabase();
    }

    public void inserir(String matricula, String token){
        deletar();
        ContentValues valores = new ContentValues();
        valores.put("matricula", matricula);
        valores.put("token", token);
        bd.insert("usuario", null, valores);
    }

    public void deletar(){
        bd.delete("usuario",null, null);
    }

    public String getMatricula(){
        String[] colunas = new String[]{"matricula", "token"};

        Cursor cursor =bd.query("usuario", colunas, null, null,null, null, "_id ASC");
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                return cursor.getString(0);
            }while(cursor.moveToNext());
        }
        return  "";
    }


    public String getToken(){
        String[] colunas = new String[]{"matricula", "token"};

        Cursor cursor =bd.query("usuario", colunas, null, null,null, null, "_id ASC");
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                return cursor.getString(0);
            }while(cursor.moveToNext());
        }
        return "";
    }
}
