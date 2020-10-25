package com.example.ajitaawno;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    private GroupAdapter adapter;
    FloatingActionButton fab;
    private Offre offres;
    private String userimage;
    private EditText editchat;
    private String iduser;
    String title;
   // private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private Object Tag;
    private User user;
    private User us;
    private String username;
    ListenerRegistration registration;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        firebaseFirestore=FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        iduser=bundle.getString("User_id");

        System.out.println("userrrrrid:"+iduser);
       /* FirebaseFirestore.getInstance().collection("users").document(iduser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc=task.getResult();
                if(task.isSuccessful()){
                     System.out.println("useremail:::::::::"+doc.getString("email"));
                     us=doc.toObject(User.class);
                    System.out.println("useremail:::::::::"+us.getEmail());
                     title=doc.getString("nom")+" " +doc.getString("prenom");
                    userimage=doc.getString("user_image");
                    System.out.println("nommm:" +title);
                    ((AppCompatActivity)getContext()).getSupportActionBar().setTitle(title);
                }
            }
        });*/

        FirebaseFirestore.getInstance().collection("users").document(iduser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                System.out.println("useremail:::::::::"+documentSnapshot.getString("email"));
                us=documentSnapshot.toObject(User.class);
                System.out.println("useremail:::::::::"+us.getEmail());
                title=documentSnapshot.getString("nom")+" " +documentSnapshot.getString("prenom");
                userimage=documentSnapshot.getString("user_image");
                System.out.println("nommm:" +title);
                ((AppCompatActivity)getContext()).getSupportActionBar().setTitle(title);
            }
        });

         RecyclerView recyclerView=view.findViewById(R.id.chat_messages);
         adapter=new GroupAdapter();
         recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
         recyclerView.setAdapter(adapter);
         Button envoyer=view.findViewById(R.id.chat_envoyer);
         editchat=view.findViewById(R.id.chat_text);

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user=documentSnapshot.toObject(User.class);
                username=documentSnapshot.getString("nom") +" " +documentSnapshot.getString("prenom");
                System.out.println("usernameecurrent:"+username);
                fetchMessage();
            }
        });

         envoyer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendMessage();
             }
         });


     return view;
    }

    private void fetchMessage() {
        if(user!=null){
            System.out.println("useeeeeeerrrrrrrrr");
            String fromId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            System.out.println(fromId);
            String toId=iduser;
            System.out.println(toId);

          registration=  FirebaseFirestore.getInstance().collection("conversations").document(fromId).collection(toId).orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        registration.remove();
                    } else {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                        if (documentChanges != null) {
                            for (DocumentChange doc : documentChanges) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    Message message = doc.getDocument().toObject(Message.class);
                                    adapter.add(new MessageItem(message));
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void sendMessage() {
      String text=editchat.getText().toString();
      editchat.setText(null);
      final String fromId=FirebaseAuth.getInstance().getUid();
      final String toId=iduser;

      long timestamp=System.currentTimeMillis();

      final Message message=new Message();
      message.setFromId(fromId);
      message.setToId(toId);
      message.setTimestamp(timestamp);
      message.setText(text);

      if(!message.getText().isEmpty()){
          FirebaseFirestore.getInstance().collection("conversations").document(fromId).collection(toId).add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
              @Override
              public void onSuccess(DocumentReference documentReference) {
                 Log.d("Test",documentReference.getId());

                 Contact contact=new Contact();
                 contact.setUid(toId);
                 contact.setUsername(title);
                 contact.setPhotoUrl(userimage);
                 contact.setTimestamp(message.getTimestamp());
                 contact.setLastMessage(message.getText());

                 FirebaseFirestore.getInstance().collection("last-messages").document(fromId).collection("contacts").document(toId).set(contact);
                 System.out.println("USERRRR:"+us.getEmail());
                 if(!us.isOnline()){
                     Notification notification=new Notification();
                     notification.setFromId(message.getFromId());
                     notification.setToId(message.getToId());
                     notification.setTimestamp(message.getTimestamp());
                     notification.setText(message.getText());
                     notification.setFromName(user.getNom()+""+user.getPrenom());

                     FirebaseFirestore.getInstance().collection("notifications").document(us.getToken()).set(notification);
                 }
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.e("Test",e.getMessage(),e);
              }
          });

          FirebaseFirestore.getInstance().collection("conversations").document(toId).collection(fromId).add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
              @Override
              public void onSuccess(DocumentReference documentReference) {
                  Log.d("Test",documentReference.getId());
                  Contact contact=new Contact();
                  contact.setUid(fromId);
                  contact.setUsername(username);
                  contact.setPhotoUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                  contact.setTimestamp(message.getTimestamp());
                  contact.setLastMessage(message.getText());
                  FirebaseFirestore.getInstance().collection("last-messages").document(toId).collection("contacts").document(fromId).set(contact);
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.e("Test",e.getMessage(),e);
              }
          });
      }
    }

    private class MessageItem extends Item<GroupieViewHolder>{

        private final Message message;
        private MessageItem(Message message){
            this.message=message;
        }
        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            TextView textemssg=viewHolder.itemView.findViewById(R.id.chat_message);
            CircleImageView imageuser=viewHolder.itemView.findViewById(R.id.chat_photo);
            textemssg.setText(message.getText());
            if(message.getFromId().equals(FirebaseAuth.getInstance().getUid())) {
                Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imageuser);
            }
            else{
                Picasso.get().load(userimage).into(imageuser);
               // Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imageuser);
            }
        }

        @Override
        public int getLayout() {
            return message.getFromId().equals(FirebaseAuth.getInstance().getUid()) ? R.layout.item_from_mssg:R.layout.item_for_mssg;
        }
    }

}
