package com.example.whatszap.activity;

import android.os.Bundle;

import com.example.whatszap.adapter.ContatosAdapter;
import com.example.whatszap.adapter.GrupoSelecionadoAdapter;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.RecyclerItemClickListener;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.whatszap.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {

    private RecyclerView recyclerMembroSelect, recyclerMember;
    private ContatosAdapter contatos;
    private GrupoSelecionadoAdapter grupoSelectAdapter;
    private List<Usuario> usuariosLista = new ArrayList<>();
    private List<Usuario> usuariosListaSelect = new ArrayList<>();
    private ValueEventListener valueEventListenerGrupo;
    private DatabaseReference userRef = ConfigFirebase.getFirebaseDatabase().child("usuarios");
    private FirebaseUser userAtual = UsuarioFirebase.getUserAtual();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        recyclerMembroSelect = findViewById(R.id.recyclerMembrosSelecionados);
        recyclerMember = findViewById(R.id.recyclerMembros);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Configuração para recyclerView de contatos

        //adapter
        contatos = new ContatosAdapter(usuariosLista, getApplicationContext());
        //configuração recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMember.setLayoutManager(layoutManager);
        recyclerMember.setHasFixedSize(true);
        recyclerMember.setAdapter(contatos);

        recyclerMember.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMember,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario usuarioSelect = usuariosLista.get(position);
                                //remover user selecionado da lista
                                usuariosLista.remove(usuarioSelect);
                                contatos.notifyDataSetChanged();

                                //Adicionando userSelecionado dentro da nova lista
                                usuariosListaSelect.add(usuarioSelect);
                                grupoSelectAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        //RecyclerView para membros selecionados
        //adapter
        grupoSelectAdapter = new GrupoSelecionadoAdapter(usuariosListaSelect, getApplicationContext());
        //configuração recyclerView
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerMembroSelect.setLayoutManager(layoutManagerHorizontal);
        recyclerMembroSelect.setHasFixedSize(true);
        recyclerMembroSelect.setAdapter(grupoSelectAdapter);

       /* recyclerMember.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMembroSelect,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario usuarioSelect = usuariosListaSelect.get(position);
                                //remover user selecionado da lista
                                usuariosListaSelect.remove(usuarioSelect);
                                grupoSelectAdapter.notifyDataSetChanged();

                                //Adicionando userSelecionado dentro da nova lista
                                usuariosListaSelect.add(usuarioSelect);


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerGrupo);
    }

    public void recuperarContatos(){
        valueEventListenerGrupo = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dados : dataSnapshot.getChildren() ){

                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUserAtual = userAtual.getEmail();
                    if(!emailUserAtual.equals(usuario.getEmail())){
                        usuariosLista.add(usuario);
                    }

                }
                contatos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}