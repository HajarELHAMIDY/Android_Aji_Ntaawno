package com.example.ajitaawno;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ajitaawno.ui.offre.MesOffreFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class PublierOffreFragment extends Fragment {
    private ImageView newPostImage;
    private EditText newPostDesc;
    private Button newPostBtn;
    private EditText newPostTitre;
    private EditText newPostNPiece;


    private Uri postImageUri=null;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_id;

    private Spinner spinnerCategorie;
    private Spinner spinnerVille;

    private ProgressBar newPostProgress;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_publier_offre, container, false);
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        current_id=firebaseAuth.getCurrentUser().getUid();
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Publier une nouvelle offre");

        newPostImage=root.findViewById(R.id.OffreImagePost);
        newPostDesc=root.findViewById(R.id.OffreDescription);
        newPostBtn=root.findViewById(R.id.OffreModifier);
        newPostProgress=root.findViewById(R.id.OffreProgress);
        newPostTitre=root.findViewById(R.id.Offretitre);
        newPostNPiece=root.findViewById(R.id.Offrenombrepiece);
        spinnerCategorie = (Spinner)root.findViewById(R.id.OffreCategorie);
        spinnerVille= (Spinner)root.findViewById(R.id.OffreVille);

  //      getSupportActionBar().setTitle("Publier Offre");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = CropImage.activity()
                        .setAspectRatio(1,1)
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String desc=newPostDesc.getText().toString();
                final String titre=newPostTitre.getText().toString();
                String valeur=newPostNPiece.getText().toString();
                final int nombrepiece=Integer.parseInt(valeur);
                final Date time= Calendar.getInstance().getTime();
                final String categorie= (String) spinnerCategorie.getSelectedItem();
                final String ville= (String) spinnerVille.getSelectedItem();

                if(!TextUtils.isEmpty(desc)){
                    newPostProgress.setVisibility(View.VISIBLE);

                    String randomName= FieldValue.serverTimestamp().toString();
                    if(postImageUri!=null){
                        StorageReference filePath=storageReference.child("OffresImage").child(".jpg");

                        filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if(task.isSuccessful()){
                                    //String downloadUri=task.getResult().getStorage().getDownloadUrl().toString();
                                    String downloadUri=postImageUri.toString();

                                    Map<String,Object> postMap=new HashMap<>();
                                    postMap.put("Image_Offre", downloadUri);
                                    postMap.put("Description",desc);
                                    postMap.put("User_id",current_id);
                                    postMap.put("TimePub",time);
                                    postMap.put("TitreOffre",titre);
                                    postMap.put("NombrePiece",nombrepiece);
                                    postMap.put("Etat","non");
                                    postMap.put("Categorie",categorie);
                                    postMap.put("Ville",ville);

                                    firebaseFirestore.collection("Offres").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()){

                                                Toast.makeText(getContext(),"Offre Publiée",Toast.LENGTH_LONG).show();
                                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.replace(R.id.nav_host_fragment, new MesOffreFragment()).commit();

                                            }
                                            else{
                                                Toast.makeText(getContext(),"Erreur",Toast.LENGTH_LONG).show();
                                            }

                                            newPostProgress.setVisibility(View.INVISIBLE);

                                        }
                                    });

                                }else{
                                    newPostProgress.setVisibility(View.INVISIBLE);

                                }
                            }
                        });

                    }else{
                        Map<String,Object> postMap=new HashMap<>();
                        postMap.put("Description",desc);
                        postMap.put("User_id",current_id);
                        postMap.put("TimePub",time);
                        postMap.put("TitreOffre",titre);
                        postMap.put("NombrePiece",nombrepiece);
                        postMap.put("Etat","non");
                        postMap.put("Categorie",categorie);
                        postMap.put("Ville",ville);

                        firebaseFirestore.collection("Offres").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(getContext(),"Offre Publiée",Toast.LENGTH_LONG).show();
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.nav_host_fragment, new MesOffreFragment()).commit();

                                }
                                else{
                                    Toast.makeText(getContext(),"Erreur",Toast.LENGTH_LONG).show();
                                }

                                newPostProgress.setVisibility(View.INVISIBLE);

                            }
                        });

                    }


                }

            }
        });





        //Spinner

        firebaseFirestore.collection("data").document("category").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc=task.getResult();
                if(task.isSuccessful()){
                    final List<String> List = new ArrayList<>();
                    ArrayList<String> listCategorie= (ArrayList<String>) doc.get("category");
                    for(int i=0;i<listCategorie.size();i++){
                        String nom=listCategorie.get(i);
                        List.add(nom);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, List);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategorie.setAdapter(arrayAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("data", e.getMessage());
            }
        });



        firebaseFirestore.collection("data").document("cities").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc=task.getResult();
                if(task.isSuccessful()){
                    final List<String> List = new ArrayList<>();
                    ArrayList<String> listVille= (ArrayList<String>) doc.get("cities");
                    for(int i=0;i<listVille.size();i++){
                        String nom=listVille.get(i);
                        List.add(nom);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, List);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerVille.setAdapter(arrayAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("data", e.getMessage());
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                postImageUri=result.getUri();
                newPostImage.setImageURI(postImageUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, new RealHomeFragment()).commit();


        }
        return super.onOptionsItemSelected(item);
    }

}
