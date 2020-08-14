package com.example.whatszap.model;

import com.example.whatszap.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Mensagem {

    private String idUser;
    private String mensagem;
    private String imagem;

    public Mensagem() {
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
}
