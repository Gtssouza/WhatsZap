package com.example.whatszap.model;

import com.example.whatszap.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.UUID;

public class Conversa implements Serializable {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMsg;
    private Usuario usuarioExibicao;

    public Conversa() {
    }

    public void salvar(){
        DatabaseReference database = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference convarRef = database.child("conversas");

        convarRef.child(this.getIdRemetente())
                .child(this.getIdDestinatario())
        .setValue(this);
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMsg() {
        return ultimaMsg;
    }

    public void setUltimaMsg(String ultimaMsg) {
        this.ultimaMsg = ultimaMsg;
    }

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }
}
