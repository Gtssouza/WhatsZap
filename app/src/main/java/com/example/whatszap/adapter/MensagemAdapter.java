package com.example.whatszap.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatszap.model.Mensagem;

import java.util.List;

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;

    public MensagemAdapter(List<Mensagem> listaMensagem, Context c) {
        this.mensagens = listaMensagem;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
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
