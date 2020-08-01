package com.example.whatszap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoLoginEmail, campoLoginSenha;
    private Button btnEntrarLogin;
    private Usuario usuario;
    private FirebaseAuth auth;

    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser userAtual = auth.getCurrentUser();
        if(userAtual != null){
            abrirActivityMain();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoLoginEmail = findViewById(R.id.editEmailLogin);
        campoLoginSenha = findViewById(R.id.editSenhaLogin);
        btnEntrarLogin = findViewById(R.id.btnLogin);

        btnEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campoEmail = campoLoginEmail.getText().toString();
                String campoSenha = campoLoginSenha.getText().toString();

                if(!campoEmail.isEmpty()){
                    if(!campoSenha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(campoEmail);
                        usuario.setSenha(campoSenha);
                        validarLogin();

                    }else{
                        Toast.makeText(LoginActivity.this, "Senha incorreta",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Email incorreto",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void validarLogin(){

        auth = ConfigFirebase.getFirebaseAutentication();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login efetuado",Toast.LENGTH_SHORT).show();
                    abrirActivityMain();
                }else{
                    String msg = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        msg = "Usuario inexistente";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        msg = "E-mail e senha não correspondem a um usuário cadastrado";
                    }catch (Exception e){
                        msg = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void abrirCadastro(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirActivityMain(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}