package com.example.whatszap.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.example.whatszap.adapter.GrupoSelecionadoAdapter;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.helper.UsuarioFirebase;
import com.example.whatszap.model.Grupo;
import com.example.whatszap.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatszap.R;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<Usuario> usuariosListaSelect = new ArrayList<>();
    private TextView txtParticipantes;
    private GrupoSelecionadoAdapter adapterGrupo;
    private RecyclerView recyclerGrupoSelect;
    private CircleImageView imgCadastroGrupo;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private Grupo grupo;
    private FloatingActionButton fabGrupoConfirma;
    private EditText nomeGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Defina o nome");

        txtParticipantes = findViewById(R.id.textTotalParticipantes);
        recyclerGrupoSelect = findViewById(R.id.recyclerCadastroGrupo);
        imgCadastroGrupo = findViewById(R.id.imgGrupo);
        storageReference = ConfigFirebase.getFirebaseStorage();
        grupo = new Grupo();
        fabGrupoConfirma = findViewById(R.id.fabConfirmaGrupo);
        nomeGrupo = findViewById(R.id.editTextNomeGrupo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras() != null){
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            usuariosListaSelect.addAll(membros);

            txtParticipantes.setText("Membros: " + usuariosListaSelect.size());
        }

        imgCadastroGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getPackageManager()) != null){
                    //Começa a activity e captura o resultado
                    startActivityForResult(intent,SELECAO_GALERIA);
                }
            }
        });

        //Configurar recyclerView
        adapterGrupo = new GrupoSelecionadoAdapter(usuariosListaSelect, getApplicationContext());
        //configuração recyclerView
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerGrupoSelect.setLayoutManager(layoutManagerHorizontal);
        recyclerGrupoSelect.setHasFixedSize(true);
        recyclerGrupoSelect.setAdapter(adapterGrupo);

        //configurar floating action button
        fabGrupoConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grupoNome = nomeGrupo.getText().toString();
                usuariosListaSelect.add(UsuarioFirebase.getDadosUsuarioLogado());
                grupo.setMembros(usuariosListaSelect);

                grupo.setNome(grupoNome);
                grupo.salvar();

                Intent i = new Intent(CadastroGrupoActivity.this, ChatActivity.class);
                i.putExtra("chatGrupo",grupo);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                Uri localImagemSelecionada = data.getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);

                    if(imagem != null){
                        imgCadastroGrupo.setImageBitmap(imagem);

                        //Recuperar dados da imagem para o firebase
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imagem.compress(Bitmap.CompressFormat.JPEG, 70,baos);
                        byte[] dadosImagem = baos.toByteArray();

                        //Salvar imagem no Firebase
                        final StorageReference imagemRef = storageReference
                                .child("imagens")
                                .child("grupos")
                                .child(grupo.getId() + ".jpeg");

                        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CadastroGrupoActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(CadastroGrupoActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Uri url = task.getResult();
                                        grupo.setFoto(url.toString());
                                    }
                                });
                            }
                        });
                    }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}