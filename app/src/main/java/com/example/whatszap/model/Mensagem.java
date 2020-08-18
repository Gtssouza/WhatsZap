package com.example.whatszap.model;

import com.example.whatszap.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Mensagem {

    private String idUser;
    private String mensagem;
    private String imagem;
    private String nome;

    public Mensagem() {
        this.setNome("");
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
