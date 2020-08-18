package com.example.whatszap.model;

import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.Base64Custom;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {
    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {
        DatabaseReference databaseReference=ConfigFirebase.getFirebaseDatabase();
        DatabaseReference grupoRef = databaseReference.child("grupos");

        String idFirebase = grupoRef.push().getKey();
        setId(idFirebase);
    }

    public void salvar(){
        DatabaseReference databaseReference=ConfigFirebase.getFirebaseDatabase();
        DatabaseReference grupoRef = databaseReference.child("grupos");
        grupoRef.child(getId()).setValue(this);

        //Salvar conversa para membros do grupo
        for(Usuario membro : getMembros()){

            String idRementente = Base64Custom.codificaBase64(membro.getEmail());
            String idDestinatario = getId();

            Conversa conversa = new Conversa();
            conversa.setIdRemetente(idRementente);
            conversa.setIdDestinatario(idDestinatario);
            conversa.setUltimaMsg("");
            conversa.setIsGroup("true");
            conversa.setGrupo(this);//vai salvar todos os dados desse grupo
            conversa.salvar();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
