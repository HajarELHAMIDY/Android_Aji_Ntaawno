package com.example.ajitaawno.ui.offre;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ajitaawno.MesOffresAdapter;
import com.example.ajitaawno.Offres;
import com.example.ajitaawno.PublierOffreFragment;
import com.example.ajitaawno.R;
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

public class MesOffreFragment extends Fragment {

    private RecyclerView offre_liste;
    private List<Offres> ListOffres;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String current_id;
    private MesOffresAdapter mesOffresAdapter;

     DocumentReference ref;
    ListenerRegistration registration;
    private Object Tag;

    public MesOffreFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_mes_offre,container,false);

        FloatingActionButton fab = v.findViewById(R.id.ajouterfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToPostActivity();
            }
        });


        ListOffres=new ArrayList<>();
        offre_liste=v.findViewById(R.id.Offre_View);

        mesOffresAdapter =new MesOffresAdapter(ListOffres);
        offre_liste.setLayoutManager(new LinearLayoutManager(getActivity()));
        offre_liste.setAdapter(mesOffresAdapter);

         firebaseFirestore =FirebaseFirestore.getInstance();
         mAuth=FirebaseAuth.getInstance();
         current_id=mAuth.getCurrentUser().getUid();

        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Mes Offres");
           AfficherMesOffres();



    return v;
    }




    private void AfficherMesOffres(){
        registration=firebaseFirestore.collection("Offres").whereEqualTo("User_id",current_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
             public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(mAuth.getCurrentUser()==null){
                    registration.remove();
                }
                else{
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType()==DocumentChange.Type.ADDED){
                        Offres offres=doc.getDocument().toObject(Offres.class);
                        offres.setId(doc.getDocument().getId());
                        ListOffres.add(offres);

                        System.out.println(offres);
                        Collections.sort(ListOffres);
                        mesOffresAdapter.notifyDataSetChanged();
                    }
                }
            }
                }

        });


    }

    private void SendUserToPostActivity() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new PublierOffreFragment()).commit();
    }


}
