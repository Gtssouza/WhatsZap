package com.example.whatszap.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.whatszap.R;
import com.example.whatszap.activity.ChatActivity;
import com.example.whatszap.adapter.ContatosAdapter;
import com.example.whatszap.adapter.ConversasAdapter;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.RecyclerItemClickListener;
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
    private DatabaseReference database;
    private DatabaseReference conversaRef;
    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        recyclerViewConversas = view.findViewById(R.id.recyclerIdConversas);

        //Configurar adapter
        adapterConversas = new ConversasAdapter(listaConversas, getActivity());

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapterConversas);

        //Configurar evento de clique
        recyclerViewConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewConversas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Conversa conversaSelecionada = listaConversas.get(position);

                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("chatContato",conversaSelecionada.getUsuarioExibicao());
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

        //Configura conversas ref
        String identificadorUser = UsuarioFirebase.getIndentificadorUser();
        database = ConfigFirebase.getFirebaseDatabase();
        conversaRef = database.child("conversas").child(identificadorUser);


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

    public void pesquisaConversas(String texto){
        //Log.d("pesquisa",texto);
        List<Conversa> listaConversaBusca = new ArrayList<>();
        for(Conversa conversa : listaConversas){
            String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
            String ultMsg = conversa.getUltimaMsg().toLowerCase();

            if(nome.contains(texto) || ultMsg.contains(texto)){
                listaConversaBusca.add(conversa);
            }
        }

        adapterConversas = new ConversasAdapter(listaConversaBusca, getActivity());
        recyclerViewConversas.setAdapter(adapterConversas);
        adapterConversas.notifyDataSetChanged();
    }

    public void recarregarConversas(){
        adapterConversas = new ConversasAdapter(listaConversas, getActivity());
        recyclerViewConversas.setAdapter(adapterConversas);
        adapterConversas.notifyDataSetChanged();
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