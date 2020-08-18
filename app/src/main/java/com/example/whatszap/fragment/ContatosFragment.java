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
import com.example.whatszap.activity.GrupoActivity;
import com.example.whatszap.adapter.ContatosAdapter;
import com.example.whatszap.adapter.ConversasAdapter;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.RecyclerItemClickListener;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Conversa;
import com.example.whatszap.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
                                List<Usuario> listaUsuarioAtualizada = adapter.getContatos();
                                Usuario userSelect = listaUsuarioAtualizada.get(position);
                                boolean cabecalho = userSelect.getEmail().isEmpty();

                                if(cabecalho){
                                    Intent intent = new Intent(getActivity(), GrupoActivity.class);
                                    startActivity(intent);
                                }else{
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatContato", userSelect);
                                    startActivity(i);
                                }

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

        adicionaMenuNovoGrupo();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
                limparListaContatos();

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

    public void limparListaContatos(){
        listaContatos.clear();
        adicionaMenuNovoGrupo();
    }

    public void adicionaMenuNovoGrupo(){
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo Grupo");
        itemGrupo.setEmail("");

        listaContatos.add(itemGrupo);
    }

    public void pesquisaContatos(String texto){
        //Log.d("pesquisa",texto);
        List<Usuario> listaContatosBusca = new ArrayList<>();
        for(Usuario contato : listaContatos){
            String nome = contato.getNome().toLowerCase();
            if(nome.contains(texto)){
                listaContatosBusca.add(contato);
            }

        }

        adapter = new ContatosAdapter(listaContatosBusca, getActivity());
        recyclerViewCont.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recarregarContatos(){
        adapter = new ContatosAdapter(listaContatos, getActivity());
        recyclerViewCont.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}