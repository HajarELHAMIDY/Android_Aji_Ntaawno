package com.example.ajitaawno;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesFragment extends Fragment{

    private GroupAdapter adapter;
    ListenerRegistration registration;
    private Contact cnt;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private ArrayList<contactItem> listes;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);
            RecyclerView rv=view.findViewById(R.id.messages_liste);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter=new GroupAdapter();
            rv.setAdapter(adapter);
             listes=new ArrayList<>();
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Mes messages");
            ChatApplication application=(ChatApplication)getActivity().getApplication();
            getActivity().getApplication().registerActivityLifecycleCallbacks(application);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                contactItem contactitem= (contactItem) item;
                Bundle bundle=new Bundle();
               bundle.putString("User_id",contactitem.contact.getUid());

                ChatFragment fragment = new ChatFragment();
                fragment.setArguments(bundle);
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
            }
        });
        updateToken();
         fetchLastMessage();


        return view;
    }

    private void updateToken() {
        String token= FirebaseInstanceId.getInstance().getToken();
        String uid=FirebaseAuth.getInstance().getUid();

        if(uid!=null){
            FirebaseFirestore.getInstance().collection("users").document(uid).update("token",token);
        }
    }


    private void fetchLastMessage(){
        String uid= FirebaseAuth.getInstance().getUid();
        registration=FirebaseFirestore.getInstance().collection("last-messages").document(uid).collection("contacts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    registration.remove();
                } else {
                    List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                    if (documentChanges != null) {
                        for (DocumentChange doc : documentChanges) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                Contact contact = doc.getDocument().toObject(Contact.class);
                                listes.add(new contactItem(contact));
                                adapter.add(new contactItem(contact));
                            }
                        }
                    }
                }
            }
        });
    }


    private class contactItem extends Item<GroupieViewHolder>{
             private final Contact contact;
             private contactItem(Contact contact){
                 this.contact=contact;
             }
        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
           TextView name= viewHolder.itemView.findViewById(R.id.message_name);
           TextView message=viewHolder.itemView.findViewById(R.id.message_text);
            CircleImageView image=viewHolder.itemView.findViewById(R.id.messages_image);

            name.setText(contact.getUsername());
            message.setText(contact.getLastMessage());
            Picasso.get().load(contact.getPhotoUrl()).into(image);


        }

        @Override
        public int getLayout() {
            return R.layout.adapter_messages;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void filter(final String key){

          ArrayList<contactItem> filter=new ArrayList<>();

          for(contactItem item: listes){
              if(item.contact.getUsername().toLowerCase().contains(key.toLowerCase())){
                  filter.add(item);
              }
          }
               adapter.update(filter);
               adapter.notifyDataSetChanged();
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rechercher, menu);
        MenuItem searchItem = menu.findItem(R.id.rechercher);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                       filter(newText);
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

}
