package com.example.whatszap.helper;

import com.example.whatszap.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class UsuarioFirebase {

    public static String getIndentificadorUser(){
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutentication();
        String email = usuario.getCurrentUser().getEmail();
        String userId = Base64Custom.codificaBase64(email);

        return userId;
    }

}
