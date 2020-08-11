package com.example.whatszap.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatszap.R;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Mensagem;

import java.lang.annotation.Target;
import java.util.List;

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensagemAdapter(List<Mensagem> listaMensagem, Context c) {
        this.mensagens = listaMensagem;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = null;
        if(viewType == TIPO_REMETENTE){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente,parent,false);
        }else if(viewType == TIPO_DESTINATARIO){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario,parent,false);
        }
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String imagem = mensagem.getImagem();

        if(imagem != null){
            Uri url = Uri.parse(imagem);
            holder.imagem.setVisibility(View.VISIBLE);
            Glide.with(context).load(url).into(holder.imagem);
            holder.mensagem.setVisibility(View.GONE);
        }else {
            holder.mensagem.setText(msg);
            holder.imagem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mens = mensagens.get(position);
        String idUser = UsuarioFirebase.getIndentificadorUser();
        if(idUser.equals(mens.getIdUser())){
            return TIPO_REMETENTE;
        }else{
            return TIPO_DESTINATARIO;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mensagem;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mensagem = itemView.findViewById(R.id.textView3);
            imagem = itemView.findViewById(R.id.imageView3);
        }
    }
}
