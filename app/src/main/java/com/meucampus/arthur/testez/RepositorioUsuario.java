package com.meucampus.arthur.testez;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by ARTHUR on 18/11/2016.
 */
public class RepositorioUsuario {
    public static File pdf;
    private static String nome;
    private static String matricula;
    private static String senha;
    private static String permissoes;
    private static String url;
    private static String foto;
    private static String curso;
    private static Bitmap bitmap;
    private static String dataUsuarios;
    private static boolean internet;

    public static boolean getInternet() {
        return internet;
    }

    public static void setInternet(boolean internet) {
        RepositorioUsuario.internet = internet;
    }

    public static String getDataUsuarios() {
        return dataUsuarios;
    }

    public static void setDataUsuarios(String dataUsuarios) {
        Log.e("dados:", dataUsuarios);
        RepositorioUsuario.dataUsuarios = dataUsuarios;
    }

    private static int vez=1;

    public static int getVez() {
        return vez;
    }

    public static void setVez(int vez) {
        RepositorioUsuario.vez = vez;
    }

    public static String mat;

    public static String getMat() {
        return mat;
    }

    public static void setMat(String mat) {
        RepositorioUsuario.mat = mat;
    }

    private static String token="";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        RepositorioUsuario.bitmap = bitmap;
    }

    public static String getCurso() {
        return curso;
    }

    public static void setCurso(String curso) {
        RepositorioUsuario.curso = curso;
    }

    public static String getFoto() {
        return foto;
    }

    public static void setFoto(String foto) {
        RepositorioUsuario.foto = foto;
    }

    public static String getUrl() {
        return url;
    }
    public void setUrl(String url){
        RepositorioUsuario.url = url;
    }


    public Usuario getUsuario(){
        Usuario usuario = new Usuario();
        try {
            usuario.setId(RepositorioUsuario.id);
            usuario.setNome(RepositorioUsuario.nome);
            usuario.setMatricula(RepositorioUsuario.matricula);
            usuario.setPermissoes(RepositorioUsuario.permissoes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  usuario;
    }
    public String getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(String permissoes) {
        RepositorioUsuario.permissoes = permissoes;
    }

    private static List<Mensagem> mensagens;

    private static int id;
    public  List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> m){
        mensagens = m;
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
    private String getSenha() {
        return senha;
    }
    private void setSenha(String senha) {
        this.senha = senha;
    }
    private static String tipo;

    private Horario horario;
    private static String[] turma;


    public String[] getTurma() {
        return turma;
    }
    public void setTurma(String[] turma) {
        this.turma = turma;
    }
    public Horario getHorario() {
        return horario;
    }
    public void setHorario(Horario horario) {
        this.horario = horario;
    }
    private static String caminhoPeriodo;

    public String getCaminhoPeriodo() {
        return caminhoPeriodo;
    }

    public void setCaminhoPeriodo(String caminhoPeriodo){this.caminhoPeriodo = caminhoPeriodo;
        Log.e("REPOSITORIO:::", caminhoPeriodo);}

}
