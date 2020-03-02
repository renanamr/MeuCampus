package com.meucampus.arthur.testez;

/**
 * Created by ARTHUR on 15/09/2016.
 */
public class Evento {
    private int id;
    private String titulo;

    private String descricao;

    private Tipo tipo;



    private String imageLink;
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    private String dataPublicacao;

    private String dataInicio;

    private String dataFim;
    private String local;
    public void setLocal(String local){
        this.local = local;
    }

    public String getLocal(){
        return local;
    }
    public Evento(String titulo, String descricao, Tipo tipo, String dataPublicacao, String dataInicio,
                  String dataFim) {
        super();
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;

        this.dataPublicacao = dataPublicacao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Evento(){

    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }


    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

}

