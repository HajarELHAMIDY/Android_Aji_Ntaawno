package com.example.ajitaawno;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {
    EditText nomText, prenomText, phoneText, emailText, pwdText, dateNaissanceText, pwdConfirmText;
    Button registerBtn;
    FirebaseAuth auth;
    FirebaseFirestore fStor;
    ImageView profile_img;
    Uri pikedUriImage;
    TextView CreateBtn;
    static int PReqCode =1;
    static int REQUESCODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        profile_img = findViewById(R.id.profile);
        nomText = findViewById(R.id.nom);
        prenomText = findViewById(R.id.prenom);
        phoneText = findViewById(R.id.tel);
        emailText = findViewById(R.id.email);
        pwdText = findViewById(R.id.pwd);
        dateNaissanceText = findViewById(R.id.dateNaissance);
        pwdConfirmText = findViewById(R.id.pwdConfirm);
        registerBtn = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();
        fStor = FirebaseFirestore.getInstance();
        CreateBtn = findViewById(R.id.login);
        profile_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             /*   if(Build.VERSION.SDK_INT>=22){
                    Log.i("click : ", "test");
                    checkAndRequestForPermission();

                }else{
                    openGallery();
                }*/
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(CreateAccountActivity.this);
            }
        });
        CreateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailText.getText().toString().trim();
                final String pwd = pwdText.getText().toString().trim();
                final String pwdConfirm = pwdConfirmText.getText().toString().trim();
                final String phone = phoneText.getText().toString().trim();
                final String dateNaissance = dateNaissanceText.getText().toString().trim();
                final String nom = nomText.getText().toString().trim();
                final String prenom = prenomText.getText().toString().trim();
               // final String imageUrl =

                FirebaseUser user = auth.getCurrentUser();


                if (TextUtils.isEmpty(nom)) {
                    nomText.setError("Merci de saisir le nom");
                    return;
                }
                if (TextUtils.isEmpty(prenom)) {
                    prenomText.setError("Merci de saisir le prénom");
                    return;
                }
                if (TextUtils.isEmpty(dateNaissance)) {
                    dateNaissanceText.setError("Merci de saisir la date de naissance");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailText.setError("Merci de saisir l'email");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    phoneText.setError("Merci de saisir le telephone");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    pwdText.setError("Merci de saisir le mot de passe");
                    return;
                }
                if (TextUtils.isEmpty(pwdConfirm)) {
                    pwdConfirmText.setError("Merci de saisir confirmer le mot de passe");
                    return;
                }
                if (!pwd.equals(pwdConfirm)) {
                    pwdConfirmText.setError("la confirmation n'est pas valide");
                    return;
                }
                if (pwd.length() < 6) {
                    pwdText.setError("Le mot de passe doit contenir plus de 5 caractères");
                    return;
                }
                auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateAccountActivity.this, "Le compte créé avec succé", Toast.LENGTH_LONG).show();
                            DocumentReference documentReference = fStor.collection("users").document(auth.getUid());
                            Map<String, Object> user = new HashMap<>();
                            user.put("nom", nom);
                            user.put("email", email);
                            user.put("prenom", prenom);
                            user.put("phone", phone);
                            user.put("dateNaissance", dateNaissance);
                            user.put("user_image",pikedUriImage.toString());

                          updateUserInfo(pikedUriImage, auth.getCurrentUser());

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CreateAccountActivity.this, "acceder a votre mail pour verifier votre email", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }

    private void openGallery() {
        Intent galeryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galeryIntent.setType("image/*");
        startActivityForResult(galeryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(CreateAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(CreateAccountActivity.this, "PLease accept for required permission", Toast.LENGTH_LONG).show();

            }else{
                ActivityCompat.requestPermissions(CreateAccountActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);


            }

        }
        else{
            openGallery();

        }

    }




    public void updateUserInfo(Uri pikedUriImage, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        if(pikedUriImage!=null){
            final StorageReference imageFilePath = mStorage.child(pikedUriImage.getLastPathSegment());
            imageFilePath.putFile(pikedUriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri).build();

                            currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                        }
                    });

                }

            });
        }

    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESCODE  && data!=null){

        pikedUriImage = data.getData();
        profile_img.setImageURI(pikedUriImage);
        }
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                pikedUriImage=result.getUri();
                profile_img.setImageURI(pikedUriImage);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }
        }
    }
}