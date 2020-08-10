package com.example.whatszap.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatszap.R;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.Base64Custom;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Mensagem;
import com.example.whatszap.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView imgPerfilChat;
    private TextView txtNomeChat;
    private Usuario userDest;
    private EditText txtMsg;

    //Identificadores de Usuario remetente e destinatario
    private String idUserRemetente;
    private String idUserDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        imgPerfilChat = findViewById(R.id.circleImagePerfil);
        txtNomeChat = findViewById(R.id.txtChatNome);
        txtMsg = findViewById(R.id.editEnviarText);

        //Recupera dados do user remetente
        idUserRemetente = UsuarioFirebase.getIndentificadorUser();

        FloatingActionButton fab = findViewById(R.id.fabEnviar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        //Recuperar dados do usuário destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userDest = (Usuario) bundle.getSerializable("contatos");
            txtNomeChat.setText(userDest.getNome());
            String foto = userDest.getFoto();
            if(foto != null){
                Uri url = Uri.parse(userDest.getFoto());
                Glide.with(ChatActivity.this).load(url).into(imgPerfilChat);
            }else{
                imgPerfilChat.setImageResource(R.drawable.padrao);
            }

            //recuperar dados usuario destinatario
            idUserDestinatario = Base64Custom.codificaBase64(userDest.getEmail());

        }

    }

    public void enviarMsg(View view){
        String msg = txtMsg.getText().toString();
        if(!msg.isEmpty()){
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUser(idUserRemetente);
            mensagem.setMensagem(msg);

            //salvar mensagem
            salvarMensagem(idUserRemetente,idUserDestinatario,mensagem);

        }else{
            Toast.makeText(ChatActivity.this,"Escreva uma mensagem",Toast.LENGTH_SHORT).show();
        }

    }

    public void salvarMensagem(String remetente, String destinatario, Mensagem txtMensg){
        DatabaseReference dbRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference msgRef = dbRef.child("mensagens");

        msgRef.child(remetente).child(destinatario).push()
                .setValue(txtMensg);

        //Limpar texto
        txtMsg.setText("");
    }

}