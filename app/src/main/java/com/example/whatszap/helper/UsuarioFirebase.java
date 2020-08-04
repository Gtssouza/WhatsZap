package com.example.whatszap.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

    public static String getIndentificadorUser(){
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutentication();
        String email = usuario.getCurrentUser().getEmail();
        String userId = Base64Custom.codificaBase64(email);

        return userId;
    }

    public static FirebaseUser getUserAtual(){
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutentication();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){
        try{
            FirebaseUser user = getUserAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil","Erro ao atualizar nome de perfil");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static boolean atualizarFotoUsuario(Uri url){
       try{
           FirebaseUser user = getUserAtual();
           UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                   .setPhotoUri(url)
                   .build();
           user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(!task.isSuccessful()){
                       Log.d("Perfil","Erro ao atualizar foto de perfil");
                   }
               }
           });
           return true;
       }catch (Exception e){
            e.printStackTrace();
            return false;
       }

    }

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUserAtual();
        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl() == null){
            usuario.setFotoUser("");
        }else{
            usuario.setFotoUser(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;
    }

}
