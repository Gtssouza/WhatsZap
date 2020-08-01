package com.example.whatszap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatszap.R;
import com.example.whatszap.config.ConfigFirebase;
import com.example.whatszap.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
    }

    public void cadastroUsuario(Usuario usuario){
        auth = ConfigFirebase.getFirebaseAutentication();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar usuário",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    String msg = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        msg = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        msg = "Digite um email válido";
                    }catch(FirebaseAuthUserCollisionException e){
                        msg = "Usuário já cadastrado";
                    }catch (Exception e){
                        msg = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            msg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarUsuario(View view){
        String textNome = campoNome.getText().toString();
        String textEmail = campoEmail.getText().toString();
        String textSenha = campoSenha.getText().toString();

        if(!textNome.isEmpty()){//verifica nome
            if(!textEmail.isEmpty()){//verifica email
                if(!textSenha.isEmpty()){//verifica senha

                    Usuario usuario = new Usuario();
                    usuario.setNome(textNome);
                    usuario.setEmail(textEmail);
                    usuario.setSenha(textSenha);

                    cadastroUsuario(usuario);

                }else {
                    Toast.makeText(CadastroActivity.this,"Preencha o senha",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(CadastroActivity.this,"Preencha o email",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroActivity.this,"Preencha o nome",Toast.LENGTH_SHORT).show();
        }
    }
}