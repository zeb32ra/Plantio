package com.example.plantio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Objects;

public class MainUserWindow extends AppCompatActivity {

    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_user_window);
        bottomNav = findViewById(R.id.bottom_nav);
        setFragment(new HomeFragment());
        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            getWindow().setBackgroundDrawableResource(R.drawable.morning_not_main_bg);
            bottomNav.setBackgroundColor(getResources().getColor(R.color.transparent));

        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            getWindow().setBackgroundDrawableResource(R.drawable.evening_not_main_bg);
            bottomNav.setBackgroundColor(getResources().getColor(R.color.EveningWhite));
        }
        else {
            getWindow().setBackgroundDrawableResource(R.drawable.night_ordinary_backg);
            bottomNav.setBackgroundColor(getResources().getColor(R.color.CardColorNight));

        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(Objects.equals(extras.getString("mode"), "note")){
                setFragment(new NotificationsFragment());
            }
            else if(Objects.equals(extras.getString("mode"), "myplants")){
                setFragment(new MainPlantsFragment());
            }

        }


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.home){
                    setFragment(new HomeFragment());
                    return true;
                }
                else if(id == R.id.my_plants){
                    setFragment(new MainPlantsFragment());
                    return true;
                }
                else if(id == R.id.notification){
                    setFragment(new NotificationsFragment());
                    return true;
                }
                return false;
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