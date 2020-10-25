package com.example.ajitaawno;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ajitaawno.ui.offre.MesOffreFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesOffresAdapter extends RecyclerView.Adapter<MesOffresAdapter.ViewHolder>{

    public List<Offres> Listoffres;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;


    FirebaseUser currentUser;
    String current_id;
    private FirestoreRecyclerAdapter adapter;
    private Object Tag;

    public MesOffresAdapter(List<Offres> Listoffres){
      this.Listoffres=Listoffres;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_offre,parent,false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        //current_profile=mAuth.getCurrentUser().getPhotoUrl().toString();
        current_id=mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String desc_data=Listoffres.get(position).getDescription();
        String titre_data=Listoffres.get(position).getTitreOffre();
        int nbpiece=Listoffres.get(position).getNombrePiece();
        String nbpiece_data=String.valueOf(nbpiece);
        String imageurl_data=Listoffres.get(position).getImage_Offre();
        final String categorie_data=Listoffres.get(position).getCategorie();
        final String ville_data=Listoffres.get(position).getVille();


        String user_id=Listoffres.get(position).getUser_id();
        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String nom=task.getResult().getString("nom");
                    String prenom = task.getResult().getString("prenom");
                    String userName=prenom+""+nom;

                    holder.setUserData(userName);
                }else{
                    Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show();
                }
            }
        });

        long millesc=Listoffres.get(position).getTimePub().getTime();
        String dateString= DateFormat.format("dd/MM/yyyy",new Date(millesc)).toString();


        holder.setDescText(desc_data);
        holder.setTitreText(titre_data);
        holder.setPieceText(nbpiece_data);
        holder.setOffreImage(imageurl_data);
        holder.setTime(dateString);
        holder.setCategorie(categorie_data);
        holder.setVille(ville_data);


        holder.sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteSelected(position);

                notifyDataSetChanged();


            }
        });
        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveSelected(position);
                notifyDataSetChanged();
            }
        });



    }

    public void deleteSelected(int position) {


final int pos = position;
        AlertDialog.Builder myPopup = new AlertDialog.Builder(context);


        myPopup.setTitle("Confirmation");

        myPopup.setMessage("Vous voulez vraiment supprimer votre offre!!!");
        myPopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                firebaseFirestore.collection("Offres").document(Listoffres.get(pos).getId()).delete();
                Toast.makeText(context,"l'enregistrement sera supprimer",Toast.LENGTH_SHORT).show();

            //    initListeView();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new MesOffreFragment()).commit();




            }
        });

        myPopup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,"l'offre reste toujours dans la liste",Toast.LENGTH_SHORT).show();
            }
        });
        myPopup.show();

    }
    public void reserveSelected(int position){
        AlertDialog.Builder myPopup = new AlertDialog.Builder(context);
        final Offres offres=Listoffres.get(position);
        String etat = offres.getEtat();
        Log.w("test", ""+offres.getEtat());
    if(etat.equals("non")){

            myPopup.setTitle("Confirmation");
            myPopup.setMessage("Vous voulez reserver cette offre à une personne!!!");
            myPopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseFirestore.collection("Offres").document(offres.getId()).update("Etat", "oui").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new MesOffreFragment()).commit();

                            Toast.makeText(context, "Offre réservée", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "erreur", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });


            myPopup.setNegativeButton("Non",new DialogInterface.OnClickListener()

            {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "L'offre est encore disponible", Toast.LENGTH_LONG).show();
                }
            });
            myPopup.show();

        }
        else{
            myPopup.setTitle("Confirmation");
            myPopup.setMessage("Vous voulez supprimer la reservation !!!");
            myPopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseFirestore.collection("Offres").document(offres.getId()).update("Etat", "non").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new MesOffreFragment()).commit();

                            Toast.makeText(context, "Offre est disponible", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "erreur", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });


            myPopup.setNegativeButton("Non",new DialogInterface.OnClickListener()

            {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "L'offre est encore réservée", Toast.LENGTH_LONG).show();
                }
            });
            myPopup.show();
        }
    }



    @Override
    public int getItemCount() {
        return Listoffres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private  View mView;
        private TextView descView;
        private TextView titreView;
        private TextView nbpieceView;
        private ImageView offreImageView;
        private TextView dateView;
        private Button sup;
        private TextView categorieView;
        private TextView villeView;
        private Button  confirmBtn ;


        private TextView nameView;
        private CircleImageView profileView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            confirmBtn = mView.findViewById(R.id.btn_confirm_offre);
            sup=mView.findViewById(R.id.supp_button);

            itemView.setOnClickListener(this);
        }



        public void setDescText(String text){
            descView=mView.findViewById(R.id.post_description);
            descView.setText(text);
        }

        public void setTitreText(String text){
            titreView=mView.findViewById(R.id.post_titre);
            titreView.setText(text);
        }

        public void setPieceText(String text){
            nbpieceView=mView.findViewById(R.id.post_nbpiece);
            nbpieceView.setText(text);
        }

        public void setOffreImage(String downloadUri){

            offreImageView=mView.findViewById(R.id.post_image);
            Glide.with(context).load(downloadUri).into(offreImageView);
        }

        public void setTime(String date){
           dateView=mView.findViewById(R.id.post_date);
           dateView.setText(date);
        }

        public void setCategorie(String text){
            categorieView=mView.findViewById(R.id.post_categorie);
            categorieView.setText(text);
        }

        public void setVille(String text){
            villeView=mView.findViewById(R.id.post_ville);
            villeView.setText(text);
        }

        public void setUserData(String name){

            nameView=mView.findViewById(R.id.post_username);
            profileView=mView.findViewById(R.id.post_userimage);

            //RequestOptions placeholderOption=new RequestOptions();
            //placeholderOption.placeholder(R.drawable.profile);
            nameView.setText(name);
            Glide.with(context).load(currentUser.getPhotoUrl()).into(profileView);

        }

        @Override
        public void onClick(View v) {
            Offres offres=Listoffres.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("id",offres.getId());
            bundle.putString("Description",offres.getDescription());
            bundle.putString("TitreOffre",offres.getTitreOffre());
            bundle.putString("User_id",offres.getUser_id());
            bundle.putInt("NombrePiece",offres.getNombrePiece());
            bundle.putString("Etat",offres.getEtat());
            bundle.putString("Categorie",offres.getCategorie());
            bundle.putString("Ville",offres.getVille());


            ModifierOffreFragment fragment = new ModifierOffreFragment();
            fragment.setArguments(bundle);
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();

        }


    }







}
