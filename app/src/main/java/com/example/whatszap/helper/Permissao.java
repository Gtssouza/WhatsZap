package com.example.whatszap.helper;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity){

        if(Build.VERSION.SDK_INT>=23){
            List<String> listaPermissoes = new ArrayList<>();

            /*Percorre as permissões passadas verificando
            uma a uma se já tem a permissão liberda*/
            for(String permissao : permissoes){
                ContextCompat.checkSelfPermission(activity,permissao);
            }

        }

        return true;
    }

}
