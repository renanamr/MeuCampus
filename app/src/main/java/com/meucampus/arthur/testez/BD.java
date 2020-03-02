package com.meucampus.arthur.testez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.meucampus.arthur.testez.Services.Turmas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 30/11/2016.
 */
public class BD {
    private SQLiteDatabase bd;
    public BD(Context context){
        BDCore auxbd = new BDCore(context);
        bd = auxbd.getWritableDatabase();
    }
    public void delete(){
        bd.delete("mensagem", null, null);

    }

    public void apagarTudo(){
        bd.delete("mensagem", null, null);
        bd.delete("usuario", null, null);
        bd.delete("turmas_virtuais", null, null);
        bd.delete("noticias", null, null);
        bd.delete("jsonsave", null, null);

    }
    public void inserir (Mensagem mensagem, String tipo, String remetente, String destinatario){
        Log.e("BD: ", mensagem.toString());
        String tipoBD="";
        if (tipo.equals("COADES/"))
            tipoBD="COADES";
        else
            if(tipo.equals("DG/"));
                tipoBD="DG";
        ContentValues valores = new ContentValues();
        valores.put("texto", mensagem.getTexto());
        valores.put("data", mensagem.getData());
        valores.put("hora", mensagem.getHora());

        valores.put("recebida", mensagem.isRecebida());
        valores.put("matriculaRemetente", remetente);
        valores.put("tipo", mensagem.getTipo());
        valores.put("matriculaDestinatario", destinatario);
        bd.insert("mensagem", null, valores);
    }
    public List<Mensagem> buscar (String idUsuario, String tipo){
        Log.e("BD idUsuario: ",idUsuario);
        Log.e("BD tipo:", tipo );
        List<Mensagem> mensagems = new ArrayList<Mensagem>();

        String[] colunas = new String[]{"texto", "data", "hora", "recebida", "matriculaRemetente", "tipo", "matriculaDestinatario"};

        Cursor cursor =bd.query("mensagem", colunas, "matriculaDestinatario = " + idUsuario+ " or matriculaRemetente = " + idUsuario, null,null, null, "_id ASC");
        Log.e("TO AQUI NO INSERIR", "MEU PARCA");
        Log.e("BD cursor", cursor.getCount()+".");
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                Log.e("Tipox", "Tioo: "+ tipo+"   Tipo bd: "+ cursor.getString(5));
                if(!tipo.equals(cursor.getString(5)))
                    continue;

                Mensagem m = new Mensagem();
                m.setTexto(cursor.getString(0));
                m.setData(cursor.getString(1));
                m.setHora(cursor.getString(2));

                boolean value2 = cursor.getInt(3) > 0;
                m.setRecebida(value2);
                m.setMatricula(cursor.getString(4));
                m.setTipo(cursor.getString(5));
                Log.e("BD Tipo", m.getTipo());
                m.setIdUsuario(cursor.getInt(6));
                m.setMatriculaRemetente(cursor.getString(4));
                m.setMatriculaDestinatario(cursor.getString(5));

                mensagems.add(m);
            }while(cursor.moveToNext());
        }
        return mensagems;
    }
    public void inserir2 (String matricula, String senha){


        ContentValues valores = new ContentValues();
        valores.put("matricula",matricula);
        valores.put("token", senha);
        bd.insert("usuario", null, valores);
    }
    public String buscarMatricula (){
        List<Mensagem> mensagems = new ArrayList<Mensagem>();

        String[] colunas = new String[]{"matricula", "token"};

        Cursor cursor =bd.query("usuario", colunas, null, null,null, null, "");
        Log.e("TO AQUI NO INSERIR", "MEU PARCA");
        Log.e("opa", cursor.getCount()+".");
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{

                return cursor.getString(0);
            }while(cursor.moveToNext());
        }
        return null;
    }

    public String buscarToken (){
        List<Mensagem> mensagems = new ArrayList<Mensagem>();

        String[] colunas = new String[]{"matricula", "token"};

        Cursor cursor =bd.query("usuario", colunas, null, null,null, null, "");
        Log.e("TO AQUI NO INSERIR", "MEU PARCA");
        Log.e("opa", cursor.getCount()+".");
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{

                return cursor.getString(1);
            }while(cursor.moveToNext());
        }
        return null;
    }

    public void inserirTurmas(String codigo, String descricao, String matriculaProfessor, String nomeProfessor, int n1, int n2, int n3, int n4){

        ContentValues valores = new ContentValues();
        valores.put("cod_turma", codigo);
        valores.put("descricao", descricao);
        valores.put("matricula_professor",matriculaProfessor);
        valores.put("nome_professor", nomeProfessor);
        valores.put("nota1", n1);
        valores.put("nota2", n2);
        valores.put("nota3", n3);
        valores.put("nota4", n4);

        bd.insert("turmas_virtuais", null, valores);
    }
    public void inserirTurmas(List<Turmas> turmas){
        try{
            deletarTurmas();
        }catch (Exception e){
            e.printStackTrace();
        }

        for (Turmas t : turmas){
            ContentValues valores = new ContentValues();
            valores.put("cod_turma", t.getCodigo());
            valores.put("descricao", t.getDescricao());
            valores.put("matricula_professor",t.getMatriculaProfessor());
            valores.put("nome_professor", t.getNomeProfessor());
            valores.put("nota1", t.getNota1());
            valores.put("nota2", t.getNota2());
            valores.put("nota3", t.getNota3());
            valores.put("nota4", t.getNota4());

            bd.insert("turmas_virtuais", null, valores);
        }
        for(Turmas t : listarTurmas()){
            Log.e("BD:::", t.getDescricao());
        }

    }

    public void inserirJson(String noticia, String evento){
        apagarJson();

        ContentValues valores = new ContentValues();
        valores.put("id", 0);
        valores.put("noticias", noticia);
        valores.put("eventos", evento);
        Log.e("notnot", getNoticias()+".");

        bd.insert("jsonsave", null, valores);
        Log.e("bd:", getNoticias());
        Log.e("bd:", getEventos());

    }

    public void apagarJson(){
        bd.delete("jsonsave", null, null);
    }
    public void deletarTurmas(){
        Log.e("BD_MAIN: DEL", "ETAR");

        bd.delete("turmas_virtuais",null, null);
    }

    public String getNoticias(){
        String[] colunas = new String[]{"id", "noticias", "eventos"};

        Cursor cursor = bd.query("jsonsave", colunas, null, null, null, null, null);
        try{
//            cursor.moveToFirst();
            if (cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    Log.e("notnotvvvv", cursor.getString(1));
                    return  cursor.getString(1);
                }while(cursor.moveToNext());

            }
        } catch(Exception e){
            e.printStackTrace();

        }
        return "";
    }

    public String getEventos(){
        String[] colunas = new String[]{"id", "noticias", "eventos"};

        Cursor cursor = bd.query("jsonsave", colunas, null, null, null, null, null);
        try{
            if (cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    Log.e("notnotvvvv", cursor.getString(2));
                    return  cursor.getString(2);
                }while(cursor.moveToNext());

            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return "";
    }
    public List<Turmas> listarTurmas(){
        List<Turmas> turmas = new ArrayList<Turmas>();

        String[] colunas = new String[]{"cod_turma", "descricao", "matricula_professor", "nome_professor", "nota1", "nota2", "nota3", "nota4"};

        Cursor cursor =bd.query("turmas_virtuais", colunas, null, null,null, null, "");
        Log.e("Cursor:::" , cursor.getCount()+";");
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                Turmas turmas1 = new Turmas(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
                turmas.add(turmas1);
            }while(cursor.moveToNext());
        }
        return turmas;
    }
}
