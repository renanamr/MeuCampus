package com.meucampus.arthur.testez;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ARTHUR on 30/11/2016.
 */
public class BDCore extends SQLiteOpenHelper{
    private static final String NOME_BD = "banco";
    private static final int VERSAO_BD = 21;

    public BDCore(Context context) {
        super (context, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {

        bd.execSQL("create table mensagem(_id integer primary key autoincrement, texto text not null, data text not null, hora text not null, recebida boolean, matriculaRemetente text not null, matriculaDestinatario text not null, tipo text not null);");
        bd.execSQL("create table usuario(matricula text primary key, token text);");
        bd.execSQL("create table turmas_virtuais(cod_turma text primary key, descricao text, matricula_professor text, nome_professor text, nota1 int, nota2 int, nota3 int, nota4 int);");
     //   bd.execSQL("create table notas(cod_turma text primary key, bimestre integer, nota integer, id integer primary key autoincrement);");
        bd.execSQL("create table noticias(id integer primary key, titulo text, descricao text, data text);");
        bd.execSQL("create table jsonsave(id integer primary key autoincrement, noticias text, eventos text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {
        bd.execSQL("drop table mensagem;");
        bd.execSQL("drop table usuario;");
        bd.execSQL("drop table if exists turmas_virtuais;");
        bd.execSQL("drop table if exists notas;");
        bd.execSQL("drop table noticias;");
        bd.execSQL("drop table if exists jsonsave;");
        onCreate(bd);
    }
}
