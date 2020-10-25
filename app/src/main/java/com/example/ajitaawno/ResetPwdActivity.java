package com.example.ajitaawno;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPwdActivity extends AppCompatActivity {
    private Button sendEmailBtn;
    private EditText emailInput;
    private FirebaseAuth auth;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_forgot);

        sendEmailBtn = findViewById(R.id.sendEmail);
        emailInput = findViewById(R.id.emailText);
        auth = FirebaseAuth.getInstance();

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Merci de saisir votre email");
                    return;
                }else{
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPwdActivity.this, "acceder a votre mail pour récuperer votre mot de passe", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }
                            else{
                                Toast.makeText(ResetPwdActivity.this, "Erreur ", Toast.LENGTH_LONG).show();

                            }
                        }
                });


        }
            }
        });
    }
}