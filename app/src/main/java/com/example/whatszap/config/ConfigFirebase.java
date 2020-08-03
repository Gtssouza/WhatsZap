package com.example.whatszap.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth autentication;
    private static StorageReference storage;

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

    //retorna a instancia do FirebaseStorage
    public static StorageReference getFirebaseStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}
