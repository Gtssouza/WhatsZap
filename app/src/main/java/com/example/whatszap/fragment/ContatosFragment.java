package com.example.whatszap.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.whatszap.R;
import com.example.whatszap.activity.ChatActivity;
import com.example.whatszap.adapter.ContatosAdapter;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.RecyclerItemClickListener;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewCont;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference userRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser userAtual;

    public ContatosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contatos, container, false);

        //Configurações iniciais
        recyclerViewCont = view.findViewById(R.id.recyclerListaContatos);
        userRef = ConfigFirebase.getFirebaseDatabase().child("usuarios");
        userAtual = UsuarioFirebase.getUserAtual();

        //Configurar adapter
        adapter = new ContatosAdapter(listaContatos, getActivity());
        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCont.setLayoutManager(layoutManager);
        recyclerViewCont.setHasFixedSize(true);
        recyclerViewCont.setAdapter(adapter);

        //Configurar evento de clique no reclyclerView
        recyclerViewCont.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewCont,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario userSelect = listaContatos.get(position);
                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("contatos", userSelect);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                ));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        listaContatos.clear();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerContatos);

    }

    public void recuperarContatos(){
        valueEventListenerContatos = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren() ){

                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUserAtual = userAtual.getEmail();
                    if(!emailUserAtual.equals(usuario.getEmail())){
                        listaContatos.add(usuario);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}