package com.example.whatszap.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificaBase64(String txt){
        return Base64.encodeToString(txt.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodificaBase64(String txtDecod){
        return new String(Base64.decode(txtDecod,Base64.DEFAULT));
    }

}
