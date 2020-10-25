package com.example.ajitaawno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


public class RealHomeFragment extends Fragment {

    private CardView btnOffre, btnDemandes, btnContactes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_test, container, false);

        btnOffre = root.findViewById(R.id.btn_offres);
        btnDemandes = root.findViewById(R.id.btn_demandes);
        btnContactes = root.findViewById(R.id.btn_p);


        btnOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new OffreFragment()).commit();

            }
        });
        btnDemandes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new DemandeFragment()).commit();

            }
        });
        btnContactes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new AproposNous()).commit();

            }
        });
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Accueil");

        return root;

    }

}
