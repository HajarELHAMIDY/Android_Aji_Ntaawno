package com.example.ajitaawno;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        final Map<String, String> data= remoteMessage.getData();

        if(data==null || data.get("sender")==null)return;

       // final Bundle bundle=new Bundle();
        //final ChatFragment fragment = new ChatFragment();
        final Intent i=new Intent();
        FirebaseFirestore.getInstance().collection("users").document(data.get("sender")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User sender=documentSnapshot.toObject(User.class);
               /* bundle.putString("id",sender.getId());
                bundle.putString("nom",sender.getNom());
                bundle.putString("prenom",sender.getPrenom());
                bundle.putString("phone",sender.getPhone());
                bundle.putString("email",sender.getEmail());
                bundle.putString("dateNaissance",sender.getDateNaissance());
                bundle.putString("user_image",sender.getUser_image());
                bundle.putString("token",sender.getToken());
                bundle.putBoolean("online",sender.isOnline());
                fragment.setArguments(bundle);*/
               i.putExtra("User_id",sender.getId());

                PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,i,0);

                NotificationManager notificationManager=( NotificationManager)getApplication().getSystemService(Context.NOTIFICATION_SERVICE );
                String notificationId="my_channel_id_01";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel= new NotificationChannel(notificationId,"My Notification",NotificationManager.IMPORTANCE_DEFAULT);

                        notificationChannel.setDescription("Channel Description");
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),notificationId);
                    builder.setAutoCancel(true)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(data.get("title"))
                            .setContentText(data.get("body"))
                            .setContentIntent(pendingIntent);

                    notificationManager.notify(1,builder.build());

            }
        });
        //((FragmentActivity)getApplicationContext()).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
    }
}
