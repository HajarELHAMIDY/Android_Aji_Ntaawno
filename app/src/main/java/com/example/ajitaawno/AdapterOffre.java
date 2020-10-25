package com.example.ajitaawno;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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


public class AdapterOffre extends BaseAdapter {
    private Context context;
    private List<Offre> listes;

    private FirebaseAuth mAuth;

    FirebaseUser currentUser;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();

    private LayoutInflater inflater;
    ListenerRegistration registration;



    public AdapterOffre(Context context, List<Offre> liste){
        this.context = context;
        this.listes = liste;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listes.size();
    }

    @Override
    public Offre getItem(int position) {
        return listes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    //pour personnalisé chaque element/*

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.fragment_offre_test,null);
        final View v = convertView;
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        // get infos about item
        Offre currentContact = getItem(position);
        String itemetat = currentContact.getEtat();
        String itemDescription = currentContact.getDescription();
        String itemTitre = currentContact.getTitreOffre();
        Date itemdate= currentContact.getTimePub();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(itemdate);
        Long itemPiece = currentContact.getNombrePiece();
        String user = currentContact.getUser();
        String imageOffre = currentContact.getImage_Offre();
        String ville = currentContact.getVille();

//
       DocumentReference documentReference=fstore.collection("users").document(user);
       registration =   documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(mAuth.getCurrentUser() == null){
                    registration.remove();

                }else{
                    TextView itemUser =v.findViewById(R.id.user_offre);
                    itemUser.setText(documentSnapshot.getString("nom") +" " +documentSnapshot.getString("prenom"));

                    CircleImageView imageusercircle =  v.findViewById(R.id.profile_user_adapter);
                    Glide.with(context).load(documentSnapshot.getString("user_image")).into(imageusercircle);

                }


            }
        });


        // Display a date in day, month, year format


        TextView itemNombrePiece = convertView.findViewById(R.id.piece_offre);
        itemNombrePiece.setText(String.valueOf(itemPiece));
        TextView itemDescriptionView = convertView.findViewById(R.id.description_offre);
        itemDescriptionView.setText(itemDescription);
        TextView itemDateView = convertView.findViewById(R.id.date_offre);
        itemDateView.setText(date);
        TextView itemTitreView = convertView.findViewById(R.id.titre_offre);
        itemTitreView.setText(itemTitre);
        TextView itemVille = convertView.findViewById(R.id.offre_ville);
        itemVille.setText(ville);
        ImageView image = convertView.findViewById(R.id.imageViewOffre);
            Glide.with(context).load(imageOffre).into(image);
//hajar.elhamidy@gmail.com
        Button contacter=convertView.findViewById(R.id.cantactOffre);

        if(user.equals(mAuth.getCurrentUser().getUid())){
            contacter.setVisibility(View.INVISIBLE);
        }
        final int pos = position;
        contacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.nav_host_fragment, new DemandeFragment()).commit();

                Offre offres=listes.get(pos);
                Bundle bundle = new Bundle();
                bundle.putString("User_id",offres.getUser());


                ChatFragment fragment = new ChatFragment();
                fragment.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
            }
        });


        return convertView;
    }



}
