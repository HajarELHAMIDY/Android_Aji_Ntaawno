package com.example.ajitaawno;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MesDemandesFragment extends Fragment {

    private RecyclerView Demande_liste;
    private List<Demande> ListDemades;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String current_id;
    private MesDemandeAdapter mesDemandesAdapter;

    ListenerRegistration registration;
    public MesDemandesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mes_demandes, container, false);
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Mes Demandes");
        FloatingActionButton fab = root.findViewById(R.id.ajouterfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToPostActivity();
            }
        });


        ListDemades=new ArrayList<>();
        Demande_liste=root.findViewById(R.id.Demande_View);

        mesDemandesAdapter =new MesDemandeAdapter(ListDemades);
        Demande_liste.setLayoutManager(new LinearLayoutManager(getActivity()));
        Demande_liste.setAdapter(mesDemandesAdapter);

        firebaseFirestore =FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        current_id=mAuth.getCurrentUser().getUid();


        AfficherMesDemandes();


        return root;
    }
    private void SendUserToPostActivity() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new PublierDemandeFragment()).commit();
    }

    private void AfficherMesDemandes(){
        registration=firebaseFirestore.collection("Demandes").whereEqualTo("User_id",current_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(mAuth.getCurrentUser()==null){
                    registration.remove();
                }
                else{
                    for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                        if(doc.getType()==DocumentChange.Type.ADDED){
                            Demande demande=doc.getDocument().toObject(Demande.class);
                            demande.setId(doc.getDocument().getId());
                            ListDemades.add(demande);
                           Collections.sort(ListDemades);
                            mesDemandesAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

        });


    }



}
