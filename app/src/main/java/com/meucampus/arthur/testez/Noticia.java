package com.meucampus.arthur.testez;

/**
 * Created by ARTHUR on 15/09/2016.
 */
public class Noticia {
    private int id;

    private String titulo;

    private String 	descricao;

    private Tipo tipo;

    private String data;

    private String dataPublicacao;

    public String getImagemLink() {
        return imagemLink;
    }
    public Noticia(){

    }
    public String imagem;
    public void setImagem(String imagem){
        this.imagem = imagem;
    }

    public String getImagem(){
        return  imagem;
    }
    public void setImagemLink(String imagemLink) {
        this.imagemLink = imagemLink;
    }

    private String imagemLink;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Noticia(String titulo, String descricao, String data, String link){
        this.data = data;
        this.descricao = descricao;
        this.titulo = titulo;
        this.imagemLink = link;

    }



}
