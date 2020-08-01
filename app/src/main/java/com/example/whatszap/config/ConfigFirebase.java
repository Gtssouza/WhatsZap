package com.example.whatszap.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth autentication;

    //retornar a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    //retornar a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutentication(){
        if(autentication == null){
            autentication = FirebaseAuth.getInstance();
        }
        return autentication;
    }
}
