package com.example.whatszap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatszap.R;
import com.example.whatszap.model.Conversa;

import java.util.List;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context c;

    public ConversasAdapter(List<Conversa> listaConversas, Context context) {
        this.conversas = listaConversas;
        this.c = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent,false);
        return new ConversasAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
