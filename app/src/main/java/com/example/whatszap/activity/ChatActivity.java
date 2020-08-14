package com.example.whatszap.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatszap.R;
import com.example.whatszap.adapter.MensagemAdapter;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.Base64Custom;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Conversa;
import com.example.whatszap.model.Mensagem;
import com.example.whatszap.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView imgPerfilChat;
    private TextView txtNomeChat;
    private Usuario userDest;
    private EditText txtMsg;
    private RecyclerView recyclerView;
    private MensagemAdapter adapterMsg;
    private List<Mensagem> mensagens = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;
    private ImageView sendImage;
    private static final int SELECAO_CAMERA =100;
    private StorageReference storageReference;


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
        recyclerView = findViewById(R.id.recyclerMsg);
        sendImage = findViewById(R.id.imgEnviarFoto);

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
        //Configuração adapter
        adapterMsg = new MensagemAdapter(mensagens,getApplicationContext());
        //Configuração RecyclerView
        RecyclerView.LayoutManager layoutManagerChat = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManagerChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMsg);

        database = ConfigFirebase.getFirebaseDatabase();
        storageReference = ConfigFirebase.getFirebaseStorage();
        mensagensRef = database.child("mensagens")
                .child(idUserRemetente)
                .child(idUserDestinatario);

        //Evento clique na camera
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try{

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if(imagem != null){

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70,baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Salvar imagem no Firebase
                    final StorageReference imagemRefs = storageReference.child("imagens")
                            .child("fotos")
                            .child(idUserRemetente)
                            .child(nomeImagem);

                    UploadTask uploadTask = imagemRefs.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ChatActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            imagemRefs.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String url = task.getResult().toString();
                                    Mensagem msg = new Mensagem();
                                    msg.setIdUser(idUserRemetente);
                                    msg.setMensagem("imagem.jpeg");
                                    msg.setImagem(url);

                                    //Salvar mensagem remetente
                                    salvarMensagem(idUserRemetente,idUserDestinatario,msg);

                                    //Salvar mensagem para destinatario
                                    salvarMensagem(idUserDestinatario,idUserRemetente,msg);

                                    //Salvar conversa
                                    salvarConversa(msg);
                                }
                            });
                        }
                    });
                }

            }catch (Exception e){

            }
        }
    }

    public void enviarMsg(View view){
        String msg = txtMsg.getText().toString();
        if(!msg.isEmpty()){
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUser(idUserRemetente);
            mensagem.setMensagem(msg);

            //salvar mensagem para o remetente
            salvarMensagem(idUserRemetente,idUserDestinatario,mensagem);

            //salvar mensagem para o destinatario
            salvarMensagem(idUserDestinatario,idUserRemetente,mensagem);

        }else{
            Toast.makeText(ChatActivity.this,"Escreva uma mensagem",Toast.LENGTH_SHORT).show();
        }

    }

    public void salvarConversa(Mensagem mensagem){
        Conversa conversaRemetente = new Conversa();
        Usuario user = new Usuario();
        conversaRemetente.setIdRemetente(idUserRemetente);
        conversaRemetente.setIdDestinatario(idUserDestinatario);
        conversaRemetente.setUltimaMsg(mensagem.getMensagem());
        conversaRemetente.setUsuarioExibicao(userDest);

        conversaRemetente.salvar();
    }

    public void salvarMensagem(String remetente, String destinatario, Mensagem txtMensg){
        DatabaseReference dbRef = ConfigFirebase.getFirebaseDatabase();
        mensagensRef = dbRef.child("mensagens");

        mensagensRef.child(remetente).child(destinatario).push()
                .setValue(txtMensg);

        //Limpar texto
        txtMsg.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMsg();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMsg(){
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapterMsg.notifyDataSetChanged();
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