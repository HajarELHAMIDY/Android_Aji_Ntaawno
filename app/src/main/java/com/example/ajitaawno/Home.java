package com.example.ajitaawno;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.ajitaawno.ui.offre.MesOffreFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    Home homeActivity;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore fstore;
    String userId;
    DocumentReference documentReference;
    ListenerRegistration registration;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        homeActivity = this;

        //ini
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();

        userId=mAuth.getCurrentUser().getUid();







        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each

        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_message,
                R.id.nav_tools,R.id.nav_offre)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       NavigationUI.setupWithNavController(navigationView, navController);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new RealHomeFragment()).commit();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id=destination.getId();
                if(id==R.id.nav_offre){
                    //      fab.hide();

                    //    FragmentTransaction fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
                    //    fragmentTransaction.replace(R.id.nav_host_fragment, new MesOffreFragment()).commit();
                }
                else{
                    //  fab.show();
                }
            }
        });



        updateNavHeader();



    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateNavHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        final TextView navUsername=headerView.findViewById(R.id.user_name);
        final TextView navUserMail=headerView.findViewById(R.id.user_email);
        ImageView navUserPhoto=headerView.findViewById(R.id.user_photo);

        navigationView.setNavigationItemSelectedListener(this);


        documentReference=fstore.collection("users").document(userId);
        registration = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                navUserMail.setText(documentSnapshot.getString("email"));
                navUsername.setText(documentSnapshot.getString("nom") +" " +documentSnapshot.getString("prenom"));
            }
        });


        //Load user image
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);

    }
    public void logout(){
        registration.remove();
        FirebaseAuth.getInstance().signOut();

        finish();
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id== R.id.nav_offre){

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new MesOffreFragment()).commit();
        }
        else if(id==R.id.nav_logout){
            registration.remove();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));

        }
        else if(id==R.id.nav_demande){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new MesDemandesFragment()).commit();


        }
        else if(id==R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new RealHomeFragment()).commit();
        }
        else if(id==R.id.nav_profile){

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new ProfileFragment()).commit();
        }
        else if(id==R.id.nav_message){

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new MessagesFragment()).commit();
        }

        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
