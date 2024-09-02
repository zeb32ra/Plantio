package com.example.plantio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class PlantsForAddActivity extends AppCompatActivity {

    SearchView sv;
    RecyclerView rv;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference allPlants = db.collection("All_Plants");
    ArrayList<Plant> plants = new ArrayList<Plant>();
    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_for_add);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sv = findViewById(R.id.search_v);
        sv.clearFocus();

        mProgressBar = findViewById(R.id.main_progress);
        mProgressBar.setBackgroundResource(R.drawable.animation_progree);
        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();



        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
        rv = findViewById(R.id.rec_v);


        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            getWindow().setBackgroundDrawableResource(R.drawable.morning_not_main_backg);
        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            getWindow().setBackgroundDrawableResource(R.drawable.evening_not_main_bg);
        }
        else {
            getWindow().setBackgroundDrawableResource(R.drawable.night_ordinary_backg);
            int id = sv.getContext()
                    .getResources()
                    .getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) sv.findViewById(id);
            textView.setTextColor(Color.WHITE);
        }



        animationDrawable.start();
        allPlants.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                Plant plant = new Plant(
                                        documentSnapshot.getString("name"),
                                        documentSnapshot.get("watering_amount").toString(),
                                        documentSnapshot.get("sun_amount").toString(),
                                        documentSnapshot.getString("danger"),
                                        documentSnapshot.getString("all_about_watering"),
                                        documentSnapshot.getString("all_about_sun"),
                                        documentSnapshot.getString("all_about_spraying"),
                                        documentSnapshot.getString("all_about_fertilizing"),
                                        documentSnapshot.getString("all_about_earth"),
                                        documentSnapshot.getString("all_about_transfer"),
                                        documentSnapshot.getString("all_about_danger"),
                                        documentSnapshot.getString("all_about_cutting"));
                                plants.add(plant);
                            }
                            rv.setLayoutManager(new LinearLayoutManager(PlantsForAddActivity.this));
                            rv.setHasFixedSize(true);
                            rv.setAdapter(new PlantsForAddAdapter(PlantsForAddActivity.this, plants, getCurrentHour()));
                            animationDrawable.stop();
                            mProgressBar.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void filterList(String text) {
        ArrayList<Plant> filtered_list = new ArrayList<>();
        for(Plant item: plants){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filtered_list.add(item);
            }
        }

        if(filtered_list.isEmpty()){
            Toast.makeText(getApplicationContext(), "Data was not fount", Toast.LENGTH_SHORT).show();
        }
        else{
            rv.setAdapter(new PlantsForAddAdapter(getApplicationContext(), filtered_list, getCurrentHour()));
        }
    }


    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}