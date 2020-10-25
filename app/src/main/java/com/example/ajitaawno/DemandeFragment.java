package com.example.ajitaawno;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemandeFragment extends Fragment{
    Spinner spinnerVille, spinnerCatecory;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore fstore;
    String category;
    String ville;
    ListView listeViewDemande;
    FloatingActionButton fab;
    ArrayAdapter<String> adapterVille;
    List<Demande> arrayDemande;
    AdapterDemande arrayAdapterDemande;

    int i = 0;
    int j=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_demande, container, false);
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Espace Demandes");
        spinnerCatecory = root.findViewById(R.id.spinnerCategoryD);
        spinnerVille = root.findViewById(R.id.spinnerVilleD);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        listeViewDemande = root.findViewById(R.id.listViewDemande);
        arrayDemande = new ArrayList();

        init_List();
        init_spinners();
        spinnerVille.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0 && i!=0){
                    ville = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), ville, Toast.LENGTH_SHORT).show();
                    i++;
                    updateListeOffre();
                }else{
                    i++;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCatecory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0 && j!=0) {
                    category = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), category, Toast.LENGTH_SHORT).show();
                    updateListeOffre();
                }else{
                    j++;
                }

                Toast.makeText(parent.getContext(), "Liste de toutes les demandes", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       /* fab = root.findViewById(R.id.fab);

      //  fab.setImageResource(R.drawable.ic_home);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new RealHomeFragment()).commit();
            }
        });
*/



        return root;
    }

    public void init_List(){
        fstore.collection("Demandes").whereEqualTo("Reservation","non").addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent( QuerySnapshot documentSnapshot, FirebaseFirestoreException e) {
                arrayDemande = new ArrayList();
                for(DocumentSnapshot docOffre : documentSnapshot){

                    String user_id = docOffre.getString("User_id");
                    String description = docOffre.getString("Description");
                    String etat = docOffre.getString("Reservation");
                    int piece =docOffre.getLong("NombrePiece").intValue();
                    Date date = docOffre.getDate("TimePub");
                    String titre= docOffre.getString("TitreDemande");
                    String image = docOffre.getString("Image_Demande");
                    String categorie= docOffre.getString("Categorie");
                    String ville = docOffre.getString("Ville");
                    arrayDemande.add(new Demande(description,etat,piece, date,titre, user_id, image, categorie,ville));
                }

                arrayAdapterDemande = new AdapterDemande(getContext(),arrayDemande);

                listeViewDemande.setAdapter(arrayAdapterDemande);
            }
        });

    }
    public void init_List_Constraint_ville(){
        fstore.collection("Demandes").whereEqualTo("Reservation","non").whereEqualTo("Ville",ville).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                arrayDemande= new ArrayList();
                for(DocumentChange docOffre:queryDocumentSnapshots.getDocumentChanges()){

                    String user_id = docOffre.getDocument().getString("User_id");
                    String description = docOffre.getDocument().getString("Description");
                    String etat = docOffre.getDocument().getString("Reservation");
                    int piece = docOffre.getDocument().getLong("NombrePiece").intValue();
                    Date date = docOffre.getDocument().getDate("TimePub");
                    String titre= docOffre.getDocument().getString("TitreDemande");
                    String image = docOffre.getDocument().getString("Image_Demande");
                    String categorie= docOffre.getDocument().getString("Categorie");
                    String ville = docOffre.getDocument().getString("Ville");
                    arrayDemande.add(new Demande(description,etat,piece, date,titre, user_id, image, categorie,ville));
                }

                arrayAdapterDemande = new AdapterDemande(getContext(),arrayDemande);

                listeViewDemande.setAdapter(arrayAdapterDemande);
            }
        });

    }
    public void init_List_Constraint_category(){
        fstore.collection("Demandes").whereEqualTo("Reservation","non").whereEqualTo("Categorie",category).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                arrayDemande = new ArrayList();
                for(DocumentChange docOffre:queryDocumentSnapshots.getDocumentChanges()){

                    String user_id = docOffre.getDocument().getString("User_id");
                    String description = docOffre.getDocument().getString("Description");
                    String etat = docOffre.getDocument().getString("Reservation");
                    int piece = docOffre.getDocument().getLong("NombrePiece").intValue();
                    Date date = docOffre.getDocument().getDate("TimePub");
                    String titre= docOffre.getDocument().getString("TitreDemande");
                    String image = docOffre.getDocument().getString("Image_Demande");
                    String categorie= docOffre.getDocument().getString("Categorie");
                    String ville = docOffre.getDocument().getString("Ville");
                    arrayDemande.add(new Demande(description,etat,piece, date,titre, user_id, image, categorie,ville));

                }

                arrayAdapterDemande = new AdapterDemande(getContext(),arrayDemande);

                listeViewDemande.setAdapter(arrayAdapterDemande);
            }
        });

    }
    public void init_List_Constraint(){
        fstore.collection("Demandes").whereEqualTo("Reservation","non").whereEqualTo("Categorie",category).whereEqualTo("Ville",ville).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                arrayDemande = new ArrayList();
                for(DocumentChange docOffre:queryDocumentSnapshots.getDocumentChanges()){


                    String user_id = docOffre.getDocument().getString("User_id");
                    String description = docOffre.getDocument().getString("Description");
                    String etat = docOffre.getDocument().getString("Reservation");
                    int piece = docOffre.getDocument().getLong("NombrePiece").intValue();
                    Date date = docOffre.getDocument().getDate("TimePub");
                    String titre= docOffre.getDocument().getString("TitreDemande");
                    String image = docOffre.getDocument().getString("Image_Demande");
                    String categorie= docOffre.getDocument().getString("Categorie");
                    String ville = docOffre.getDocument().getString("Ville");
                    arrayDemande.add(new Demande(description,etat,piece, date,titre, user_id, image, categorie,ville));

                }

                arrayAdapterDemande = new AdapterDemande(getContext(),arrayDemande);

                listeViewDemande.setAdapter(arrayAdapterDemande);

            }
        });

    }

    public void init_spinners() {

        FirebaseFirestore.getInstance().collection("data").document("cities").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                adapterVille = new ArrayAdapter<String>(DemandeFragment.super.getContext(),android.R.layout.simple_spinner_dropdown_item,(List<String>) document.get("cities"));
                spinnerVille.setAdapter(adapterVille);


                adapterVille.notifyDataSetChanged();

            }
        });

        FirebaseFirestore.getInstance().collection("data").document("category").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                adapterVille = new ArrayAdapter<String>(DemandeFragment.super.getContext(),android.R.layout.simple_spinner_dropdown_item,(List<String>) document.get("category"));
                spinnerCatecory.setAdapter(adapterVille);

                adapterVille.notifyDataSetChanged();

            }
        });
    }


    public void updateListeOffre(){

        if(category==null && ville!=null){
            Log.w("ville", "ville");
            init_List_Constraint_ville();

        }
        else if(ville== null && category!=null){
            Log.w("categorie", "categorie");
            init_List_Constraint_category();

        }else{
            Log.w("Les 2", "categorie");
            init_List_Constraint();
        }



    }
}
