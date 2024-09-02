package com.example.plantio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddPlant_Activity extends AppCompatActivity {

    ImageView imageView_1, imageView_2, imageView_3, sun_iv_1, sun_iv_2, sun_iv_3, plant_iv;
    View line;
    Button btn;
    TextView Water, Sun, Danger;

    TextView danger_text, about_w, about_sun, about_spray, about_f, about_e, about_t, about_c, about_d,
            plant_name;
    TextView watering, sun, spray, fertilize, earth, transfer, cutting, danger;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference plants_ref = db.collection("Users_plants");

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_plant);
        sharedPreferences = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);



        Water = findViewById(R.id.Water);
        Sun = findViewById(R.id.Sun);
        Danger = findViewById(R.id.Danger);

        imageView_1 = findViewById(R.id.water_drop_1);
        imageView_2 = findViewById(R.id.water_drop_2);
        imageView_3 = findViewById(R.id.water_drop_3);
        sun_iv_1 = findViewById(R.id.sun_iv_1);
        sun_iv_2 = findViewById(R.id.sun_iv_2);
        sun_iv_3 = findViewById(R.id.sun_iv_3);
        line = findViewById(R.id.line);
        btn = findViewById(R.id.btn);
        danger_text = findViewById(R.id.danger_text);
        about_w = findViewById(R.id.about_watering);
        about_sun = findViewById(R.id.about_sun);
        about_spray = findViewById(R.id.about_spray);
        about_f = findViewById(R.id.about_fertilize);
        about_e = findViewById(R.id.about_earth);
        about_t = findViewById(R.id.about_transfer);
        about_c = findViewById(R.id.about_cutting);
        about_d = findViewById(R.id.about_danger);
        plant_name = findViewById(R.id.name);
        plant_iv = findViewById(R.id.item_img_view_for_add);

        watering = findViewById(R.id.watering);
        sun = findViewById(R.id.sun);
        spray = findViewById(R.id.spray);
        fertilize = findViewById(R.id.fertilize);
        earth = findViewById(R.id.earth);
        transfer = findViewById(R.id.transfer);
        cutting = findViewById(R.id.cutting);
        danger = findViewById(R.id.danger);

        ArrayList<ImageView> water_drops = new ArrayList<>();
        water_drops.add(imageView_1);
        water_drops.add(imageView_2);
        water_drops.add(imageView_3);

        ArrayList<ImageView> suns = new ArrayList<>();
        suns.add(sun_iv_1);
        suns.add(sun_iv_2);
        suns.add(sun_iv_3);

        int iterator = 0;

        Bundle extras = getIntent().getExtras();
        if(extras != null){


            while (iterator + 1 <= Integer.parseInt(extras.getString("water_amount"))){
                water_drops.get(iterator).setVisibility(View.VISIBLE);
                iterator += 1;
            }

            iterator = 0;
            while (iterator + 1 <= Integer.parseInt(extras.getString("sun_amount"))){
                suns.get(iterator).setVisibility(View.VISIBLE);
                iterator += 1;
            }

            danger_text.setText(extras.getString("danger"));
            if(danger_text.getText().toString().equals("Не опасно")){
                danger_text.setTextColor(getResources().getColor(R.color.Green));
            }
            else{
                danger_text.setTextColor(getResources().getColor(R.color.Red));
            }
            about_w.setText(extras.getString("about_water"));
            about_sun.setText(extras.getString("about_sun"));
            about_spray.setText(extras.getString("about_spray"));
            about_f.setText(extras.getString("about_ferlilize"));
            about_e.setText(extras.getString("about_earth"));
            about_t.setText(extras.getString("about_transfer"));
            about_c.setText(extras.getString("about_cutting"));
            about_d.setText(extras.getString("about_danger"));
            plant_name.setText(extras.getString("plant_name"));
            String path = extras.getString("path");

            StorageReference img_ref = storageReference.child(path);
            img_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(plant_iv);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            getWindow().setBackgroundDrawableResource(R.drawable.morning_not_main_bg);
            line.setBackgroundColor(getResources().getColor(R.color.NameMorningColor));
            btn.setBackgroundColor(getResources().getColor(R.color.ColorSecondaryDay));
        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            getWindow().setBackgroundDrawableResource(R.drawable.evening_not_main_bg);
            btn.setBackgroundColor(getResources().getColor(R.color.WhiteTransparrent));
        }
        else {
            getWindow().setBackgroundDrawableResource(R.drawable.night_ordinary_backg);
            danger_text.setTextColor(getResources().getColor(R.color.white));
            about_w.setTextColor(getResources().getColor(R.color.white));
            about_sun.setTextColor(getResources().getColor(R.color.white));
            about_spray.setTextColor(getResources().getColor(R.color.white));
            about_f.setTextColor(getResources().getColor(R.color.white));
            about_e.setTextColor(getResources().getColor(R.color.white));
            about_t.setTextColor(getResources().getColor(R.color.white));
            about_c.setTextColor(getResources().getColor(R.color.white));
            about_d.setTextColor(getResources().getColor(R.color.white));
            plant_name.setTextColor(getResources().getColor(R.color.white));
            watering.setTextColor(getResources().getColor(R.color.white));
            sun.setTextColor(getResources().getColor(R.color.white));
            spray.setTextColor(getResources().getColor(R.color.white));
            fertilize.setTextColor(getResources().getColor(R.color.white));
            earth.setTextColor(getResources().getColor(R.color.white));
            transfer.setTextColor(getResources().getColor(R.color.white));
            cutting.setTextColor(getResources().getColor(R.color.white));
            danger.setTextColor(getResources().getColor(R.color.white));
            line.setForeground(getResources().getDrawable(R.color.white));
            btn.setBackgroundColor(getResources().getColor(R.color.CardButtonColorNight));
            btn.setTextColor(getResources().getColor(R.color.white));

            Water.setTextColor(getResources().getColor(R.color.white));
            Sun.setTextColor(getResources().getColor(R.color.white));
            Danger.setTextColor(getResources().getColor(R.color.white));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                View dialogView = layoutInflater.inflate(R.layout.dialog_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddPlant_Activity.this);
                alertDialogBuilder.setView(dialogView);

                EditText userInput = dialogView.findViewById(R.id.plant_name_input);
                ImageView logo_view = dialogView.findViewById(R.id.logo_img_v);
                if(getCurrentHour() >= 21 || getCurrentHour() < 6){
                    logo_view.setImageDrawable(getDrawable(R.drawable.without_text_small_logo_night));
                }

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        UserPlant userPlant = new UserPlant("",
                                                "", "",
                                                "", userInput.getText().toString(),
                                                plant_name.getText().toString(),
                                                sharedPreferences.getString("ID", "default"));

                                        plants_ref.add(userPlant).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Растение успешно добавлено!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


}