package com.example.plantio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class WelcomeWindow extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor settingsEditor;
    EditText editText;
    Button name_btn;
    TextView tv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users_ref = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_window);
        name_btn = findViewById(R.id.input_name_btn);
        editText = findViewById(R.id.editText);
        tv = findViewById(R.id.tv);
        sharedPreferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        settingsEditor = sharedPreferences.edit();
        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            getWindow().setBackgroundDrawableResource(R.drawable.morning_not_main_backg);
        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            getWindow().setBackgroundDrawableResource(R.drawable.evening_ordinary_backg);
        }
        else {
            getWindow().setBackgroundDrawableResource(R.drawable.night_ordinary_backg);
            editText.setTextColor(getResources().getColor(R.color.white));
            tv.setTextColor(getResources().getColor(R.color.white));

        }


        name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName();
                Intent intent = new Intent(getApplicationContext(), MainUserWindow.class);
                startActivity(intent);
            }
        });
    }


    private void setName(){
        if(!sharedPreferences.contains("Name")){
            String user_name = editText.getText().toString();
            User user = new User(user_name);
            users_ref.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    settingsEditor.putString("ID", documentReference.getId());
                    settingsEditor.putString("Name", user_name);
                    settingsEditor.apply();
                    Toast.makeText(getApplicationContext(), sharedPreferences.getString("Name", "NameDefault") + " "
                            + sharedPreferences.getString("ID", "ID_Default"), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}