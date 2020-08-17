package com.example.whatszap.activity;

import android.os.Bundle;

import com.example.whatszap.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.whatszap.R;

import java.util.ArrayList;
import java.util.List;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<Usuario> usuariosListaSelect = new ArrayList<>();
    private TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTotal = findViewById(R.id.textViewtotal);

        FloatingActionButton fab = findViewById(R.id.fabConfirmaGrupo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras() != null){
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            usuariosListaSelect.addAll(membros);

            txtTotal.setText("total :" + usuariosListaSelect.size());
        }
    }
}