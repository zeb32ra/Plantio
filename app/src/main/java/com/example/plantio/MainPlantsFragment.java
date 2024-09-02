package com.example.plantio;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainPlantsFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference plants_ref = db.collection("Users_plants");
    TextView text;
    SharedPreferences sharedPreferences;
    Button btn;
    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;

    PlantDictionary plantDictionary = new PlantDictionary();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_plants, container, false);
        recyclerView = view.findViewById(R.id.rec_v);
        text = view.findViewById(R.id.text_name);
        plantDictionary.load_data();
        btn = view.findViewById(R.id.btn_plants);

        sharedPreferences = getContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        mProgressBar = view.findViewById(R.id.main_progress);
        mProgressBar.setBackgroundResource(R.drawable.animation_progree);
        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();

        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            btn.setBackgroundColor(getResources().getColor(R.color.ColorButtonDay));
        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            btn.setBackgroundColor(getResources().getColor(R.color.EveningWhite));
        }
        else {
            text.setTextColor(getResources().getColor(R.color.white));
            btn.setBackgroundColor(getResources().getColor(R.color.CardButtonColorNight));
        }


        ArrayList<UserPlant> user_plants = new ArrayList<UserPlant>();
        ArrayList<String> plants_id = new ArrayList<>();

        animationDrawable.start();
        plants_ref.whereEqualTo("user", sharedPreferences.getString("ID", "default"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        UserPlant userPlant = new UserPlant(
                                documentSnapshot.getString("last_cutting_date"),
                                documentSnapshot.getString("last_fertilizing_date"),
                                documentSnapshot.getString("last_spraying_date"),
                                documentSnapshot.getString("last_watering_date"),
                                documentSnapshot.getString("plant_name"),
                                documentSnapshot.getString("plant_type"),
                                documentSnapshot.getString("user"));
                        plants_id.add(documentSnapshot.getId());
                        user_plants.add(userPlant);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(new RecyclerAdapterAllPlants(getContext(), user_plants, plants_id, getCurrentHour()));
                    animationDrawable.stop();
                    mProgressBar.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlantsForAddActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }


    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}