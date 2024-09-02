package com.example.plantio;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Calendar;


public class NotificationsFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference plants_ref = db.collection("Users_plants");
    CollectionReference notif_ref = db.collection("Notifications");

    ArrayList<Notification> notificationArrayList = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    RecyclerView rv;
    SharedPreferences sharedPreferences;
    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        sharedPreferences = getContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        rv = view.findViewById(R.id.rv);
        mProgressBar = view.findViewById(R.id.main_progress);
        mProgressBar.setBackgroundResource(R.drawable.animation_progree);
        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();

        animationDrawable.start();
        plants_ref.whereEqualTo("user", sharedPreferences.getString("ID", "default"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                notif_ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(QueryDocumentSnapshot documentSnapshot1: task.getResult()){
                                            if(documentSnapshot1.get("plant_id").toString().equals(documentSnapshot.getId())){
                                                Notification new_not = new Notification(
                                                        documentSnapshot1.get("plant_id").toString(),
                                                        documentSnapshot1.get("name").toString(),
                                                        documentSnapshot1.get("week_day").toString(),
                                                        documentSnapshot1.get("time").toString());
                                                notificationArrayList.add(new_not);
                                                names.add(documentSnapshot.get("plant_name").toString());
                                            }
                                        }
                                        rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                        rv.setHasFixedSize(true);
                                        rv.setAdapter(new NotificationsRecyclerMain(getContext(), notificationArrayList, names, getCurrentHour()));
                                        animationDrawable.stop();
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                });
                            }

                        }
                        else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}