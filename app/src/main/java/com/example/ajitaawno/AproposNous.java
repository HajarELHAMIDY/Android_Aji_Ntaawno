package com.example.ajitaawno;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class AproposNous extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public AproposNous() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_apropos_nous, container, false);
        TextView desc = root.findViewById(R.id.desc_app);
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("A propos de nous");

          //  desc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        return root;
    }




}
