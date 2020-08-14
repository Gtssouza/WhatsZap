package com.example.whatszap.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatszap.R;
import com.example.whatszap.adapter.ContatosAdapter;
import com.example.whatszap.adapter.ConversasAdapter;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Conversa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversas;
    private ConversasAdapter adapterConversas;
    private List<Conversa> listaConversas = new ArrayList<>();
    private DatabaseReference dbRef;
    private DatabaseReference conversaRef;
    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        recyclerViewConversas = view.findViewById(R.id.recyclerIdConversas);
       // userRef = ConfigFirebase.getFirebaseDatabase().child("usuarios");
        //userAtual = UsuarioFirebase.getUserAtual();

        //Configurar adapter
        adapterConversas = new ConversasAdapter(listaConversas, getActivity());
        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapterConversas);

        //Configura conversas ref
        String identificadorUser = UsuarioFirebase.getIndentificadorUser();
        dbRef = ConfigFirebase.getFirebaseDatabase();
        conversaRef = dbRef.child("conversas").child(identificadorUser);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConveras();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversaRef.removeEventListener(childEventListenerConversas);
    }

    public void recuperarConveras(){

        childEventListenerConversas = conversaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Recuperar conversas
                Conversa conversa = dataSnapshot.getValue(Conversa.class);
                listaConversas.add(conversa);
                adapterConversas.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}