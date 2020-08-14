package com.example.whatszap.fragment;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversas;
    private ConversasAdapter adapterConversas;
    private List<Conversa> listaConversas = new ArrayList<>();

    public ConversasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        recyclerViewConversas = view.findViewById(R.id.recyclerIdConversas);
        userRef = ConfigFirebase.getFirebaseDatabase().child("usuarios");
        userAtual = UsuarioFirebase.getUserAtual();

        //Configurar adapter
        adapterConversas = new ConversasAdapter(listaConversas, getActivity());
        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapterConversas);
        return view;
    }
}