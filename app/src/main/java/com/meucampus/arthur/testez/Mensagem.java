package com.meucampus.arthur.testez;

/**
 * Created by renan on 06/10/2016.
 */
public class Mensagem {
    private String texto;
    private int id;
    private String data;
    private String hora;


    private int idUsuario;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    private String matricula;
    private String tipo;
    private boolean recebida;
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public boolean isRecebida() {
        return recebida;
    }
    public void setRecebida(boolean recebida) {
        this.recebida = recebida;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public Mensagem(String texto, String hora, String data) {
        this.texto = texto;
        this.hora = hora;
        this.data = data;

    }

    public Mensagem(){

    }

    public boolean isTipoMensagem(String tipo){
        if(tipo.equals(getTipo()))
            return false;
        else
            return true;
    }

    private String matriculaRemetente;
    private String matriculaDestinatario;

    public String getMatriculaRemetente() {
        return matriculaRemetente;
    }

    public void setMatriculaRemetente(String matriculaRemetente) {
        this.matriculaRemetente = matriculaRemetente;
    }

    public String getMatriculaDestinatario() {
        return matriculaDestinatario;
    }

    public void setMatriculaDestinatario(String matriculaDestinatario) {
        this.matriculaDestinatario = matriculaDestinatario;
    }
}
