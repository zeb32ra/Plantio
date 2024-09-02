package com.example.plantio;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class PlantCareFragment extends Fragment {

    String plant_id;
    RecyclerView recyclerView;
    TextView text_1, text_2, text_3, text_4, text1, text2, text3, Water, Sun, Danger, danger_text, watering,
    about_watering, sun, about_sun, spray, about_spray, fertilize, about_fertilize, earth, about_earth,
            transfer, about_transfer, cutting, about_cutting, danger, about_danger;
    View line, line_2, line_3, line_4;
    SharedPreferences sharedPreferences;
    ImageView imageView_1, imageView_2, imageView_3, sun_iv_1, sun_iv_2, sun_iv_3;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference plants_mood = db.collection("Plant_Mood_Statistics");
    CollectionReference plants_journal = db.collection("Plant_journal");
    CollectionReference all_plants = db.collection("All_Plants");
    CollectionReference user_plants = db.collection("Users_plants");

    CollectionReference notification_ref = db.collection("Notifications");

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
    String today = formatter.format(calendar.getTime());
    SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm");
    String time;
    Button add_notif;

    String sun_amount;
    String water_amount;
    Context context;
    ScrollView scroll;
    final boolean[] mood_note_already_exists = {false};
    PlantCareFragment(String plant_id){
        this.plant_id = plant_id;
    }

    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;
    ImageButton sick, neutral, best, water, spray_ib, fertile, cut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plant_care, container, false);
        check_mood_notes();
        sick = view.findViewById(R.id.sick_btn);
        neutral = view.findViewById(R.id.neutr_btn);
        best = view.findViewById(R.id.best_btn);

        scroll = view.findViewById(R.id.scroll);

        mProgressBar = view.findViewById(R.id.main_progress);
        mProgressBar.setBackgroundResource(R.drawable.animation_progree);
        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();

        add_notif = view.findViewById(R.id.add_notif);
        context = getContext();

        recyclerView = view.findViewById(R.id.recycler);

        water = view.findViewById(R.id.water_btn);
        spray_ib = view.findViewById(R.id.spray_btn);
        fertile = view.findViewById(R.id.fertile_btn);
        cut = view.findViewById(R.id.cut_btn);

        text_1 = view.findViewById(R.id.text_1);
        text_2 = view.findViewById(R.id.text_2);
        text_3 = view.findViewById(R.id.text_3);
        text_4 = view.findViewById(R.id.text_4);
        line = view.findViewById(R.id.line);
        line_2 = view.findViewById(R.id.line_2);
        line_3 = view.findViewById(R.id.line_3);
        line_4 = view.findViewById(R.id.line_4);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);

        Water = view.findViewById(R.id.Water);
        Sun = view.findViewById(R.id.Sun);
        Danger = view.findViewById(R.id.Danger);
        danger_text = view.findViewById(R.id.danger_text);
        watering = view.findViewById(R.id.watering);
        about_watering = view.findViewById(R.id.about_watering);
        sun = view.findViewById(R.id.sun);
        about_sun = view.findViewById(R.id.about_sun);
        spray = view.findViewById(R.id.spray);
        about_spray = view.findViewById(R.id.about_spray);
        fertilize = view.findViewById(R.id.fertilize);
        about_fertilize = view.findViewById(R.id.about_fertilize);
        earth = view.findViewById(R.id.earth);
        about_earth = view.findViewById(R.id.about_earth);
        transfer = view.findViewById(R.id.transfer);
        about_transfer = view.findViewById(R.id.about_transfer);
        cutting = view.findViewById(R.id.cutting);
        about_cutting = view.findViewById(R.id.about_cutting);
        danger = view.findViewById(R.id.danger);
        about_danger = view.findViewById(R.id.about_danger);

        imageView_1 = view.findViewById(R.id.water_drop_1);
        imageView_2 = view.findViewById(R.id.water_drop_2);
        imageView_3 = view.findViewById(R.id.water_drop_3);
        sun_iv_1 = view.findViewById(R.id.sun_iv_1);
        sun_iv_2 = view.findViewById(R.id.sun_iv_2);
        sun_iv_3 = view.findViewById(R.id.sun_iv_3);



        sharedPreferences = getContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        animationDrawable.start();
        best.setEnabled(false);
        neutral.setEnabled(false);
        sick.setEnabled(false);

        water.setEnabled(false);
        spray_ib.setEnabled(false);
        cut.setEnabled(false);
        fertile.setEnabled(false);
//        create_notification_channel();
        LoadDataIntoRecycler();
        Loading_data_into_top_section();





        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            add_notif.setBackgroundColor(getResources().getColor(R.color.WhiteTransparrent));
            line.setForeground(getResources().getDrawable(R.color.NameMorningColor));
            line_2.setForeground(getResources().getDrawable(R.color.NameMorningColor));
            line_3.setForeground(getResources().getDrawable(R.color.NameMorningColor));
            line_4.setForeground(getResources().getDrawable(R.color.NameMorningColor));
        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            add_notif.setBackgroundColor(getResources().getColor(R.color.WhiteTransparrent));
        }
        else {
            text_1.setTextColor(getResources().getColor(R.color.white));
            text_2.setTextColor(getResources().getColor(R.color.white));
            text_3.setTextColor(getResources().getColor(R.color.white));
            text_4.setTextColor(getResources().getColor(R.color.white));

            line.setForeground(getResources().getDrawable(R.color.WhiteTransparrent));
            line_2.setForeground(getResources().getDrawable(R.color.WhiteTransparrent));
            line_3.setForeground(getResources().getDrawable(R.color.WhiteTransparrent));
            line_4.setForeground(getResources().getDrawable(R.color.WhiteTransparrent));

            text1.setTextColor(getResources().getColor(R.color.white));
            text2.setTextColor(getResources().getColor(R.color.white));
            text3.setTextColor(getResources().getColor(R.color.white));

            Water.setTextColor(getResources().getColor(R.color.white));
            Sun.setTextColor(getResources().getColor(R.color.white));
            Danger.setTextColor(getResources().getColor(R.color.white));
            danger_text.setTextColor(getResources().getColor(R.color.white));
            watering.setTextColor(getResources().getColor(R.color.white));
            about_watering.setTextColor(getResources().getColor(R.color.white));
            sun.setTextColor(getResources().getColor(R.color.white));
            about_sun.setTextColor(getResources().getColor(R.color.white));
            spray.setTextColor(getResources().getColor(R.color.white));
            about_spray.setTextColor(getResources().getColor(R.color.white));
            fertilize.setTextColor(getResources().getColor(R.color.white));
            about_fertilize.setTextColor(getResources().getColor(R.color.white));
            earth.setTextColor(getResources().getColor(R.color.white));
            about_earth.setTextColor(getResources().getColor(R.color.white));
            transfer.setTextColor(getResources().getColor(R.color.white));
            about_transfer.setTextColor(getResources().getColor(R.color.white));
            cutting.setTextColor(getResources().getColor(R.color.white));
            about_cutting.setTextColor(getResources().getColor(R.color.white));
            danger.setTextColor(getResources().getColor(R.color.white));
            about_danger.setTextColor(getResources().getColor(R.color.white));

            add_notif.setBackgroundColor(getResources().getColor(R.color.CardButtonColorNight));
        }






        sick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mood_note_already_exists[0]){
                    PlantMoodCheck mood = new PlantMoodCheck(today, "Плохо", plant_id);
                    time = time_formatter.format(calendar.getTime());
                    JournalNote note = new JournalNote("Плохое состояние", today, plant_id,
                            "Отметка о состоянии", time);
                    plants_mood.add(mood);
                    plants_journal.add(note);
                    mood_note_already_exists[0] = true;
                    Toast.makeText(view.getContext(), "Состояние успешно записано!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "Вы уже отмечали состояние сегодня", Toast.LENGTH_SHORT).show();
                }
            }
        });

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mood_note_already_exists[0]){
                    PlantMoodCheck mood = new PlantMoodCheck(today, "Нормально", plant_id);
                    time = time_formatter.format(calendar.getTime());
                    JournalNote note = new JournalNote("Нормальное состояние", today, plant_id,
                            "Отметка о состоянии", time);
                    plants_journal.add(note);
                    plants_mood.add(mood);
                    mood_note_already_exists[0] = true;
                    Toast.makeText(view.getContext(), "Состояние успешно записано!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "Вы уже отмечали состояние сегодня", Toast.LENGTH_SHORT).show();
                }
            }
        });

        best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mood_note_already_exists[0]){
                    PlantMoodCheck mood = new PlantMoodCheck(today, "Отлично", plant_id);
                    time = time_formatter.format(calendar.getTime());
                    JournalNote note = new JournalNote("Отличное состояние", today, plant_id,
                            "Отметка о состоянии", time);
                    plants_journal.add(note);
                    plants_mood.add(mood);
                    mood_note_already_exists[0] = true;
                    Toast.makeText(view.getContext(), "Состояние успешно записано!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "Вы уже отмечали состояние сегодня", Toast.LENGTH_SHORT).show();
                }
            }
        });

        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View dialogView = layoutInflater.inflate(R.layout.journal_dialog_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(dialogView);

                EditText userInput = dialogView.findViewById(R.id.comment_input);
                ImageView logo_view = dialogView.findViewById(R.id.img_v);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        time = time_formatter.format(calendar.getTime());
                                        JournalNote note = new JournalNote("Полив", today,
                                                plant_id, userInput.getText().toString(), time);

                                        user_plants.document(plant_id)
                                                .update("last_watering_date", today);

                                        plants_journal.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(view.getContext(), "Новые данные добавлены в журнал!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Loading_data_into_top_section();


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

        spray_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View dialogView = layoutInflater.inflate(R.layout.journal_dialog_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(dialogView);

                EditText userInput = dialogView.findViewById(R.id.comment_input);
                ImageView logo_view = dialogView.findViewById(R.id.img_v);

                logo_view.setImageDrawable(getResources().getDrawable(R.drawable.spray));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        time = time_formatter.format(calendar.getTime());
                                        JournalNote note = new JournalNote("Опрыскивание", today,
                                                plant_id, userInput.getText().toString(), time);

                                        user_plants.document(plant_id)
                                                .update("last_spraying_date", today);
                                        plants_journal.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(view.getContext(), "Новые данные добавлены в журнал!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Loading_data_into_top_section();


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

        fertile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View dialogView = layoutInflater.inflate(R.layout.journal_dialog_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(dialogView);

                EditText userInput = dialogView.findViewById(R.id.comment_input);
                ImageView logo_view = dialogView.findViewById(R.id.img_v);
                logo_view.setImageDrawable(getResources().getDrawable(R.drawable.fertilizer));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        time = time_formatter.format(calendar.getTime());
                                        JournalNote note = new JournalNote("Удобрение", today,
                                                plant_id, userInput.getText().toString(), time);

                                        user_plants.document(plant_id)
                                                .update("last_fertilizing_date", today);
                                        plants_journal.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(view.getContext(), "Новые данные добавлены в журнал!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Loading_data_into_top_section();


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

        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View dialogView = layoutInflater.inflate(R.layout.journal_dialog_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(dialogView);

                EditText userInput = dialogView.findViewById(R.id.comment_input);
                ImageView logo_view = dialogView.findViewById(R.id.img_v);
                logo_view.setImageDrawable(getResources().getDrawable(R.drawable.pruning));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        time = time_formatter.format(calendar.getTime());
                                        JournalNote note = new JournalNote("Обрезка", today,
                                                plant_id, userInput.getText().toString(), time);

                                        user_plants.document(plant_id)
                                                .update("last_cutting_date", today);

                                        plants_journal.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(view.getContext(), "Новые данные добавлены в журнал!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Loading_data_into_top_section();


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

        add_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View dialogView = layoutInflater.inflate(R.layout.dialog_notification_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(dialogView);

                ImageView logo_view = dialogView.findViewById(R.id.logo_img_v);

                Spinner action = dialogView.findViewById(R.id.action);
                Spinner day = dialogView.findViewById(R.id.day);

                TextView time = dialogView.findViewById(R.id.time);

                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(dialogView.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                time.setText(selectedHour + ":" + selectedMinute);
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                if(getCurrentHour() > 21 || getCurrentHour() < 6){
                    logo_view.setImageDrawable(getResources().getDrawable(R.drawable.without_text_small_logo_night));
                }

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                            Notification newNotification = new Notification(
                                                    plant_id,
                                                    action.getSelectedItem().toString(),
                                                    day.getSelectedItem().toString(),
                                                    time.getText().toString()
                                            );

                                            notification_ref.add(newNotification);
                                            LoadDataIntoRecycler();
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

        return view;
    }

    private void check_mood_notes() {
        plants_mood.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    if(Objects.equals(documentSnapshot.getString("date"), today) &&
                            Objects.equals(documentSnapshot.getString("plant_id"), plant_id)){
                        mood_note_already_exists[0] = true;

                    }
                }
            }
        });
    }

//    private void create_notification_channel() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "foxandroidReminderChannel";
//            String description = "Chanell for Alarm Manager";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel chanell = new NotificationChannel("foxandroid", name, importance);
//            chanell.setDescription(description);
//            NotificationManager notificationManager = getSystemService(getContext(), NotificationManager.class);
//            notificationManager.createNotificationChannel(chanell);
//        }
//    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private void Loading_data_into_top_section(){
        user_plants.whereEqualTo("user", sharedPreferences.getString("ID", "default"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                if(documentSnapshot.getId().equals(plant_id)){
                                    text_1.setText("Полив: " + documentSnapshot.getString("last_watering_date"));
                                    text_2.setText("Удобрение: " + documentSnapshot.getString("last_fertilizing_date"));
                                    text_3.setText("Опрыскивание: " + documentSnapshot.getString("last_spraying_date"));
                                    text_4.setText("Обрезка: " + documentSnapshot.getString("last_cutting_date"));

                                    Loading_data_into_bottom_section(documentSnapshot.getString("plant_type"));
                                }
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void Loading_data_into_bottom_section(String plant_name){
        ArrayList<ImageView> water_drops = new ArrayList<>();
        water_drops.add(imageView_1);
        water_drops.add(imageView_2);
        water_drops.add(imageView_3);

        ArrayList<ImageView> suns = new ArrayList<>();
        suns.add(sun_iv_1);
        suns.add(sun_iv_2);
        suns.add(sun_iv_3);



        all_plants.whereEqualTo("name", plant_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    sun_amount = documentSnapshot.getString("sun_amount");
                    water_amount = documentSnapshot.getString("watering_amount");

                    danger_text.setText(documentSnapshot.getString("danger"));
                    if(danger_text.getText().toString().equals("Не опасно")){
                        danger_text.setTextColor(getResources().getColor(R.color.Green));
                    }
                    else{
                        danger_text.setTextColor(getResources().getColor(R.color.Red));
                    }

                    about_watering.setText(documentSnapshot.getString("all_about_watering"));
                    about_sun.setText(documentSnapshot.getString("all_about_sun"));
                    about_spray.setText(documentSnapshot.getString("all_about_spraying"));
                    about_fertilize.setText(documentSnapshot.getString("all_about_fertilizing"));
                    about_earth.setText(documentSnapshot.getString("all_about_earth"));
                    about_transfer.setText(documentSnapshot.getString("all_about_transfer"));
                    about_cutting.setText(documentSnapshot.getString("all_about_cutting"));
                    about_danger.setText(documentSnapshot.getString("all_about_danger"));

                    int iterator = 0;
                    while (iterator + 1 <= Integer.parseInt(water_amount)){
                        water_drops.get(iterator).setVisibility(View.VISIBLE);
                        iterator += 1;
                    }

                    iterator = 0;
                    while (iterator + 1 <= Integer.parseInt(sun_amount)){
                        suns.get(iterator).setVisibility(View.VISIBLE);
                        iterator += 1;
                    }

                    animationDrawable.stop();
                    mProgressBar.setVisibility(View.GONE);
                    best.setEnabled(true);
                    neutral.setEnabled(true);
                    sick.setEnabled(true);

                    water.setEnabled(true);
                    spray_ib.setEnabled(true);
                    cut.setEnabled(true);
                    fertile.setEnabled(true);
                }
            }
        });



    }

    private void LoadDataIntoRecycler(){
        ArrayList<Notification> notificationArrayList = new ArrayList<Notification>();

        notification_ref.whereEqualTo("plant_id", plant_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                Notification new_not = new Notification(
                                        documentSnapshot.get("plant_id").toString(),
                                        documentSnapshot.get("name").toString(),
                                        documentSnapshot.get("week_day").toString(),
                                        documentSnapshot.get("time").toString());
                                notificationArrayList.add(new_not);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(new NotificationsRecycler(getContext(), notificationArrayList, getCurrentHour()));
                        }
                        else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}