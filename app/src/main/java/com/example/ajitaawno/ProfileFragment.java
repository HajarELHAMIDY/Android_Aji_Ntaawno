package com.example.ajitaawno;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore fstore;
    String userId;
    TextView username,tel,dateN,email;
    CircleImageView imagrUser;
    DocumentReference documentReference;
    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_test, container, false);
        imagrUser = root.findViewById(R.id.image_user_profile);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        email = root.findViewById(R.id.EmailProfile);
        username = root.findViewById(R.id.userNameProfile);
        dateN = root.findViewById(R.id.DateNProfile);
        tel = root.findViewById(R.id.TelProfile);
        userId=mAuth.getCurrentUser().getUid();
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Profil");

        initProfile();
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void initProfile(){
        fstore.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    email.setText(task.getResult().getString("email"));
                    username.setText(task.getResult().getString("nom") +" " +task.getResult().getString("prenom"));
                    Glide.with(getContext()).load(task.getResult().getString("user_image")).into(imagrUser);
                    tel.setText(task.getResult().getString("phone"));
                    dateN.setText(task.getResult().getString("dateNaissance"));
                } else {
                    Toast.makeText(getContext(), "Erreur", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
