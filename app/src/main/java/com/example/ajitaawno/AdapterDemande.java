package com.example.ajitaawno;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDemande extends BaseAdapter {
    private Context context;
    private List<Demande> listes;
    private FirebaseAuth mAuth;

    FirebaseUser currentUser;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();

    private LayoutInflater inflater;
    ListenerRegistration registration;


    public AdapterDemande(Context context, List<Demande> liste){
        this.context = context;
        this.listes = liste;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listes.size();
    }

    @Override
    public Demande getItem(int position) {
        return listes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    //pour personnalisé chaque element/*

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.fragment_demande_test,null);
        final View v = convertView;
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        // get infos about item
        Demande currentContact = getItem(position);
        String itemetat = currentContact.getReservation();
        String itemDescription = currentContact.getDescription();
        String itemTitre = currentContact.getTitreDemande();
        Date itemdate= currentContact.getTimePub();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(itemdate);
        int itemPiece = currentContact.getNombrePiece();
        String user = currentContact.getUser();
        String imageOffre = currentContact.getImage_Demande();
        String ville = currentContact.getVille();

//maryamtaiaa8@gmail.com
        DocumentReference documentReference=fstore.collection("users").document(user);
      registration=  documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(mAuth.getCurrentUser() == null){
                    registration.remove();

                }else {
                    TextView itemUser = v.findViewById(R.id.user_demande);
                    itemUser.setText(documentSnapshot.getString("nom") + " " + documentSnapshot.getString("prenom"));
                    CircleImageView imageusercircle = v.findViewById(R.id.profile_user_adapter_demande);
                    Glide.with(context).load(documentSnapshot.getString("user_image")).into(imageusercircle);

                }
            }
        });




        TextView itemNombrePiece = convertView.findViewById(R.id.piece_demande);
        itemNombrePiece.setText(String.valueOf(itemPiece));
        TextView itemDescriptionView = convertView.findViewById(R.id.description_demande);
        itemDescriptionView.setText(itemDescription);
        TextView itemDateView = convertView.findViewById(R.id.date_demande);
        TextView itemVille = convertView.findViewById(R.id.demande_ville);
        itemVille.setText(ville);
        itemDateView.setText(date);
        TextView itemTitreView = convertView.findViewById(R.id.titre_demande);
        itemTitreView.setText(itemTitre);

        ImageView image = convertView.findViewById(R.id.imageViewDemande);

        Glide.with(context).load(imageOffre).into(image);
//hajar.elhamidy@gmail.com
         Button contacter=convertView.findViewById(R.id.cantactDemande);
        final int pos = position;
        if(user.equals(mAuth.getCurrentUser().getUid())){
            contacter.setVisibility(View.INVISIBLE);
        }

        contacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Demande demandes=listes.get(pos);
                Bundle bundle = new Bundle();
                bundle.putString("User_id",demandes.getUser());

                ChatFragment fragment = new ChatFragment();
                fragment.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
            }
        });



        return convertView;
    }



}
