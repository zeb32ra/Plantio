package com.example.plantio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    ImageView iv;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor settingsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        iv = findViewById(R.id.logo);


        if (getCurrentHour() >= 6 && getCurrentHour() < 17) {
            getWindow().setBackgroundDrawableResource(R.drawable.morning_main_background);
        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            getWindow().setBackgroundDrawableResource(R.drawable.evening_main_background);
        } else {
            getWindow().setBackgroundDrawableResource(R.drawable.night_main_backg);
            iv.setImageDrawable(getDrawable(R.drawable.night_logo));

        }

        Intent intent;
        sharedPreferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        settingsEditor = sharedPreferences.edit();


        if (!sharedPreferences.contains("Name")) {
            intent = new Intent(getApplicationContext(), WelcomeWindow.class);
        } else {
            intent = new Intent(getApplicationContext(), MainUserWindow.class);
        }
        Runnable r = () -> {
            startActivity(intent);
        };
        Handler h = new Handler();
        h.postDelayed(r, 3000);


    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


}

