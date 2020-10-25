package com.example.ajitaawno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText editEmail,editPassword;
    Button bLogin;
    TextView CreateBtn;
    TextView pwd_forgot;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test);
//        if (fAuth.getCurrentUser()!=null) {
  //          startActivity(new Intent(Login.this, Home.class));
    //        finish();
     //   }
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        editEmail=findViewById(R.id.name);
        editPassword=findViewById(R.id.password);
        pwd_forgot = findViewById(R.id.forgot_pwd);
        fAuth=FirebaseAuth.getInstance();
        bLogin=findViewById(R.id.login);
        CreateBtn =findViewById(R.id.register);
        pwd_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ResetPwdActivity.class));
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editEmail.getText().toString().trim();
                String password=editPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    editEmail.setError("Introuvable");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    editPassword.setError("Introuvable");
                    return;
                }

                if(password.length()<6){
                    editPassword.setError("taille inférieure de 6");
                    return;
                }




                //authentification

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            if (!fAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(Login.this,"Verifie votre Email",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Login.this,"Connexion Réussi",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Home.class));
                            }
                        }
                        else{
                           Toast.makeText(Login.this,"Error ! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        CreateBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
            }
        });
    }
}
