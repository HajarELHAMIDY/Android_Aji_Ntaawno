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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ajitaawno.ui.offre.MesOffreFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ModifierOffreFragment extends Fragment implements View.OnClickListener {
    private EditText mtitre;
    private EditText mdescription;
    private EditText mpiece;
    private ImageView mimage;
    private Uri postImageUri=null;
    private Spinner mcategorie;
    private Spinner mville;

    private StorageReference storageReference;

    private FirebaseFirestore firebaseFirestore;
    private Offres offres;
    private Button modifier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modifier_offre, container, false);
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Modifier une offre");
        Bundle bundle = this.getArguments();

        offres= new Offres(bundle.getString("TitreOffre"),bundle.getString("Description"),bundle.getInt("NombrePiece"), bundle.getString("Image_Offre"),bundle.getString("User_Id"),bundle.getString("Etat"),bundle.getString("Categorie"),bundle.getString("Ville"));
        offres.setId(bundle.getString("id"));
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        mtitre=root.findViewById(R.id.Offretitre);
        mdescription=root.findViewById(R.id.OffreDescription);
        mpiece=root.findViewById(R.id.Offrenombrepiece);
        mimage=root.findViewById(R.id.OffreImagePost);
        mcategorie=root.findViewById(R.id.OffreCategorie);
        mville=root.findViewById(R.id.OffreVille);
        modifier=root.findViewById(R.id.OffreModifier);
        modifier.setText("Modifier");

        mtitre.setText(offres.getTitreOffre());
        mdescription.setText(offres.getDescription());
        mpiece.setText(String.valueOf(offres.getNombrePiece()));
        if(offres.getImage_Offre()!=null){
            mimage.setImageURI(Uri.parse(offres.getImage_Offre()));
            postImageUri=(Uri.parse(offres.getImage_Offre()));
        }



       root.findViewById(R.id.OffreModifier).setOnClickListener(this);

        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = CropImage.activity()
                        .setAspectRatio(1,1)
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
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
                    mcategorie.setAdapter(arrayAdapter);
                    for(int i=0;i<listCategorie.size();i++){
                        if(offres.getCategorie().equals(listCategorie.get(i))){
                            mcategorie.setSelection(i);
                            System.out.println(i);
                            return;
                        }
                    }

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
                    mville.setAdapter(arrayAdapter);
                    for(int i=0;i<listVille.size();i++){
                        if(offres.getVille().equals(listVille.get(i))){
                            mville.setSelection(i);
                            System.out.println(i);
                            return;
                        }
                    }
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
    private void modifier() {
        final String titre = mtitre.getText().toString();
        final String description = mdescription.getText().toString();
        final String nombrepiece = mpiece.getText().toString();
        final String categorie = (String) mcategorie.getSelectedItem();
        Log.w("cat", categorie);

        final String ville = (String) mville.getSelectedItem();
        Log.w("ville",ville);
        StorageReference filePath = storageReference.child("OffresImage").child(".jpg");

        if (postImageUri != null) {

                filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String downloadUri = postImageUri.toString();

                            firebaseFirestore.collection("Offres").document(offres.getId()).update("Description", description, "TitreOffre", titre, "NombrePiece", Integer.parseInt(nombrepiece), "Image_Offre", downloadUri, "Categorie", categorie, "Ville", ville).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Offre modifiée", Toast.LENGTH_LONG).show();
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.nav_host_fragment,new MesOffreFragment()).commit();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "erreur", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        else {
 ;
            firebaseFirestore.collection("Offres").document(offres.getId()).update("Description", description, "TitreOffre", titre, "NombrePiece", Integer.parseInt(nombrepiece), "Categorie", categorie, "Ville", ville).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Offre modifiée", Toast.LENGTH_LONG).show();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment,new MesOffreFragment()).commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "erreur", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.OffreModifier:
                modifier();
                break;
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, new RealHomeFragment()).commit();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                postImageUri=result.getUri();
                mimage.setImageURI(postImageUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }
        }
    }


}
