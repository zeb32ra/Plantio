package com.example.plantio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class UserPlantDetailActivity extends AppCompatActivity {

    Button care, journal, statistic;
    String plant_id;

    ImageView imageView;
    TextView plant_name;
    View line;
    boolean night_mode = false;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_plant_detail);

        plant_name = findViewById(R.id.name);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            plant_id = extras.getString("id");
            String[] name = extras.getString("name").split(" ");
            plant_name.setText(String.join("\n", name));

            StorageReference img_ref = storageReference.child(extras.getString("path"));
            img_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(UserPlantDetailActivity.this)
                            .load(uri)
                            .into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }



        care = findViewById(R.id.btn_care);
        journal = findViewById(R.id.btn_journal);
        statistic = findViewById(R.id.btn_statistic);

        imageView = findViewById(R.id.item_img_view_for_add);
        line = findViewById(R.id.line);


        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                care.setPressed(true);
                care.invalidate();
                Handler handler1 = new Handler();
                Runnable r1 = new Runnable() {
                    public void run() {
                        care.setPressed(false);
                        care.invalidate();
                        care.performClick();
                    }
                };
                handler1.postDelayed(r1, 100);

            }
        };
        handler.postDelayed(r, 100);


        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            getWindow().setBackgroundDrawableResource(R.drawable.morning_not_main_bg);
            line.setForeground(getDrawable(R.color.NameMorningColor));
        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            getWindow().setBackgroundDrawableResource(R.drawable.evening_not_main_bg);
        }
        else {
            night_mode = true;
            getWindow().setBackgroundDrawableResource(R.drawable.night_ordinary_backg);
            plant_name.setTextColor(getColor(R.color.white));
            journal.setTextColor(getResources().getColor(R.color.white));
            statistic.setTextColor(getResources().getColor(R.color.white));
            line.setForeground(getDrawable(R.color.white));
        }

        care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!night_mode){
                    care.setTextColor(getResources().getColor(R.color.white));
                    journal.setTextColor(getResources().getColor(R.color.black));
                    statistic.setTextColor(getResources().getColor(R.color.black));
                }
                else{
                    care.setTextColor(getResources().getColor(R.color.CardButtonColorNight));
                    journal.setTextColor(getResources().getColor(R.color.white));
                    statistic.setTextColor(getResources().getColor(R.color.white));
                }

                setFragment(new PlantCareFragment(plant_id));
            }
        });

        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!night_mode){
                    care.setTextColor(getResources().getColor(R.color.black));
                    journal.setTextColor(getResources().getColor(R.color.white));
                    statistic.setTextColor(getResources().getColor(R.color.black));
                }
                else{
                    care.setTextColor(getResources().getColor(R.color.white));
                    journal.setTextColor(getResources().getColor(R.color.CardButtonColorNight));
                    statistic.setTextColor(getResources().getColor(R.color.white));
                }

                setFragment(new Journal_fragment(plant_id));
            }
        });

        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!night_mode){
                    care.setTextColor(getResources().getColor(R.color.black));
                    journal.setTextColor(getResources().getColor(R.color.black));
                    statistic.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    care.setTextColor(getResources().getColor(R.color.white));
                    journal.setTextColor(getResources().getColor(R.color.white));
                    statistic.setTextColor(getResources().getColor(R.color.CardButtonColorNight));
                }


                setFragment(new Plant_Statistics(plant_id));
                Toast.makeText(getApplicationContext(), "Нажмите на диаграмму", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay, fragment).commit();
    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}